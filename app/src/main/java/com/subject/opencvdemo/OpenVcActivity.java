package com.subject.opencvdemo;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgproc.CLAHE;
import org.opencv.imgproc.Imgproc;



import java.nio.ByteBuffer;

public class OpenVcActivity extends AppCompatActivity {




    ImageView img;
    Bitmap original, grayscale, histogram, resize, threshold;
    int fixedwidth = 480;
    int fixedheight = 800;
    Button btn_original, btn_grayscale, btn_histogram, btn_resize, btn_threshold;
    Mat rgbMat;
    Mat grayMat;
    Mat CLAHEmat;

    static {
        System.loadLibrary("opencv_java3");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opencv_demo);

        img = (ImageView) findViewById(R.id.imageView);

        btn_original = (Button) findViewById(R.id.btn_original);
        btn_grayscale = (Button) findViewById(R.id.btn_grayscale);
        btn_histogram = (Button) findViewById(R.id.btn_histogram);
        btn_resize = (Button) findViewById(R.id.btn_resize);
        btn_threshold = (Button) findViewById(R.id.btn_threshold);

        original = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);

        btn_original.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img.setImageBitmap(original);
            }
        });

        btn_grayscale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
/*grayscale = getGrayscale(original);
img.setImageBitmap(grayscale);*/
                grayscale = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);

                rgbMat = new Mat();
                Utils.bitmapToMat(grayscale, rgbMat);

                grayMat = new Mat(grayscale.getHeight(), grayscale.getWidth(),
                        CvType.CV_8U, new Scalar(1));
                Imgproc.cvtColor(rgbMat, grayMat, Imgproc.COLOR_RGB2GRAY, 1);
                Utils.matToBitmap(grayMat, grayscale);
                img.setImageBitmap(grayscale);
                Log.e("Width & Height:-", grayscale.getWidth() + "-" + grayscale.getHeight());
            }
        });

        btn_histogram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int lnth = grayscale.getByteCount();

                ByteBuffer dst = ByteBuffer.allocate(lnth);
                grayscale.copyPixelsToBuffer(dst);
                byte[] byteArray = dst.array();

                Log.e("byte array", byteArray + "");
                Mat orgImage = new Mat();
                orgImage.put(0, 0, byteArray);

                Mat labImage = new Mat(grayscale.getHeight(), grayscale.getWidth(), CvType.CV_8UC1);
                Imgproc.cvtColor(orgImage, labImage, Imgproc.COLOR_BGR2Lab);

                CLAHE clahe = Imgproc.createCLAHE();
                CLAHEmat = new Mat(grayscale.getHeight(), grayscale.getWidth(), CvType.CV_8UC1);
                clahe.apply(labImage, CLAHEmat);

                Utils.matToBitmap(CLAHEmat, histogram);
                img.setImageBitmap(histogram);
            }
        });

        btn_threshold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap bmp = BitmapFactory.decodeResource(getResources(),  R.mipmap.ic_launcher_round);

                Mat rgbMat = new Mat();
                Utils.bitmapToMat(bmp, rgbMat);

                Mat grayMat = new Mat(histogram.getHeight(), histogram.getWidth(),
                        CvType.CV_8U, new Scalar(1));
                Imgproc.cvtColor(rgbMat, grayMat, Imgproc.COLOR_RGB2GRAY, 1);
                Mat bwMat = new Mat();

                Imgproc.cvtColor(rgbMat, grayMat, Imgproc.COLOR_RGB2GRAY);
                Imgproc.equalizeHist(grayMat, grayMat);

                Imgproc.threshold(grayMat, bwMat, 127.5, 255.0, Imgproc.THRESH_OTSU);
                Utils.matToBitmap(bwMat, bmp);
                Imgproc.createCLAHE();
                img.setImageBitmap(bmp);
            }
        });
    }


}
