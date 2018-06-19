package tw.kauhiant.mygame;

/**
 * Created by kauhia on 2018/6/5.
 */

/**
 * Plain : ( horizon , vertical )
 *  X : ( Y , Z )
 *  Y : ( Z , X )
 *  Z : ( X , Y )
 * */

public class Plain {
    private Dimension dimension;
    private int   value;

    public Dimension getDimension(){
        return dimension;
    }

    public int getValue(){
        return value;
    }

    public Plain(Point3D point, Dimension dimension){
        this.dimension = dimension;

        switch (dimension){
            case X:
                this.value = point.X();
                break;

            case Y:
                this.value = point.Y();
                break;

            case Z:
                this.value = point.Z();
                break;
        }
    }

    public Vector2D vector3to2(Vector3D vector){

        switch (dimension){
            case X:
                switch (vector){
                    case Xp: return Vector2D.Out;
                    case Xn: return Vector2D.In;
                    case Yp: return Vector2D.Right;
                    case Yn: return Vector2D.Left;
                    case Zp: return Vector2D.Up;
                    case Zn: return Vector2D.Down;
                }
            case Y:
                switch (vector){
                    case Xp: return Vector2D.Up;
                    case Xn: return Vector2D.Down;
                    case Yp: return Vector2D.Out;
                    case Yn: return Vector2D.In;
                    case Zp: return Vector2D.Right;
                    case Zn: return Vector2D.Left;
                }
            case Z:
                switch (vector){
                    case Xp: return Vector2D.Right;
                    case Xn: return Vector2D.Left;
                    case Yp: return Vector2D.Up;
                    case Yn: return Vector2D.Down;
                    case Zp: return Vector2D.Out;
                    case Zn: return Vector2D.In;
                }
        }

        return null;
    }

    public Vector3D vector2to3(Vector2D vector){
        switch (dimension){
            case X:
                switch (vector){
                    case Up:
                        return Vector3D.Zp;
                    case Down:
                        return Vector3D.Zn;
                    case Left:
                        return Vector3D.Yn;
                    case Right:
                        return Vector3D.Yp;
                    case In:
                        return Vector3D.Xn;
                    case Out:
                        return Vector3D.Xp;
                }
            case Y:
                switch (vector){
                    case Up:
                        return Vector3D.Xp;
                    case Down:
                        return Vector3D.Xn;
                    case Left:
                        return Vector3D.Zn;
                    case Right:
                        return Vector3D.Zp;
                    case In:
                        return Vector3D.Yn;
                    case Out:
                        return Vector3D.Yp;
                }
            case Z:
                switch (vector){
                    case Up:
                        return Vector3D.Yp;
                    case Down:
                        return Vector3D.Yn;
                    case Left:
                        return Vector3D.Xn;
                    case Right:
                        return Vector3D.Xp;
                    case In:
                        return Vector3D.Zn;
                    case Out:
                        return Vector3D.Zp;
                }
        }

        return null;
    }

    public boolean isEqual(Plain plain){
        if(this.dimension != plain.dimension)
            return false;

        if(this.value != plain.value)
            return false;

        return true;
    }
}
