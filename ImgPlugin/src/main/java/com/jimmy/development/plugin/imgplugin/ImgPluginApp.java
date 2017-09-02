package com.jimmy.development.plugin.imgplugin;

import android.app.Application;

import com.jimmy.development.plugin.imgplugin.request.RequestInit;

/**
 * Created by jinguochong on 2017/8/30.ØØ
 */

public class ImgPluginApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        RequestInit.initRequest();
    }
}
