package tw.kauhiant.mygame;

import android.graphics.Bitmap;

/**
 * Created by kauhia on 2018/6/10.
 */

public class Shape {
    private Bitmap[] sixShape = new  Bitmap[6];

    public void setShapeAt(Vector2D vector, Bitmap shape){
        sixShape[index(vector)] = shape;
    }

    public Bitmap getShapeAt(Vector2D vector){
        return sixShape[index(vector)];
    }

    // return -1 is impossible
    private int index(Vector2D vector){
        switch (vector){
            case Up:
                return 0;

            case Down:
                return 1;

            case Left:
                return 2;

            case Right:
                return 3;

            case In:
                return 4;

            case Out:
                return 5;
        }

        return -1;
    }
}
