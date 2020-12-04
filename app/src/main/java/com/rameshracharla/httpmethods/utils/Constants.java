package com.rameshracharla.httpmethods.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;

import androidx.appcompat.app.AlertDialog;

public class Constants {
    Context context;
    MyProgressDialog Loading;

    public Constants(Context context) {
        this.context = context;
    }

    /* Check Whether Internet is connected or Not */
    public boolean isConnected() {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        boolean isConnected = activeNetwork != null && activeNetwork.isAvailable() &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }

    /* No Internet Connection Dialog */
    public void noInternetConnectionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("No Internet Connection!");
        builder.setMessage("Check your connection or try again.");
        builder.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
                Intent intent = new Intent(Settings.ACTION_SETTINGS);
                context.startActivity(intent);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.setCancelable(true);
        dialog.show();
    }

    /*show Progress Dialogue*/
    public void showProgressDialogue() {
        Loading = new MyProgressDialog(context);

        try {
            Loading.show();
            Loading.setCancelable(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*Dismiss Progress Dialogue*/
    public void dismissProgressDialogue() {
        if (Loading.isShowing()) {
            try {
                Loading.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}
