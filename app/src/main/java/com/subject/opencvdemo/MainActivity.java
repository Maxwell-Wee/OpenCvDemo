package com.subject.opencvdemo;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.subject.opencvdemo.models.CameraData;
import com.subject.opencvdemo.models.MatData;
import com.subject.opencvdemo.utils.OpenCVHelper;
import com.subject.opencvdemo.views.CameraPreview;
import com.subject.opencvdemo.views.DrawView;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.imgproc.Imgproc;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

public class MainActivity extends AppCompatActivity implements Camera.AutoFocusCallback {


    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int REQUEST_CAMERA = 1;
    private static final int SIZE = 400;

    private ImageView imageView;
    private ImageView imageView1;

    static {
        if (!OpenCVLoader.initDebug()) {
        }
    }


    private PublishSubject<CameraData> subject = PublishSubject.create();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.image_croup);
        imageView1 = findViewById(R.id.image_croup1);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},
                    REQUEST_CAMERA);
        } else {
            init();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CAMERA:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    this.runOnUiThread(this::init);
                } else {
                    finish();
                }
                break;
        }
    }

    @SuppressLint("CheckResult")
    private void init() {
        CameraPreview cameraPreview = new CameraPreview(this);
        FrameLayout layout = findViewById(R.id.root_view);
        cameraPreview.init();
        layout.addView(cameraPreview, 0,
                new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));
        cameraPreview.setCallback(new Camera.PreviewCallback() {
            @Override
            public void onPreviewFrame(byte[] data, Camera camera) {
                CameraData cameraData = new CameraData();
                cameraData.data = data;
                cameraData.camera = camera;

                subject.onNext(cameraData);
            }
        });
        cameraPreview.setOnClickListener(v -> cameraPreview.focus());

        DrawView drawView = findViewById(R.id.draw_layout);
        subject.concatMap(
                cameraData -> OpenCVHelper.getRgbMat(new MatData(), cameraData.data, cameraData.camera))
                .map(matData -> {
                    matData.cameraRatio = (float) cameraPreview.getHeight() / matData.oriMat.height();
                    return matData;
                })
                .concatMap(this::detectRect)
                .compose(mainAsync())
                .subscribe(matData -> {
                    if (drawView != null) {
                        if (matData.cameraPath != null) {
                            drawView.setPath(matData.cameraPath);
                            cropPicture(matData.oriMat, matData.points);
                            matData.oriMat.release();
                            matData.monoChrome.release();

                        } else {
                            drawView.setPath(null);
                        }
                        drawView.invalidate();
                    }
                });
    }

    private Observable<MatData> detectRect(MatData mataData) {
        return Observable.just(mataData)
                .concatMap(OpenCVHelper::getMonochromeMat)
                .concatMap(OpenCVHelper::getContoursMat)
                .concatMap(OpenCVHelper::getPath);
    }

    private static <T> ObservableTransformer<T, T> mainAsync() {
        return obs -> obs.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread());
    }


    @Override
    public void onAutoFocus(boolean success, Camera camera) {
        if (success) {
            Log.e("onAutoFocus", "success");
        }
    }


    private void cropPicture(Mat picture, List<Point> pts) {
        Point tl = pts.get(0);
        Point tr = pts.get(1);
        Point br = pts.get(2);
        Point bl = pts.get(3);

        double widthA = Math.sqrt(Math.pow((br.x - bl.x), 2.0) + Math.pow((br.y - bl.y), 2.0));
        double widthB = Math.sqrt(Math.pow((tr.x - tl.x), 2.0) + Math.pow((tr.y - tl.y), 2.0));

        double dw = Math.max(widthA, widthB);
        int maxWidth = (int) (dw);

        double heightA = Math.sqrt(Math.pow((tr.x - br.x), 2.0) + Math.pow((tr.y - br.y), 2.0));
        double heightB = Math.sqrt(Math.pow((tl.x - bl.x), 2.0) + Math.pow((tl.y - bl.y), 2.0));

        double dh = Math.max(heightA, heightB);
        int maxHeight = (int) (dh);

        Mat croppedPic = new Mat(maxHeight, maxWidth, CvType.CV_8UC4);

        Mat src_mat = new Mat(4, 1, CvType.CV_32FC2);
        Mat dst_mat = new Mat(4, 1, CvType.CV_32FC2);

        src_mat.put(0, 0, tl.x, tl.y, tr.x, tr.y, br.x, br.y, bl.x, bl.y);
        dst_mat.put(0, 0, 0.0, 0.0, dw, 0.0, dw, dh, 0.0, dh);

        Mat m = Imgproc.getPerspectiveTransform(src_mat, dst_mat);

        Imgproc.warpPerspective(picture, croppedPic, m, croppedPic.size());


        Bitmap bitmap = Bitmap.createBitmap(croppedPic.cols(), croppedPic.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(croppedPic, bitmap);
        imageView1.setImageBitmap(bitmap);

        m.release();
        src_mat.release();
        dst_mat.release();
    }
}
