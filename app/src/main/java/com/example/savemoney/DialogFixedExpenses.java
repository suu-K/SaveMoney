package com.example.savemoney;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class DialogFixedExpenses extends DialogFragment {


    EditText name, num;

    View dialog;

    @NonNull
    @Override
    public Dialog onCreateDialog(@NonNull Bundle savedInstanceState) {
        dialog=View.inflate(getActivity(),R.layout.dialog_fixed_expenses,null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("고정지출 추가");
        builder.setView(dialog);
        builder.setPositiveButton("입력", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                name=(EditText) getView().findViewById(R.id.name);
                //num=(EditText) viewGroup.findViewById(R.id.amount);
                        /*db= myHelper.getWritableDatabase();
                        db.execSQL("INSERT into fixedExpenses(name, amount) values('"+fixedExpensesName.getText().toString()+"',"+fixedExpensesNum.getText().toString()+");" );
                        db.close();
                NumIdealSpend.setText(fixedExpensesName.getText().toString());
                Toast.makeText(getActivity(),"변경되었습니다.",Toast.LENGTH_LONG).show();*/

            }
        });
        return builder.create();
    }

}