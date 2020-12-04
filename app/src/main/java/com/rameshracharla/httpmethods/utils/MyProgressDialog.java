package com.rameshracharla.httpmethods.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.rameshracharla.httpmethods.R;


public class MyProgressDialog extends ProgressDialog {

    View rootView;

    public MyProgressDialog(Context context) {
        super(context, R.style.ThemeDialogCustom);
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        rootView = mInflater.inflate(R.layout.progress_dialog, null);
        rootView.setBackgroundColor(Color.TRANSPARENT);
    }

    @Override
    public void show() {
        super.show();
        setContentView(R.layout.progress_dialog);
    }

    @Override
    public void setMessage(CharSequence message) {
        TextView txtM = (TextView) rootView.findViewById(R.id.txtMessage);
        txtM.setText(message);
    }

    ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        try {
            View view = this.findViewById(R.id.txtMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}