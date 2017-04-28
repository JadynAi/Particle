package com.example.jadynai.particle;

import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.util.Log;

import java.util.Random;

/**
 * @version:
 * @FileDescription:
 * @Author:jing
 * @Since:2017/4/27
 * @ChangeList:
 */

public class FloatParticle {

    // 火花外侧阴影大小
    private static final float BLUR_SIZE = 5.0F;
    private static final String TAG = "FloatParticle";

    private PointF start;
    private Point end;
    private Point c1;
    private Point c2;
    private Random random = new Random();
    private Paint mPaint = new Paint();

    private static final float DISTANCE = 255;
    private float mCurDistance = 0;
    private static final float MOVE_PER_FRAME = 1f;

    private int mWidth;
    private int mHeight;
    private float mX;
    private float mY;

    private float mRadius = 5f;

    public FloatParticle(float x, float y, int width, int height) {
        this.mWidth = width;
        this.mHeight = height;
        this.mX = x;
        this.mY = y;

        // 打开抗锯齿
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.WHITE);
        /*
         * 设置画笔样式为填充 Paint.Style.STROKE：描边 Paint.Style.FILL_AND_STROKE：描边并填充
         * Paint.Style.FILL：填充
         */
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.FILL);
        // 设置外围模糊效果
        mPaint.setMaskFilter(new BlurMaskFilter(BLUR_SIZE, BlurMaskFilter.Blur.SOLID));
    }


    public void drawItem(Canvas canvas) {
        // 更新赛贝尔曲线点
        if (mCurDistance == 0) {
            start = new PointF(mX, mY);
            end = getRandomPoint((int) start.x, (int) start.y, (int) DISTANCE);// 取值范围distance
            c1 = getRandomPoint((int) start.x, (int) start.y, random.nextInt(mWidth / 2)); // 取值范围width/2
            c2 = getRandomPoint(end.x, end.y, random.nextInt(mWidth / 2));// 取值范围width/2
        }

        // 计算塞贝儿曲线的当前点
        float t = mCurDistance / DISTANCE;
        PointF bezierPoint = CalculateBezierPoint(t, start, c1, c2, end);
        mX = bezierPoint.x;
        mY = bezierPoint.y;

        mCurDistance += MOVE_PER_FRAME;

//        mPaint.setAlpha((int) (DISTANCE - mCurDistance));

        if (mCurDistance >= DISTANCE) {
            mCurDistance = 0;
        }
        Log.d(TAG, "drawItem: mx : " + mX);
        Log.d(TAG, "drawItem: mY : " + mY);


        canvas.drawCircle(mX, mY, mRadius, mPaint);
    }

    /**
     * 计算塞贝儿曲线
     *
     * @param t  时间，范围0-1
     * @param s  起始点
     * @param c1 拐点1
     * @param c2 拐点2
     * @param e  终点
     * @return 塞贝儿曲线在当前时间下的点
     */
    private PointF CalculateBezierPoint(float t, PointF s, Point c1, Point c2, Point e) {
        float u = 1 - t;
        float tt = t * t;
        float uu = u * u;
        float uuu = uu * u;
        float ttt = tt * t;

        PointF p = new PointF((s.x * uuu), (s.y * uuu));
        p.x += 3 * uu * t * c1.x;
        p.y += 3 * uu * t * c1.y;
        p.x += 3 * u * tt * c2.x;
        p.y += 3 * u * tt * c2.y;
        p.x += ttt * e.x;
        p.y += ttt * e.y;

        return p;
    }

    /**
     * 根据基准点获取指定范围为半径的随机点
     */
    private Point getRandomPoint(int baseX, int baseY, int r) {
        if (r <= 0) {
            r = 1;
        }
        int x = random.nextInt(r);
        int y = (int) Math.sqrt(r * r - x * x);

        x = baseX + getRandomPNValue(x);
        y = baseY + getRandomPNValue(y);

//        if (x > width || x < 0 || y > height || y < 0) {
//            return getRandomPoint(baseX, baseY, r);
//        }

        if (x > mWidth) {
            x = mWidth - r;
        } else if (x < 0) {
            x = r;
        } else if (y > mHeight) {
            y = mHeight - r;
        } else if (y < 0) {
            y = r;
        }

        return new Point(x, y);
    }

    /**
     * 获取随机正负数
     */
    private int getRandomPNValue(int value) {
        return random.nextBoolean() ? value : 0 - value;
    }

    public float getRadius() {
        return mRadius;
    }

    public void setRadius(float radius) {
        mRadius = radius;
    }
}
