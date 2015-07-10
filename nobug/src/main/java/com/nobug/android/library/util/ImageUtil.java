package com.nobug.android.library.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by rrobbie on 2015-01-27.
 */
public class ImageUtil {

    public static File createImage(String folder, String path) {

        Bitmap bitmap = BitmapFactory.decodeFile(path);
        Bitmap createBitmap = resizeBitmap(bitmap, 2048);
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        createBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);

        try {
            makeDirectory(folder);

            File file = new File(Environment.getExternalStorageDirectory()
                    + File.separator + folder + File.separator
                    + Long.toHexString(System.currentTimeMillis()) + ".jpg");
            file.createNewFile();
            FileOutputStream fo = new FileOutputStream(file);
            fo.write(outStream.toByteArray());
            fo.close();

            return file;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new File(path);
    }

    private static Bitmap resizeBitmap(Bitmap source, int maxResolution) {
        int originalWidth = source.getWidth();
        int originalHeight = source.getHeight();
        int newWidth = originalWidth;
        int newHeight = originalHeight;
        float rate = 0.0f;

        if (originalWidth > originalHeight) {
            if (maxResolution < originalWidth) {
                rate = maxResolution / (float) originalWidth;
                newHeight = (int) (originalHeight * rate);
                newWidth = maxResolution;
            }
        } else {
            if (maxResolution < originalHeight) {
                rate = maxResolution / (float) originalHeight;
                newWidth = (int) (originalWidth * rate);
                newHeight = maxResolution;
            }
        }

        return Bitmap.createScaledBitmap(source, newWidth, newHeight, true);
    }

//  ================================================================================================

    private static void makeDirectory(String folder) {
        String path = Environment.getExternalStorageDirectory() + File.separator + folder;
        File file = new File(path);

        if(!file.exists()) {
            file.mkdirs();
        }
    }

    private static void deleteFiles(File file) {
        if(file.exists()) {
            File[] files = file.listFiles();
            if(files != null) {
                for(int i = 0; i < files.length; ++i) {
                    deleteFiles(files[i]);
                }
            }

            file.delete();
        }
    }

    public static void deleteDirectory(String folder) {
        String path = Environment.getExternalStorageDirectory() + File.separator + folder;
        File file = new File(path);
        if(file.exists()) {
            deleteFiles(file);
        }
    }


}