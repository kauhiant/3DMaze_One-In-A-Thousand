package tw.kauhiant.mygame;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.Calendar;

/**
 * Created by kauhia on 2018/6/8.
 */

public class Map2D {
    private Map3D binded;

    public Map2D(Map3D parent){
        this.binded = parent;
    }

    private Grid getAt(Point2D position){
        return binded.getAt(position.binded);
    }

    public void drawOn(GameScence scence, Range2D range){
        Plain plain = range.centor.getPlain();
        int gridPerLine = range.extra * 2 + 1;
        Range2D.Iterator iterator = range.iterator();
        scence.formate(gridPerLine);

        for (int i = 0; i < gridPerLine; ++i)
            for (int j = 0; j < gridPerLine; ++j){
                Grid grid = getAt(iterator.iterator);
                if(grid != null){
                    if(grid.background != null){
                        Bitmap temp = scence.formateBitmap(grid.background);
                        scence.drawGridAt(temp,j,i);
                    }
                    if(grid.obj != null){
                        Bitmap temp = scence.formateBitmap(grid.obj.shape());
                        scence.drawGridAt(temp ,j,i);
                        if(grid.obj instanceof Animal){
                            Bitmap targetShape = scence.formateBitmap(((Animal)grid.obj).shapeFor(plain));
                            scence.drawGridAt(targetShape,j,i);
                        }
                    }
                }

                iterator.moveToNext();
            }
    }


}
