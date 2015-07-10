package com.nobug.android.library.util;

import java.text.DecimalFormat;

/**
 * Created by rrobbie on 2015-02-27.
 */
public class StringUtil {
    public static String toNumFormat(int num) {
        DecimalFormat decimal = new DecimalFormat("#,###");
        return decimal.format(num);
    }
}
