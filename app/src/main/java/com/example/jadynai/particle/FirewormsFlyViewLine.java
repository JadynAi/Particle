package com.example.jadynai.particle;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @version:
 * @FileDescription: 萤火虫飞舞view
 * @Author:jing
 * @Since:2017/4/26
 * @ChangeList:
 */

public class FirewormsFlyViewLine extends SurfaceView implements SurfaceHolder.Callback, Runnable {

    private static final String TAG = "FirewormsFlyView";
    public static final int MAX_NUM = 160;
    private SurfaceHolder mHolder;
    private Canvas mCanvas;

    private Thread mThread;
    private int mMeasuredWidth;
    private int mMeasuredHeight;
    private boolean isRun;

    private Random mRandom = new Random();

    private List<FloatParticleLine> mCircles = new ArrayList<>();

    public FirewormsFlyViewLine(Context context) {
        super(context);
        init(context);
    }

    public FirewormsFlyViewLine(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public FirewormsFlyViewLine(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mHolder = getHolder();
        mHolder.addCallback(this);
        setZOrderOnTop(true);//设置画布  背景透明
        mHolder.setFormat(PixelFormat.TRANSLUCENT);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mMeasuredWidth = getMeasuredWidth();
        mMeasuredHeight = getMeasuredHeight();

        if (mCircles.size() == 0) {
            for (int i = 0; i < MAX_NUM; i++) {
                FloatParticleLine f = new FloatParticleLine(getF() * mMeasuredWidth, getF() * mMeasuredHeight, mMeasuredWidth, mMeasuredHeight);
                f.setRadius(mRandom.nextInt(2) + 1.2f);
                mCircles.add(f);
            }
        }
        isRun = true;
        mThread = new Thread(this);
        mThread.start();
    }

    private float getF() {
        float v = mRandom.nextFloat();
        if (v < 0.2f) {
            return v + 0.2f;
        } else if (v >= 0.85f) {
            return v - 0.2f;
        } else {
            return v;
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        drawBackgound();
        Log.d(TAG, "surfaceChanged: ");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d(TAG, "surfaceDestroyed: ");
        isRun = false;
    }

    @Override
    public void run() {
        while (isRun) {
            try {
                mCanvas = mHolder.lockCanvas(null);
                if (mCanvas != null) {
                    synchronized (mHolder) {
                        // 清屏
                        mCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

                        for (FloatParticleLine circle : mCircles) {
                            circle.drawItem(mCanvas);
                        }
                        // 控制帧数
                        Thread.sleep(25);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (mCanvas != null) {
                    mHolder.unlockCanvasAndPost(mCanvas);
                }
            }
        }
    }

    private void drawBackgound() {
        mCanvas = mHolder.lockCanvas();
        mCanvas.drawColor(Color.TRANSPARENT);
        mHolder.unlockCanvasAndPost(mCanvas);
    }
}
