package tw.kauhiant.mygame;

/**
 * Created by kauhia on 2018/6/5.
 */

public class Range2D {
    public Point2D centor;
    public int extra;

    public Range2D(Point2D centor, int extra){
        this.centor = centor.copy();
        this.extra = extra;
    }

    public Iterator iterator(){
        return new Iterator(this);
    }

    public class Iterator{
        public Point2D iterator;

        private int rangeLine;
        private int count = 0;
        private int limit = 0;

        public Iterator(Range2D range){
            this.iterator = range.centor.copy();
            this.rangeLine = range.extra * 2 + 1;
            iterator.moveFor(Vector2D.Left, range.extra);
            iterator.moveFor(Vector2D.Up, range.extra);
        }

        public void moveToNext(){
            if(limit == rangeLine) return;

            if(count == rangeLine-1){
                iterator.moveFor(Vector2D.Left, rangeLine-1);
                iterator.moveFor(Vector2D.Down, 1);
                count = 0;
                ++limit;
                return;
            }

            iterator.moveFor(Vector2D.Right, 1);
            ++count;

        }
    }
}
