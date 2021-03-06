package com.example.savemoney;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class DialogFixedExpenses extends DialogFragment {

    EditText name, amount;
    MyDialogListener listener;

    public static DialogFixedExpenses newInstance(MyDialogListener listener) {
        DialogFixedExpenses fragment = new DialogFixedExpenses();
        fragment.listener = listener;
        return fragment;
    }

    public interface MyDialogListener {
        void myCallback(String name, int amount);
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@NonNull Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_fixed_expenses, null);
        name = (EditText) view.findViewById(R.id.name);
        amount = (EditText) view.findViewById(R.id.amount);
        builder.setView(view).setPositiveButton("입력", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.myCallback(name.getText().toString(), Integer.parseInt(amount.getText().toString()));
            }
        });
        return builder.create();
    };


}