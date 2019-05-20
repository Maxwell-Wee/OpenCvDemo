package com.subject.opencvdemo.application;

import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Toast;

import com.subject.opencvdemo.TestActivity;
import com.subject.opencvdemo.views.DragFloatActionButton;
import com.subject.opencvdemo.views.FlatingMovebutton;

import org.opencv.android.Utils;

public class OpenCvApplication extends Application {
    public static Context mContext;

    public WindowManager wm;
    private com.subject.opencvdemo.views.FlatingMovebutton customeMovebutton;
    private com.subject.opencvdemo.views.DragFloatActionButton dragFloatActionButton;

    private WindowManager.LayoutParams wmParams = new WindowManager.LayoutParams();

    public WindowManager.LayoutParams getParams() {
        return wmParams;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mContext = this;
        setFloatingButton();

    }


    private void setFloatingButton() {
        wm = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = getResources().getDisplayMetrics();
        int widthPixels = dm.widthPixels;
        int heightPixels = dm.heightPixels;
        wmParams = getParams();
        wmParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        wmParams.format = PixelFormat.RGBA_8888;//设置背景图片
        wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;//
        wmParams.gravity = Gravity.LEFT | Gravity.TOP ;//

        wmParams.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 200, getResources().getDisplayMetrics()); //设置图片大小
        wmParams.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 200, getResources().getDisplayMetrics());
        wmParams.x = widthPixels - wmParams.width;
        wmParams.y = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 60, getResources().getDisplayMetrics());

        customeMovebutton = new FlatingMovebutton(getApplicationContext());

        gradientChange(customeMovebutton, 40000);
        wm.addView(customeMovebutton, wmParams);


        customeMovebutton.setOnSpeakListener(() -> {


            Toast.makeText(this, "点击事件", Toast.LENGTH_SHORT).show();





        });
    }

    public void gradientChange(final View view, final long duration) {
        int startColorA = Color.parseColor("#1AADE0");
        int startColorB = Color.parseColor("#1AADE0");
        int startColorC = Color.parseColor("#F40FAB");
        int startColorD = Color.parseColor("#FDCD71");
        int startColorE = Color.parseColor("#C05EF2");
        int startColorF = Color.parseColor("#1A6CDB");
        int startColorG = Color.parseColor("#FDCD71");

        int endColorA = Color.parseColor("#913bae");
        int endColorB = Color.parseColor("#fd5b91");
        int endColorC = Color.parseColor("#efce98");
        int endColorD = Color.parseColor("#913bae");
        int endColorE = Color.parseColor("#09a2e7");
        int endColorF = Color.parseColor("#19dbb0");
        int endColorG = Color.parseColor("#19dbb0");

        final int[] gradientColor = new int[2];
        ValueAnimator animator = ValueAnimator.ofInt(startColorA, startColorB, startColorC, startColorD, startColorE, startColorF, startColorG, startColorA);
        animator.addUpdateListener(animation -> {
            gradientColor[0] = (int) animation.getAnimatedValue();
        });

        animator.setDuration(duration);
        animator.setEvaluator(new ArgbEvaluator());
        animator.setRepeatCount(-1);
        animator.start();
        ValueAnimator animator2 = ValueAnimator.ofInt(endColorA, endColorB, endColorC, endColorD, endColorE, endColorF, endColorG, endColorA);
        animator2.addUpdateListener(animation -> {
            gradientColor[1] = (int) animation.getAnimatedValue();
            view.setBackground(null);
            view.setBackground(changeGradientColor(gradientColor));
        });
        animator2.setDuration(duration);
        animator2.setEvaluator(new ArgbEvaluator());
        animator2.setRepeatCount(-1);
        animator2.start();
    }

    private GradientDrawable changeGradientColor(final int startColor[]) {
        GradientDrawable gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.BR_TL, startColor);
        gradientDrawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);
        return gradientDrawable;
    }


}
