package com.subject.opencvdemo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

public class UseBootPageActivity extends AppCompatActivity {

    private ImageView screenImageView ;

    private String imagePath ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,
                WindowManager.LayoutParams. FLAG_FULLSCREEN);
        setContentView(R.layout.activity_test2);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_use_boot_page);
        screenImageView = findViewById(R.id.screenImageView);

        Intent i  = getIntent();
        imagePath = i.getStringExtra("image_path");

        if (imagePath != null ){
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            screenImageView.setImageBitmap(bitmap);
        }else {
            Log.e("---->>"," imagePath  is  null ");
        }





    }





}
