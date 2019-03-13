package com.zhangshen147.android.simple2048;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.zhangshen147.android.simple2048.config.GameConfig;
import com.zhangshen147.android.simple2048.enumerate.GameStatus;
import com.zhangshen147.android.simple2048.interfaces.OnGameStatusChangedListener;
import com.zhangshen147.android.simple2048.view.MainGameBoard;
import com.zhangshen147.android.simple2048.view.ScoreView;

/**
 * @author zhangshen
 * @version 1.0
 */
public class MainActivity extends AppCompatActivity implements OnGameStatusChangedListener {

    private static final String TAG = "MainActivity";

    // view fields
    private ScoreView mCurrentScoreView;
    private ScoreView mHigestScoreView;
    private ImageView mNewGameButton;
    private MainGameBoard mMainGameBoard;

    // used only once
    private boolean mSingleton = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findView();
        addListener();
        Log.d(TAG, "onCreate: ");

        // 取出最高分
        SharedPreferences sp = getSharedPreferences(GameConfig.SP_SIMPLE_2048, Context.MODE_PRIVATE);
        int highScore = sp.getInt(GameConfig.SP_KEY_HIGHEST_SCORE, 0);
        mHigestScoreView.setScore(highScore);
    }



    @Override             // 当视图获得焦点时，自动开启游戏
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && mSingleton){
            mMainGameBoard.newGame();
        }
        mSingleton = false;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // 数据持久化，将当前分数、游戏状态、滑动标志位暂存到 SP 中
//        SharedPreferences sp = getSharedPreferences(GameConfig.SP_SIMPLE_2048, Context.MODE_PRIVATE);
//        int score = mMainGameBoard.getCurrentScore();
//        String status = mMainGameBoard.getCurrentGameStatus().toString();
//        boolean fling = mMainGameBoard.getIsEnableFling();
//        sp.edit().putInt(SP_SCORE, score)
//                .putString(SP_STATUS, status)
//                .putBoolean(SP_FLING, fling)
//                .apply();
//        Log.d(TAG, "onPause: 往SP中存入 " + score);
//        Log.d(TAG, "onPause: 往SP中存入 " + status);
//        Log.d(TAG, "onPause: 往SP中存入 " + fling);

        // 数据持久化，将二维数组暂存到 SP 中
        // TODO

    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // 数据持久化，从 SP 中取出暂存的当前分数、游戏状态
//        SharedPreferences sp = getSharedPreferences(GameConfig.SP_SIMPLE_2048, Context.MODE_PRIVATE);
//        int score = sp.getInt(SP_SCORE, 0);
//        String status = sp.getString(SP_STATUS, "NORMAL");
//        boolean fling = sp.getBoolean(SP_FLING, true);
//        mMainGameBoard.setCurrentScore(score);
//        mMainGameBoard.setCurrentGameStatus(GameStatus.valueOf(status));
//        mMainGameBoard.setIsEnableFling(fling);
//
//        Log.d(TAG, "onResume: 从SP中读出 " + score);
//        Log.d(TAG, "onResume: 从SP中读出 " + status);
//        Log.d(TAG, "onResume: 从SP中读出 " + fling);

        // 数据持久化，从 SP 中取出二维数组
        // TODO
    }


    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");
    }
    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
    }
    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart: ");
    }
    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: ");
    }


    // The next 5 methods with @Override are implementations of OnGameStatusChangedListener
    @Override
    public void onScoreChange(int score) {
        mCurrentScoreView.setScore(score);
    }


    @Override
    public void onGameNormal() {
        mMainGameBoard.mCurrentGameStatus = GameStatus.NORMAL;
        mMainGameBoard.invalidate();
        mMainGameBoard.mIsEnableFling = true;
        mNewGameButton.setActivated(false);
    }


    @Override
    public void onGameFail(int score) {
        mMainGameBoard.mCurrentGameStatus = GameStatus.FAIL;
        mMainGameBoard.invalidate();
        mMainGameBoard.mIsEnableFling = false;
        mNewGameButton.setActivated(true);
        saveScoreToSP(score);
        mMainGameBoard.setCurrentScore(getScoreFromSP());
    }


    @Override
    public void onGameSuccess(int score) {
        mMainGameBoard.mCurrentGameStatus = GameStatus.SUCCESS;
        mMainGameBoard.invalidate();
        mMainGameBoard.mIsEnableFling = false;
        mNewGameButton.setActivated(false);
        saveScoreToSP(score);
        mMainGameBoard.setCurrentScore(getScoreFromSP());
    }


    @Override
    public void onGameSuccessFinally(int score) {
        mMainGameBoard.mCurrentGameStatus = GameStatus.SUCCESS_FINALLY;
        mMainGameBoard.invalidate();
        mMainGameBoard.mIsEnableFling = false;
        mNewGameButton.setActivated(true);
        saveScoreToSP(score);
        mMainGameBoard.setCurrentScore(getScoreFromSP());
    }


    private void findView() {
        mCurrentScoreView = findViewById(R.id.current_score);
        mHigestScoreView = findViewById(R.id.highest_score);
        mNewGameButton = findViewById(R.id.new_game);
        mMainGameBoard = findViewById(R.id.layout_game_board);
    }


    private void addListener() {
        mNewGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMainGameBoard.newGame();
            }
        });
    }


    private void saveScoreToSP(int score) {
        SharedPreferences sp = getSharedPreferences(GameConfig.SP_SIMPLE_2048, Context.MODE_PRIVATE);
        int beforeHighestScore = sp.getInt(GameConfig.SP_KEY_HIGHEST_SCORE, 0);
        if (score > beforeHighestScore) {
            sp.edit().putInt(GameConfig.SP_KEY_HIGHEST_SCORE, score).apply();
        }
    }


    private int getScoreFromSP() {
        SharedPreferences sp = getSharedPreferences(GameConfig.SP_SIMPLE_2048, Context.MODE_PRIVATE);
        return sp.getInt(GameConfig.SP_KEY_HIGHEST_SCORE, 0);
    }
}
