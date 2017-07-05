package com.example.jadynai.particle;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.media.ThumbnailUtils;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.jadynai.particle.view.Particle;

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
    public static final int MAX_NUM = 25;
    private SurfaceHolder mHolder;
    private Canvas mCanvas;

    private Thread mThread;
    private int mMeasuredWidth;
    private int mMeasuredHeight;
    private boolean isRun;

    private Random mRandom = new Random();

    private List<Particle> mCircles = new ArrayList<>();

    private Paint mPaint;
    private Bitmap mStarBitmap;
    private Matrix mMatrix;

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
        mPaint = new Paint();

        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.WHITE);
        mPaint.setAlpha(Particle.ALPHA_MAX);
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.FILL);
        mMatrix = new Matrix();
        mStarBitmap = getParticleBitmap(R.drawable.fish_master);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mMeasuredWidth = getMeasuredWidth();
        mMeasuredHeight = getMeasuredHeight();

        if (mCircles.size() == 0) {
            for (int i = 0; i < MAX_NUM; i++) {
                Particle f = new Particle(mStarBitmap, mMatrix, mPaint, getF() * mMeasuredWidth, getF() * mMeasuredHeight, mMeasuredWidth, mMeasuredHeight);
//                f.setRadius(mRandom.nextFloat() + 1.0f);
                mCircles.add(f);
            }
        }
        isRun = true;
        mThread = new Thread(this);
        mThread.start();
    }

    private float getF() {
        float v = mRandom.nextFloat();
        if (v < 0.15f) {
            return v + 0.15f;
        } else if (v >= 0.85f) {
            return v - 0.15f;
        } else {
            return v;
        }
    }

    private Bitmap getParticleBitmap(@DrawableRes int resId) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        return ThumbnailUtils.extractThumbnail(BitmapFactory.decodeResource(getContext().getResources(), resId, options), dip2px(30), dip2px(30), ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
//        return Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getContext().getResources(), resId, options),
//                dip2px(30), dip2px(30), true);
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

                        for (Particle circle : mCircles) {
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

    private int dip2px(float value) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, getContext().getResources().getDisplayMetrics());
    }
}
