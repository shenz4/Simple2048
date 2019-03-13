package com.zhangshen147.android.simple2048.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.zhangshen147.android.simple2048.R;

/**
 * @author zhangshen
 * @version 1.0
 */
public class ScoreView extends View{

    // content to display
    private String mTitle;
    private int mScore;

    // color
    private int mTitleTextColor;
    private int mScoreTextColor;


    // dimension
    private float mWidth;
    private float mHeight;
    private float mTitleTextSize;
    private float mScoreTextSize;

    // others
    private Paint mPaint = new Paint();


    // constructor
    public ScoreView(Context context) {
        this(context, null);
    }
    public ScoreView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public ScoreView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ScoreView);
        for (int i = 0; i < ta.getIndexCount(); i++) {
            int option = ta.getIndex(i);
            switch ( option ){
                case R.styleable.ScoreView_score_color:
                    mScoreTextColor = ta.getColor(option, 0);
                    break;
                case R.styleable.ScoreView_title_color:
                    mTitleTextColor = ta.getColor(option, 0);
                    break;
                case R.styleable.ScoreView_title:
                    mTitle = ta.getString(option);
                    break;
                case R.styleable.ScoreView_title_size:
                    mTitleTextSize = ta.getDimension(option, 0);
                    break;
                case R.styleable.ScoreView_score_size:
                    mScoreTextSize = ta.getDimension(option, 0);
                    break;
            }
        }
        ta.recycle();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawTitle(canvas);
        drawScore(canvas);
    }


    private void drawTitle(Canvas canvas) {
        mPaint.setColor(mTitleTextColor);
        mPaint.setTextSize(mTitleTextSize);
        mPaint.setTypeface(Typeface.DEFAULT_BOLD);

        float titleWidth = mPaint.measureText(mTitle);
        float startX = (mWidth - titleWidth)/2;

        Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
        float centerY = mHeight/4 + Math.abs(fontMetrics.ascent)/2 - fontMetrics.descent/2;

        canvas.drawText(mTitle, startX, centerY, mPaint);
    }

    private void drawScore(Canvas canvas) {
        mPaint.setColor(mScoreTextColor);
        mPaint.setTextSize(mScoreTextSize);
        mPaint.setTypeface(Typeface.DEFAULT_BOLD);

        float titleWidth = mPaint.measureText(String.valueOf(mScore));
        float startX = (mWidth - titleWidth)/2;

        Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
        float centerY = 3 * mHeight / 4 + Math.abs(fontMetrics.ascent)/2 - fontMetrics.descent/2;

        canvas.drawText(String.valueOf(mScore), startX, centerY, mPaint);
    }


    // 供外部调用，动态更新分数
    public void setScore(int score){
        mScore = score;
        invalidate();
    }
}
