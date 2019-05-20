package com.subject.opencvdemo.views;

import android.animation.TypeEvaluator;
import android.graphics.Point;

public class FloatTypeEvaluator implements TypeEvaluator {

    @Override
    public Object evaluate(float fraction, Object startValue, Object endValue) {
        Point startPoint = (Point) startValue;
        Point endPoint = (Point) endValue;
//      直线公式
        float mX = startPoint.x + fraction * (endPoint.x - startPoint.x);
        float mY = startPoint.y + fraction * (endPoint.y - startPoint.y);
        Point point = new Point((int)mX, (int)mY);
//      这里会产生从开始画滑动到结束的所有坐标
        return point;
    }
}
