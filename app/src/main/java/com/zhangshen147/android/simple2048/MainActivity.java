package com.zhangshen147.android.simple2048;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import com.zhangshen147.android.simple2048.config.GameConfig;
import com.zhangshen147.android.simple2048.enumerate.Action;
import com.zhangshen147.android.simple2048.interfaces.OnGameStatusChangedListener;
import com.zhangshen147.android.simple2048.view.GameBoardLayout;
import com.zhangshen147.android.simple2048.view.ScoreView;

public class MainActivity extends AppCompatActivity implements OnGameStatusChangedListener {

    private ScoreView mCurrentScoreView;
    private ScoreView mHigestScoreView;
    private ImageView mNewGameButton;

    private GameBoardLayout mGameBoard;
    private GestureDetector mGestDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 取消 title bar
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE | Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        // find view
        mCurrentScoreView = (ScoreView) findViewById(R.id.current_score);
        mHigestScoreView = (ScoreView) findViewById(R.id.highest_score);
        mNewGameButton = (ImageView) findViewById(R.id.new_game);
        mGameBoard = (GameBoardLayout) findViewById(R.id.game_board);

        mNewGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGameBoard.newGame();
            }
        });

        mGestDetector = (GestureDetector) new GestureDetector(this, new MyGestDetector());

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

    @Override
    public void onScoreChange(int score) {
        mCurrentScoreView.setScore(score);
    }

    @Override
    public void onGameOver(int score) {
        Toast.makeText(this, "game over", Toast.LENGTH_LONG).show();
        SharedPreferences sp = getSharedPreferences(GameConfig.SP_SIMPLE2048, Context.MODE_PRIVATE);
        int beforeHighestScore = sp.getInt(GameConfig.SP_KEY_HIGHESTSCORE, 0);
        if (score > beforeHighestScore) {
            sp.edit().putInt(GameConfig.SP_KEY_HIGHESTSCORE, score).commit();
        }


    }

    private class MyGestDetector extends GestureDetector.SimpleOnGestureListener {

        private static final String TAG = "MyGestDetector";

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

            float x = e2.getX() - e1.getX();
            float y = e2.getY() - e1.getY();

            if (Math.abs(velocityX) >= Math.abs(velocityY)) {
                if (x > GameConfig.FLING_MIN_DISTANCE) {
                    mGameBoard.action(Action.RIGHT);
                    Log.e(TAG, "onFling: RIGHT");
                    return true;
                }
                if (x < -GameConfig.FLING_MIN_DISTANCE) {
                    mGameBoard.action(Action.LEFT);
                    Log.e(TAG, "onFling: LEFT");
                    return true;
                }

            } else {
                if (y > GameConfig.FLING_MIN_DISTANCE) {
                    mGameBoard.action(Action.DOWN);
                    Log.e(TAG, "onFling: DOWN");
                    return true;
                }
                if (y < -GameConfig.FLING_MIN_DISTANCE) {
                    mGameBoard.action(Action.UP);
                    Log.e(TAG, "onFling: UP");
                    return true;
                }
            }
            return true;
        }

    }


}
