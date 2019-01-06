package com.zhangshen147.android.simple2048;

import android.content.Context;

public class MainGame {

    // SharedPreference 所需的键名
    private static final String SP_HISTORY_HIGH_SCORE = "high score";

    // 本局游戏最终得分
    public static int FINAL_SCORE;

    // 游戏的状态
    public static final int GAME_LOST = -1;
    public static final int GAME_WIN = 1;
    public static final int GAME_NORMAL = 0;
    public static final int GAME_ENDLESS = 2;
    public static final int GAME_ENDLESS_WON = 3;

    public GameBoard grid = null;
    public AnimationGrid aGrid;

    // 游戏规格，4*4 的棋盘
    public final int gameSpec = 4;
    final int startTiles = 2;


    public int gameState = 0;
    public boolean canUndo;

    public long score = 0;
    public long highScore = 0;

    public long lastScore = 0;
    public int lastGameState = 0;

    private long bufferScore = 0;
    private int bufferGameState = 0;

    private Context mContext;
    private MainView mView;

    public MainGame(Context context, MainView view){
        mContext = context;
        mView = view;
    }

    public void newGame(){
        if (grid == null) {
            grid = new GameBoard(numSquaresX, numSquaresY);
        } else {
            prepareUndoState();
            saveUndoState();
            grid.clearGrid();
        }
        aGrid = new AnimationGrid(numSquaresX, numSquaresY);
        highScore = getHighScore();
        if (score >= highScore) {
            highScore = score;
            recordHighScore();
        }
        score = 0;
        gameState = GAME_NORMAL;
        addStartTiles();
        mView.refreshLastTime = true;
        mView.resyncTime();
        mView.invalidate();
    }

}
