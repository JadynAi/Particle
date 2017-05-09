package com.example.jadynai.particle;

import android.content.res.Resources;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.TypedValue;

import java.util.Random;

/**
 * @version:
 * @FileDescription:
 * @Author:jing
 * @Since:2017/4/27
 * @ChangeList:
 */

public class FloatParticleLine {

    private static final String TAG = "FloatParticle";

    private static final int ALPHA_MAX = 200;
    private static final int ALPHA_MIN = 50;

    private static final float INNER_RATIO = 0.2f;

    // 火花外侧阴影大小
    private static final float BLUR_SIZE = 2.5f;
    private static final float DEFAULT_RADIUS = 2f;

    private Random mRandom = new Random();
    private Paint mPaint = new Paint();

    private int mWidth, mHeight;
    private float mX, mY;

    private float mStartX, mStartY;

    private float mRadius = DEFAULT_RADIUS;
    private float mStartRadius = DEFAULT_RADIUS;

    private float mDisX;
    private float mDisY;

    private boolean mIsAddX;
    private boolean mIsAddY;
    private float mDistance;

    private boolean mIsNeedChange = true;

    public FloatParticleLine(float x, float y, int width, int height) {
        this.mWidth = width;
        this.mHeight = height;
        this.mX = x;
        this.mY = y;

        mStartX = x;
        mStartY = y;

        setRandomParm();

        // 打开抗锯齿
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.WHITE);
        mPaint.setAlpha(ALPHA_MAX);
        /*
         * 设置画笔样式为填充 Paint.Style.STROKE：描边 Paint.Style.FILL_AND_STROKE：描边并填充
         * Paint.Style.FILL：填充
         */
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.FILL);
        // 设置外围模糊效果
        mPaint.setMaskFilter(new BlurMaskFilter(BLUR_SIZE, BlurMaskFilter.Blur.SOLID));
    }

    private void setRandomParm() {
        // 2017/5/2-上午10:47 x和y的方向
        mIsAddX = mRandom.nextBoolean();
        mIsAddY = mRandom.nextBoolean();

        // 2017/5/2-上午10:47 x和y的取值
        mDisX = mRandom.nextFloat() + 1.05f;
        mDisY = mRandom.nextFloat() + 1.1f;

        // 2017/5/9-上午11:43 判断移动的最大距离
        if (judgeInner()) {
            mDistance = mRandom.nextInt((int) (0.5f * mWidth)) + (0.25f * mWidth);
        } else {
            if (mIsAddX && mIsAddY) {
                // 右下
                mDistance = getHypotenuse(mWidth - mStartX, mHeight - mStartY);
            } else if (!mIsAddX && mIsAddY) {
                // 左下
                mDistance = getHypotenuse(mStartX, mHeight - mStartY);
            } else if (mIsAddX && !mIsAddY) {
                // 右上
                mDistance = getHypotenuse(mWidth - mStartX, mStartY);
            } else {
                mDistance = getHypotenuse(mStartX, mStartY);
            }
            mDistance = mDistance - 10f;
        }

        mIsNeedChange = mDistance >= (0.4f * getHypotenuse(0.5 * mWidth, 0.5 * mHeight));
    }

    public void drawItem(Canvas canvas) {
        if (mX == mStartX) {
            mPaint.setAlpha(ALPHA_MAX);
        }
        //绘制
        canvas.drawCircle(mX += getPNValue(mIsAddX, mDisX), mY += getPNValue(mIsAddY, mDisY), mRadius, mPaint);
        double moveDis = Math.sqrt(Math.pow(mX - mStartX, 2) + Math.pow(mY - mStartY, 2));
        if (mIsNeedChange) {
            float ratio = (float) (1 - (moveDis / mDistance));
            mPaint.setAlpha((int) (ALPHA_MAX * ratio));
            mRadius = mStartRadius * ratio;
        }
        if (moveDis >= mDistance || mPaint.getAlpha() <= ALPHA_MIN) {
            resetDisXY();
        }
    }

    private boolean judgeInner() {
        float judgeWL = INNER_RATIO * mWidth;
        float judgeWR = (1 - INNER_RATIO) * mWidth;

        float judgeHT = INNER_RATIO * mHeight;
        float judgeHB = (1 - INNER_RATIO) * mHeight;

        boolean judgeX = mX >= judgeWL && mX <= judgeWR;
        boolean judgeY = mY >= judgeHT && mY <= judgeHB;
        if (judgeX && judgeY) {
            return true;
        } else {
            return false;
        }
    }

    @Deprecated
    private boolean judgeOutline() {
        boolean x = mX <= 0 || mX >= (mWidth - 10);
        boolean y = mY <= 0 || mY >= (mHeight - 10);
        if (x || y) {
            return true;
        } else {
            return false;
        }
    }

    private void resetDisXY() {
        setRandomParm();

        mPaint.setAlpha(0);
        mX = mStartX;
        mY = mStartY;
        mRadius = mStartRadius;
    }

    private float getPNValue(boolean isAdd, float value) {
        return isAdd ? value : (0 - value);
    }

    public float getRadius() {
        return mRadius;
    }


    private boolean is = true;

    public void setRadius(float radius) {
        mRadius = dip2Px(radius);
        mStartRadius = mRadius;
    }

    private int dip2Px(float pxValue) {
        int dp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, pxValue, Resources.getSystem().getDisplayMetrics());
        return dp;
    }

    /**
     * @param x
     * @param y
     * @return 斜边
     */
    private float getHypotenuse(double x, double y) {
        return (float) Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
    }
}
