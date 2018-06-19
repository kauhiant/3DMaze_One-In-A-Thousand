package tw.kauhiant.mygame;

import android.graphics.Bitmap;

/**
 * Created by kauhia on 2018/6/10.
 */

class GlobalAsset {
    static Bitmap groundShape;
    static Bitmap stoneShape;
    static Bitmap foodShape;

    static Bitmap attackBMP;
    static Bitmap straightBMP;
    static Bitmap horizonBMP;

    static Map3D gameMap;
    static Shape shape;
    static GameScence gameScence;
    static Animal player;
    static int playerViewRange = 4;
    static int gameSpeed = 250;

}
