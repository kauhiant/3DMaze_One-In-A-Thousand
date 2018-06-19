package tw.kauhiant.mygame;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener {
    GameScence gameSence;
    Button gameStartButton;

    TextView playerInfo;
    TextView gameInfo;

    ImageButton attack;
    ImageButton straigh;
    ImageButton horizom;
    ImageButton mask;

    ImageButton plainButton;
    ImageButton menu;

    RelativeLayout vectorGroup;
    ImageButton upButton;
    ImageButton downButton;
    ImageButton leftButton;
    ImageButton rightButton;

/*
* For Test
* */
    Animal player;
    int[] colors;
    int playerViewRange = GlobalAsset.playerViewRange;
    final int clockTime = GlobalAsset.gameSpeed;
    ArrayList<AiAnimal> enemys;
    Map2D map;
    boolean isGameStart = false;
    boolean playerMove = false;
    boolean gamePause = true;
    boolean controlBySensor = false;

/*
 * Sensor For Move
 * */
    float baseDx=0f;
    float baseDy=0f;
    float sensorForMove = 2f;
    float sensorForTurn = 1.5f;
    boolean setBaseSensor = false;
    SensorManager sensorManager;
    SensorEventListener listener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            if(!controlBySensor)return;

            final float dy = sensorEvent.values[0];
            final float dx = sensorEvent.values[1];

            if(setBaseSensor){
                setBaseSensor = false;
                baseDx = dx;
                baseDy = dy;
            }

            if(gamePause) return;

            float maxDiff = 0;
            if(Math.abs(dx - baseDx) > Math.abs(dy - baseDy)){
                maxDiff = Math.abs(dx-baseDx);
                if(dx-baseDx < -1*sensorForTurn){
                    player.turnTo(Vector2D.Left);
                }
                else if(dx-baseDx > sensorForTurn){
                    player.turnTo(Vector2D.Right);
                }
            }else{
                maxDiff = Math.abs(dy-baseDy);
                if(dy-baseDy < -1*sensorForTurn){
                    player.turnTo(Vector2D.Up);
                }else if(dy-baseDy > sensorForTurn){
                    player.turnTo(Vector2D.Down);
                }
            }

            if(maxDiff < sensorForMove)
                playerMove = false;
            else
                playerMove = true;

        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };

/*
*  Timer Update
* */
    private Handler timerHandler = new Handler();

    final Runnable timer = new Runnable() {
        @Override
        public void run() {
            timerHandler.postDelayed(this,clockTime);
            if(gamePause)return;

            runForClock();
        }
    };


/*
*  Game Start
* */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("test","on Create");

        // Full Screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Wake Lock
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.activity_main);

        // Find All Views From XML
        findAllViews();

        //  Sensor Initial
        sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        sensorManager.registerListener(listener,sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),SensorManager.SENSOR_DELAY_GAME);
        setBaseSensor();

/**
 * Initial Game
 */
        // Game Status
        timerHandler.postDelayed(timer,1000);
        gamePause = false;
        setGlobalData();

        colors = new int[8];
        colors[0] = Color.RED;
        colors[1] = Color.GREEN;
        colors[2] = Color.BLUE;
        colors[3] = Color.YELLOW;
        colors[4] = Color.GRAY;
        colors[5] = Color.MAGENTA;
        colors[6] = Color.CYAN;
        colors[7] = Color.WHITE;


        Map3D chooseColor = new Map3D(10,1,0);

        for(int i=0;i<10;++i){
            for(int j=0; j<10; ++j){
                if(i==0 || j==0 || i==9  || j==9){
                    Stone stone = new Stone(new Point3D(i,j,0));
                    chooseColor.insertObjAt(stone.position,stone);
                }
                else{
                    chooseColor.removeAt(new Point3D(i,j,0));
                }
            }
        }

        Point3D colorGrid = new Point3D(1,5,0);
        for(int i=1;i<9;++i){
            chooseColor.getAt(colorGrid).background = createColorBitmap(colors[i-1]);
            colorGrid.moveFor(Vector3D.Xp,1);
        }
        gameInitioal(chooseColor,Color.TRANSPARENT,Dimension.Z,0);

/*
 * Set OnClick For 8 Buttons Actions
 * */

        attack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!player.attack())
                    actionFail();
            }
        });

        straigh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!player.straight())
                    actionFail();
            }
        });

        horizom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!player.horizon())
                    actionFail();
            }
        });

        mask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                player.mask();
            }
        });

        plainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dimension dimension = player.changePlain();
                switch (dimension){
                    case X:
                        plainButton.setImageResource(R.drawable.x);
                        break;

                    case Y:
                        plainButton.setImageResource(R.drawable.y);
                        break;

                    case Z:
                        plainButton.setImageResource(R.drawable.z);
                        break;
                }
            }
        });

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchControlMode();
            }
        });

        upButton.setOnTouchListener(this);

        downButton.setOnTouchListener(this);

        leftButton.setOnTouchListener(this);

        rightButton.setOnTouchListener(this);

        gameStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int playerColor = player.color;
                int layers = player.position.X()+player.position.Y()+1;
                int numOfEnemy = player.getPower()+1;
                gameInitioal(new Map3D(64,layers,numOfEnemy*layers),playerColor,Dimension.Z,numOfEnemy);
                gameStartButton.setVisibility(View.INVISIBLE);
                isGameStart = true;
            }
        });

    }

    private void findAllViews(){
        gameStartButton=findViewById(R.id.gamestart);
        playerInfo=findViewById(R.id.playerinfo);
        gameInfo=findViewById(R.id.gameinfo);
        gameSence = findViewById(R.id.test);
        GlobalAsset.gameScence = gameSence;

        attack = findViewById(R.id.attack);
        straigh= findViewById(R.id.straight);
        horizom=findViewById(R.id.horizon);
        mask = findViewById(R.id.mask);

        plainButton=findViewById(R.id.plain);
        menu=findViewById(R.id.menu);

        vectorGroup = findViewById(R.id.vectorgroup);
        upButton = findViewById(R.id.up);
        downButton = findViewById(R.id.down);
        leftButton = findViewById(R.id.left);
        rightButton = findViewById(R.id.right);
    }

    private void setGlobalData(){
        GlobalAsset.stoneShape = BitmapFactory.decodeResource(getResources(),R.drawable.stone);
        GlobalAsset.foodShape = BitmapFactory.decodeResource(getResources(),R.drawable.food);
        GlobalAsset.groundShape = BitmapFactory.decodeResource(getResources(),R.drawable.ground);
        GlobalAsset.attackBMP = BitmapFactory.decodeResource(getResources(),R.drawable.attack);
        GlobalAsset.straightBMP = BitmapFactory.decodeResource(getResources(),R.drawable.straight);
        GlobalAsset.horizonBMP = BitmapFactory.decodeResource(getResources(),R.drawable.horizon);

        GlobalAsset.shape = new Shape();
        GlobalAsset.shape.setShapeAt(Vector2D.Up,BitmapFactory.decodeResource(getResources(),R.drawable.up));
        GlobalAsset.shape.setShapeAt(Vector2D.Down,BitmapFactory.decodeResource(getResources(),R.drawable.down));
        GlobalAsset.shape.setShapeAt(Vector2D.Left,BitmapFactory.decodeResource(getResources(),R.drawable.left));
        GlobalAsset.shape.setShapeAt(Vector2D.Right,BitmapFactory.decodeResource(getResources(),R.drawable.right));
        GlobalAsset.shape.setShapeAt(Vector2D.In,BitmapFactory.decodeResource(getResources(),R.drawable.in));
        GlobalAsset.shape.setShapeAt(Vector2D.Out,BitmapFactory.decodeResource(getResources(),R.drawable.out));
    }

    private void gameInitioal(Map3D gameMap, int playerColor, Dimension initDimention, int numOfEnemy){
        // Map Initial
        GlobalAsset.gameMap = gameMap;

        // Player View
        map = new Map2D(GlobalAsset.gameMap);

    /*
        *  Player Initial
        * */
        // Set Player's Position
        final Point3D playersPosition = GlobalAsset.gameMap.randPoint();

        // Create Player's Animal
        player = new Animal(playersPosition,initDimention,playerColor,GlobalAsset.shape,Vector2D.Out,10,100);
        GlobalAsset.player = player;
        GlobalAsset.gameMap.priorityInsertObjAt(player.position,player);

     /*
          *  Enemy Initial
          * */
        // Create Enemys
        enemys = new ArrayList<AiAnimal>();
        int count = 0;
        while (enemys.size() < numOfEnemy && count < numOfEnemy*10){
            ++count;
            final Point3D enemyPosition = GlobalAsset.gameMap.findAnEmptyGrid();
            if(enemyPosition == null)continue;

            final AiAnimal enemy = new AiAnimal(enemyPosition,initDimention,colors[count%8],GlobalAsset.shape,Vector2D.Left,10,100);
            enemys.add(enemy);
            GlobalAsset.gameMap.insertObjAt(enemy.position,enemy);

        }
    }


    private void runForClock(){
        gameSence.clear();

        if(playerMove){
            player.move();
        }

        Iterator<AiAnimal>iter = enemys.iterator();
        while (iter.hasNext()){
            AiAnimal each = iter.next();
            each.auto();
            if(each.isDead())
                iter.remove();
        }

        GlobalAsset.gameMap.insertRandomFood();

        map.drawOn(gameSence,new Range2D(player.posit,playerViewRange));
        ActionManager.drawAction();

        playerInfo.setText(String.format("HP:%d\nEP:%d\nPosition (%d,%d,%d)",
                player.getHp(),player.getPower(),player.position.X(),player.position.Y(),player.position.Z()));

        gameInfo.setText(String.format("剩餘:%d",enemys.size()+1));

        if(!isGameStart){
            gameInfo.setText("請選擇顏色");
            if(player.position.Y() == 5){
                int colorIndex = player.position.X()-1;
                player.color = colors[colorIndex];
            }
        }
        else if(enemys.size() == 0){
            gameInfo.setText("你贏了");
            gamePause = true;
        }

        if(player.isDead()){
            gameInfo.setText("你已經死了");
            gamePause = true;
        }

    }

    private void setBaseSensor(){
        setBaseSensor = true;
    }

    private void actionFail(){
        Toast.makeText(this,"EP 不足!",Toast.LENGTH_SHORT).show();
    }

    private Bitmap createColorBitmap(int color){
        Bitmap b = Bitmap.createBitmap(100,100, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        c.drawColor(color);
        return b;
    }

    @Override
    protected void onStart(){
        super.onStart();
        Log.d("test","on Start");
    }

    @Override
    protected void onResume(){
        super.onResume();
        gamePause = false;
        Log.d("test","on Resume");
    }

    @Override
    protected void onPause(){
        super.onPause();
        gamePause = true;
        Log.d("test","on Pause");
    }

    @Override
    protected void onStop(){
        super.onStop();
        Log.d("test","on Stop");
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        Log.d("test","on Destroy");
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        if(controlBySensor)return false;

        switch (view.getId()){
            case R.id.up:
                player.turnTo(Vector2D.Up);
                break;

            case R.id.down:
                player.turnTo(Vector2D.Down);
                break;

            case R.id.left:
                player.turnTo(Vector2D.Left);
                break;

            case R.id.right:
                player.turnTo(Vector2D.Right);
                break;
        }

        if(motionEvent.getAction() == MotionEvent.ACTION_UP){
            playerMove = false;
        }else if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
            playerMove = true;
        }

        return false;
    }

    public void switchControlMode(){
        if(controlBySensor){
            controlBySensor = false;
            vectorGroup.setVisibility(View.VISIBLE);
        }
        else {
            controlBySensor = true;
            setBaseSensor();
            vectorGroup.setVisibility(View.GONE);
        }
    }
}
