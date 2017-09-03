package com.example.coustomtoolbar.ImageCache;

import android.content.Context;

import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.engine.cache.ExternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator;
import com.bumptech.glide.module.AppGlideModule;


/**
 * Created by SEELE on 2017/8/31.
 */

@GlideModule
public class MyGlideModul extends AppGlideModule {
    @Override
    public void applyOptions(final Context context, GlideBuilder builder) {
        MemorySizeCalculator calculator = new MemorySizeCalculator.Builder(context)
                .setMemoryCacheScreens(2)
                .build();
        builder.setMemoryCache(new LruResourceCache(calculator.getMemoryCacheSize()))
                .setDiskCache(new ExternalCacheDiskCacheFactory(context,"GlideCache",200 * 1024 * 1024));
    }
}
