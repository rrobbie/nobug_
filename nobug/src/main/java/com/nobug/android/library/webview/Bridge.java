package com.nobug.android.library.webview;

import android.content.Context;
import android.webkit.JavascriptInterface;

/**
 * Created by rrobbie on 2015-04-07.
 */
public class Bridge implements IBridge {
    Context mContext;

    public Bridge(Context context) {
        mContext = context;
    }

    @Override
    @JavascriptInterface
    public void sample(String url) {
        //  TODO

    }

}
