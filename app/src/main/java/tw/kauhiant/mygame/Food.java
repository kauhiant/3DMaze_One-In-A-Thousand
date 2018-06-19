package tw.kauhiant.mygame;

import android.graphics.Bitmap;

import javax.microedition.khronos.opengles.GL;

/**
 * Created by kauhia on 2018/6/10.
 */

public class Food extends GameObject {
    public Food(Point3D position) {
        super(position);
    }

    @Override
    public Bitmap shape() {
        return GlobalAsset.foodShape;
    }
}
