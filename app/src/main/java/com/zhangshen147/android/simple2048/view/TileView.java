package com.zhangshen147.android.simple2048.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.zhangshen147.android.simple2048.R;

/**
 * @author zhangshen
 * @version 1.0
 */
public class TileView extends View{

    private static final String TAG = "TileView";

    private int mValue;
    private String mValueString;
    private Paint mPaint = new Paint();
    private int mTextColor;
    private Rect mBound;

    // root view 的宽度、高度、内边距
    private int mViewWidth;
    private int mViewHeight;
    private float mTextSize;


    public TileView(Context context, float textSize) {
        this(context, null, textSize);
    }
    public TileView(Context context, @Nullable AttributeSet attrs, float textSize) {
        this(context, attrs, 0, textSize);
    }
    public TileView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, float textSize) {
        super(context, attrs, defStyleAttr);
        setWillNotDraw(false);
        mTextColor = Color.parseColor("#88000000");
        mTextSize = textSize;
    }


    void setValue(int val){
        this.mValue = val;
        mValueString = mValue + "";
        mPaint.setTextSize(mTextSize);
        mPaint.setTypeface(Typeface.DEFAULT_BOLD);
        mBound = new Rect();
        mPaint.getTextBounds(mValueString,0, mValueString.length(), mBound);
        invalidate();
        Log.d(TAG, "setValue: value为" + String.valueOf(val));
    }

    int getValue(){
       return mValue;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mViewWidth = getMeasuredWidth();
        mViewHeight = getMeasuredHeight();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int bgColor;
        switch (mValue){
            case 0:
                bgColor = getResources().getColor(R.color.color_0);
                break;
            case 2:
                bgColor = getResources().getColor(R.color.color_2);
                break;
            case 4:
                bgColor = getResources().getColor(R.color.color_4);
                break;
            case 8:
                bgColor = getResources().getColor(R.color.color_8);
                break;
            case 16:
                bgColor = getResources().getColor(R.color.color_16);
                break;
            case 32:
                bgColor = getResources().getColor(R.color.color_32);
                break;
            case 64:
                bgColor = getResources().getColor(R.color.color_64);
                break;
            case 128:
                bgColor = getResources().getColor(R.color.color_128);
                break;
            case 256:
                bgColor = getResources().getColor(R.color.color_256);
                break;
            case 512:
                bgColor = getResources().getColor(R.color.color_512);
                break;
            case 1024:
                bgColor = getResources().getColor(R.color.color_1024);
                break;
            case 2048:
                bgColor = getResources().getColor(R.color.color_2048);
                break;
            default:
                bgColor = getResources().getColor(R.color.color_default);
                break;
        }

        mPaint.setColor(bgColor);
        mPaint.setStyle(Paint.Style.FILL);

        // 绘制背景，版本判断，若 API 21 以上，绘制圆角更美观
        if (Build.VERSION.SDK_INT >= 21){
            canvas.drawRoundRect(0, 0, mViewWidth, mViewHeight, 25, 25 , mPaint);
        }else{
            canvas.drawRect(0, 0, mViewWidth, mViewHeight, mPaint);

        }

        // 绘制数字
        if (mValue != 0){
            mPaint.setColor(mTextColor);
            mPaint.setTextAlign(Paint.Align.CENTER);
            double x = getWidth() / 2.0;
            double y = getHeight() / 2.0 + mBound.height() / 2.0;
            canvas.drawText(mValueString, (float) x, (float) y, mPaint);
        }
        Log.d(TAG, "onDraw: 被调用!");
    }
}
