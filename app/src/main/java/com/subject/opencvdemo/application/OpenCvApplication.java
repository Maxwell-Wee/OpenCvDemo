package com.subject.opencvdemo.application;

import android.app.Application;
import android.content.Context;

public class OpenCvApplication extends Application {
    public static Context mContext;


    @Override
    public void onCreate() {
        super.onCreate();

        mContext = this;
    }
}
