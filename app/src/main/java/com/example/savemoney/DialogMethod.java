package com.example.savemoney;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class DialogMethod extends DialogFragment {

    EditText name;
    String kinds;
    RadioButton credit, debit;
    MyDialogListener listener;

    public static DialogMethod newInstance(MyDialogListener listener) {
        DialogMethod fragment = new DialogMethod();
        fragment.listener = listener;
        return fragment;
    }

    public interface MyDialogListener {
        void myCallback(String name, String kinds);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@NonNull Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_method, null);
        name = (EditText) view.findViewById(R.id.name);
        credit = (RadioButton) view.findViewById(R.id.credit);
        debit = (RadioButton) view.findViewById(R.id.debit);

        builder.setView(view).setPositiveButton("입력", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(credit.isChecked())
                    kinds = "신용카드";
                else if(debit.isChecked())
                    kinds = "체크카드";
                listener.myCallback(name.getText().toString(), kinds);
            }
        });
        return builder.create();
    };


}