package com.example.codingtest.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.codingtest.beans.Row;
import com.facebook.cache.disk.DiskCacheConfig;
import com.facebook.common.internal.Supplier;
import com.facebook.common.util.ByteConstants;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.cache.MemoryCacheParams;
import com.facebook.imagepipeline.core.ImagePipelineConfig;

import java.io.File;
import java.util.Iterator;
import java.util.List;


public class Utils {

    /**
     * 5 MB mem cache
     */
    static final int MAX_MEMORY_CACHE_SIZE = 5 * 1024 * 1024;

    /**
     * custom cache for fresco library used to provide custom cache sizes for disk and mem.
     *
     * @param context app context
     */
    public static void initFresco(Context context) {
        String diskCachePath = context.getCacheDir() + "/Fresco/";
        File diskCacheDir = new File(diskCachePath);

        ImagePipelineConfig.Builder builder = ImagePipelineConfig.newBuilder(context);
        final MemoryCacheParams bitmapCacheParams = new MemoryCacheParams(
                MAX_MEMORY_CACHE_SIZE, // Max total size of elements in the cache
                Integer.MAX_VALUE,                     // Max entries in the cache
                MAX_MEMORY_CACHE_SIZE, // Max total size of elements in eviction queue
                Integer.MAX_VALUE,                     // Max length of eviction queue
                Integer.MAX_VALUE);                    // Max cache entry size
        builder.setBitmapMemoryCacheParamsSupplier(
                new Supplier<MemoryCacheParams>() {
                    public MemoryCacheParams get() {
                        return bitmapCacheParams;
                    }
                })
                .setMainDiskCacheConfig(
                        DiskCacheConfig.newBuilder(context)
                                .setBaseDirectoryPath(diskCacheDir)
                                .setBaseDirectoryName("demo")
                                .setMaxCacheSize(40 * ByteConstants.MB)
                                .build());
        ImagePipelineConfig config = builder.build();

        Fresco.initialize(context, config);
    }

    /**
     * Check for data network connectivity.
     *
     * @param context context
     */
    public static boolean isDataNetworkConnected(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        return isConnected;
    }

    /**
     * Iterate through the parsed list and check is title or description is null, if so, remove that item from the list.
     *
     * @param results parsed row items
     * @return processed data set
     */
    public static List<Row> cleanFactsData(List<Row> results) {
        Iterator<Row> iterator = results.iterator();
        while (iterator.hasNext()) {
            Row tempResult = iterator.next();
            if (tempResult.getTitle() == null || tempResult.getDescription() == null) {
                iterator.remove();
            }
        }
        return results;
    }
}
