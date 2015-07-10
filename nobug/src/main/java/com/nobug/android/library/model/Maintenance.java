package com.nobug.android.library.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by rrobbie on 2015-02-09.
 */
public class Maintenance {

    public String message = null;
    public String start_date = null;
    public String end_date = null;
    public String link = null;

    public Maintenance(JSONObject data) {
        try {
            JSONObject item = data.getJSONObject("maintenance");
            message = item.getString("message");
            start_date = item.getString("start_date");
            end_date = item.getString("end_date");
            link = item.getString("link");
        } catch(JSONException e) {
            e.printStackTrace();
        }
    }



}
