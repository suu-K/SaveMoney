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



    public class myDBHelper extends SQLiteOpenHelper {
        public myDBHelper(Context context) {
            super(context, "saveMoney", null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE cart (" +
                    "id INTEGER PRIMARY KEY," +
                    "name TEXT NOT NULL," +
                    "category TEXT NOT NULL," +
                    "interestRate INTEGER" +
                    ");");
            db.execSQL("CREATE TABLE expenses (" +
                    "id INTEGER PRIMARY KEY," +
                    "amount INTEGER NOT NULL," +
                    "category TEXT NOT NULL," +
                    "content TEXT," +
                    "method TEXT NOT NULL," +
                    "cardId INTEGER," +
                    "installment INTEGER," +
                    "interest INTEGER," +
                    "monexpend INTEGER," +
                    "FOREIGN KEY(cardId) REFERENCES card(id)" +
                    ");");
            db.execSQL("CREATE TABLE cart (" +
                    "id INTEGER PRIMARY KEY," +
                    "name TEXT NOT NULL," +
                    "category TEXT NOT NULL," +
                    "interestRate INTEGER" +
                    ");");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS groupTBL");
            onCreate(db);

        }
    }

}