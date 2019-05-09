package com.subject.opencvdemo.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.WindowManager;

import com.subject.opencvdemo.R;
import com.subject.opencvdemo.application.OpenCvApplication;

public class CustomeMovebutton extends android.support.v7.widget.AppCompatImageView {
    private final int statusHeight;
    int sW;
    int sH;
    private float mTouchStartX;
    private float mTouchStartY;
    private float x;
    private float y;
    private boolean isMove=false;
    private Context context;
    private WindowManager wm = (WindowManager) getContext().getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
    private WindowManager.LayoutParams wmParams = ((OpenCvApplication) getContext().getApplicationContext()).getParams();
    private float mLastX;
    private float mLastY;
    private float mStartX;
    private float mStartY;
    private long mDownTime;
    private long mUpTime;
    private OnSpeakListener listener;

    public CustomeMovebutton(Context context) {
        this(context,null);
        this.context = context;
    }
    public CustomeMovebutton(Context context, AttributeSet attrs) {
        this(context, attrs,-1);
    }
    public CustomeMovebutton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs,defStyleAttr);
        sW = wm.getDefaultDisplay().getWidth();
        sH = wm.getDefaultDisplay().getHeight();
        statusHeight = getStatusHeight(context);
    }

    /**
     * 状态栏的高度
     *
     */
    public static int getStatusHeight(Context context) {
        int statusHeight = -1;
        try {
            Class clazz = Class.forName("com.android.internal.R$dimen");    //使用反射获取实例
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height")
                    .get(object).toString());
            statusHeight = context.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusHeight;
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //获取相对屏幕的坐标，即以屏幕左上角为原点
        x = event.getRawX();
        y = event.getRawY() - statusHeight;   //statusHeight是系统状态栏的高度
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:    //按下
                mTouchStartX = event.getX();
                mTouchStartY = event.getY();
                mStartX = event.getRawX();
                mStartY = event.getRawY();
                mDownTime = System.currentTimeMillis();
                isMove = false;
                break;
            case MotionEvent.ACTION_MOVE:   //手指移动
                updateViewPosition();
                isMove = true;
                break;
            case MotionEvent.ACTION_UP:    //手抬起
                mLastX = event.getRawX();
                mLastY = event.getRawY();
                mUpTime = System.currentTimeMillis();
                //按下到抬起的时间大于500毫秒,并且抬手到抬手绝对值大于20像素处理点击事件
                if(mUpTime - mDownTime < 500){
                    if(Math.abs(mStartX- mLastX )< 20.0 && Math.abs(mStartY - mLastY) < 20.0){
                        if (listener!=null){
                            listener.onSpeakListener();
                        }
                    }
                }

                break;
        }
        return true;
    }

    private void updateViewPosition() {
        wmParams.x = (int) (x - mTouchStartX);
        wmParams.y = (int) (y- mTouchStartY);
        wm.updateViewLayout(this, wmParams);  //刷新显示
    }

    /**
     * 设置点击回调接口
     */
    public interface OnSpeakListener{
        void onSpeakListener();
    }
    public void setOnSpeakListener(OnSpeakListener listener){
        this.listener=listener;
    }
}