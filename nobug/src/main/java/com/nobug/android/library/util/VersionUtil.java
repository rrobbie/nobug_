package com.nobug.android.library.util;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.util.Log;

import java.util.StringTokenizer;

import rrobbie.library.R;
import rrobbie.library.model.Maintenance;
import rrobbie.library.model.Version;

public class VersionUtil {

    static Context mContext;
    static Version version;
    static Maintenance maintenance;

    public static final int NOME = -1;
    public static final int MAJOR = 0;
    public static final int MINOR = 1;
    public static final int POINT = 2;

    public static int compareVersion(String curVersion, String otherVersion) {
        try {
            String[] app = curVersion.split("\\.");
            String[] server = otherVersion.split("\\.");

            if(app.length != server.length)
                return calculateVersion(curVersion, otherVersion);
            else
                return calculateVersion(app, server);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return VersionUtil.NOME;
    }

    private static int calculateVersion(String[] app, String[] server) {
        int size = Math.min(app.length, server.length);

        for (int i = 0; i < size; i++) {
            int a = Integer.parseInt(app[i]);
            int s = Integer.parseInt(server[i]);

            if( a > s  ) {
                return VersionUtil.NOME;
            }

            if (a < s) {
                return i;
            }
        }
        return VersionUtil.NOME;
    }

    private static int calculateVersion(String curVersion, String otherVersion) {
        String app = curVersion.replace(".", "");
        String server = otherVersion.replace(".", "");

        int size = Math.min(app.length(), server.length());

        if (Integer.parseInt(app) < Integer.parseInt(server)) {
            for (int i = 0; i < size; i++) {
                int a = app.charAt(i);
                int s = server.charAt(i);

                if( a > s  ) {
                    return VersionUtil.NOME;
                }

                if (a < s) {
                    return i;
                }
            }
        }
        return VersionUtil.NOME;
    }

//  ===============================================================================================

    public static String getVersionName(Context ctx) {
        try {
            PackageInfo packageInfo = ctx.getPackageManager().getPackageInfo(ctx.getPackageName(), 0);
            return packageInfo.versionName.split(" ")[0];
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int getVersionCode(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

//  ===============================================================================================

    public static int compareVersionNameString(String curVersion, String otherVersion) {
        StringTokenizer curVerNumbers = new StringTokenizer(curVersion, ".");
        StringTokenizer otherVerNumbers = new StringTokenizer(otherVersion, ".");

        do {
            int compareValue = curVerNumbers.nextToken().compareToIgnoreCase(otherVerNumbers.nextToken());

            if(compareValue == 0) {
                continue;
            } else {
                return compareValue*-1;
            }
        }while(curVerNumbers.hasMoreElements() && otherVerNumbers.hasMoreElements());

        if(curVerNumbers.hasMoreElements()) {
            return -1;
        } else if(otherVerNumbers.hasMoreElements()) {
            return 1;
        }
        return 0;
    }

//  ================================================================================================

    private static DialogInterface.OnClickListener okListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            try {
                String marketUrl = (DeviceUtil.isInstalledFromTStore(mContext, mContext.getPackageName())) ? version.url2 : version.url1;
                mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(marketUrl)));
                Activity activity = (Activity) mContext;
                activity.finish();
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    };

    private static DialogInterface.OnClickListener endListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            Activity activity = (Activity) mContext;
            activity.finish();
        }
    };

    private static DialogInterface.OnClickListener showLinkListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            try {
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(maintenance.link));
                mContext.startActivity(i);
                Activity activity = (Activity) mContext;
                activity.finish();
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    };

//  ================================================================================================

    public static int update(Context context, Version item, DialogInterface.OnClickListener cancelListener) {
        mContext = context;
        version = item;

        String appVersion = VersionUtil.getVersionName(context).toString();
        String serverVersion = item.version;

        int value = VersionUtil.compareVersion(appVersion, serverVersion);

        Log.d("rrobbie", "value : " + value + " / appversion : " +appVersion + " / " + serverVersion );

        switch (value) {
            case VersionUtil.MAJOR:
            case VersionUtil.MINOR:
                AlertUtil.alertOkCancel(context, null, context.getString(R.string.update_alert_force), null, null, okListener, endListener  );
                break;

            case VersionUtil.POINT:
                AlertUtil.alertOkCancel(context, null, context.getString(R.string.update_alert_choose), null, null, okListener, cancelListener  );
                break;

            default:
                break;
        }
        return value;
    }

    public static int updateSelection(Context context, Version item, DialogInterface.OnClickListener cancelListener) {
        mContext = context;
        version = item;

        String appVersion = VersionUtil.getVersionName(context).toString();
        String serverVersion = item.version;

        int value = VersionUtil.compareVersionNameString(appVersion, serverVersion);

        switch (value) {
            case VersionUtil.MINOR:
                AlertUtil.alertOkCancel(context, null, context.getString(R.string.update_alert_choose), null, null, okListener, cancelListener  );
                break;
        }
        return value;
    }

    public static void showMaintenance(Context context, Maintenance item) {
        mContext = context;
        maintenance = item;

        String message = String.format("%s\n\n"+ mContext.getString(R.string.maintenance_start_date) + "\n %s\n"
                                            + mContext.getString(R.string.maintenance_end_date) + "\n %s", item.message, item.start_date, item.end_date);

        if( item.link == null || item.link.trim().toString().length() < 1 ) {
            showLinkListener = null;
        }

        AlertUtil.alertOkCancel(context, mContext.getString(R.string.maintenance_title),
                                        mContext.getString(R.string.maintenance_link),
                                        mContext.getString(R.string.maintenance_close),
                                        message, showLinkListener, endListener );
    }


}
