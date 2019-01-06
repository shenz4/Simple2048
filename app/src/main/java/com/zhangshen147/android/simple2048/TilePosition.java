package com.zhangshen147.android.simple2048;

public class TilePosition {
    private int x, y;

    public TilePosition(int x, int y){
        this.x = x;
        this.y = y;
    }

    void setX(int x){
        this.x = x;
    }

    void setY(int y){
        this.y = y;
    }

    int getX(){
        return x;
    }

    int getY(){
        return y;
    }
}
