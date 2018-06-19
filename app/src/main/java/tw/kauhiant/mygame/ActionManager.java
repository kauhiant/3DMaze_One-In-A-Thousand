package tw.kauhiant.mygame;

import java.util.ArrayList;

/**
 * Created by kauhia on 2018/6/16.
 */

public abstract class ActionManager {
    static private ArrayList<Action> actionList = new ArrayList<Action>();

    static public void addAction(Actions action, XYPair position, Vector2D vector){
        actionList.add(new Action(action, position, vector));
    }

    static public void drawAction(){
        for (Action each : actionList) {
            each.drawAction();
        }
        actionList.clear();
    }
}
