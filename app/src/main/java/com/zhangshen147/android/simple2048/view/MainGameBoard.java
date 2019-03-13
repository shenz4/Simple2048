package com.zhangshen147.android.simple2048.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.zhangshen147.android.simple2048.R;
import com.zhangshen147.android.simple2048.config.GameConfig;
import com.zhangshen147.android.simple2048.enumerate.Action;
import com.zhangshen147.android.simple2048.enumerate.GameStatus;
import com.zhangshen147.android.simple2048.interfaces.OnGameStatusChangedListener;
import com.zhangshen147.android.simple2048.util.AnimationManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * @author zhangshen
 * @version 1.0
 */
public class MainGameBoard extends RelativeLayout {

    private static final String TAG = "GameBoard";
    // public fields
    public GameStatus mCurrentGameStatus;
    public boolean mIsEnableFling;
    // MainGameBoard View's height and width
    private int mLayoutWidth;
    private int mLayoutHeight;
    // Tile View's margin which makes tiles separate
    private float mTileMargin;
    // private fields
    private TileView[][] mTiles;
    private GestureDetector mGestDetector;
    private Paint mPaint = new Paint();
    private OnGameStatusChangedListener mGameStatusListener;
    private int mCurrentScore;
    private int mTileTextSize;
    private AnimationManager mAnimator;

    // used only once
    private boolean once = true;


    // constructor
    public MainGameBoard(Context context) {
        this(context, null);
    }

    public MainGameBoard(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MainGameBoard(Context context, AttributeSet attrs, int defStyleAttr) {

        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.MainGameBoard);
        mTileMargin = ta.getDimension(R.styleable.MainGameBoard_tile_margin, 2);
        mTileTextSize = (int) ta.getDimension(R.styleable.MainGameBoard_tile_text_size, 0);
        ta.recycle();

        // init
        mAnimator = new AnimationManager(this);
        mCurrentGameStatus = GameStatus.NORMAL;
        mGameStatusListener = (OnGameStatusChangedListener) context;
        mGestDetector = new GestureDetector(getContext(), new MyGestureListener());
        mTiles = new TileView[GameConfig.GAME_LEVEL][GameConfig.GAME_LEVEL];

        addListener();
    }


    public void setCurrentScore(int mCurrentScore) {
        this.mCurrentScore = mCurrentScore;
    }


    @Override
    public void onDrawForeground(Canvas canvas) {
        super.onDrawForeground(canvas);
        Bitmap bitmap = Bitmap.createBitmap(mLayoutWidth, mLayoutHeight, Bitmap.Config.ARGB_8888);
        drawMaskOnForeground(bitmap);
        canvas.drawBitmap(bitmap, 0, 0, null);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mLayoutWidth = mLayoutHeight = Math.min(getMeasuredWidth(), getMeasuredHeight());
        float mTileLength = (mLayoutWidth - 2 * mTileMargin * GameConfig.GAME_LEVEL) / GameConfig.GAME_LEVEL;

        if (once) {
            int n = GameConfig.GAME_LEVEL;
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (mTiles[i][j] == null) {
                        mTiles[i][j] = new TileView(getContext(), mTileTextSize);
                    }
                    TileView currentTile = mTiles[i][j];

                    // id 值不能为 0 ，故需加 1
                    currentTile.setId(i * n + j + 1);

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

                    addView(currentTile, lp);
                }

            }
        }
        once = false;
        setMeasuredDimension(mLayoutWidth, mLayoutHeight);
        Log.d(TAG, "onMeasure: ");
    }


    public void newGame() {
        int n = GameConfig.GAME_LEVEL;
        mCurrentScore = 0;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                mTiles[i][j].setValue(0);
            }
        }
        generateNum();
        generateNum();
        if (mGameStatusListener != null) {
            mGameStatusListener.onGameNormal();
            mGameStatusListener.onScoreChange(mCurrentScore);
        }
        Log.d(TAG, "newGame: ");
    }


    private void addListener() {
        getRootView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mIsEnableFling) {
                    mIsEnableFling = !mIsEnableFling;
                    switch (mCurrentGameStatus) {
                        case FAIL:
                            mCurrentGameStatus = GameStatus.NORMAL;
                            mGameStatusListener.onGameNormal();
                            break;
                        case SUCCESS:
                            mIsEnableFling = !mIsEnableFling;
                            break;
                        case SUCCESS_FINALLY:
                            mCurrentGameStatus = GameStatus.NORMAL;
                            mGameStatusListener.onGameNormal();
                            break;
                    }
                }
            }
        });
    }


    private void drawMaskOnForeground(Bitmap bitmap) {
        int transparentColor = Color.parseColor("#61ffffff");
        String shownText;
        Canvas canvas = new Canvas(bitmap);
        switch (mCurrentGameStatus) {
            case NORMAL:
                break;
            case FAIL:
                canvas.drawColor(transparentColor);
                shownText = getContext().getString(R.string.game_fail);
                drawTextOnForeground(canvas, shownText, mTileTextSize * 1.8);
                break;
            case SUCCESS:
                canvas.drawColor(transparentColor);
                shownText = getContext().getString(R.string.success);
                drawTextOnForeground(canvas, shownText, mTileTextSize * 1.0);
                break;
            case SUCCESS_FINALLY:
                canvas.drawColor(transparentColor);
                shownText = getContext().getString(R.string.perfect_success);
                drawTextOnForeground(canvas, shownText, mTileTextSize * 1.8);
                break;
        }
    }


    private void drawTextOnForeground(Canvas canvas, String text, double textSize) {
        mPaint.setColor(getResources().getColor(R.color.text_brown));
        mPaint.setFakeBoldText(true);
        mPaint.setTextSize((float) textSize);
        mPaint.setTextAlign(Paint.Align.CENTER);
        Rect r = new Rect();
        mPaint.getTextBounds(text, 0, text.length(), r);
        float x = (float) mLayoutWidth / 2;
        float y = (float) mLayoutHeight / 2;
        canvas.drawText(text, x, y, mPaint);
    }


    // 在空瓦片处随机生成 2 或 4
    public void generateNum() {

        if (isHasEmptyPosition()) {
            int n = GameConfig.GAME_LEVEL;
            Random random = new Random();

            int x, y;
            do {
                x = random.nextInt(n);
                y = random.nextInt(n);
            } while (mTiles[x][y].getValue() != 0);

            mTiles[x][y].setValue(Math.random() > 0.8 ? 4 : 2);
            Log.d(TAG, "generateNum: ");
        }
    }


    private boolean isHasEmptyPosition() {
        for (TileView[] items : mTiles) {
            for (TileView item : items) {
                if (item.getValue() == 0) {
                    return true;
                }
            }
        }
        return false;
    }


    private boolean isGameFailed() {
        int n = GameConfig.GAME_LEVEL;

        if (isHasEmptyPosition()) {
            return false;
        } else {

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
                    return false;
                }
            }


            for (int j = 0; j < n; j++) {
                // 把不为 0 的 tiles 收集起来
                List<TileView> row = new ArrayList<TileView>();
                for (int i = 0; i < n; i++) {
                    if (mTiles[i][j].getValue() != 0) {
                        row.add(mTiles[i][j]);
                    }
                }
                // 若需要移动，则置 tag 为 false;
                if (isRequireMerge(convertListToArray(row, n))) {
                    return false;
                }
            }

        }
        return true;
    }


    void attemptToMove(Action action) {
        int n = GameConfig.GAME_LEVEL;
        switch (action) {
            case UP:
                for (int j = 0; j < n; j++) {
                    ArrayBlockingQueue<TileView> queue = new ArrayBlockingQueue<>(n);
                    for (int i = 0; i < n; i++) {
                        if (mTiles[i][j].getValue() != 0) {
                            queue.add(mTiles[i][j]);
                        }
                    }

                    for (int i = 0; i < n; i++) {
                        TileView source = queue.poll();
                        if (source == null) {
                            break;
                        }
                        TileView dest = mTiles[i][j];
                        mAnimator.moveToUp(source, dest);
                    }

                    for (int i = 1; i < n && mTiles[i][j].getValue() != 0; i++) {
                        TileView dest = mTiles[i][j];
                        TileView source = mTiles[i][j + 1];
                        if (dest.getValue() == source.getValue()) {
                            mAnimator.moveToUp(source, dest);
                            dest.setValue(2 * dest.getValue());
                            mCurrentScore += dest.getValue();
                            mGameStatusListener.onScoreChange(mCurrentScore);
                        }
                    }
                }
                break;
            case DOWN:
                for (int j = 0; j < n; j++) {
                    ArrayBlockingQueue<TileView> queue = new ArrayBlockingQueue<>(n);
                    for (int i = n - 1; i >= 0; i--) {
                        if (mTiles[i][j].getValue() != 0) {
                            queue.add(mTiles[i][j]);
                        }
                    }

                    for (int i = n - 1; i >= 0; i--) {
                        TileView source = queue.poll();
                        if (source == null) {
                            break;
                        }
                        TileView dest = mTiles[i][j];
                        mAnimator.moveToDown(source, dest);
                    }

                    for (int i = n - 2; i >= n && mTiles[i][j].getValue() != 0; i--) {
                        TileView dest = mTiles[i][j];
                        TileView source = mTiles[i][j + 1];
                        if (dest.getValue() == source.getValue()) {
                            mAnimator.moveToDown(source, dest);
                            dest.setValue(2 * dest.getValue());
                            mCurrentScore += dest.getValue();
                            mGameStatusListener.onScoreChange(mCurrentScore);
                        }
                    }
                }
                break;
            case LEFT:
                for (int i = 0; i < n; i++) {
                    ArrayBlockingQueue<TileView> queue = new ArrayBlockingQueue<>(n);
                    for (int j = 0; j < n; j++) {
                        if (mTiles[i][j].getValue() != 0) {
                            queue.add(mTiles[i][j]);
                        }
                    }
                    for (int j = 0; j < n; j++) {
                        TileView source = queue.poll();
                        if (source == null) {
                            break;
                        }
                        TileView dest = mTiles[i][j];
                        dest.setValue(dest.getValue());
                        mAnimator.moveToLeft(source, dest);
                    }

                    for (int j = 1; j < n && mTiles[i][j].getValue() != 0; j++) {
                        TileView dest = mTiles[i][j];
                        TileView source = mTiles[i][j - 1];
                        if (dest.getValue() == source.getValue()) {
                            mAnimator.moveToLeft(source, dest);
                            dest.setValue(2 * dest.getValue());
                            mCurrentScore += dest.getValue();
                            mGameStatusListener.onScoreChange(mCurrentScore);
                        }
                    }
                }
                break;
            case RIGHT:
                for (int i = 0; i < n; i++) {
                    ArrayBlockingQueue<TileView> queue = new ArrayBlockingQueue<>(n);
                    for (int j = n - 1; j >= 0; j--) {
                        if (mTiles[i][j].getValue() != 0) {
                            queue.add(mTiles[i][j]);
                        }
                    }

                    for (int j = n - 1; j >= 0; j--) {
                        TileView source = queue.poll();
                        if (source == null) {
                            break;
                        }
                        TileView dest = mTiles[i][j];
                        mAnimator.moveToRight(source, dest);
                    }

                    for (int j = n - 2; j >= n && mTiles[i][j].getValue() != 0; j--) {
                        TileView dest = mTiles[i][j];
                        TileView source = mTiles[i][j + 1];
                        if (dest.getValue() == source.getValue()) {
                            mAnimator.moveToRight(source, dest);
                            dest.setValue(2 * dest.getValue());
                            mCurrentScore += dest.getValue();
                            mGameStatusListener.onScoreChange(mCurrentScore);
                        }
                    }
                }
                break;
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mGestDetector.onTouchEvent(event);
    }


    boolean isRequireMerge(int[] array) {
        for (int i = 0; i < array.length - 1; i++) {
            if (array[i] == array[i + 1]) {
                return true;
            }
        }
        return false;
    }


    /**
     * 把 arrayList 转存到固定大小的数组中，方便操作
     *
     * @param sourceList   需转换的 list
     * @param expectedSize 目标数组的大小
     */
    private int[] convertListToArray(List<TileView> sourceList, int expectedSize) {

        int[] destArray = new int[expectedSize];

        // 存非 0 的
        for (int k = 0; k < sourceList.size(); k++) {
            destArray[k] = sourceList.get(k).getValue();
        }
        // 有空位则补 0
        for (int k = sourceList.size(); k < expectedSize; k++) {
            destArray[k] = 0;
        }
        return destArray;
    }


    private boolean isGameSuccess() {
        if (mCurrentGameStatus != GameStatus.NORMAL) {
            return false;
        }
        for (TileView[] items : mTiles) {
            for (TileView item : items) {
                if (item.getValue() == 2048) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isGameSuccessFinally() {
        if (mCurrentGameStatus != GameStatus.SUCCESS) {
            return false;
        }
        for (TileView[] items : mTiles) {
            for (TileView item : items) {
                if (item.getValue() < 2048) {
                    return false;
                }
            }
        }
        return true;
    }


    void mergeTiles(int[] array) {
        // 若前面的瓦片取值与自身相同，则合并之

        for (int i = 0; i < array.length - 1; i++) {
            if (array[i] == array[i + 1]) {

                // 值翻倍
                int val = array[i] * 2;
                array[i] = val;
                // 加分
                mCurrentScore += val;
                if (mGameStatusListener != null) {
                    mGameStatusListener.onScoreChange(mCurrentScore);
                }
                // 前移
                for (int k = i + 1; k < array.length - 1; k++) {
                    array[k] = array[k + 1];
                }
                // 最后一位置0
                array[array.length - 1] = 0;
            }
        }
    }


    /**
     * @describe 这个手势探测器需要根据 mIsEnableFling 这个成员变量 2 种状态中来回切换，其中：
     * 状态 1 为：响应上下左右滑动事件
     * 状态 2 为：响应触摸事件
     */
    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        private static final String TAG = "MyGestureListener";

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }


        @Override
        public boolean onSingleTapUp(MotionEvent e) {

            // 如果当前状态允许响应滑动事件，说明此时不需要响应触摸事件，凭空消费事件即可
            if (mIsEnableFling) {
                return true;
            }
            if (mCurrentGameStatus == GameStatus.SUCCESS) {
                mCurrentGameStatus = GameStatus.NORMAL;
                mGameStatusListener.onGameNormal();
            }

            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

            // 如果当前状态不允许响应滑动事件，凭空消费事件即可
            if (!mIsEnableFling) {
                return true;
            }

            float x = e2.getX() - e1.getX();
            float y = e2.getY() - e1.getY();
            if (Math.abs(velocityX) >= Math.abs(velocityY)) {
                if (x > GameConfig.FLING_MIN_DISTANCE) {
                    attemptToMove(Action.RIGHT);
                    Log.d(TAG, "onFling: RIGHT");
                } else if (x < -GameConfig.FLING_MIN_DISTANCE) {
                    attemptToMove(Action.LEFT);
                    Log.d(TAG, "onFling: LEFT");
                }

            } else {
                if (y > GameConfig.FLING_MIN_DISTANCE) {
                    attemptToMove(Action.DOWN);
                    Log.d(TAG, "onFling: DOWN");
                } else if (y < -GameConfig.FLING_MIN_DISTANCE) {
                    attemptToMove(Action.UP);
                    Log.d(TAG, "onFling: UP");
                }
            }


            switch (mCurrentGameStatus) {
                case NORMAL:
                    if (isGameFailed()) {
                        mGameStatusListener.onGameFail(mCurrentScore);
                    } else if (isGameSuccess()) {
                        mGameStatusListener.onGameSuccess(mCurrentScore);
                    }
                    break;
                case SUCCESS:
                    if (isGameFailed()) {
                        mGameStatusListener.onGameFail(mCurrentScore);
                    } else if (isGameSuccessFinally()) {
                        mGameStatusListener.onGameSuccessFinally(mCurrentScore);
                    }
                    break;
            }
            return true;
        }
    }
}
