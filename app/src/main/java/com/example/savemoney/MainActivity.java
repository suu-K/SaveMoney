package com.example.savemoney;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    TabMain tabMain;
    TabExpend tabExpend;
    TabSetting tabSetting;
    TabStatistic tabStatistic;
    TabWallet tabWallet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        tabMain = new TabMain();
        tabExpend = new TabExpend();
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
                    case R.id.tabExpend:{
                        getSupportFragmentManager().beginTransaction() .replace(R.id.main_layout, tabExpend).commitAllowingStateLoss();
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



    public class myDBHelper extends SQLiteOpenHelper {
        public myDBHelper(Context context) {
            super(context, "saveMoney", null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE expend (" +
                    "id INTEGER PRIMARY KEY," +
                    "amount INTEGER NOT NULL," +
                    "category TEXT NOT NULL," +
                    "content TEXT," +
                    "method TEXT NOT NULL," +
                    "cardId INTEGER," +
                    "installment INTEGER," +
                    "interest INTEGER," +
                    "monexpend INTEGER," +
                    "FOREIGN KEY(card) REFERENCES card(id)" +
                    ");");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS groupTBL");
            onCreate(db);

        }
    }

}