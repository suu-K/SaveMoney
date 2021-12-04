package com.example.savemoney;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.WindowManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    TabMain tabMain;
    TabExpenses tabExpenses;
    TabSetting tabSetting;
    TabStatistic tabStatistic;
    TabWallet tabWallet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        tabMain = new TabMain();
        tabExpenses = new TabExpenses();
        tabSetting = new TabSetting();
        tabStatistic = new TabStatistic();
        tabWallet = new TabWallet();

        getSupportFragmentManager().beginTransaction().replace(R.id.main_layout,tabMain).commitAllowingStateLoss();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.tabMain:{
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_layout, tabMain).commitAllowingStateLoss();
                        return true;
                    }
                    case R.id.tabExpenses:{
                        getSupportFragmentManager().beginTransaction() .replace(R.id.main_layout, tabExpenses).commitAllowingStateLoss();
                        return true;
                    }
                    case R.id.tabStatistic:{
                        getSupportFragmentManager().beginTransaction() .replace(R.id.main_layout, tabStatistic).commitAllowingStateLoss();
                        return true;
                    }
                    case R.id.tabWallet:{
                        getSupportFragmentManager().beginTransaction() .replace(R.id.main_layout, tabWallet).commitAllowingStateLoss();
                        return true;
                    }
                    case R.id.tabSetting:{
                        getSupportFragmentManager().beginTransaction() .replace(R.id.main_layout, tabSetting).commitAllowingStateLoss();
                        return true;
                    }
                    default: return false;
                }
            }
        });
    }

}