package com.zhangshen147.android.simple2048;

import java.util.ArrayList;


public class GameBoard {

    public Tile[][] field;
    public Tile[][] undoField;
    private Tile[][] bufferField;


    public GameBoard(int size) {
        field = new Tile[size][size];
        undoField = new Tile[size][size];
        bufferField = new Tile[size][size];
        clearGrid();
        clearUndoGrid();
    }


    public TilePosition randomAvailableCell() {
        // 从可用格子中随机选择一个
        ArrayList<TilePosition> availableCells = getAvailableCells();
       if (availableCells.size() > 0) {
           return availableCells.get((int) Math.floor(Math.random() * availableCells.size()));
       }
       return null;
    }

    public ArrayList<TilePosition> getAvailableCells() {
        // 找出所有可用格子
        ArrayList<TilePosition> availableCells = new ArrayList<TilePosition>();
        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field[0].length; j++) {
                if (field[i][j] == null) {
                    availableCells.add(new TilePosition(i, j));
                }
            }
        }
        return availableCells;
    }

    public boolean isCellsAvailable() {
        return (getAvailableCells().size() > 0);
    }

    public boolean isCellAvailable(TilePosition cell) {
        return !isCellOccupied(cell);
    }

    public boolean isCellOccupied(TilePosition cell) {
        return (getCellContent(cell) != null);
    }

    public Tile getCellContent(TilePosition cell) {
        if (cell != null && isCellWithinBounds(cell)) {
            return field[cell.getX()][cell.getY()];
        } else {
            return null;
        }
    }

    public Tile getCellContent(int x, int y) {
        if (isCellWithinBounds(x, y)) {
            return field[x][y];
        } else {
            return null;
        }
    }

    // 检测是否越界
    public boolean isCellWithinBounds(TilePosition cell) {
        return 0 <= cell.getX() && cell.getX() < field.length
            && 0 <= cell.getY() && cell.getY() < field[0].length;
    }

    public boolean isCellWithinBounds(int x, int y) {
        return 0 <= x && x < field.length
                && 0 <= y && y < field[0].length;
    }
    
    public void insertTile(Tile tile) {
        field[tile.getX()][tile.getY()] = tile;
    }

    public void removeTile(Tile tile) {
        field[tile.getX()][tile.getY()] = null;
    }

    public void saveTiles() {
        for (int i = 0; i < bufferField.length; i++) {
            for (int j = 0; j < bufferField[0].length; j++) {
                if (bufferField[i][j] == null) {
                    undoField[i][j] = null;
                } else {
                    undoField[i][j] = bufferField[i][j];
//                  undoField[i][j] = new Tile(i, j, bufferField[i][j].getValue());
                }
            }
        }
    }

    public void prepareSaveTiles() {
        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field[0].length; j++) {
                if (field[i][j] == null) {
                    bufferField[i][j] = null;
                } else {
                    bufferField[i][j] = field[i][j];
//                  bufferField[i][j] = new Tile(i, j, field[i][j].getValue());
                }
            }
        }
    }

    // 返回上一步
    public void revertTiles() {
        for (int i = 0; i < undoField.length; i++) {
            for (int j = 0; j < undoField[0].length; j++) {
                if (undoField[i][j] == null) {
                    field[i][j] = null;
                } else {
                    field[i][j] = undoField[i][j];
//                  field[i][j] = new Tile(i, j, undoField[i][j].getValue());
                }
            }
        }
    }

    public void clearGrid() {
        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field[0].length; j++) {
                field[i][j] = null;
            }
        }
    }

    public void clearUndoGrid() {
        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field[0].length; j++) {
                undoField[i][j] = null;
            }
        }
    }
}
