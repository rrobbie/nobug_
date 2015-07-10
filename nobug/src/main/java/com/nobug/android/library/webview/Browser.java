package com.nobug.android.library.webview;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Message;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.DownloadListener;
import android.webkit.GeolocationPermissions.Callback;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ZoomButtonsController;

import java.io.File;

import rrobbie.library.R;

public class Browser extends WebView implements OnBrowseListener {

    Context mContext;
    OnBrowseListener onBrowseListener;
    RelativeLayout layout;
    RelativeLayout popupContainer;
    TextView titleView;

    Browser popup;
    FullScreenVideoView fullVideo = null;

    ProgressBar progressBar;

    public Browser(Context context) {
        super(context);
        mContext = context;
    }

    public Browser(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public Browser(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;

        createProgressBar();
        setWebViewClient(new BrowserWebViewClient(mContext, onBrowseListener));
    }

    public void setOnBrowserListener(OnBrowseListener listener, RelativeLayout layout, RelativeLayout popupContainer, TextView titleView) {
        this.layout = layout;
        this.popupContainer = popupContainer;
        this.titleView = titleView;
        setOnBrowserListener(listener);
    }

    public void setOnBrowserListener(OnBrowseListener listener) {
        this.onBrowseListener = listener;
        setWebViewClient(new BrowserWebViewClient(mContext, onBrowseListener));
    }

    @Override
    public void loadUrl(String url) {
        try {
            Uri uri = Uri.parse(url);
            if(uri.getScheme() == null) {
                url = Protocol.HTTP + url;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.loadUrl(url);
    }

    public void setContext(Context context) {
        mContext = context;
        setWebViewClient(new BrowserWebViewClient(mContext, onBrowseListener));

        Log.d("rrobbie", "setContext");
    }

    public void setBridge(Object obj) {
//        view.addJavascriptInterface(obj, "android");
    }

    //  =================================================================================================================

    protected void setProperties(Browser view, WebChromeClient webClient) {
        view.getSettings().setJavaScriptEnabled(true);
        view.getSettings().setPluginState(WebSettings.PluginState.ON);
        view.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        view.getSettings().setDomStorageEnabled(true);
        view.getSettings().setUseWideViewPort(true);
        view.getSettings().setLoadWithOverviewMode(true);
        view.getSettings().setLoadsImagesAutomatically(true);
        view.getSettings().setBuiltInZoomControls(false);
        view.getSettings().setSupportZoom(false);
        view.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        view.getSettings().setSupportMultipleWindows(true);
        view.getSettings().setUserAgentString(getSettings().getUserAgentString() + " " );
        view.getSettings().setGeolocationEnabled(true);
        view.getSettings().setAllowFileAccess(true);
        view.getSettings().setDatabaseEnabled(true);

        view.addJavascriptInterface(new Bridge(mContext), "android");

        WebChromeClient client = (webClient == null) ? webChromeClient : webClient;
        view.setWebChromeClient(client);
        view.setDownloadListener(downloadListener);
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        return super.onSaveInstanceState();
    }

    @Override
    protected void onFinishInflate() {
        setProperties(this, null);
        disableWebviewZoomControls();
        super.onFinishInflate();
    }

//  =================================================================================================================

    private void createProgressBar() {
        progressBar = new ProgressBar( mContext, null, android.R.attr.progressBarStyleHorizontal );
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 6);
        progressBar.setLayoutParams(params);
        progressBar.setMax(100);
        progressBar.setProgressDrawable( mContext.getResources().getDrawable(R.drawable.progress) );
    }

//  ==================================================================================================================

    private void downwload(String url) {
        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.GINGERBREAD) {
            try {
                Uri uri = Uri.parse(url);
                String fileName = uri.getLastPathSegment();

                DownloadManager.Request request = new DownloadManager.Request(uri);

                File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                file.mkdirs();

                request.setTitle( mContext.getString(R.string.browser_title) );
                request.setDescription(fileName);
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);
                request.setVisibleInDownloadsUi(true);
                DownloadManager manager = (DownloadManager)getContext().getSystemService(Context.DOWNLOAD_SERVICE);
                manager.enqueue(request);
                Toast.makeText(getContext(), mContext.getString(R.string.browser_download), Toast.LENGTH_SHORT).show();


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

//  =================================================================================================================

    private void disableWebviewZoomControls() {
        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            getSettings().setDisplayZoomControls(false);
        } else {
            try {
                ZoomButtonsController zoomButtonsController;
                zoomButtonsController = ((ZoomButtonsController)getClass().getMethod("getZoom", null).invoke(this, null));
                zoomButtonsController.getContainer().setVisibility(View.GONE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

//  ================================================================================================

    private DownloadListener downloadListener = new DownloadListener() {
        @Override
        public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
            try {
                 Intent i = new Intent(Intent.ACTION_VIEW);
                i.setDataAndType(Uri.parse(url), mimetype);
                getContext().startActivity(i);
            } catch (Exception ex) {
                downwload(url);
                ex.printStackTrace();
            }
        }
    };

//  ================================================================================================

    @Override
    public void onLoadStart(String url) {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onLoadFinished(String url) {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onLoadFail(String url, int errorCode, String errorMessage) {
        progressBar.setVisibility(View.GONE);

        String dialogMessage = "";
        switch (errorCode) {
            case WebViewClient.ERROR_BAD_URL:
            case WebViewClient.ERROR_HOST_LOOKUP:
            case WebViewClient.ERROR_FILE_NOT_FOUND:
            case WebViewClient.ERROR_UNSUPPORTED_SCHEME:
                dialogMessage = mContext.getString(R.string.url_connect_failed);
                break;
            case WebViewClient.ERROR_UNKNOWN:
                dialogMessage = mContext.getString(R.string.page_load_failed);
                break;
            default:
                dialogMessage = mContext.getString(R.string.network_error_message);
                break;
        }

        goBack();

        new AlertDialog.Builder(mContext)
                .setTitle( mContext.getString(R.string.app_name) )
                .setMessage(dialogMessage)
                .setNegativeButton(mContext.getString(R.string.alert_confirm), null)
                .show();
    }

    @Override
    public void onLoadProgress(int progress) {
        progressBar.setProgress(progress);
    }

//  ====================================================================================================================================

    private WebChromeClient webChromeClient = new WebChromeClient() {
        @Override
        public void onProgressChanged(WebView view, final int newProgress) {
            post(new Runnable() {
                @Override
                public void run() {
                    if(onBrowseListener != null) {
                        onBrowseListener.onLoadProgress(newProgress);
                    }
                }
            });
            super.onProgressChanged(view, newProgress);
        }

        @Override
        public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
            Dialog dialog = new AlertDialog.Builder(getContext())
                    .setMessage(message)
                    .setPositiveButton(android.R.string.ok, new AlertDialog.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            result.confirm();
                        }
                    })
                    .setCancelable(false)
                    .create();

            dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    result.cancel();
                }
            });
            dialog.show();
            return true;
        }

        @Override
        public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
            Dialog dialog = new AlertDialog.Builder(getContext())
                    .setMessage(message)
                    .setPositiveButton(android.R.string.ok, new AlertDialog.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            result.confirm();
                        }
                    })
                    .setNegativeButton(android.R.string.cancel, new AlertDialog.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            result.cancel();
                        }
                    })
                    .setCancelable(false)
                    .create();

            dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    result.cancel();
                }
            });
            dialog.show();
            return true;
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
        }

        @Override
        public void onShowCustomView(View view, int requestedOrientation, CustomViewCallback callback) {
            onShowCustomView(view, callback);
        }

        @Override
        public void onShowCustomView(View view, CustomViewCallback callback) {
            fullVideo = new FullScreenVideoView(Browser.this, (FrameLayout)view, callback);
            fullVideo.show();
        }

        @Override
        public void onHideCustomView() {
            fullVideo.hide();
            super.onHideCustomView();
        }

        @Override
        public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
            super.onCreateWindow(view, isDialog, isUserGesture, resultMsg);

            createPopUp();
            WebViewTransport transport = (WebViewTransport) resultMsg.obj;
            transport.setWebView(popup);
            resultMsg.sendToTarget();
            return true;
        }

        @Override
        public void onCloseWindow(WebView w) {
            super.onCloseWindow(w);
            closePopUp();
        }

        @Override
        public void onGeolocationPermissionsShowPrompt(String origin, Callback callback) {
            final String myOrigin = origin;
            final Callback myCallback = callback;

            AlertDialog.Builder builder = new AlertDialog.Builder(mContext)
                    .setMessage( R.string.permissions )
                    .setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            myCallback.invoke(myOrigin, true, true);
                        }
                    })
                    .setNegativeButton(R.string.rejection, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            myCallback.invoke(myOrigin, false, false);
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }

    };

    private WebChromeClient popupWebChromeClient = new WebChromeClient() {
        @Override
        public void onProgressChanged(WebView view, final int newProgress) {
            post(new Runnable() {
                @Override
                public void run() {
                    if(onBrowseListener != null) {
                        onBrowseListener.onLoadProgress(newProgress);
                        showProgress(1);
                    }
                }
            });
            super.onProgressChanged(view, newProgress);
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            titleView.setText(title);
            super.onReceivedTitle(view, title);
        }
    };

    private String loadUrl = null;

    private void showProgress(int newProgress) {
        try {
            int len = layout.getChildCount() - 1;

            if( layout.getChildAt(len) != null ) {
                ProgressBar progress = (ProgressBar)layout.getChildAt(len);
                progress.setProgress(newProgress);

                int visible = ( newProgress == 100 || newProgress == 0 ) ? View.GONE : View.VISIBLE;
                progress.setVisibility(visible);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void createPopUp() {
        if( layout == null || popupContainer == null )
            return;

        if( popup == null ) {
            popup = new Browser(mContext);
            setProperties(popup, popupWebChromeClient);
            popup.setWebViewClient(new BrowserWebViewClient(mContext, new OnBrowseListener() {
                @Override
                public void onLoadStart(String url) {
                    loadUrl = url;
                    if( !url.equals(mContext.getString(R.string.blank)) ) {
                        layout.setVisibility(View.VISIBLE);
                    }

                    showProgress(1);
                }

                @Override   public void onLoadFinished(String url) {showProgress(100);}
                @Override   public void onLoadFail(String url, int errorCode, String errorMessage) {showProgress(0);}
                @Override   public void onLoadProgress(int progress) {  }
            }));

            popup.setLayoutParams(this.getLayoutParams());
            popupContainer.addView(popup);
        }

        layout.setVisibility(View.VISIBLE);
    }

    public void closePopUp() {
        if( layout == null || popupContainer == null )
            return;

        layout.setVisibility(View.GONE);
        popup.loadUrl( mContext.getString(R.string.blank) );
    }

//  ============================================================================================


}
