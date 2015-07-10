package com.nobug.android.library.webview;

import android.graphics.Color;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient.CustomViewCallback;
import android.webkit.WebView;
import android.widget.FrameLayout;

/**
 * Created by rrobbie on 2015-02-06.
 */
public class FullScreenVideoView implements View.OnKeyListener {

    FrameLayout rootView = null;
    WebView webview = null;
    FrameLayout contentView = null;
    FrameLayout videoView = null;
    CustomViewCallback videoViewCallback = null;

    public FullScreenVideoView(WebView webview, FrameLayout videoView, CustomViewCallback callback) {
        this.webview = webview;
        this.videoView = videoView;
        this.videoViewCallback = callback;
        rootView = (FrameLayout) this.webview.getRootView();
        webview.setOnKeyListener(this);
    }

    public void show() {
        contentView = new FrameLayout(webview.getContext());
        contentView.addView(videoView, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        contentView.setBackgroundColor(Color.BLACK);
        rootView.addView(contentView, FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
    }

    public void hide() {
        rootView.removeView(contentView);
        videoViewCallback.onCustomViewHidden();
        webview.setOnKeyListener(null);
    }

    @Override
    public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
        if(arg1 == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return false;
    }

}
