package com.zhangshen147.android.simple2048;

public class Tile extends TilePosition {
    private int value;

    public Tile(int x, int y, int value){
        super(x, y);
        this.value = value;
    }

    public Tile(TilePosition tilePosition, int value){
        super(tilePosition.getX(), tilePosition.getY());
        this.value = value;
    }

    private void setValue(int value){
        this.value = value;
    }

    private int getValue(){
        return value;
    }
}
