package com.example.savemoney;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;


public class TabMain extends Fragment {
    ViewGroup viewGroup;

    TextView textSpendMoney, textProgressBar;
    ProgressBar progressBar;
    MyDBHelper myDBHelper;
    SQLiteDatabase db;
    Cursor cursor;
    BarChart barChart;

    ArrayList<Integer> valList = new ArrayList<>();
    ArrayList<String> labelList = new ArrayList<>();

    int spend = 0;
    int goal = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewGroup = (ViewGroup) inflater.inflate(R.layout.tab_main,container,false);

        textSpendMoney = (TextView) viewGroup.findViewById(R.id.textSpendMoney);
        textProgressBar = (TextView) viewGroup.findViewById(R.id.textProgressBar);
        progressBar = (ProgressBar) viewGroup.findViewById(R.id.progressBar);
        barChart = (BarChart) viewGroup.findViewById(R.id.mainChart);
        myDBHelper = new MyDBHelper(getActivity());


        db = myDBHelper.getReadableDatabase();

        cursor = db.rawQuery("SELECT strftime('%Y-%m', date) AS month, sum(amount) FROM expenses WHERE strftime('%Y-%m', 'now', 'localtime') = strftime('%Y-%m', date) GROUP BY month;", null);
        while(cursor.moveToNext()){
            spend = cursor.getInt(1);
        }
        cursor.close();
        textSpendMoney.setText(spend+"원");

        cursor = db.rawQuery("SELECT goal FROM user", null);
        while(cursor.moveToNext()){
            goal = cursor.getInt(0);
        }
        cursor.close();
        textProgressBar.setText(spend+"/"+goal);
        db.close();

        if(goal == 0)
            progressBar.setProgress(100);
        else
            progressBar.setProgress((int)(100 * spend/goal));

        dataSetting();
        graphInitSetting();
        BarChartGraph(labelList, valList);

        return viewGroup;
    }

    public void dataSetting(){
        labelList.clear();
        valList.clear();
        int fixed = 0;
        db = myDBHelper.getReadableDatabase();

        Cursor cursor;
        cursor = db.rawQuery("SELECT strftime('%Y-%m', date) as month, sum(amount) as sum FROM expenses " +
                "WHERE strftime('%Y-%m', date) BETWEEN strftime('%Y-%m', 'now', '-4 months') AND strftime('%Y-%m', 'now', 'localtime') " +
                "GROUP BY month;", null);

        Cursor cursorMax = db.rawQuery("SELECT sum(amount) FROM fixedExpenses", null);

        while(cursorMax.moveToNext()) {
            fixed = cursorMax.getInt(0);
        }

        while (cursor.moveToNext()) {
            labelList.add(cursor.getString(0));
            valList.add(cursor.getInt(1) + fixed);
        }
        cursorMax.close();
        cursor.close();
        db.close();
    }

    public void graphInitSetting(){
        barChart.setTouchEnabled(false); //확대하지못하게 막아버림! 별로 안좋은 기능인 것 같아~
        barChart.setPinchZoom(false);
        barChart.setDrawGridBackground(false);
        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        barChart.getXAxis().setDrawGridLines(false);
    }

    private void BarChartGraph(ArrayList<String> labelList, ArrayList<Integer> valList) {
        barChart.clear();
        int max = 0;
        // BarChart 메소드
        ArrayList<BarEntry> entries = new ArrayList<>();
        for (int i = 0; i < valList.size(); i++) {
            entries.add(new BarEntry(i, (Integer) valList.get(i)));
            max = Math.max(max, valList.get(i));
        }

        BarDataSet dataSet = new BarDataSet(entries, "일일 사용시간"); // 변수로 받아서 넣어줘도 됨
        dataSet.setAxisDependency(YAxis.AxisDependency.LEFT);

        ArrayList<String> labels = new ArrayList<String>();
        for (int i = 0; i < labelList.size(); i++) {
            labels.add((String) labelList.get(i));
        }

        BarData data = new BarData(dataSet);
        dataSet.setColors(ColorTemplate.LIBERTY_COLORS); //

        XAxis xAxis = barChart.getXAxis();
        barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
        barChart.setAutoScaleMinMaxEnabled(true);
        barChart.getAxisLeft().setAxisMaximum((float) (max * 1.1));
        barChart.getAxisRight().setEnabled(false);
        barChart.setData(data);
        barChart.animateXY(1000, 1000);
        barChart.invalidate();
    }

}