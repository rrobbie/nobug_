package com.nobug.android.library.util;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;

import rrobbie.library.R;


public class AlertUtil {

    public static void alert(Context context,String title, String message) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setPositiveButton(R.string.alert_confirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.show();
    }

    public static void alertOk(Context context, String title, String message, String confirm, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);

        if( title != null ) {
            dialog.setTitle(title);
        }

        dialog.setMessage(message).setCancelable(false);
        String onText = confirm != null ? confirm : context.getString(R.string.alert_confirm);
        dialog.setPositiveButton(onText, listener);

        dialog.setNegativeButton(R.string.alert_cancel,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        dialog.show();
    }

    public static Dialog alertOkCancel(Context context, String title, String message, String okText, String cacelText,
                                     DialogInterface.OnClickListener okListener, DialogInterface.OnClickListener cancelListener) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);

        if( title != null ) {
            dialog.setTitle(title);
        }

        dialog.setMessage(message).setCancelable(false);

        if( okListener != null ) {
            okText = okText != null ? okText : context.getString(R.string.alert_confirm);
            dialog.setPositiveButton(okText, okListener);
        }

        cacelText = cacelText != null ? cacelText : context.getString(R.string.alert_cancel);

        dialog.setNegativeButton(cacelText, cancelListener);
        return dialog.show();
    }

//  ================================================================================================

}
