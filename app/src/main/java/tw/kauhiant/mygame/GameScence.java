package tw.kauhiant.mygame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by kauhia on 2018/6/4.
 */

public class GameScence extends View {
    private Paint paint;
    private Bitmap bitmap;
    private Canvas canvas;

    private int gridWidth;
    private int gridHeight;
    private int gridPerLine;

    public int getGridPerLine(){return gridPerLine;}

    public GameScence(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    private void Init(int width, int height){
        bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
        canvas.drawColor(Color.GRAY);
        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(4);
    }

    public void formate(int gridPerLine){
        this.gridPerLine = gridPerLine;
        this.gridWidth = getWidth() / gridPerLine;
        this.gridHeight = getHeight() / gridPerLine;
    }

    @Override
    protected void onLayout(boolean changed, int left,int top, int right, int bottom){
        super.onLayout(changed, left, top, right, bottom);
        Init(getWidth(), getHeight());
    }

    @Override
    protected  void onDraw(Canvas canvas){
        super.onDraw(canvas);

        if(bitmap != null)
            canvas.drawBitmap(bitmap,0,0,paint);
    }


    public void clear(){
        canvas.drawColor(Color.GRAY);
    }

    public void clear(int color){
        canvas.drawColor(color);
    }

    public void drawGridAt(Bitmap grid, int x, int y){
        // grid = formateBitmap(grid);
        canvas.drawBitmap(grid,x*gridWidth,y*gridHeight,paint);
    }


    public Bitmap formateBitmap(Bitmap bitmap){
        bitmap = Bitmap.createScaledBitmap(bitmap,gridWidth,gridHeight,true);
        return bitmap;
    }

    public Bitmap formateBitmap(Bitmap bitmap, int scaleWidth, int scaleHeight, int rotateDegree){
        Matrix matrix = new Matrix();
        matrix.postRotate(rotateDegree);
        bitmap = Bitmap.createScaledBitmap(bitmap,gridWidth*scaleWidth,gridHeight*scaleHeight,true);
        return Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);
    }

    public void drawOneGridAt(Bitmap bitmap,XYPair dist, Vector2D vector){
        int center = gridPerLine / 2;
        int x = center + dist.x;
        int y = center + dist.y;
        bitmap = formateBitmap(bitmap);
        switch (vector){
            case Up:
                drawGridAt(bitmap,x, y-1);
                break;

            case Down:
                drawGridAt(bitmap,x,y+1);
                break;

            case Left:
                drawGridAt(bitmap,x-1,y);
                break;

            case Right:
                drawGridAt(bitmap,x+1,y);
                break;
        }
    }

    public void drawStraight(Bitmap bitmap,XYPair dist, Vector2D vector){
        int center = gridPerLine / 2;
        int x = center + dist.x;
        int y = center + dist.y;
        switch (vector){
            case Up:{
                bitmap = formateBitmap(bitmap,3,1,-90);
                drawGridAt(bitmap,x,y-3);
            }break;

            case Down: {
                bitmap = formateBitmap(bitmap,3,1,90);
                drawGridAt(bitmap, x, y + 1);
            }break;

            case Left: {
                bitmap = formateBitmap(bitmap,3,1,180);
                drawGridAt(bitmap, x - 3, y);
            }break;

            case Right:
                bitmap = formateBitmap(bitmap,3,1,0);
                drawGridAt(bitmap,x + 1,y);
                break;
        }
    }

    public void drawHorizon(Bitmap bitmap,XYPair dist, Vector2D vector){
        int center = gridPerLine / 2;
        int x = center + dist.x;
        int y = center + dist.y;

        switch (vector){
            case Up:
                bitmap = formateBitmap(bitmap,3,1,0);
                drawGridAt(bitmap,x-1,y-1);
                break;

            case Down: {
                bitmap = formateBitmap(bitmap,3,1,180);
                drawGridAt(bitmap, x-1, y + 1);
            }break;

            case Left: {
                bitmap = formateBitmap(bitmap,3,1,-90);
                drawGridAt(bitmap, x - 1, y-1);
            }break;

            case Right:{
                bitmap = formateBitmap(bitmap,3,1,90);
                drawGridAt(bitmap, x + 1, y-1);
            }break;
        }
    }

}
