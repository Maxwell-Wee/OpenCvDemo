package com.subject.opencvdemo;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.AppOpsManager;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Binder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

import java.lang.reflect.Method;

public class TestActivity extends AppCompatActivity {


    private RelativeLayout view;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        view = (RelativeLayout) findViewById(R.id.view);


        Animation animation = AnimationUtils.loadAnimation(this,R.anim.view_alpha_anim);
        view.startAnimation(animation);





    }

}
