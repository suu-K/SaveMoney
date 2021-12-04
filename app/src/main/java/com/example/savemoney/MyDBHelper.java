package com.example.savemoney;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;

public class MyDBHelper extends SQLiteOpenHelper {
    public MyDBHelper(Context context) {
        super(context, "SaveMoney", null, 1);
    }

    @Override
    public void onCreate(@NonNull SQLiteDatabase db) {
        db.execSQL("CREATE TABLE card (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT NOT NULL," +
                "isCredit TEXT NOT NULL," +
                "interestRate REAL" +
                ");");
        db.execSQL("CREATE TABLE expenses (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "amount INTEGER NOT NULL," +
                "category TEXT NOT NULL," +
                "content TEXT," +
                "method TEXT NOT NULL," +
                "cardId INTEGER REFERENCES card(id)," +
                "installmentPeriod INTEGER," +
                "interest," +
                "monExpend INTEGER," +
                "date DATE DEFAULT CURRENT_TIMESTAMP" +
                ");");
        db.execSQL("CREATE TABLE fixedExpenses (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT NOT NULL," +
                "amount INTEGER NOT NULL" +
                ");");
        db.execSQL("CREATE TABLE category (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT NOT NULL" +
                ");");
        db.execSQL("CREATE TABLE user (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "goal INTEGER NOT NULL" +
                ");");
    }

    @Override
    public void onUpgrade(@NonNull SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS card");
        db.execSQL("DROP TABLE IF EXISTS expenses");
        db.execSQL("DROP TABLE IF EXISTS fixedExpenses");
        db.execSQL("DROP TABLE IF EXISTS category");
        db.execSQL("DROP TABLE IF EXISTS user");
        onCreate(db);
    }
}
