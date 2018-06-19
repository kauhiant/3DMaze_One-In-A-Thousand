package tw.kauhiant.mygame;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

import javax.microedition.khronos.opengles.GL;

/**
 * Created by kauhia on 2018/6/8.
 */

public class Map3D {
    private Grid[][][] map;
    private int leng;
    private int layers;

    public int xBound(){
        return map.length;
    }

    public int yBound(){
        return map[0].length;
    }

    public int zBound(){
        return map[0][0].length;
    }

    public Map3D(int leng, int layers, int initFoods){
        this.leng = leng;
        this.layers= layers;
        map = new Grid[leng][leng][layers];
        initMap();
        for(int i=0;i<initFoods;++i)
            this.insertRandomFood();
    }


    private void initMap(){
        Random random = new Random();
        for(int i=0;i<map.length;++i)
            for (int j=0;j<map[i].length;++j)
                for (int k=0;k<map[i][j].length;++k){
                    map[i][j][k] = new Grid();
                    map[i][j][k].background = GlobalAsset.groundShape;
                    if(random.nextFloat() < 0.3)
                        map[i][j][k].obj = new Stone(new Point3D(i,j,k));
                }
    }

    private boolean outOfBound(Point3D position){
        return (position.X()<0 || position.Y()<0 || position.Z()<0 || position.X()>=map.length || position.Y()>=map[0].length || position.Z()>=map[0][0].length);
    }

    public Grid getAt(Point3D position){
        if(outOfBound(position))
            return null;
        return map[position.X()][position.Y()][position.Z()];
    }

    public boolean insertObjAt(Point3D position, GameObject object){
        if(this.getAt(position).obj != null)
            return false;

        this.getAt(position).obj = object;
        return true;
    }

    public boolean priorityInsertObjAt(Point3D position, GameObject object){
        if(this.getAt(position).obj != null)
            removeAt(position);

        this.getAt(position).obj = object;
        return true;
    }

    public void removeAt(Point3D position){
        getAt(position).obj = null;
    }

    public void setBackgroundAt(Point3D position, Bitmap background){
        getAt(position).background = background;
    }

    public void swap(Point3D p1, Point3D p2){
        GameObject temp = getAt(p1).obj;
        getAt(p1).obj = getAt(p2).obj;
        getAt(p2).obj = temp;

        if(getAt(p1).obj != null)
            getAt(p1).obj.position.setValueBy(p1);

        if(getAt(p2).obj != null)
            getAt(p2).obj.position.setValueBy(p2);
    }

    public Map2D createMap2D(Plain plain){
        return new Map2D(this);
    }

    private Random random = new Random();
    public Point3D randPoint(){
        return new Point3D(random.nextInt(leng),random.nextInt(leng),random.nextInt(layers));
    }

    public void insertRandomFood(){
        Point3D point = this.randPoint();
        this.insertObjAt(point,new Food(point));
    }

    public Point3D findAnEmptyGrid(){
        Random random = new Random(Calendar.getInstance().getTimeInMillis());
        Point3D point = new Point3D(random.nextInt(xBound()),random.nextInt(yBound()),random.nextInt(zBound()));

        for (int i=0; i<layers; ++i){
            if(this.getAt(point).obj == null)
                return point;
            point.setValue(random.nextInt(xBound()),random.nextInt(yBound()),random.nextInt(zBound()));
        }
        return null;
    }

    public ArrayList<Grid> gridsOfLine(Point2D position, Vector2D vector, int distance){
        ArrayList<Grid> gridsOfLine = new ArrayList<Grid>();
        Point2D iter = position.copy();
        if(!iter.moveFor(vector,1))
            return gridsOfLine;

        while (distance-- != 0){
            gridsOfLine.add(getAt(iter.binded));
            if(!iter.moveFor(vector,1))
                break;
        }
        return gridsOfLine;
    }
}
