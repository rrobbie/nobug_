package com.nobug.android.library.util;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by rrobbie on 2015-02-27.
 */
public class SerializableUtil {

    public static void writeSerializeObject(Context ctx, String key, Object object) throws IOException {
        FileOutputStream fos = ctx.openFileOutput(getFileName(key), Context.MODE_PRIVATE);
        ObjectOutputStream os = new ObjectOutputStream(fos);
        os.writeObject(object);
        os.close();
    }

    public static Object readSerializeObject(Context ctx, String key) throws IOException, ClassNotFoundException {
        FileInputStream fis = ctx.openFileInput(getFileName(key));
        ObjectInputStream is = new ObjectInputStream(fis);
        Object result = is.readObject();
        is.close();

        return result;
    }

    private static String getFileName(String key) {
        return key + ".data";
    }
}
