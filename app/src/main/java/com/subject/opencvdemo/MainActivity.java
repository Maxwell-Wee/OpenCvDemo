package com.subject.opencvdemo;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.subject.opencvdemo.models.CameraData;
import com.subject.opencvdemo.models.MatData;
import com.subject.opencvdemo.utils.OpenCVHelper;
import com.subject.opencvdemo.views.CameraPreview;
import com.subject.opencvdemo.views.DrawView;

import org.opencv.android.OpenCVLoader;

import java.text.SimpleDateFormat;
import java.util.Date;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

public class MainActivity extends AppCompatActivity {


    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int REQUEST_CAMERA = 1;
    private static final int SIZE = 400;

    static {
        if (!OpenCVLoader.initDebug()) {
            Log.v(TAG, "init OpenCV");
        }
    }

    private PublishSubject<CameraData> subject = PublishSubject.create();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        cameraPreview.setCallback((data, camera) -> {
            CameraData cameraData = new CameraData();
            cameraData.data = data;
            cameraData.camera = camera;
            subject.onNext(cameraData);
        });
        cameraPreview.setOnClickListener(v -> cameraPreview.focus());
        DrawView drawView = findViewById(R.id.draw_layout);
        subject.concatMap(
                new Function<CameraData, ObservableSource<? extends MatData>>() {
                    @Override
                    public ObservableSource<? extends MatData> apply(CameraData cameraData) throws Exception {


                        Log.e("----->>>","ObservableSource");


                        return OpenCVHelper.getRgbMat(new MatData(), cameraData.data, cameraData.camera);
                    }
                })
                .concatMap(new Function<MatData, ObservableSource<? extends MatData>>() {
                    @Override
                    public ObservableSource<? extends MatData> apply(MatData matData) throws Exception {
                        return OpenCVHelper.resize(matData, SIZE, SIZE);
                    }
                })
                .map(new Function<MatData, MatData>() {
                    @Override
                    public MatData apply(MatData matData) throws Exception {
                        matData.resizeRatio = (float) matData.oriMat.height() / matData.resizeMat.height();
                        matData.cameraRatio = (float) cameraPreview.getHeight() / matData.oriMat.height();
                        return matData;
                    }
                })
                .concatMap(this::detectRect)
                .compose(mainAsync())
                .subscribe(new Consumer<MatData>() {
                    @Override
                    public void accept(MatData matData) throws Exception {
                        if (drawView != null) {
                            if (matData.cameraPath != null) {
                                drawView.setPath(matData.cameraPath);


                                Log.e("-->>path", matData.cameraPath.toString());
                            } else {
                                drawView.setPath(null);
                            }
                            drawView.invalidate();
                        }
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
}
