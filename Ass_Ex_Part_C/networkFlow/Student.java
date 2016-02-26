package networkFlow;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by iva on 2/7/16.
 */
public class Student extends Vertex {

    /**
     * Variable to keep track whether the student is SE engineering student
     * */
    protected boolean isSE;

    /**
     * an arraylist of all ranked projects by a student
     * */
    protected ArrayList<Integer> choices;

    /**
     * Instantiates a new vertex.
     *
     * @param i the vertex label
     */
    public Student(int i, String isSE, ArrayList<Integer> choices) {
        super(i);
        if (isSE.equals("Y")) {
            this.isSE = true;
        }
        else {
            this.isSE = false;
        }
        this.choices = choices;
    }

    public int getLabel(){
        return super.label;
    }

    public ArrayList<Integer> getChoices(){
        return this.choices;
    }

    public boolean isSE(){
        return this.isSE;
    }
}
