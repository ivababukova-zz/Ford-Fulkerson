package networkFlow;

/**
 * Created by iva on 2/7/16.
 */
public class Lecturer extends Vertex {

    protected int uquota; // the upper quota of the lecturer

    protected int lquota; // the lower quota of the lecturer
    /**
     * Instantiates a new vertex.
     *
     * @param i the vertex label
     */
    public Lecturer(int i, int capacity, int lquota) {
        super(i);
        this.uquota = capacity;
        this.lquota = lquota;
    }

    public int getLabel(){
        return super.label;
    }

    public int getUquota(){
        return this.uquota;
    }

    public int getLquota(){
        return this.lquota;
    }
}
