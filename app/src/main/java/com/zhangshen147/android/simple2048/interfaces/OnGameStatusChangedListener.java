package com.zhangshen147.android.simple2048.interfaces;

public interface OnGameStatusChangedListener {

    void onScoreChange(int score);

    void onGameOver(int score);
}