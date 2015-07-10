package com.nobug.android.library.util;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by rrobbie on 2015-02-12.
 */
public class NetworkUtil {

    public static boolean isConnected(final Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mobile = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (mobile.isConnected() || wifi.isConnected()) {
            return true;
        }

        return false;
    }

    public static boolean connectIntent(final Context context, final Intent intent) {
        if(isConnected(context)) {
            return true;
        }

/*
        AlertUtil.alertOk(context, context.getString(R.string.network_connection), context.getString(R.string.network_error_message), context.getString(R.string.retry),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            Activity activity = (Activity) context;
                            activity.finish();
                            context.startActivity(intent);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
*/
        return false;
    }

}
