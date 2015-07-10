package com.nobug.android.library.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by rrobbie on 2015-03-03.
 */
public class DateUtil {

    public static String getDateFormat(Date date, String textFormat) {
        SimpleDateFormat dateForamt = new SimpleDateFormat(textFormat);
        return dateForamt.format(date);
    }

}
