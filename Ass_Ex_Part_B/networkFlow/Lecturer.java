package networkFlow;

/**
 * Created by iva on 2/7/16.
 */
public class Lecturer extends Vertex {

    protected int capacity; // how many students can the lecturer supervise?

    /**
     * Instantiates a new vertex.
     *
     * @param i the vertex label
     */
    public Lecturer(int i, int capacity) {
        super(i);
        this.capacity = capacity;
    }

    public int getLabel(){
        return super.label;
    }

    public int getCapacity(){
        return this.capacity;
    }
}
