package tw.kauhiant.mygame;

import android.graphics.Bitmap;

/**
 * Created by kauhia on 2018/6/10.
 */

public class Stone extends GameObject {

    public Stone(Point3D position) {
        super(position);
    }

    @Override
    public Bitmap shape() {
        return GlobalAsset.stoneShape;
    }
}
