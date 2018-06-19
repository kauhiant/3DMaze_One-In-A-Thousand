package tw.kauhiant.mygame;

/**
 * Created by kauhia on 2018/6/7.
 */

public class Int {
    private int value;

    public Int(int value){
        this.value = value;
    }

    public int val(){
        return value;
    }

    public void set(int value){
        this.value = value;
    }

    public void add(int value){
        this.value += value;
    }
}
