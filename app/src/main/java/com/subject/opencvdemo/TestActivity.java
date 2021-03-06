package com.subject.opencvdemo;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;

public class TestActivity extends AppCompatActivity {


    private RelativeLayout view;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        view = (RelativeLayout) findViewById(R.id.view);

//        gradientChange(view,40000);


    }

    public void  gradientChange(final View view, final long duration) {
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
        ValueAnimator animator = ValueAnimator.ofInt(startColorA,startColorB,startColorC,startColorD,startColorE,startColorF,startColorG,startColorA);
        animator.addUpdateListener(animation -> {
            gradientColor[0] = (int) animation.getAnimatedValue();
        });

        animator.setDuration(duration);
        animator.setEvaluator(new ArgbEvaluator());
        animator.setRepeatCount(-1);
        animator.start();
        ValueAnimator animator2 = ValueAnimator.ofInt(endColorA,endColorB,endColorC,endColorD,endColorE,endColorF,endColorG,endColorA);
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
