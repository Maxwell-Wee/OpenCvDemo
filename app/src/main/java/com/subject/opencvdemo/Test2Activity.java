package com.subject.opencvdemo;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.subject.opencvdemo.views.DragFloatActionButton;

public class Test2Activity extends AppCompatActivity {


    DragFloatActionButton dragFloatActionButton;
    TextView textView ;

    @Override
    @SuppressWarnings("static-access")
    @SuppressLint("InflateParams")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test2);


        textView = findViewById(R.id.action_text);


        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Test2Activity.this,TestActivity.class);
                startActivity(intent);




            }
        });




//
//        dragFloatActionButton = findViewById(R.id.dragFloatActionButton);
//
//
//        gradientChange(dragFloatActionButton, 40000);

//
//        /*这个注释不要移除，在打开界面提示的时候，后期还有用*/
//        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
//        intent.setData(Uri.parse("package:" + getPackageName()));
//        startActivityForResult(intent, 100);

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
