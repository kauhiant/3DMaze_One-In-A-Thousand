package tw.kauhiant.mygame;

import android.util.Pair;

import java.security.PublicKey;

/**
 * Created by kauhia on 2018/6/5.
 */

public class Point2D {
    public Point3D binded;
    public Dimension dimension;
    private Int x;
    private Int y;

    public int getX(){
        return x.val();
    }
    public int getY(){return y.val();}

    public Plain getPlain(){
        return  new Plain(binded,dimension);
    }

    public Point2D(Point3D binded, Dimension dimension){
        this.bind(binded,dimension);
    }

    public Point2D copy(){
        return new Point2D(binded.copy(), dimension);
    }

    public void bind(Point3D binded, Dimension dimension){
        this.binded = binded;
        this.changePlain(dimension);
    }

    public void changePlain(Dimension dimension){
        this.dimension = dimension;
        Pair<Int,Int> temp = binded.toPoint2D(dimension);
        this.x = temp.first;
        this.y = temp.second;
    }

    public boolean moveFor(Vector2D vector, int distance){
        switch (vector){
            case Up:
                this.y.add(1*distance);
                return true;

            case Down:
                this.y.add(-1*distance);
                return true;

            case Left:
                this.x.add(-1*distance);
                return true;

            case Right:
                this.x.add(1*distance);
                return true;
        }
        return  false;
    }

    public int distanceTo(Point2D target){

        if( !this.getPlain().isEqual(target.getPlain()))
            return -1;

        int tmp = 0;
        int max = Integer.MIN_VALUE;

        tmp = Math.abs(this.x.val() - target.x.val());
        max = tmp > max ? tmp : max;

        tmp = Math.abs(this.y.val() - target.y.val());
        max = tmp > max ? tmp : max;

        return max;
    }


}

