package networkFlow;

/**
 * Created by iva on 2/7/16.
 */
public class Project extends Vertex {

    protected int capacity; // how many students can take this project?

    protected int proposedBy; // the id of the lecturer who proposed this project

    protected boolean isSE; // is the project suitable for software engineering students?

    /**
     * Instantiates a new vertex.
     *
     * @param i the vertex label
     */
    public Project(int i, String isSE, int capacity, int proposedBy) {
        super(i);
        if (isSE.equals("Y")) {
            this.isSE = true;
        }
        else {
            this.isSE = false;
        }
        this.capacity = capacity;
        this.proposedBy = proposedBy;
    }

    public int getLabel(){
        return super.label;
    }

    public boolean isSE(){
        return this.isSE;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getProposedBy() {
        return proposedBy;
    }
}
