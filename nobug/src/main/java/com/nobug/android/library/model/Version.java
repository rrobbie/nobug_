package com.nobug.android.library.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by rrobbie on 2015-02-09.
 */
public class Version {

    public String code = null;
    public String url1 = null;
    public String url2 = null;
    public String status = null;
    public String version = null;

    public static Version getItem(JSONObject data) {
        Version item = new Version();
        try {
            item.code = data.getString("code");
            item.url1 = data.getString("url1");
            item.url2 = data.getString("url2");
            item.status = data.getString("status");
            item.version = data.getString("version");
        } catch(JSONException e) {
            e.printStackTrace();
        }
        return item;
    }

}
