package com.example.savemoney;

import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;

import org.w3c.dom.Text;

import java.util.ArrayList;


public class TabWallet extends Fragment {
    ViewGroup viewGroup;

    TextView NumIdealSpend, AddCategory, AddMethod, AddFixedExpenses;
    LinearLayout layoutCategory, layoutMethod, layoutFixedExpenses;
    MyDBHelper myHelper;
    SQLiteDatabase db;


    View dialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewGroup = (ViewGroup) inflater.inflate(R.layout.tab_wallet, container, false);

        NumIdealSpend = (TextView) viewGroup.findViewById(R.id.idealSpend);
        AddCategory = (TextView) viewGroup.findViewById(R.id.AddCategory);
        AddMethod = (TextView) viewGroup.findViewById(R.id.AddMethod);
        AddFixedExpenses = (TextView) viewGroup.findViewById(R.id.AddFixedExpenses);

        layoutCategory = (LinearLayout) viewGroup.findViewById(R.id.layoutCategory);
        layoutMethod = (LinearLayout) viewGroup.findViewById(R.id.layoutMethod);
        layoutFixedExpenses = (LinearLayout) viewGroup.findViewById(R.id.layoutFixedExpenses);

        myHelper = new MyDBHelper(getActivity());

        /*db= myHelper.getReadableDatabase();
        Cursor cursorCategory = db.rawQuery("Select * from category", null);
        ArrayList<String> categoryList = new ArrayList<String>();
        if(cursorCategory.getCount()==0){
            categoryList.add("등록된 카테고리가 없습니다.");
        }else {
            while (cursorCategory.moveToNext()) {
                categoryList.add(cursorCategory.getString(1));
            }
        }

        db.close();

        final TextView arrayCategory[]=new TextView[categoryList.size()];

        for(int i=0;i<categoryList.size();i++){
            arrayCategory[i].setText(categoryList.get(i));
            layoutCategory.addView(arrayCategory[i]);
        }*/

        NumIdealSpend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dlgIdealSpend = new AlertDialog.Builder(getActivity());
                dlgIdealSpend.setTitle("목표 소비 금액 입력");
                final EditText editText = new EditText(getActivity());
                dlgIdealSpend.setView(editText);
                dlgIdealSpend.setPositiveButton("입력", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            int num = Integer.parseInt(editText.getText().toString());
                            Toast.makeText(getActivity(), "변경되었습니다.", Toast.LENGTH_SHORT).show();
                            NumIdealSpend.setText(editText.getText().toString() + " 원");
                        } catch (NumberFormatException e) {
                            Toast.makeText(getActivity(), "숫자만 입력하세요.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                dlgIdealSpend.show();
            }
        });

        AddCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dlgCategory = new AlertDialog.Builder(getActivity());
                dlgCategory.setTitle("항목 추가");
                final EditText editText = new EditText(getActivity());
                dlgCategory.setView(editText);
                dlgCategory.setPositiveButton("입력", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        db = myHelper.getWritableDatabase();
                        db.execSQL("INSERT into category(name) values('" + editText.getText().toString() + "');");
                        db.close();
                    }
                });
                dlgCategory.show();
            }
        });

        AddMethod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogMethod dialog = DialogMethod.newInstance(new DialogMethod.MyDialogListener() {
                    @Override
                    public void myCallback(String name, String kinds) {
                        if(name != null && kinds != null){
                            db = myHelper.getWritableDatabase();
                            db.execSQL("INSERT into card(name, isCredit) values('" + name + "', '" + kinds + "');");
                            db.close();
                        }
                        else{
                            AddMethod.setText("실패");
                        }
                    }
                });
                dialog.show(getFragmentManager(), "addDialog");
            }
        });
        Fragment fragment = this;
        AddFixedExpenses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFixedExpenses dialog = DialogFixedExpenses.newInstance(new DialogFixedExpenses.MyDialogListener() {
                    @Override
                    public void myCallback(String name, int amount) {
                        if(name != null) {
                            db = myHelper.getWritableDatabase();
                            db.execSQL("INSERT into fixedExpenses(name, amount) values('" + name + "', " + amount + ");");
                            db.close();
                        }
                        else
                            AddFixedExpenses.setText("실패");
                    }
                });
                dialog.show(getFragmentManager(), "addDialog");
            }
        });


        return viewGroup;
    }
}