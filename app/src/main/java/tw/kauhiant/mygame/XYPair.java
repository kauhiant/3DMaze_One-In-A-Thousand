package tw.kauhiant.mygame;

/**
 * Created by kauhia on 2018/6/16.
 */

public class XYPair {
    public int x,y;
    public XYPair(int x, int y){
        this.x = x;
        this.y = y;
    }

    public static XYPair center(){
        return new XYPair(0,0);
    }
}
