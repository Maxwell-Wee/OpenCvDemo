package com.subject.opencvdemo;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class Test2Activity extends AppCompatActivity {


    @Override
    @SuppressWarnings("static-access")
    @SuppressLint("InflateParams")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        /*这个注释不要移除，在打开界面提示的时候，后期还有用*/
//        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
//        intent.setData(Uri.parse("package:" + getPackageName()));
//        startActivityForResult(intent, 100);


    }
}
