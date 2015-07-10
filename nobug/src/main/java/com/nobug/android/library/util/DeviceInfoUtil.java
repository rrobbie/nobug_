package com.nobug.android.library.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by rrobbie on 2015-02-27.
 */
public class DeviceInfoUtil {

    public static String getAppVersion(Context context) throws PackageManager.NameNotFoundException, UnsupportedEncodingException {
        PackageInfo i = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        return URLEncoder.encode(i.versionName, "utf-8");
    }

    public static String getAppVersionCode(Context context) throws PackageManager.NameNotFoundException, UnsupportedEncodingException {
        PackageInfo i = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        return URLEncoder.encode(i.versionCode+"", "utf-8");
    }

    public static String getDeviceName() throws UnsupportedEncodingException {
        return URLEncoder.encode(Build.MODEL, "utf-8");
    }

    public static String getDeviceId(Context context) throws UnsupportedEncodingException {
        String android_id = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        return URLEncoder.encode(android_id, "utf-8");
    }

    public static String getOsVersion() {
        return Build.VERSION.RELEASE;
    }

    public static int getOsSdkInt() {
        return Build.VERSION.SDK_INT;
    }

    public static String getWifiInfo(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        String ssid = wifiInfo.getSSID();
        int rssi = wifiInfo.getRssi();
        String supplicantState = wifiInfo.getSupplicantState().name();
        String msg = String.format("SSID: %s, rssi: %d, ApState: %s", ssid, rssi, supplicantState);
        return msg;
    }

    public static String getPhoneNumber(Context context){
        TelephonyManager mTelephonyMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return mTelephonyMgr.getLine1Number();
    }

    public static String getKeyHash(Context context) {
        PackageInfo packageInfo;
        String key = null;
        try {
            //getting application package name, as defined in manifest
            String packageName = context.getApplicationContext().getPackageName();
            //Retriving package info
            packageInfo = context.getPackageManager().getPackageInfo(packageName, PackageManager.GET_SIGNATURES);
            Log.e("Package Name=", context.getApplicationContext().getPackageName());
            for (Signature signature : packageInfo.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                key = new String(Base64.encode(md.digest(), 0));
                // String key = new String(Base64.encodeBytes(md.digest()));
                Log.e("Key Hash=", key);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("Name not found", e1.toString());
        }
        catch (NoSuchAlgorithmException e) {
            Log.e("No such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("Exception", e.toString());
        }

        return key;
    }
}
