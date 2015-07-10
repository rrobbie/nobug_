package com.nobug.android.library.webview;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.webkit.MimeTypeMap;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by rrobbie on 2015-02-06.
 */
public class BrowserWebViewClient extends WebViewClient {

    protected final String IMAGE = "image";
    protected final String M3U8 = ".m3u8";

    protected Context mContext;
    protected OnBrowseListener listener = null;

    public BrowserWebViewClient(Context context, OnBrowseListener listener) {
        mContext = context;
        this.listener = listener;
    }

//  ===============================================================================================

    protected void loadIntent(String url) {
        try {
            Intent intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
            if (mContext.getPackageManager().resolveActivity(intent, 0) == null) {
                Uri uri = Uri.parse(String.format("market://details?id=%s", intent.getPackage()));
                mContext.startActivity(new Intent(Intent.ACTION_VIEW, uri));
            } else {
                mContext.startActivity(intent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void loadRtsp(String url) {
        try {
            mContext.startActivity( new Intent(Intent.ACTION_VIEW, Uri.parse(url)) );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void loadTel(String url) {
        try {
             mContext.startActivity(new Intent(Intent.ACTION_DEFAULT, Uri.parse(url)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void loadMymeType(String url, String mimeType) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.parse(url), mimeType);
            mContext.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void loadM3u8(String url) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.parse(url), MimeType.MPEG);
            mContext.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//  ================================================================================================

    @Override
    public void onPageFinished(WebView view, final String url) {
        if (listener != null) {
            listener.onLoadFinished(url);
        }
        super.onPageFinished(view, url);
    }

    @Override
    public void onPageStarted(WebView view, final String url, Bitmap favicon) {
        if (listener != null) {
            listener.onLoadStart(url);
        }
        super.onPageStarted(view, url, favicon);
    }

    @Override
    public void onReceivedError(WebView view, final int errorCode, final String description, final String failingUrl) {
        if (listener != null) {
            listener.onLoadFail(failingUrl, errorCode, description);
        }
        super.onReceivedError(view, errorCode, description, failingUrl);
    }

    @Override
    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
        super.onReceivedSslError(view, handler, error);
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        if(url.indexOf("___target=_blank") > -1){
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            mContext.startActivity(i);
            return true;
        } else if (url.toLowerCase().startsWith(Protocol.INTENT)) {
            loadIntent(url);
        } else if (url.toLowerCase().indexOf(Protocol.RTSP) != -1) {
            loadRtsp(url);
        } else if (url.toLowerCase().startsWith(Protocol.TEL)) {
            loadTel(url);
        } else if (url.toLowerCase().startsWith(Protocol.HTTP) || url.toLowerCase().startsWith(Protocol.HTTPS)) {
            if (url.toLowerCase().indexOf(M3U8) != -1) {
                loadM3u8(url);
                return true;
            }

            try {
                String fileExtension = MimeTypeMap.getSingleton().getFileExtensionFromUrl(url);
                if (fileExtension != null) {
                    String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension);
                    if (mimeType != null && mimeType.indexOf(IMAGE) != -1) {
                        loadMymeType(url, mimeType);
                        return true;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            view.loadUrl(url);
        } else {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            try {
                mContext.startActivity(intent);
            } catch (Exception e) {
                return super.shouldOverrideUrlLoading(view, url);
            }
        }
        return true;
    }

}