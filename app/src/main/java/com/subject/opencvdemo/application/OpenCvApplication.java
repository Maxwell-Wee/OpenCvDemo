package com.subject.opencvdemo.application;

import android.app.Application;
import android.content.Context;
import android.view.WindowManager;

public class OpenCvApplication extends Application {
    public static Context mContext;


    private WindowManager.LayoutParams wmParams=new WindowManager.LayoutParams();
    public WindowManager.LayoutParams getParams(){
        return wmParams;
    }
    @Override
    public void onCreate() {
        super.onCreate();

        mContext = this;
    }





}
