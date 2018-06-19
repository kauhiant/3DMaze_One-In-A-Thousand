package tw.kauhiant.mygame;

import android.util.Pair;

/**
 * Created by kauhia on 2018/6/5.
 */

public class Point3D {

    private Int x;
    private Int y;
    private Int z;


    public int X() {return x.val();}
    public int Y() {return y.val();}
    public int Z() {return z.val();}

    public Point3D(int x, int y, int z){
        this.x = new Int(x);
        this.y = new Int(y);
        this.z = new Int(z);
    }

    public Point3D copy(){
        return new Point3D(X(),Y(),Z());
    }

    public void setValue(int x, int y, int z){
        this.x.set(x);
        this.y.set(y);
        this.z.set(z);
    }

    public void setValueBy(Point3D target){
        setValue(target.X(),target.Y(),target.Z());
    }

    public int distanceTo(Point3D target){
        int tmp = 0;
        int max = Integer.MIN_VALUE;

        tmp = Math.abs(this.X() - target.X());
        max = tmp > max ? tmp : max;

        tmp = Math.abs(this.Y() - target.Y());
        max = tmp > max ? tmp : max;

        tmp = Math.abs(this.Z() - target.Z());
        max = tmp > max ? tmp : max;

        return max;
    }


    public Pair<Int,Int> toPoint2D(Dimension dimension){
        switch (dimension){
            case X:
                return new Pair<>(y,z);
            case Y:
                return new Pair<>(z,x);
            case Z:
                return new Pair<>(x,y);
        }
        return null;
    }

    public boolean isInRange(Range2D range){

        if( ! this.isOnPlain(range.centor.getPlain()))
            return false;

        if(this.distanceTo(range.centor.binded) > range.extra)
            return false;

        return true;
    }

    public boolean isOnEdge(Range2D range){
        Point2D this2DPoint = new Point2D(this, range.centor.getPlain().getDimension());

        if( ! this2DPoint.getPlain().isEqual(range.centor.getPlain()))
            return false;

        if(this2DPoint.distanceTo(range.centor) != range.extra)
            return false;

        return true;
    }

    public boolean isOnPlain(Plain plain){
        switch (plain.getDimension()){
            case X:
                return this.X() == plain.getValue();
            case Y:
                return this.Y() == plain.getValue();
            case Z:
                return this.Z() == plain.getValue();
        }
        return false;
    }

    public void moveFor(Vector3D vector, int dist){
        switch (vector){
            case Xp:
                this.x.add(dist);
                break;

            case Xn:
                this.x.add(-dist);
                break;

            case Yp:
                this.y.add(dist);
                break;

            case Yn:
                this.y.add(-dist);
                break;

            case Zp:
                this.z.add(dist);
                break;

            case Zn:
                this.z.add(-dist);
                break;
        }
    }
}
