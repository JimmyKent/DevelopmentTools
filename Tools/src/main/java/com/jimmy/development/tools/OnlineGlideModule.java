package com.jimmy.development.tools;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.module.GlideModule;

/**
 * Created by jinguochong on 17-3-7.
 */
public class OnlineGlideModule implements GlideModule {
    @Override
    public void applyOptions(final Context context, GlideBuilder glideBuilder) {
        //String downLoadPath = ImageUtil.GLIDE_CACHE_PATH;
        //File cacheLocation = new File(downLoadPath);
        //cacheLocation.mkdirs();
        //glideBuilder.setDiskCache(new DiskLruCacheFactory(ImageUtil.PATH_MEIZU, "glide", ImageUtil.CACHE_SIZE_30_MEGABYTES));

    }

    @Override
    public void registerComponents(Context context, Glide glide) {
    }
}
