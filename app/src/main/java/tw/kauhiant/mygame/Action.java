package tw.kauhiant.mygame;


import android.graphics.Bitmap;
import android.support.annotation.Nullable;

import javax.microedition.khronos.opengles.GL;

/**
 * Created by kauhia on 2018/6/16.
 */

public class Action {
    private Actions action;
    private XYPair position;
    private Vector2D vector;

    public Action(Actions action, XYPair position, Vector2D vector){
        this.action = action;
        this.position = position;
        this.vector = vector;
    }

    public void drawAction(){
        switch (action){
            case Attack:
                GlobalAsset.gameScence.drawOneGridAt(
                        bmpOf(action),
                        position,
                        vector);
                break;

            case Straight:
                GlobalAsset.gameScence.drawStraight(
                        bmpOf(action),
                        position,
                        vector);
                break;

            case Horizon:
                GlobalAsset.gameScence.drawHorizon(
                        bmpOf(action),
                        position,
                        vector);
                break;
        }

    }

    private Bitmap bmpOf(Actions action){
        switch (action){
            case Attack:
                return GlobalAsset.attackBMP;
            case Straight:
                return GlobalAsset.straightBMP;
            case Horizon:
                return GlobalAsset.horizonBMP;
            case Mask:
                return GlobalAsset.stoneShape;
        }
        return null;
    }
}
