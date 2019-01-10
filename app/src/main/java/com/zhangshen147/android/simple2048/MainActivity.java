package com.zhangshen147.android.simple2048;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.zhangshen147.android.simple2048.config.GameConfig;
import com.zhangshen147.android.simple2048.enumerate.Action;
import com.zhangshen147.android.simple2048.enumerate.GameStatus;
import com.zhangshen147.android.simple2048.interfaces.OnGameStatusChangedListener;
import com.zhangshen147.android.simple2048.view.GameBoardLayout;
import com.zhangshen147.android.simple2048.view.ScoreView;

public class MainActivity extends AppCompatActivity implements OnGameStatusChangedListener {

    private ScoreView mCurrentScoreView;
    private ScoreView mHigestScoreView;
    private ImageView mNewGameButton;
    private GameBoardLayout mGameBoard;

    private GestureDetector mGestDetector;
    private Handler mHandle = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        findView();
        addListener();

        mGestDetector = new GestureDetector(this, new MyGestureListener());
        mHandle.postAtTime(new Runnable() {
            @Override
            public void run() {
                mGameBoard.newGame();
            }
        }, 2000);

    }

    private void addListener() {
        mNewGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGameBoard.newGame();
            }
        });
    }

    private void findView() {
        mCurrentScoreView = findViewById(R.id.current_score);
        mHigestScoreView = findViewById(R.id.highest_score);
        mNewGameButton = findViewById(R.id.new_game);
        mGameBoard = findViewById(R.id.layout_game_board);
    }


    @Override
    protected void onResume() {
        super.onResume();

        // 读取 sp 之前存储过的 highest score
        SharedPreferences sp = getSharedPreferences(GameConfig.SP_SIMPLE2048, Context.MODE_PRIVATE);
        int score = sp.getInt(GameConfig.SP_KEY_HIGHESTSCORE, 0);
        mHigestScoreView.setScore(score);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mGestDetector.onTouchEvent(event);
    }


    // GameBoardView 的回调方法，每次得分时，在这里更新记分板分数
    @Override
    public void onScoreChange(int score) {
        mCurrentScoreView.setScore(score);
        // 读取 sp 之前存储过的 highest score
        SharedPreferences sp = getSharedPreferences(GameConfig.SP_SIMPLE2048, Context.MODE_PRIVATE);
        int highestScore = sp.getInt(GameConfig.SP_KEY_HIGHESTSCORE, 0);
        mHigestScoreView.setScore(highestScore);
    }


    // GameBoardView 的回调方法，游戏结束时，弹出 game over 界面
    @Override
    public void onGameOver(int score) {

        // 利用 SP 保存数据
        SharedPreferences sp = getSharedPreferences(GameConfig.SP_SIMPLE2048, Context.MODE_PRIVATE);
        int beforeHighestScore = sp.getInt(GameConfig.SP_KEY_HIGHESTSCORE, 0);
        if (score > beforeHighestScore) {
            sp.edit().putInt(GameConfig.SP_KEY_HIGHESTSCORE, score).apply();
        }

    }



    /**
     * @describe 手势监听，响应上下左右滑动事件
     */
    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener {

        private static final String TAG = "MyGestureListener";

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

            float x = e2.getX() - e1.getX();
            float y = e2.getY() - e1.getY();

            if (Math.abs(velocityX) >= Math.abs(velocityY)) {
                if (x > GameConfig.FLING_MIN_DISTANCE) {
                    mGameBoard.action(Action.RIGHT);
                    Log.d(TAG, "onFling: RIGHT");
                    return true;
                }
                if (x < -GameConfig.FLING_MIN_DISTANCE) {
                    mGameBoard.action(Action.LEFT);
                    Log.d(TAG, "onFling: LEFT");
                    return true;
                }

            } else {
                if (y > GameConfig.FLING_MIN_DISTANCE) {
                    mGameBoard.action(Action.DOWN);
                    Log.d(TAG, "onFling: DOWN");
                    return true;
                }
                if (y < -GameConfig.FLING_MIN_DISTANCE) {
                    mGameBoard.mCurrentGameStatus = GameStatus.FAIL;
                    mGameBoard.invalidate();
//                    mGameBoard.action(Action.UP);
                    Log.d(TAG, "onFling: UP");
                    return true;
                }
            }
            return true;
        }
    }


}
