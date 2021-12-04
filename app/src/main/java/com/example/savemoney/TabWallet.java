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
import androidx.fragment.app.FragmentTransaction;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Locale;


public class TabWallet extends Fragment {
    ViewGroup viewGroup;

    TextView NumIdealSpend, AddCategory, AddMethod, AddFixedExpenses;
    LinearLayout layoutCategory, layoutMethod, layoutFixedExpenses;
    MyDBHelper myHelper;
    SQLiteDatabase db;

    Fragment fragment = this;

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

        db = myHelper.getReadableDatabase();

        onCreateCategory();
        onCreateMethod();
        onCreateFixedExpenses();

        Cursor tmp = db.rawQuery("SELECT * FROM user", null);
        while(tmp.moveToNext()){
            NumIdealSpend.setText(tmp.getString(1).toString() + " 원");
        }

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
                            db = myHelper.getWritableDatabase();
                            db.execSQL("INSERT into user(goal) values('" + editText.getText().toString() + "');");
                            db.close();
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

                        TextView newView = new TextView(getActivity());
                        newView.setText(editText.getText().toString());
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        params.topMargin = 10;
                        newView.setLayoutParams(params);
                        newView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                AlertDialog.Builder dlgCategory = new AlertDialog.Builder(getActivity());
                                dlgCategory.setMessage("삭제하시겠습니까?");
                                dlgCategory.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        db = myHelper.getWritableDatabase();
                                        db.execSQL("DELETE FROM category WHERE name = '" + editText.getText().toString() + "';");
                                        db.close();
                                        layoutCategory.removeView(newView);
                                    }
                                });
                                dlgCategory.setNegativeButton("취소", null);
                                dlgCategory.show();
                            }
                        });
                        layoutCategory.addView(newView);
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

                            TextView newView = new TextView(getActivity());
                            newView.setText(name + " - " + kinds);
                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            params.topMargin = 10;
                            newView.setLayoutParams(params);
                            newView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    AlertDialog.Builder dlgCategory = new AlertDialog.Builder(getActivity());
                                    dlgCategory.setMessage("삭제하시겠습니까?");
                                    dlgCategory.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            db = myHelper.getWritableDatabase();
                                            db.execSQL("DELETE FROM card WHERE name = '" + name + "';");
                                            db.close();
                                            layoutMethod.removeView(newView);
                                        }
                                    });
                                    dlgCategory.setNegativeButton("취소", null);
                                    dlgCategory.show();
                                }
                            });
                            layoutMethod.addView(newView);
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

                            TextView newView = new TextView(getActivity());
                            newView.setText(name + " - " + amount + "원");
                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            params.topMargin = 10;
                            newView.setLayoutParams(params);
                            newView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    AlertDialog.Builder dlgCategory = new AlertDialog.Builder(getActivity());
                                    dlgCategory.setMessage("삭제하시겠습니까?");
                                    dlgCategory.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            db = myHelper.getWritableDatabase();
                                            db.execSQL("DELETE FROM fixedExpenses WHERE name = '" + name + "';");
                                            db.close();
                                            layoutFixedExpenses.removeView(newView);
                                        }
                                    });
                                    dlgCategory.setNegativeButton("취소", null);
                                    dlgCategory.show();
                                }
                            });
                            layoutFixedExpenses.addView(newView);
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

    public void onCreateCategory(){
        layoutCategory.removeAllViews();
        Cursor categoryCursor = db.rawQuery("SELECT * FROM category", null);
        while(categoryCursor.moveToNext()){
            int id = categoryCursor.getInt(0);
            String str = categoryCursor.getString(1);

            TextView newView = new TextView(getActivity());
            newView.setText(str);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.topMargin = 10;
            newView.setLayoutParams(params);
            newView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder dlgCategory = new AlertDialog.Builder(getActivity());
                    dlgCategory.setMessage("삭제하시겠습니까?");
                    dlgCategory.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            db = myHelper.getWritableDatabase();
                            db.execSQL("DELETE FROM category WHERE name = '" + str + "';");
                            db.close();
                            layoutCategory.removeView(newView);
                        }
                    });
                    dlgCategory.setNegativeButton("취소", null);
                    dlgCategory.show();
                }
            });
            layoutCategory.addView(newView);
        }
    }

    public void onCreateMethod(){
        layoutMethod.removeAllViews();
        Cursor methodCursor = db.rawQuery("SELECT * FROM card", null);
        while(methodCursor.moveToNext()){
            int id = methodCursor.getInt(0);
            String str = methodCursor.getString(1) + " - " + methodCursor.getString(2);

            TextView newView = new TextView(getActivity());
            newView.setText(str);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.topMargin = 10;
            newView.setLayoutParams(params);
            newView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder dlgCategory = new AlertDialog.Builder(getActivity());
                    dlgCategory.setMessage("삭제하시겠습니까?");
                    dlgCategory.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            db = myHelper.getWritableDatabase();
                            db.execSQL("DELETE FROM card WHERE id = " + id + ";");
                            db.close();
                            layoutMethod.removeView(newView);
                        }
                    });
                    dlgCategory.setNegativeButton("취소", null);
                    dlgCategory.show();
                }
            });
            layoutMethod.addView(newView);
        }
    }

    public void onCreateFixedExpenses(){
        layoutFixedExpenses.removeAllViews();
        Cursor fixedCursor = db.rawQuery("SELECT * FROM fixedExpenses", null);
        while(fixedCursor.moveToNext()){
            int id = fixedCursor.getInt(0);
            String str = fixedCursor.getString(1) + " - " + fixedCursor.getInt(2) + "원";

            TextView newView = new TextView(getActivity());
            newView.setText(str);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.topMargin = 10;
            newView.setLayoutParams(params);
            newView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder dlgCategory = new AlertDialog.Builder(getActivity());
                    dlgCategory.setMessage("삭제하시겠습니까?");
                    dlgCategory.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            db = myHelper.getWritableDatabase();
                            db.execSQL("DELETE FROM fixedExpenses WHERE id = " + id + ";");
                            db.close();
                            layoutFixedExpenses.removeView(newView);
                        }
                    });
                    dlgCategory.setNegativeButton("취소", null);
                    dlgCategory.show();
                }
            });
            layoutFixedExpenses.addView(newView);
        }
    }

}