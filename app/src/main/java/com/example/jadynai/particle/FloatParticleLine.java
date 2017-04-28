package com.example.jadynai.particle;

import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.Random;

/**
 * @version:
 * @FileDescription:
 * @Author:jing
 * @Since:2017/4/27
 * @ChangeList:
 */

public class FloatParticleLine {

    public static final int ALPHA_MAX = 200;

    // 火花外侧阴影大小
    private static final float BLUR_SIZE = 2.5f;
    private static final String TAG = "FloatParticle";

    private Random mRandom = new Random();
    private Paint mPaint = new Paint();

    private int mWidth, mHeight;
    private float mX, mY;

    private float mStartX, mStartY;

    private float mRadius = 5f;
    private float mDisX;
    private float mDisY;

    private boolean mIsAddX;
    private boolean mIsAddY;
    private float mDistance;


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
        mIsAddX = mRandom.nextBoolean();
        mIsAddY = mRandom.nextBoolean();

        mDisX = mRandom.nextInt(2) + 0.2f;
        mDisY = mRandom.nextInt(2) + 0.3f;

        mDistance = mRandom.nextInt((int) (0.25f * mWidth)) + (0.125f * mWidth);
    }

    public void drawItem(Canvas canvas) {
        if (mX == mStartX) {
            mPaint.setAlpha(ALPHA_MAX);
        }
        canvas.drawCircle(mX += getPNValue(mIsAddX, mDisX), mY += getPNValue(mIsAddY, mDisY), mRadius, mPaint);
        if (judgeInner()) {
            float gapX = mX - mStartX;
            float ratio = 1 - (gapX / mDistance);
            mPaint.setAlpha((int) (255 * ratio));

            if (gapX >= mDistance) {
                resetDisXY();
                return;
            }

            if (mY - mStartY >= mDistance) {
                resetDisXY();
                return;
            }
            return;
        }

        if (judgeOutline()) {
            resetDisXY();
        }
    }

    private boolean judgeInner() {
        float ratio = 0.4f;
        float judgeWL = ratio * mWidth;
        float judgeWR = (1 - ratio) * mWidth;

        float judgeHT = ratio * mHeight;
        float judgeHB = (1 - ratio) * mHeight;

        boolean judgeX = mX >= judgeWL && mX <= judgeWR;
        boolean judgeY = mY >= judgeHT && mY <= judgeHB;
        if (judgeX && judgeY) {
            return true;
        } else {
            return false;
        }
    }

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
    }

    private float getPNValue(boolean isAdd, float value) {
        return isAdd ? value : 0 - value;
    }

    public float getRadius() {
        return mRadius;
    }

    public void setRadius(float radius) {
        mRadius = radius;
    }
}
