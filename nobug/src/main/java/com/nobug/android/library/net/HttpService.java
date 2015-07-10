package com.nobug.android.library.net;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * Created by rrobbie on 2015-04-13.
 */
public class HttpService {

    private static AsyncHttpClient client = new AsyncHttpClient();

//  =================================================================================================================

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(url, params, responseHandler);
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(url, params, responseHandler);
    }

    public static void delete(Context contex, String url, AsyncHttpResponseHandler responseHandler) {
        client.delete(contex, url, responseHandler);
    }

//  =================================================================================================================

    public static AsyncHttpClient getClient() {
        return client;
    }


}
