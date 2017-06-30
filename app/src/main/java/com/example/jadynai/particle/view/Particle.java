package com.example.jadynai.particle.view;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.Log;
import android.util.TypedValue;

import java.util.Random;

/**
 * @version:
 * @FileDescription:
 * @Author:jing
 * @Since:2017/4/27
 * @ChangeList:
 */

public class Particle {

    private static final String TAG = "FloatParticle";

    public static final int ALPHA_MAX = 160;
    private static final int ALPHA_MIN = 75;

    private Random mRandom = new Random();
    private Paint mPaint;

    private int mWidth, mHeight;
    private float mX, mY;

    private float mStartX, mStartY;

    private float mDisX;
    private float mDisY;

    private boolean mIsAddX;
    private boolean mIsAddY;

    private Bitmap mDrawBitmap;
    private Matrix mBitmapMatrix;

    private float mBitmapCenterX, mBitmapCenterY;
    private int mDrawBitmapWidth;
    private int mDrawBitmapHeight;

    private float mDegrees;
    private float mAddDegree;

    public Particle(Bitmap drawBitmap, Matrix matrix, Paint paint, float x, float y, int width, int height) {
        mDrawBitmap = drawBitmap;
        mBitmapMatrix = matrix;

        mDrawBitmapWidth = drawBitmap.getWidth();
        mDrawBitmapHeight = drawBitmap.getHeight();

        mBitmapCenterX = mDrawBitmapWidth / 2f;
        mBitmapCenterY = mDrawBitmapHeight / 2f;

        mPaint = paint;

        this.mWidth = width;
        this.mHeight = height;
        this.mX = x;
        this.mY = y;

        mStartX = x;
        mStartY = y;

        mIsAddX = mRandom.nextBoolean();
        mIsAddY = mRandom.nextBoolean();

        setRandomParm();
    }

    private void setRandomParm() {
        mDisX = mRandom.nextInt(2) + 1.2f;
        mDisY = mRandom.nextInt(2) + 1.2f;
        mAddDegree = mRandom.nextInt(5) + 3f;
    }

    public void drawItem(Canvas canvas) {
        //绘制
        mBitmapMatrix.reset();
        mBitmapMatrix.preTranslate(mX += getPNValue(mIsAddX, mDisX), mY += getPNValue(mIsAddY, mDisY));
        mBitmapMatrix.preRotate(mDegrees += mAddDegree, mBitmapCenterX, mBitmapCenterY);
        canvas.drawBitmap(mDrawBitmap, mBitmapMatrix, mPaint);
        Log.d(TAG, "mX : " + mX);
        Log.d(TAG, "mY : " + mY);
        judgeOutline();
    }

    private void judgeOutline() {
        boolean judgeX = mX <= 0 || mX >= (mWidth - mDrawBitmapWidth);
        boolean judgeY = mY <= 0 || mY >= (mHeight - mDrawBitmapHeight);
        if (judgeX) {
            mIsAddX = !mIsAddX;
            mIsAddY = mRandom.nextBoolean();
            setRandomParm();
            if (mX <= 0) {
                mX = 0;
            } else {
                mX = mWidth - mDrawBitmapWidth;
            }
            return;
        }
        if (judgeY) {
            mIsAddY = !mIsAddY;
            mIsAddX = mRandom.nextBoolean();
            setRandomParm();
            if (mY <= 0) {
                mY = 0;
            } else {
                mY = mHeight - mDrawBitmapHeight;
            }
        }
    }


    private float getPNValue(boolean isAdd, float value) {
        return isAdd ? value : (0 - value);
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

    public void setDrawBitmap(Bitmap drawBitmap) {
        mDrawBitmap = drawBitmap;
        mDrawBitmapWidth = drawBitmap.getWidth();
        mDrawBitmapHeight = drawBitmap.getHeight();
        mBitmapCenterX = mDrawBitmapWidth / 2f;
        mBitmapCenterY = mDrawBitmapHeight / 2f;
    }
}
