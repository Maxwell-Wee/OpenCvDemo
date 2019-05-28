package com.subject.opencvdemo.views;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;

import com.subject.opencvdemo.application.OpenCvApplication;

public class FloatMoveView extends android.support.v7.widget.AppCompatImageView {

    private float mTouchStartX;
    private float mTouchStartY;
    private float mRawX;
    private float mRawY;
    private Context context;
    private WindowManager wm = (WindowManager) getContext().getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
    DisplayMetrics dm = getResources().getDisplayMetrics();

    private WindowManager.LayoutParams wmParams = ((OpenCvApplication) getContext().getApplicationContext()).getParams();
    private float mLastX;
    private float mLastY;
    private float mStartX;
    private float mStartY;
    private long mDownTime;
    private long mUpTime;
    private int mScreenHeight;
    private int mScreenWidth;
    private int mActionBarHeight;

    private Point mEdgePoint[] = new Point[8];


    private OnSpeakListener listener;

    public FloatMoveView(Context context) {
        this(context, null);
    }

    public FloatMoveView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public FloatMoveView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        mActionBarHeight = dp2px(60);


    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        setEdgePoint();

        //获取相对屏幕的坐标，即以屏幕左上角为原点
        mRawX = event.getRawX();
        mRawY = event.getRawY();   //statusHeight是系统状态栏的高度
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:    //按下
                mTouchStartX = event.getX();
                mTouchStartY = event.getY() + dp2px(27.6f);

                mStartX = event.getRawX();
                mStartY = event.getRawY();
                mDownTime = System.currentTimeMillis();
                break;
            case MotionEvent.ACTION_MOVE:   //手指移动
                updateViewPosition();
                break;
            case MotionEvent.ACTION_UP:    //手抬起
                mLastX = event.getRawX();
                mLastY = event.getRawY();
                mUpTime = System.currentTimeMillis();
//                //按下到抬起的时间大于500毫秒,并且抬手到抬手绝对值大于20像素处理点击事件
                if (listener != null && mUpTime - mDownTime < 120 && Math.abs(mStartX - mLastX) < 20.0 && Math.abs(mStartY - mLastY) < 20.0) {   //判断是点击事件而不是移动
                    listener.onSpeakListener();
                }
                Point centerPoint = new Point((int) (event.getRawX() - event.getX()), (int) (event.getRawY() - event.getY()) - dp2px(28.1f));
                double distance = 0.0;
                Point distancePoint = null;
                for (Point point : mEdgePoint) {
                    double currentDistance = getDistance(centerPoint, point);
                    if (distancePoint == null) {
                        distancePoint = point;
                        distance = currentDistance;
                    }
                    if (distance > currentDistance) {
                        distance = getDistance(centerPoint, point);
                        distancePoint = point;
                    }
                }

                ValueAnimator animator = ValueAnimator.ofObject(new FloatTypeEvaluator(), centerPoint, distancePoint).setDuration(250);
                animator.setInterpolator(new LinearInterpolator());
                animator.addUpdateListener(animation -> {
                    Point currentPoint = (Point) animation.getAnimatedValue();
                    wmParams.x = currentPoint.x;
                    wmParams.y = currentPoint.y;
                    wm.updateViewLayout(this, wmParams);
                });
                animator.setRepeatCount(0);
                animator.start();


                break;
        }
        return true;
    }

    private void updateViewPosition() {
        wmParams.x = (int) (mRawX - mTouchStartX);
        wmParams.y = (int) (mRawY - mTouchStartY);
        wm.updateViewLayout(this, wmParams);  //刷新显示
    }

    public void setEdgePoint() {
        if (mScreenHeight != 0) {
            return;
        }
        mScreenWidth = dm.widthPixels - getWidth();
        mScreenHeight = dm.heightPixels - getHeight();

        mEdgePoint[0] = new Point(dp2px(4), mActionBarHeight);
        mEdgePoint[1] = new Point(mScreenWidth / 2, mActionBarHeight);
        mEdgePoint[2] = new Point(mScreenWidth - dp2px(4), mActionBarHeight);
        mEdgePoint[3] = new Point(mScreenWidth - dp2px(4), mScreenHeight / 2);

        mEdgePoint[4] = new Point(mScreenWidth - dp2px(4), mScreenHeight);
        mEdgePoint[5] = new Point(mScreenWidth / 2, mScreenHeight);
        mEdgePoint[6] = new Point(dp2px(4), mScreenHeight);

        mEdgePoint[7] = new Point(dp2px(4), mScreenHeight / 2);
    }

    public interface OnSpeakListener {
        void onSpeakListener();
    }

    public void setOnSpeakListener(OnSpeakListener listener) {
        this.listener = listener;
    }

    public int dp2px(float value) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, getResources().getDisplayMetrics());
    }

    private double getDistance(Point point, Point borderPoint) {
        return Math.sqrt(Math.pow(borderPoint.x - point.x, 2) + Math.pow(borderPoint.y - point.y, 2));
    }
}