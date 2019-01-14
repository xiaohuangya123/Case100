package com.xhj.case01_pic_cache.widgets;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import java.io.IOException;
import java.io.InputStream;


/**
 * Author: Created by XHJ on 2019/1/13.
 * 种一棵树最好的时间是十年前，其次是现在。
 *
 * 自定义View实现加载超大图片，通过手势拖动查看图片
 */
public class BigImageView extends View implements GestureDetector.OnGestureListener {
    private static final String TAG = "BigImageView";
    private BitmapRegionDecoder mDecoder;
    //绘制区域
    private Rect mRect = new Rect();

    private int mScaledTouchSlop;

    //上一次滑动的坐标
    private int mLastX = 0;
    private int mLastY = 0;

    //图片的宽度和高度
    private int mImageWidth, mImageHeigh;
    // 手势控制器
    private GestureDetector mGestureDetector;
    private BitmapFactory.Options options;

    private Paint mPaint;

    //继承View需要重写4个构造方法 开始
    public BigImageView(Context context) {
        super(context);
        init(context,null);
    }

    public BigImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public BigImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public BigImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }
    //继承View需要重写4个构造方法 结束

    private void init(Context context, AttributeSet attrs){
        mPaint = new Paint();
        mPaint.setAntiAlias(true);// 消除锯齿
        mPaint.setDither(true);//消除抖动
        //设置显示图片的参数，如果对图片质量又要求，采用ARGB_8888模式
        options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;

        mScaledTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        Log.d(TAG,mScaledTouchSlop + "");
        //初始化手势控制器
        mGestureDetector = new GestureDetector(context,this);
        //获取图片的宽高
        InputStream is = null;
        try {
            is = getResources().getAssets().open("chruch.jpg");
            //初始化BitmapRegionDecode，并用它来显示图片
            //如果在decodeStream之前使用is，会导致出错
            // 此时流的起始位置已经被移动过了，需要调用is.reset()来重置，
            // 然后再decodeStream(imgInputStream, null, options)
            mDecoder = BitmapRegionDecoder.newInstance(is,false);
            BitmapFactory.Options tempOptions = new BitmapFactory.Options();
            // 设置为true则只获取图片的宽高等信息，不加载进内存
            tempOptions.inJustDecodeBounds = true;
            is.reset();
            BitmapFactory.decodeStream(is,null,tempOptions);
            mImageWidth = tempOptions.outWidth;
            mImageHeigh = tempOptions.outHeight;

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(is != null){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     *重写onMeasure方法
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = getMeasuredWidth();
        int height = getMeasuredHeight();

        int imageWidth = mImageWidth;
        int imageHeight = mImageHeigh;

        //默认显示图片的中心区域
        mRect.left = imageWidth / 2 - width / 2;
        mRect.top = imageHeight / 2 - height / 2;
        mRect.right = mRect.left + width;
        mRect.bottom = mRect.top + height;
    }

    /**
     * 重写onDraw方法
     */
    @Override
    protected void onDraw(Canvas canvas) {
        Bitmap bitmap = mDecoder.decodeRegion(mRect, options);
        canvas.drawBitmap(bitmap,0, 0, mPaint);
    }

    /**
     *把触摸事件交给手势控制器处理
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //把触摸事件交给手势控制器处理
        return mGestureDetector.onTouchEvent(event);
    }

    //实现手势监听的6个方法 开始
    @Override
    public boolean onDown(MotionEvent e) {
        //getX()是表示Widget相对于自身左上角的x坐标,
        // 而getRawX()是表示相对于屏幕左上角的x坐标值
        mLastX = (int)e.getRawX();
        mLastY = (int)e.getRawY();
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        int x = (int)e2.getRawX();
        int y = (int)e2.getRawY();
        move(x,y);
        return true;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        mLastX = (int)e.getRawX();
        mLastY = (int)e.getRawY();
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        int x = (int)e2.getRawX();
        int y = (int)e2.getRawY();
        move(x,y);
        return true;
    }
    //实现手势监听的6个方法 结束

    /**
     * 手势移动的时候，更新图片显示区域`    `
     * @param x
     * @param y
     */
    private void move(int x, int y){
        boolean isInvalidate = false;
        int deltaX = x - mLastX;
        int deltaY = y - mLastY;
        //如果图片宽度大于屏幕宽度
        if(mImageWidth > getWidth()){
            //移动rect区域
            mRect.offset(-deltaX, 0);
            //检查是否到达图片最右端
            if(mRect.right > mImageWidth){
                mRect.right = mImageWidth;
                mRect.left = mImageWidth - getWidth();
            }

            //检查左端
            if(mRect.left <0){
                mRect.left = 0;
                mRect.right = getWidth();
            }
            isInvalidate = true;
        }
        //如果图片高度大于屏幕高度
        if(mImageHeigh > getHeight()){
            mRect.offset(0, -deltaY);

            //是否到达最底部
            if(mRect.bottom > mImageHeigh){
                mRect.bottom = mImageHeigh;
                mRect.top = mImageHeigh - getHeight();
            }

            //是否到达最顶部
            if(mRect.top < 0){
                mRect.top = 0;
                mRect.bottom = getHeight();
            }
            isInvalidate = true;
        }
        if (isInvalidate) {
            invalidate();
        }
        mLastX = x;
        mLastY = y;
    }
}
