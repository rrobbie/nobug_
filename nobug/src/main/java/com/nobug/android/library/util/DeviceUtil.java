package com.nobug.android.library.util;

import android.content.Context;

/**
 * Created by rrobbie on 2015-02-10.
 */
public class DeviceUtil {

    private static String T_STORE = "com.skt.skaf.A000Z00040";
    private static String PLAY_STORE = "com.android.vending";

    public static boolean isInstalledFromTStore(Context context, String packageName){
        boolean fromTstore 	= false;
        if(!isInstalledFromGooglePlay(context, packageName) && isInstalledApp(context, T_STORE)){
            fromTstore 	= true;
        }
        return fromTstore;
    }

    public static boolean isInstalledFromGooglePlay(Context context, String packageName){
        boolean fromGooglePlay = true;
        try{
            final String from = context.getPackageManager().getInstallerPackageName(packageName);
            if(from == null || !from.equals(PLAY_STORE)){
                fromGooglePlay = false;
            }
        }catch(Exception ex){
            fromGooglePlay = false;
        }
        return fromGooglePlay;
    }

    public static boolean isInstalledApp(Context context, String packageName) {
        if (context != null && packageName != null) {
            try {
                context.getPackageManager().getInstallerPackageName(packageName);
                return true;
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }

}
