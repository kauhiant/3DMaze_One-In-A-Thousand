package tw.kauhiant.mygame;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.Pair;

import javax.microedition.khronos.opengles.GL;

/**
 * Created by kauhia on 2018/6/10.
 */

public class Animal extends GameObject {
    public Point2D posit;

    protected int color;
    protected Shape shape;
    protected Vector3D vector;
    protected int power;
    protected int hp;

    public int getHp(){
        return this.hp;
    }

    public int getPower(){
        return this.power;
    }

    public Vector3D getVector(){return vector;}

    public Vector2D getVector2D(){return posit.getPlain().vector3to2(this.vector);}

    public Animal(Point3D position, Dimension initView, int color, Shape shape, Vector2D vector, int power, int hp) {
        super(position);
        this.posit = new Point2D(super.position,initView);
        this.color = color;
        this.shape = shape;
        this.vector= posit.getPlain().vector2to3(vector);
        this.power= power;
        this.hp = hp;
    }

    @Override
    public Bitmap shape() {
        // draw a circle
        Paint paint = new Paint();
        paint.setColor(this.color);
        paint.setAlpha(55 + this.hp * 2);
        paint.setStyle(Paint.Style.FILL);
        Bitmap b = Bitmap.createBitmap(100,100, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        c.drawCircle(b.getWidth()/2,b.getHeight()/2,b.getWidth()*0.4f,paint);
        return b;
    }

    public Bitmap shapeFor(Plain plain){
        if(isMasking)
            return GlobalAsset.stoneShape;

        return shape.getShapeAt(plain.vector3to2(this.vector));
    }

    protected void beAttack(Animal enemy){
        isMasking = false;
        Log.d("BeAttack",String.format("%d",enemy.power));
        this.hp -= enemy.power * 2;

        if(this.isDead()){
            this.hp = 0;
            enemy.eatFood(this.power);
            this.power = 0;
            GlobalAsset.gameMap.removeAt(super.position);
        }
    }

    public boolean isDead(){
        return this.hp <= 0;
    }

    private void eatFood(int num){
        this.hp += 5*num;
        if(hp > 100)hp =100;

        this.power += num;
    }

    public void changePlain(Dimension dimension){
        this.posit.changePlain(dimension);
    }

    public Dimension changePlain(){
        Dimension dimension = this.forwardDimention();
        this.changePlain(dimension);
        return dimension;
    }

    @Nullable
    private Grid targetGrid(){
        Point2D temp = this.posit.copy();
        temp.moveFor(temp.getPlain().vector3to2(vector),1);
        if(temp.getPlain().isEqual(this.posit.getPlain()))
            return GlobalAsset.gameMap.getAt(temp.binded);

        return null;
    }

    public void turnTo(Vector2D vector){
        this.vector = posit.getPlain().vector2to3(vector);
    }

    public void move(){

        Point2D temp = this.posit.copy();
        temp.moveFor(temp.getPlain().vector3to2(vector),1);
        // temp.binded.moveFor(this.vector,1);
        Grid targetGrid = GlobalAsset.gameMap.getAt(temp.binded);

        if(targetGrid == null)
            return;

        if(targetGrid.obj == null){
            isMasking = false;
            GlobalAsset.gameMap.swap(position, temp.binded);
        }

        if (targetGrid.obj instanceof Food){
            isMasking = false;
            this.eatFood(1);
            GlobalAsset.gameMap.removeAt(temp.binded);
            GlobalAsset.gameMap.swap(position,temp.binded);
        }
    }

    public void moveFor(Vector2D vector){
        this.turnTo(vector);
        this.move();
    }

    private boolean funcAction(int powerReduce){
        return (this.power >= powerReduce);
    }

    private void consumeEP(int reduce){
        this.power -= reduce;
        if(this.power < 0)
            this.power = 0;
    }

    public boolean attack(){
        if(!funcAction(1))
            return false;

        Vector2D tempVect = posit.getPlain().vector3to2(this.vector);
        if(tempVect == Vector2D.In || tempVect == Vector2D.Out)
            return true;

        Grid targetGrid = targetGrid();
        if(targetGrid != null && targetGrid.obj != null && targetGrid.obj instanceof Animal){
            Animal target = (Animal) targetGrid.obj;
            target.beAttack(this);
        }

        consumeEP(1);
        registerAction(Actions.Attack);

        return true;
    }

    public boolean straight(){
        if(!funcAction(3))
            return false;

        Point2D temp = this.posit.copy();
        Vector2D tempVect = temp.getPlain().vector3to2(this.vector);
        if(tempVect == Vector2D.In || tempVect == Vector2D.Out)
            return true;

        for(int i=0; i<3; ++i){
            temp.moveFor(temp.getPlain().vector3to2(this.vector),1);
            Grid targetGrid = GlobalAsset.gameMap.getAt(temp.binded);
            if(targetGrid != null && targetGrid.obj != null && targetGrid.obj instanceof Animal){
                Animal target = (Animal) targetGrid.obj;
                target.beAttack(this);
            }
        }

        consumeEP(3);
        registerAction(Actions.Straight);

        return true;
    }

    @Nullable
    private Pair<Vector2D,Vector2D> orthogonal(Vector2D vector){
        if(vector == Vector2D.In || vector == Vector2D.Out)
            return null;

        if(vector == Vector2D.Up || vector == Vector2D.Down)
            return new Pair<>(Vector2D.Left, Vector2D.Right);
        else
            return new Pair<>(Vector2D.Up, Vector2D.Down);
    }

    public boolean horizon(){
        if(!funcAction(3))
            return false;

        Point2D temp = this.posit.copy();

        Vector2D tempVect = temp.getPlain().vector3to2(this.vector);
        if(tempVect == Vector2D.In || tempVect == Vector2D.Out)
            return true;

        temp.moveFor(tempVect,1);
        Pair<Vector2D,Vector2D> twoVector = orthogonal(tempVect);
        temp.moveFor(twoVector.first,1);

        for(int i=0; i<3; ++i) {
            Grid targetGrid = GlobalAsset.gameMap.getAt(temp.binded);
            if(targetGrid != null && targetGrid.obj != null && targetGrid.obj instanceof Animal) {
                Animal target = (Animal) targetGrid.obj;
                target.beAttack(this);
            }
            temp.moveFor(twoVector.second,1);
        }

        consumeEP(3);
        registerAction(Actions.Horizon);

        return true;
    }

    protected boolean isMasking = false;
    public void mask(){
        isMasking = true;
    }

    public Dimension forwardDimention(){
        switch (this.vector){
            case Xn:
            case Xp:
                return Dimension.X;

            case Yn:
            case Yp:
                return Dimension.Y;

            case Zn:
            case Zp:
                return Dimension.Z;
        }
        return  null;
    }

    public boolean isInRange(Range2D range){
        return this.position.isInRange(range);
    }

    public XYPair diff2Position(Point2D position){
        Point2D thisTempPoint2 = new Point2D(this.position.copy(),position.dimension);
        return new XYPair(
                thisTempPoint2.getX() - position.getX(),
                position.getY() - thisTempPoint2.getY()
                );
    }

    private void registerAction(Actions action){
        if(this.isInRange(new Range2D(GlobalAsset.player.posit,GlobalAsset.playerViewRange)))
            ActionManager.addAction(action,
                    this.diff2Position(GlobalAsset.player.posit),
                    GlobalAsset.player.posit.getPlain().vector3to2(this.getVector()));
    }
}
