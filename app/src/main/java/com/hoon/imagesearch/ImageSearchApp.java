package com.hoon.imagesearch;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

public class ImageSearchApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
    }
}
