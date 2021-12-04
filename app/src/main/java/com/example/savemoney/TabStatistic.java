package com.example.savemoney;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

public class TabStatistic extends Fragment {
    ViewGroup viewGroup;
    ArrayList<Integer> valList = new ArrayList<>(); // ArrayList 선언
    ArrayList<String> labelList = new ArrayList<>(); // ArrayList 선언
    BarChart barChart;
    MyDBHelper myDBHelper;
    SQLiteDatabase db;

    Button btnInsert, btnUpgrade;

    enum Type{ term, category, method };
    Type type = Type.term;
    int term = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewGroup = (ViewGroup) inflater.inflate(R.layout.tab_statistic,container,false);

        btnInsert = (Button) viewGroup.findViewById(R.id.btnInsert);
        btnUpgrade = (Button) viewGroup.findViewById(R.id.btnUpgrade);

        myDBHelper = new MyDBHelper(getActivity());
        barChart = (BarChart) viewGroup.findViewById(R.id.barChart);

        dataSetting(type, term, "2021-01-01", "2021-12-12");             //데이터 세팅
        graphInitSetting();       //그래프 기본 세팅
        BarChartGraph(labelList, valList);

        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db = myDBHelper.getWritableDatabase();
                ArrayList<String> d = new ArrayList<String>();
                d.add("2021-10-02 00:00:00");
                d.add("2021-10-02 00:00:00");
                d.add("2021-10-03 00:00:00");
                d.add("2021-11-03 00:00:00");
                d.add("2021-11-01 00:00:00");
                d.add("2021-11-01 00:00:00");
                d.add("2021-11-01 00:00:00");
                d.add("2021-11-10 00:00:00");
                d.add("2021-12-12 00:00:00");
                d.add("2021-12-14 00:00:00");
                ArrayList<String> cate = new ArrayList<String>();
                cate.add("생활비");
                cate.add("생활비");
                cate.add("식비");
                cate.add("통신비");
                cate.add("식비");
                cate.add("품위유지비");
                cate.add("생활비");
                cate.add("식비");
                cate.add("통신비");
                cate.add("생활비");
                ArrayList<String> me = new ArrayList<>();
                me.add("현금");
                me.add("현금");
                me.add("신용카드");
                me.add("현금");
                me.add("체크카드");
                me.add("신용카드");
                me.add("체크카드");
                me.add("신용카드");
                me.add("현금");
                me.add("체크카드");
                for(int i=0;i<10;i++) {
                    int amount = (int) (Math.random() * 10000 + 10000);
                    db.execSQL("INSERT INTO expenses(amount, category, method, date) VALUES(" +
                            "" + amount + "," +
                            "'"+ cate.get(i) +"'," +
                            "'"+ me.get(i) +"'," +
                            "'" + d.get(i) +
                            "');");
                }
                dataSetting(type, term, "2021-01-01", "2021-12-12");
                BarChartGraph(labelList, valList);
                barChart.invalidate();
            }
        });
        btnUpgrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db = myDBHelper.getWritableDatabase();
                myDBHelper.onUpgrade(db, 1, 2);
                db.close();
                dataSetting(type, term, "2021-01-01", "2021-12-31");
                BarChartGraph(labelList, valList);
                barChart.invalidate();
            }
        });

        return viewGroup;
    }

    public void dataSetting(Type type, int i, String start, String end){
        labelList.clear();
        valList.clear();
        db = myDBHelper.getReadableDatabase();
        Cursor cursor;
        String col = "", group = "";
        String where = "strftime('%Y-%m-%d', date) BETWEEN '" + start + "' AND '" + end + "'";
        switch(type) {
            case term:
                switch (i) {
                case 0: //월간
                    col = "strftime('%Y-%m', date) as month, sum(amount) as sum";
                    group = "month";
                    break;
                case 1: //주간
                    col = "strftime('%Y-%w', date) as week, sum(amount) as sum";
                    group = "week";
                    break;
                case 2: //일간
                    col = "strftime('%Y-%w-%d', date) as day, sum(amount) as sum";
                    group = "day";
                    break;
                }
            break;
            case method:
                col = "method, sum(amount)";
                group = "method";
                break;
            case category:
                col = "category, sum(amount)";
                group = "category";
                break;
        }
        cursor = db.rawQuery("SELECT "+ col +" FROM expenses WHERE " + where + " GROUP BY " + group + ";", null);

        while (cursor.moveToNext()) {
            labelList.add(cursor.getString(0));
            valList.add(cursor.getInt(1));
        }
        cursor.close();
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