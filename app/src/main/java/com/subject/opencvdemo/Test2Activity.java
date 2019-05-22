package com.subject.opencvdemo;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.subject.opencvdemo.views.DragFloatActionButton;

public class Test2Activity extends AppCompatActivity {


    private ImageView imageView;

    RelativeLayout relativeLayout ;

    private  int times = 0;
    @Override
    @SuppressWarnings("static-access")
    @SuppressLint("InflateParams")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,
                WindowManager.LayoutParams. FLAG_FULLSCREEN);
        setContentView(R.layout.activity_test2);
        getSupportActionBar().hide();
        imageView = findViewById(R.id.imageView);

        relativeLayout = findViewById(R.id.relativeLayout);








        imageView.setOnClickListener(v -> {
            relativeLayout(capture(this));
        });




    }




    public static Bitmap convertToBlackWhite(Bitmap bmp) {
        int width = bmp.getWidth(); // 获取位图的宽
        int height = bmp.getHeight(); // 获取位图的高
        int[] pixels = new int[width * height]; // 通过位图的大小创建像素点数组

        bmp.getPixels(pixels, 0, width, 0, 0, width, height);
        int alpha = 0xFF << 24;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int grey = pixels[width * i + j];

                int red = ((grey & 0x00FF0000) >> 16);
                int green = ((grey & 0x0000FF00) >> 8);
                int blue = (grey & 0x000000FF);

                grey = (int) (red * 0.3 + green * 0.59 + blue * 0.11);
                grey = alpha | (grey << 16) | (grey << 8) | grey;
                pixels[width * i + j] = grey;
            }
        }
        Bitmap newBmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);

        newBmp.setPixels(pixels, 0, width, 0, 0, width, height);

        Bitmap resizeBmp = ThumbnailUtils.extractThumbnail(newBmp, 380, 460);
        return resizeBmp;
    }


    public Bitmap capture(Activity activity) {
        activity.getWindow().getDecorView().setDrawingCacheEnabled(true);
        Bitmap bmp = activity.getWindow().getDecorView().getDrawingCache();
        return bmp;
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
