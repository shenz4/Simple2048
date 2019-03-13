package com.zhangshen147.android.simple2048.util;

import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

import com.zhangshen147.android.simple2048.view.MainGameBoard;
import com.zhangshen147.android.simple2048.view.TileView;

public class AnimationManager {
    MainGameBoard mGameInstance;

    public AnimationManager(MainGameBoard context) {
        mGameInstance = context;
    }


    public void moveToLeft(final TileView source, final TileView dest){
        final int temp = source.getValue();
        int distance = dest.getLeft() - source.getLeft();
        TranslateAnimation anim = new TranslateAnimation(0, distance, 0, 0);
        anim.setDuration(400);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                source.setValue(0);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                dest.setValue(temp);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        source.startAnimation(anim);
    }

    public void moveToRight(final TileView source, final TileView dest){
        final int temp = source.getValue();
        int distance = dest.getLeft() - source.getLeft();
        TranslateAnimation anim = new TranslateAnimation(0, distance, 0, 0);
        anim.setDuration(400);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                source.setValue(0);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                dest.setValue(temp);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        source.startAnimation(anim);
    }

    public void moveToDown(final TileView source, final TileView dest){
        final int temp = source.getValue();
        int distance = dest.getTop() - source.getTop();
        TranslateAnimation anim = new TranslateAnimation(0, 0, 0, distance);
        anim.setDuration(400);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                source.setValue(0);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                dest.setValue(temp);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        source.startAnimation(anim);
    }

    public void moveToUp(final TileView source, final TileView dest){
        final int temp = source.getValue();
        int distance = dest.getTop() - source.getTop();
        TranslateAnimation anim = new TranslateAnimation(0, 0, 0, distance);
        anim.setDuration(400);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                source.setValue(0);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                dest.setValue(temp);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        source.startAnimation(anim);
    }
}
