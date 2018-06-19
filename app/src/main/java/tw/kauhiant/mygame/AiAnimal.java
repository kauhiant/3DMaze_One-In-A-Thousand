package tw.kauhiant.mygame;

import android.util.Log;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

/**
 * Created by kauhia on 2018/6/14.
 */

public class AiAnimal extends Animal {
    private Random random;
    private int targetEP;
    private int timid;

    public AiAnimal(Point3D position, Dimension initView, int color, Shape shape, Vector2D vector, int power, int hp) {
        super(position, initView, color, shape, vector, power, hp);
        random = new Random(Calendar.getInstance().getTimeInMillis());
        targetEP = random.nextInt(40)+10;
        timid = random.nextInt(20);
    }

    public void auto(){
        ArrayList<Grid> grigs = GlobalAsset.gameMap.gridsOfLine(this.posit,this.getVector2D(),4);
        boolean haveFood = false;
        boolean haveEnemy= false;
        boolean haveStone = false;
        Animal nearEnemy = null;

        for(Grid grid : grigs){
            if(grid == null) break;
            if(grid.obj == null) continue;

            if(grid.obj instanceof Stone){
                haveStone = true;
            }
            else if(!haveStone && grid.obj instanceof Food){
                haveFood = true;
            }
            else if(grid.obj instanceof Animal){
                Animal enemy = (Animal) grid.obj;
                if(!enemy.isMasking){
                    haveEnemy = true;
                    if(nearEnemy == null)
                        nearEnemy = (Animal) grid.obj;
                }
            }
        }

        if(!haveFood && !haveEnemy){
            randTurn();
            if(!isMasking){
                randMove(0.7f);
            }

            if(this.hp < timid) {
                if(!isMasking){
                    mask();
                    Log.d("AI","AI Mask");
                }
            }
        }

        else if(haveFood){
            move();
        }



        else if(haveEnemy){
            if( this.power > targetEP
                    || this.power >= 10 && nearEnemy.hp<20)
            {
                boolean success = autoAction(this.position.distanceTo(nearEnemy.position));
                Log.d("AI",String.format("AI Auto Action : %b, EP=%d / %d",success,this.power,this.targetEP));
                if(nearEnemy == null)
                    Log.d("AI","ERROR");
                else if(this.position.distanceTo(nearEnemy.position) == 0)
                    Log.d("AICheck",String.format("Enemy position (%d %d %d)",nearEnemy.position.X(),nearEnemy.position.Y(),nearEnemy.position.Z()));
            }
            else if(this.hp < 20){
                randTurn();
                move();
                Log.d("AI","AI Run Away");
            }
            else {
                randTurn();
                randMove(0.5f);
            }
        }


    }

    private void randTurn(){
        switch (random.nextInt(8)){
            case 0:
                this.turnTo(Vector2D.Up);
                break;

            case 1:
                this.turnTo(Vector2D.Down);
                break;

            case 2:
                this.turnTo(Vector2D.Left);
                break;

            case 3:
                this.turnTo(Vector2D.Right);
                break;

            case 4:
                this.changePlain();
                break;
        }
    }

    private void randMove(float probability){
        if(random.nextFloat() < probability)
            this.move();
    }

    private boolean autoAction(int distance){
        if(distance > 1)
            return straight();
        else
            return attack();
    }
}
