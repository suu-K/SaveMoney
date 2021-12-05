package com.example.savemoney;

import android.app.DatePickerDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class TabStatistic extends Fragment {
    ViewGroup viewGroup;
    ArrayList<Integer> valList = new ArrayList<>(); // ArrayList 선언
    ArrayList<String> labelList = new ArrayList<>(); // ArrayList 선언
    BarChart barChart;
    MyDBHelper myDBHelper;
    SQLiteDatabase db;
    TextView dateStart, dateEnd;

    Button btnMethod, btnDuration, btnCategory, btnDay, btnMonth, btnWeek;

    enum Type{ term, category, method };
    Type type = Type.term;
    int term = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewGroup = (ViewGroup) inflater.inflate(R.layout.tab_statistic,container,false);

        btnDuration=(Button) viewGroup.findViewById(R.id.btnDuration);
        btnCategory=(Button) viewGroup.findViewById(R.id.btnCategory);
        btnMethod=(Button) viewGroup.findViewById(R.id.btnMethod);
        btnDay=(Button) viewGroup.findViewById(R.id.btnDay);
        btnMonth=(Button) viewGroup.findViewById(R.id.btnMonth);
        btnWeek=(Button) viewGroup.findViewById(R.id.btnWeek);

        myDBHelper = new MyDBHelper(getActivity());
        barChart = (BarChart) viewGroup.findViewById(R.id.barChart);

        dateStart=(TextView) viewGroup.findViewById(R.id.dateStart);
        dateEnd=(TextView) viewGroup.findViewById(R.id.dateEnd);

        Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        if(mMonth<10){
            dateStart.setText(mYear+"-0" + (mMonth+1) + "-01");
            dateEnd.setText(mYear+"-0" + (mMonth+1) + "-31");
        }
        else{
            dateStart.setText(mYear+"-" + (mMonth+1) + "-01");
            dateEnd.setText(mYear+"-" + (mMonth+1) + "-31");
        }

        btnMethod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type=Type.method;
                dataSetting(type, term, dateStart.getText().toString(), dateEnd.getText().toString());
                BarChartGraph(labelList, valList);
                barChart.invalidate();
                btnDay.setVisibility(View.INVISIBLE);
                btnWeek.setVisibility(View.INVISIBLE);
                btnMonth.setVisibility(View.INVISIBLE);
            }
        });
        btnCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type=Type.category;
                dataSetting(type, term, dateStart.getText().toString(), dateEnd.getText().toString());
                BarChartGraph(labelList, valList);
                barChart.invalidate();
                btnDay.setVisibility(View.INVISIBLE);
                btnWeek.setVisibility(View.INVISIBLE);
                btnMonth.setVisibility(View.INVISIBLE);
            }
        });
        btnDuration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type=Type.term;
                dataSetting(type, term, dateStart.getText().toString(), dateEnd.getText().toString());
                BarChartGraph(labelList, valList);
                barChart.invalidate();
                btnDay.setVisibility(View.VISIBLE);
                btnWeek.setVisibility(View.VISIBLE);
                btnMonth.setVisibility(View.VISIBLE);
            }
        });

        btnDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                term=2;
                dataSetting(type, term, dateStart.getText().toString(), dateEnd.getText().toString());
                BarChartGraph(labelList, valList);
                barChart.invalidate();
            }
        });
        btnWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                term=1;
                dataSetting(type, term, dateStart.getText().toString(), dateEnd.getText().toString());
                BarChartGraph(labelList, valList);
                barChart.invalidate();
            }
        });
        btnMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                term=0;
                dataSetting(type, term, dateStart.getText().toString(), dateEnd.getText().toString());
                BarChartGraph(labelList, valList);
                barChart.invalidate();
            }
        });

        dateStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        if(month<10){
                            if(dayOfMonth<10) { dateStart.setText(year+"-0" + (month+1) + "-0" + dayOfMonth); }
                            else {dateStart.setText(year+"-0" + (month+1) + "-" + dayOfMonth);}
                        }
                        else {
                            if(dayOfMonth<10) { dateStart.setText(year+"-" + (month+1) + "-0" + dayOfMonth); }
                            else {dateStart.setText(year+"-" + (month+1) + "-" + dayOfMonth);}
                        }
                        dataSetting(type, term, dateStart.getText().toString(), dateEnd.getText().toString());
                        BarChartGraph(labelList, valList);
                        barChart.invalidate();
                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
        dateEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        if(month<10){
                            if(dayOfMonth<10) { dateEnd.setText(year+"-0" + (month+1) + "-0" + dayOfMonth); }
                            else {dateEnd.setText(year+"-0" + (month+1) + "-" + dayOfMonth);}
                        }
                        else {
                            if(dayOfMonth<10) { dateEnd.setText(year+"-" + (month+1) + "-0" + dayOfMonth); }
                            else {dateEnd.setText(year+"-" + (month+1) + "-" + dayOfMonth);}
                        }
                        dataSetting(type, term, dateStart.getText().toString(), dateEnd.getText().toString());
                        BarChartGraph(labelList, valList);
                        barChart.invalidate();
                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });


       // dataSetting(type, term, "2021-01-01", "2021-12-12");             //데이터 세팅
        graphInitSetting();       //그래프 기본 세팅
        BarChartGraph(labelList, valList);


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
        barChart.getDescription().setEnabled(false);
    }

    private void BarChartGraph(ArrayList<String> labelList, ArrayList<Integer> valList) {
        barChart.clear();
        int max = 0;
        int min = Integer.MAX_VALUE;
        // BarChart 메소드
        ArrayList<BarEntry> entries = new ArrayList<>();
        for (int i = 0; i < valList.size(); i++) {
            entries.add(new BarEntry(i, (Integer) valList.get(i)));
            max = Math.max(max, valList.get(i));
            min = Math.min(min, valList.get(i));
        }

        BarDataSet dataSet = new BarDataSet(entries, "지출"); // 변수로 받아서 넣어줘도 됨
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
        barChart.getAxisLeft().setAxisMinimum((float) (min * 0.7));
        barChart.getAxisRight().setEnabled(false);
        barChart.setData(data);
        barChart.animateXY(1000, 1000);
        barChart.invalidate();
    }
}