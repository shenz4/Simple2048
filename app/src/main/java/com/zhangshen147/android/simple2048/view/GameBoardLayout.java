package com.zhangshen147.android.simple2048.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.RelativeLayout;

import com.zhangshen147.android.simple2048.R;
import com.zhangshen147.android.simple2048.config.GameConfig;
import com.zhangshen147.android.simple2048.enumerate.Action;
import com.zhangshen147.android.simple2048.interfaces.OnGameStatusChangedListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 * @object 游戏主面板
 */
public class GameBoardLayout extends RelativeLayout {

    private static final String TAG = "GameBoard";


    // 存放所有 tile 的数组
    private TileView[][] mTiles;
    // root layout 的宽度、高度、内边距
    private float mLayoutPadding;
    // 瓦片外边距、边长
    private float mTileMargin;
    private float mTileLength;
    // game board 边长
    private float mLayoutLength;
    // 当前分数
    private int mCurrentScore;
    // 手势监听、游戏状态监听
    private OnGameStatusChangedListener mGameStatusListener;
    // others
    private Context mContext;
    private Paint mPaint;
    private boolean once = false;
    private int mLayoutBackgroundColor;
    private int mTileTextSize;

    // Constructor
    public GameBoardLayout(Context context) {
        this(context, null);
    }

    public GameBoardLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }


    public GameBoardLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.GameBoardLayout);
        mLayoutPadding = ta.getDimension(R.styleable.GameBoardLayout_layout_padding, 2);
        mTileMargin = ta.getDimension(R.styleable.GameBoardLayout_tile_margin, 2);
        mLayoutBackgroundColor = ta.getColor(R.styleable.GameBoardLayout_bottom_color, 0);
        mTileTextSize = (int)ta.getDimension(R.styleable.GameBoardLayout_tile_text_size, 0);
        ta.recycle();

        mContext = context;
        mGameStatusListener = (OnGameStatusChangedListener) context;
    }


    @Override
    protected void dispatchDraw(Canvas canvas) {
        if(mPaint == null){
            mPaint = new Paint();
        }
        mPaint.setColor(mLayoutBackgroundColor);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            canvas.drawRoundRect(0, 0 ,mLayoutLength, mLayoutLength, 40, 40, mPaint);
        }

        super.dispatchDraw(canvas);
        newGame();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mLayoutLength = Math.min(getMeasuredWidth(), getMeasuredHeight());
        mTileLength = (mLayoutLength - 2 * mTileMargin * GameConfig.GAME_LEVEL)
                / GameConfig.GAME_LEVEL;

        if (!once) {
            int n = GameConfig.GAME_LEVEL;
            if (mTiles == null) {
                mTiles = new TileView[GameConfig.GAME_LEVEL][GameConfig.GAME_LEVEL];
            }

            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    TileView t = new TileView(getContext(), mTileTextSize);
                    mTiles[i][j] = t;

                    // id 值不能为 0 ，故需加 1
                    t.setId(i * n + j + 1);

                    // 为每个瓦块设置边距
                    RelativeLayout.LayoutParams lp = new LayoutParams((int) mTileLength, (int) mTileLength);
                    lp.setMargins((int) mTileMargin, (int) mTileMargin, (int) mTileMargin, (int) mTileMargin);

                    // 如果不是第一行
                    if (i != 0) {
                        lp.addRule(RelativeLayout.BELOW, mTiles[i - 1][j].getId());
                    }

                    // 如果不是第一列
                    if (j != 0) {
                        lp.addRule(RelativeLayout.RIGHT_OF, mTiles[i][j - 1].getId());
                    }

                    addView(t, lp);
                    Log.e(TAG, String.valueOf(i) + "," + String.valueOf(j));
                    Log.e(TAG, "id=" + String.valueOf(t.getId()));
                }

            }
        }
        once = true;
        setMeasuredDimension((int) mLayoutLength, (int) mLayoutLength);
    }




    public void newGame() {

        int n = GameConfig.GAME_LEVEL;

        mCurrentScore = 0;

        if (mTiles == null) {
            mTiles = new TileView[GameConfig.GAME_LEVEL][GameConfig.GAME_LEVEL];
        }

        for (int i = 0; i < GameConfig.GAME_LEVEL; i++) {
            for (int j = 0; j < GameConfig.GAME_LEVEL; j++) {

                if (mTiles[i][j] == null) {
                    mTiles[i][j] = new TileView(mContext, mTileTextSize);
                }
                mTiles[i][j].setValue(0);
            }
        }
        if (mGameStatusListener != null) {
            mGameStatusListener.onScoreChange(mCurrentScore);
        }

        generateNum();
        generateNum();
    }

    // 生成随机数
    private void generateNum() {

        // 如果游戏结束
        if (checkOver()) {
            Log.e(TAG, "generateNum: ganme over!");
            if (mGameStatusListener != null) {
                mGameStatusListener.onGameOver(mCurrentScore);
            }
            return;
        }

        // 如果格子没满
        if (!isFull()) {
            int n = GameConfig.GAME_LEVEL;
            Random random = new Random();

            int x, y;
            do {
                x = random.nextInt(n);
                y = random.nextInt(n);
            } while (mTiles[x][y].getValue() != 0);

            mTiles[x][y].setValue(Math.random() > 0.8 ? 4 : 2);
            Log.e(TAG, "generateNum: x,y" + String.valueOf(x) + "," + String.valueOf(y));

        }
    }


    /**
     * @return false 棋盘还没有满，可以继续游戏
     */
    private boolean isFull() {
        for (TileView[] items : mTiles) {
            for (TileView item : items) {
                if (item.getValue() == 0) {
                    return false;
                }
            }
        }
        return true;
    }


    /**
     * @return false 游戏还可以继续
     */
    private boolean checkOver() {
        int n = GameConfig.GAME_LEVEL;

        boolean tag = true;
        if (!isFull()) {
            tag = false;
        } else {
            label:
            for (int i = 0; i < n; i++) {

                // 把不为 0 的 tiles 收集起来
                List<TileView> row = new ArrayList<TileView>();
                for (int j = 0; j < n; j++) {
                    if (mTiles[i][j].getValue() != 0) {
                        row.add(mTiles[i][j]);
                    }
                }

                // 若需要移动，则置 tag 为 false;
                if (isRequireMerge(convertListToArray(row, n))) {
                    tag = false;
                    break label;
                }
            }
        }
        return tag;
    }


    public void action(Action action) {

        int n = GameConfig.GAME_LEVEL;
        switch (action) {
            case UP:
                // 上滑，需要按列，从上往下收集非 0 数字
                for (int j = 0; j < n; j++) {

                    // 把这一列的值不为 0 的瓦片收集起来
                    List<TileView> column = new ArrayList<TileView>();
                    for (int i = 0; i < n; i++) {
                        if (mTiles[i][j].getValue() != 0) {
                            column.add(mTiles[i][j]);
                        }
                    }
                    // 转 list 为 array
                    int[] num_array = convertListToArray(column, n);
                    // 若需要移动，则合并
                    if (isRequireMerge(num_array)) {
                        mergeTiles(num_array);
                    }
                    // 在瓦片中放入数组中的新值
                    int count = 0;
                    for (int i = 0; i <= n-1; i++) {
                        mTiles[i][j].setValue(num_array[count]);
                        count++;
                        Log.e(TAG, "action: i,j,v分别为" + String.valueOf(i) + "," + String.valueOf(j) + "," + String.valueOf(num_array[count-1]));
                    }

                }
                break;
            case DOWN:
                // 下滑，需要按列，从下往上收集数字
                for (int j = 0; j < n; j++) {

                    // 把这一列的值不为 0 的瓦片收集起来
                    List<TileView> column= new ArrayList<TileView>();
                    for (int i = n - 1; i >= 0; i--) {
                        if (mTiles[i][j].getValue() != 0) {
                            column.add(mTiles[i][j]);
                        }
                    }
                    // 转 list 为 array
                    int[] num_array = convertListToArray(column, n);
                    // 检测是否需要合并操作，如有进行之
                    if (isRequireMerge(num_array)) {
                        mergeTiles(num_array);
                    }
                    // 在瓦片中放入数组中的新值
                    int count = 0;
                    for (int i = n - 1; i >= 0; i--) {
                        mTiles[i][j].setValue(num_array[count]);
                        count++;
                        Log.e(TAG, "action: i,j,v分别为" + String.valueOf(i) + "," + String.valueOf(j) + "," + String.valueOf(num_array[count-1]));
                    }
                }
                break;
            case LEFT:
                // 左滑，需要按行，从左到右收集数字
                for (int i = 0; i < n; i++) {

                    List<TileView> row = new ArrayList<TileView>();
                    for (int j = 0; j < n; j++) {
                        if (mTiles[i][j].getValue() != 0) {
                            row.add(mTiles[i][j]);
                        }
                    }

                    int[] num_array = convertListToArray(row, n);
                    if (isRequireMerge(num_array)) {
                        mergeTiles(num_array);
                    }

                    int count = 0;
                    for (int j = 0; j < n; j++) {
                        mTiles[i][j].setValue(num_array[count]);
                        count++;
                        Log.e(TAG, "action: i,j,v分别为" + String.valueOf(i) + "," + String.valueOf(j) + "," + String.valueOf(num_array[count-1]));
                    }
                }
                break;
            case RIGHT:
                // 右滑，需要按行，从右到左收集数字
                for (int i = 0; i < n; i++) {

                    List<TileView> row = new ArrayList<TileView>();
                    for (int j = n-1; j >= 0; j--) {
                        if (mTiles[i][j].getValue() != 0) {
                            row.add(mTiles[i][j]);
                        }
                    }

                    int[] num_array = convertListToArray(row, n);

                    if (isRequireMerge(num_array)) {
                        mergeTiles(num_array);
                    }

                    int count = 0;
                    for (int j = n-1; j >= 0; j--) {
                        mTiles[i][j].setValue(num_array[count]);
                        count++;
                        Log.e(TAG, "action: i,j,v分别为" + String.valueOf(i) + "," + String.valueOf(j) + "," + String.valueOf(num_array[count-1]));
                    }
                }
        }
        generateNum();

    }


    /**
     * 把 arrayList 转存到固定大小的数组中，方便操作
     * @param sourceList 需转换的 list
     * @param expectedSzie 目标数组的大小
     */
    private int[] convertListToArray(List<TileView> sourceList, int expectedSzie){

        int[] destArray = new int[expectedSzie];

        // 存非 0 的
        for (int k = 0; k < sourceList.size(); k++) {
            destArray[k] = sourceList.get(k).getValue();
        }
        // 有空位则补 0
        for ( int k = sourceList.size(); k < expectedSzie; k++){
            destArray[k] = 0;
        }
        return destArray;
    }


    boolean isRequireMerge(int[] array) {
        for (int i = 0; i < array.length - 1; i++) {
            if (array[i] == array[i+1]) {
                return true;
            }
        }
        return false;
    }


    void mergeTiles(int[] array) {

        for (int i = 0; i < array.length - 1; i++) {
            if (array[i] == array[i+1]) {

                // 值翻倍
                int val = array[i] * 2;
                array[i] = val;
                // 加分
                mCurrentScore += val;
                if (mGameStatusListener != null){
                    mGameStatusListener.onScoreChange(mCurrentScore);
                }
                // 前移
                for (int k = i + 1; k < array.length - 1; k++) {
                    array[k] = array[k+1];
                }
                // 最后一位置0
                array[array.length - 1] = 0;
            }
        }
    }

}
