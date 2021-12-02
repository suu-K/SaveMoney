package com.example.savemoney;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class TabExpenses extends Fragment {
    ViewGroup viewGroup;

    Button btnCash, btnDebit, btnCredit, btnExpenses;
    EditText edtMoney, edtMemo;
    Spinner spinnerCategory, spinnerCard, spinnerInstallment;

    MyDBHelper myHelper;
    SQLiteDatabase db;

    int amount;
    String method = "현금";
    String memo = "";
    String category;
    int cardId;
    int installmentPeriod = 1;
    int interest = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewGroup = (ViewGroup) inflater.inflate(R.layout.tab_expenses,container,false);

        btnCash = (Button) viewGroup.findViewById(R.id.btnCash);
        btnDebit = (Button) viewGroup.findViewById(R.id.btnDebit);
        btnCredit = (Button) viewGroup.findViewById(R.id.btnCredit);
        btnExpenses = (Button) viewGroup.findViewById(R.id.btnExpenses);
        edtMoney = (EditText) viewGroup.findViewById(R.id.edtMoney);
        edtMemo = (EditText) viewGroup.findViewById(R.id.edtMemo);
        spinnerCategory = (Spinner) viewGroup.findViewById(R.id.spinnerCategory);
        spinnerCard = (Spinner) viewGroup.findViewById(R.id.spinnerCard);
        spinnerInstallment = (Spinner) viewGroup.findViewById(R.id.spinnerInstallmenet);

        myHelper = new MyDBHelper(getActivity());
        db = myHelper.getReadableDatabase();

        //현금 버튼 선택
        btnCash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                method = "현금";
                installmentPeriod = 1;
                spinnerCard.setVisibility(View.INVISIBLE);
                spinnerInstallment.setVisibility(View.INVISIBLE);
            }
        });
        //체크카드 버튼 선택
        btnDebit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                method = "체크카드";
                installmentPeriod = 1;
                spinnerCard.setVisibility(View.VISIBLE);
                spinnerInstallment.setVisibility(View.INVISIBLE);
            }
        });
        //신용카드 버튼 선택
        btnCredit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                method = "신용카드";
                spinnerCard.setVisibility(View.VISIBLE);
                spinnerInstallment.setVisibility(View.VISIBLE);
            }
        });
        Cursor cursorCategory = db.rawQuery("Select * from category", null);
        ArrayList<String> categoryList = new ArrayList<String>();
        if(cursorCategory.getCount()==0){
            categoryList.add("등록된 카테고리가 없습니다.");
        }else {
            while (cursorCategory.moveToNext()) {
                categoryList.add(cursorCategory.getString(1));
            }
        }
        Cursor cursorCard = db.rawQuery("Select * from card", null);
        ArrayList<String> cardList = new ArrayList<String>();
        if(cursorCard.getCount()==0){
            cardList.add("등록된 카드가 없습니다.");
        }else {
            while (cursorCard.moveToNext()) {
                cardList.add(cursorCard.getString(1));
            }
        }
        final String[] installmentList = {"2개월(무이자)", "3개월(무이자)", "4개월(무이자)", "5개월(무이자)", "6개월(무이자)", "2개월","3개월","4개월","5개월","6개월","12개월","24개월"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, categoryList);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, cardList);
        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, installmentList);
        spinnerCategory.setAdapter(adapter);
        spinnerCard.setAdapter(adapter2);
        spinnerInstallment.setAdapter(adapter3);
        //카테고리 선택
        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                category = categoryList.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(getActivity(), "선택되지 않음", Toast.LENGTH_SHORT).show();
            }
        });
        //카드 선택
        spinnerCard.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(cursorCard.getCount() != 0) {
                    cursorCard.moveToPosition(i);
                    cardId = cursorCard.getInt(0);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(getActivity(), "선택되지 않음", Toast.LENGTH_SHORT).show();
            }
        });

        spinnerInstallment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(installmentList[i].contains("무이자")){
                    interest = 0;
                    installmentPeriod = Integer.parseInt(installmentList[i].replaceAll("[^0-9]", ""));
                }else{
                    interest = -1;
                    installmentPeriod = Integer.parseInt(installmentList[i].replaceAll("[^0-9]", ""));
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(getActivity(), "선택되지 않음", Toast.LENGTH_SHORT).show();
            }
        });

        btnExpenses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int monExpend = amount / installmentPeriod;
                memo = edtMemo.getText().toString();
                if(interest == -1){
                    Cursor cursor = db.rawQuery("Select interestRate FROM card WHERE id = " + cardId + ";", null);
                    interest = (int) (cursor.getFloat(0) * amount / 100);
                    monExpend += interest;
                    cursor.close();
                }
                db.close();
                db = myHelper.getWritableDatabase();
                db.execSQL("INSERT INTO expenses(amount, category, content, method, cardId, installmentPeriod, interest, monExpend, date) VALUES(" +
                        amount + ",'" +
                        category + "','" +
                        memo + "','" +
                        method + "'," +
                        cardId + "," +
                        installmentPeriod + "," +
                        interest + "," +
                        monExpend + "," +
                        ");");
                db = myHelper.getReadableDatabase();
            }
        });

        return viewGroup;
    }

}