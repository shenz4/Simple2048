package com.zhangshen147.android.simple2048;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class MainView extends View{

    // 本轮成绩和历史最高分
    private int currentScore;
    private int highestScore;


    // 游戏标题
    private static final String gameTitle = "2048";
    // 常驻界面底部的提示信息
    private static final String bottomNotice= "Swipe to move. 2 + 2 = 4. Reach 2048";
    // 画笔
    private Paint mPaint = new Paint();


    /********界面各组件的规格参数*********/
    // 通用
    private int screenWidth;
    private int screenHeight;

    // 计分板块
    float top_width;

    // 游戏板块
    double game_board_size;

    // 提示板块
    float bottom_width;

    int background_color;
    int text_color;
    int block_background_color;
    int block_text_color;

    // 字体大小
    float title_text_size;
    float block_text_size;


    // const
    private static final String TAG = "MainView";
    private double screenMiddleHeight;
    private double screenMiddleWidth;
    private double grid_size;
    private double grid_margin_size;
    private double game_board_beginX;
    private double game_board_beginY;
    private double title_beginX;
    private double title_beginY;
    private double notice_beginX;
    private double notice_beginY;
    private double highestScore_width;
    private double currentScore_width;
    private double highestScore_beginX;
    private double currentScore_beginX;
    private double highestScore_beginY;
    private double currentScore_beginY;
    private double currentScore_height;
    private double highestScore_height;
    private double revoke_width;
    private double revok_height;
    private double remake_width;
    private double remake_height;
    private double revoke_beginX;
    private double revoke_beginY;
    private double remake_beginX;
    private double remake_beginY;


    public MainView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.MainView);

        // 提取 dimension attrs
//        top_width = ta.getDimension(R.styleable.MainView_top_width, 0);
//        game_board_size = ta.getDimension(R.styleable.MainView_game_board_width, 0);
//        bottom_width = ta.getDimension(R.styleable.MainView_bottom_width, 0);
        // 提取 color attrs
        background_color = ta.getColor(R.styleable.MainView_background_color, 0);
        text_color = ta.getColor(R.styleable.MainView_background_color, 0);
        block_background_color = ta.getColor(R.styleable.MainView_background_color, 0);
        block_text_color = ta.getColor(R.styleable.MainView_background_color, 0);
        // 提取 text size attrs
//        title_text_size = ta.getDimension(R.styleable.MainView_title_text_size, 0);
//        block_text_size = ta.getDimension(R.styleable.MainView_block_text_size, 0);
        //提取 grid size attrs

    }

    @Override
    protected void onSizeChanged(int width, int height, int oldw, int oldh) {
        super.onSizeChanged(width, height, oldw, oldh);
        Log.e(TAG, "onSizeChanged: 屏幕大小为：" + String.valueOf(height) + "*" + String.valueOf(width) );
        getLayoutParameter(width, height);
        prepareBitmap();
    }

    private void prepareBitmap() {

    }

    private void getLayoutParameter(int width, int height ) {
        // 计算每个 little view 的规格参数，回赋给成员变量
        screenWidth = width;
        screenHeight = height;

        // screen 中值点
        screenMiddleWidth = width/2;
        screenMiddleHeight = height/2;

        // grid 规格
        grid_size = width/5;
        grid_margin_size = grid_size/7;

        // game board 区域
        game_board_size = 5*grid_margin_size + 4*grid_size;
        game_board_beginX = screenMiddleWidth - 2.5*grid_margin_size - 2*grid_size;
        game_board_beginY = 2*grid_size;

        // 2048 区域
        title_beginX = game_board_beginX;
        title_beginY = 3*grid_margin_size;

        // notice 区域
        notice_beginX = game_board_beginX;
        notice_beginY = screenHeight - 3*grid_margin_size;

        // highestScore 区域
        highestScore_width = mPaint.measureText("HIGHEST SCORE") + 2*grid_margin_size;
        highestScore_height = highestScore_width*0.8;
        highestScore_beginX = screenWidth - grid_margin_size - highestScore_width;
        highestScore_beginY = title_beginY;

        // currentScore 区域
        currentScore_width = mPaint.measureText("SCORE") + 2*grid_margin_size;
        currentScore_height = highestScore_height;
        currentScore_beginX = highestScore_beginX - grid_margin_size - currentScore_width;
        currentScore_beginY = title_beginY;

        // revoke 按钮
        revoke_width = highestScore_height;
        revok_height = revoke_width;
        revoke_beginX = currentScore_beginX;
        revoke_beginY = currentScore_beginY + currentScore_height + grid_margin_size;

        // remake 按钮
        remake_width = highestScore_height;
        remake_height = remake_width;
        remake_beginX = highestScore_beginX;
        remake_beginY = highestScore_beginY + highestScore_height + grid_margin_size;

    }
}
