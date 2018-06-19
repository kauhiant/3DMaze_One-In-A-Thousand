package tw.kauhiant.mygame;

import android.graphics.Bitmap;

/**
 * Created by kauhia on 2018/6/5.
 */

public abstract class GameObject {
    protected Point3D position;

    public GameObject(Point3D position){
        this.position = position;
    }

    public abstract Bitmap shape();
}
