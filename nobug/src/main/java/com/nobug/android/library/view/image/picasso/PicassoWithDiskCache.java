package com.nobug.android.library.view.image.picasso;

import android.content.Context;

import com.squareup.picasso.Downloader;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.UrlConnectionDownloader;

import java.io.File;

/**
 * Created by rrobbie on 2015-01-19.
 */
public class PicassoWithDiskCache {

    private static final String BIG_CACHE_PATH                   = "imageCache";
    private static final int    MAX_DISK_CACHE_SIZE              = 32 * 1024 * 1024;
    
	private static Picasso instance = null;

    public static Picasso with(Context c) {
		if(instance == null) {
			synchronized (Picasso.class) {
				if(instance == null) {
					instance = new Picasso.Builder(c).downloader(createBigCacheDownloader(c)).build(); 
				}
			}
		}
		return instance;
	}

    static Downloader createBigCacheDownloader(Context ctx) {
        try {
            Class.forName("com.squareup.okhttp.OkHttpClient");
            File cacheDir = createDefaultCacheDir(ctx, BIG_CACHE_PATH);
            long cacheSize = MAX_DISK_CACHE_SIZE;
            OkHttpDownloader downloader = new OkHttpDownloader(cacheDir, cacheSize);
            return downloader;
        } catch (Exception e) {
            return new UrlConnectionDownloader(ctx);
        }
    }

    static File createDefaultCacheDir(Context context, String path) {
        File cacheDir = context.getExternalCacheDir();
        if (cacheDir == null)
            cacheDir = context.getCacheDir();
        File cache = new File(cacheDir, path);
        if (!cache.exists()) {
            cache.mkdirs();
        }
        return cache;
    }

}
