package com.nobug.android.library.webview;

/**
 * Created by rrobbie on 2015-02-06.
 */
public interface OnBrowseListener {
    public void onLoadStart(String url);
    public void onLoadFinished(String url);
    public void onLoadFail(String url, int errorCode, String errorMessage);
    public void onLoadProgress(int progress);
}
