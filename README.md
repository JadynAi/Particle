> 原创文章，转载请注明出处
*我的博客地址--->* [这里！这里！](http://ailoli.me/)

## 新增解决方案，不再继承SurfaceView，而是继承自View。文章地址：[新的粒子动画](http://ailoli.me/2017/06/28/%E7%B2%92%E5%AD%90%E5%8D%87%E7%BA%A7/)

## 萤火虫飞舞粒子效果
>本项目中我提供了两种方案，最终呈现的效果如下：

![](http://JadynAi.github.io/img/20170428-blog-particle.gif)

## 实现原理

Android的粒子效果、粒子动画，已经有很多开源的轮子了。作为一个坚定的轮子主义者，我google了大半天，却没有找到这种类似于萤火虫飞舞的效果。只好自己来实现这种效果。

---

相比较普通的View，SurfaceView更加适合这种不断变化的画面，所以选择SurfaceView来实现。现在把思路再重新梳理一下：

- 大小不同的粒子在区域内随机分布
- 粒子做无规则运动，然后消失

##### 粒子区域内随机分布#####

这个简单，我们在callBack的方法内直接循环生成一个粒子的数组即可。方位的话使用Random即可。

```
if (mCircles.size() == 0) {
    for (int i = 0; i < MAX_NUM; i++) {
        FloatParticleLine f = new FloatParticleLine(getF() * mMeasuredWidth, getF() * mMeasuredHeight, mMeasuredWidth, mMeasuredHeight);
        f.setRadius(mRandom.nextInt(2) + 1.2f);
        mCircles.add(f);
    }
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
```

> getF（）方法是限制在区域内取值，mMeasuredWidth、mMeasuredHeight为SurfaceView的宽和高。
>
> 这里的宽和高在粒子对象FloatParticleLine，内会用到。

然后我们在创建一个线程，在run（）方法内做无线循环的绘制即可，为了避免无意义的绘制，可以使用Thread.sleep方法来控制帧数。

```
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
```

isRun的变量我们会在SurfaceView内callBack的surfaceDestroyed方法中置为false

##### 粒子做无规则运动

- 方案一

其实看到这种粒子效果，首先应该想到的就是Canvas了。

在SurfaceView里就是通过不断地循环调用FloatParticleLine类的drawItem（）方法来实现粒子的运动。我第一种方案的实现，就是每一个粒子在被创建出来的时候，就随机选择一个方向开始运动，滑过一定的轨迹之后让其消失就好了。

至于怎么选择随机方向，我这里的做法是，分别随机生成一个x和y轴上的递增或者递减的数值，然后每次在前一次绘制的基础上，x和y分别递增递减，直到运动到屏幕边缘或者是规定的运动距离满足了再消失即可。

```
//随机生成参数
private void setRandomParm() {
    // 2017/5/2-上午10:47 x和y的方向
    mIsAddX = mRandom.nextBoolean();
    mIsAddY = mRandom.nextBoolean();

    // 2017/5/2-上午10:47 x和y的取值
    mDisX = mRandom.nextInt(2) + 0.2f;
    mDisY = mRandom.nextInt(2) + 0.3f;

    // 2017/5/2-上午10:47 内部区域的运动最远距离
    mDistance = mRandom.nextInt((int) (0.25f * mWidth)) + (0.125f * mWidth);
}
```

绘制图形：

```
public void drawItem(Canvas canvas) {
    if (mX == mStartX) {
        mPaint.setAlpha(ALPHA_MAX);
    }
    //绘制
    canvas.drawCircle(mX += getPNValue(mIsAddX, mDisX), mY += getPNValue(mIsAddY, mDisY), mRadius, mPaint);
    //内部区域运动到一定距离消失
    if (judgeInner()) {
        float gapX = Math.abs(mX - mStartX);
        float ratio = 1 - (gapX / mDistance);
        mPaint.setAlpha((int) (255 * ratio));
        mRadius = mStartRadius * ratio;
        if (gapX >= mDistance || mY - mStartY >= mDistance) {
            resetDisXY();
            return;
        }
        return;
    }
    //外部区域运动到屏幕边缘消失
    if (judgeOutline()) {
        resetDisXY();
    }
}

private void resetDisXY() {
        setRandomParm();

        mPaint.setAlpha(0);
        mX = mStartX;
        mY = mStartY;
        mRadius = mStartRadius;
    }
```

> judgeInner()和judgeOutline()是判断区域的方法，内部区域的点和外部区域的店消失时机不同

在透明度为0也就是粒子消失时，让粒子回到原点，再重新选择一个方向，进行下一步运动轨迹。

- 方案二

方案二粒子做的运动是贝塞尔曲线，函数实在网上找到的一个函数。每当粒子做完一次曲线运动后，再随机生成一段新的贝塞尔曲线即可。

思路和方案一的思路都是一样的，无非就是运动的轨迹不同而已。

## 总结

做完之后回头再看，发现这个项目的原理其实并不难，可以说是简单了。但刚开始起步的时候真的还是比较懵的，原因就是没有思路。

所以做任何效果，思路最重要。


