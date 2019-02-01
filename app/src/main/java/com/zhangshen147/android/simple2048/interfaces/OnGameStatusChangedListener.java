package com.zhangshen147.android.simple2048.interfaces;

/**
 * @author zhangshen
 * @version 1.0
 */
public interface OnGameStatusChangedListener {

    void onScoreChange(int score);

    void onGameNormal();

    void onGameFail(int score);

    void onGameSuccess(int score);

    void onGameSuccessFinally(int score);
}