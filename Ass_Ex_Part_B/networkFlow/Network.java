package networkFlow;
import java.util.*;

/**
 * The Class Network.
 * Represents a network - inherits from DirectedGraph class.
 */
public class Network extends DirectedGraph {

	/** The source vertex of the network. */
	protected Vertex source;
	
	/** The label of the source vertex. */
	protected int sourceLabel;
	
	/** The sink vertex of the network. */
	protected Vertex sink;
	
	/** The label of the sink vertex. */
	protected int sinkLabel;

	/**
	 * Instantiates a new network.
	 * @param n the number of vertices
	 */
	public Network (int n) {
		super(n);

		// add the source vertex - assumed to have label 0
		sourceLabel = 0;
		source = addVertex(sourceLabel);
		// add the sink vertex - assumed to have label numVertices - 1
		sinkLabel = numVertices - 1;
		sink = addVertex(sinkLabel);

		// add the remaining vertices
		for (int i = 1 ; i <=numVertices-2 ; i++) 
			addVertex(i);
	}

	/**
	 * Gets the source vertex.
	 * @return the source vertex
	 */
	public Vertex getSource() {
		return source;
	}

	/**
	 * Gets the sink vertex.
	 * @return the sink vertex
	 */
	public Vertex getSink() {
		return sink;
	}

	/**
	 * Adds the edge with specified source and target vertices and capacity.
	 * @param source the source endpoint vertex
	 * @param target the target endpoint vertex
	 * @param cap the capacity of the edge
	 */
	public void addEdge(Vertex source, Vertex target, int cap) {
		Edge e = new Edge(source, target, cap);
		adjLists.get(source.getLabel()).addLast(target);
		adjMatrix[source.getLabel()][target.getLabel()]=e;
	}

	/**
	 * Returns true if and only if the assignment of integers to the flow fields of 
	 * each edge in the network is a valid flow.
	 * @return true, if the assignment is a valid flow
	 *
     * valid flow:
     * 1) total flow out from s = total flow into t
     * 2) capacity constr: for every edge, 0<=flow<=capacity
     * 3) flow conservation constr: for every vertex apart from s and t, total in flow = total out flow
     * */
	public boolean isFlow() {
        /** a matrix that contains the flow of every edge or 0 if the edge does not exist.
         * flowMatrix[i][j] contains the flow of the edge (i,j), where i and j are labels
         * of the nodes. */
        int[][] flowMatrix = new int[numVertices][numVertices];

        int inflow[] = new int[numVertices];
        int outflow[] = new int[numVertices];

        /** initialize inflow and outflow */
        for (int i=0; i<numVertices;i++){
            inflow[i] = 0; // total incoming flow for a node with label i
            outflow[i] = 0; // total outgoing flow for a node with label i
        }

        /** build the flow matrix */
        for (int i=0;i<numVertices;i++) {
            for (int j=0;j<numVertices;j++) {
                Edge e = getAdjMatrixEntry(getVertexByIndex(i),getVertexByIndex(j));
                int flow = 0;
                if (e != null) {
                    flow = e.getFlow(); // update the flow var if the edge exists
                    if (flow < 0 || flow > e.getCap()) { // 2)
                        return false;
                    }
                }
                flowMatrix[i][j] = flow; // if the edge does not exist, we put 0
            }

        }
        /** derive the incomming and outgoing flow for every vertex */
        for(int i = 0; i < numVertices; i++) {
            for (int j = 0; j < numVertices; j++) {
                outflow[i] += flowMatrix[j][i];
                inflow[i] += flowMatrix[i][j];
            }
        }

        /** check the flow conservation constraint */
        for (int i = 1; i < numVertices - 1; i++) { // 3)
            if (inflow[i] != outflow[i]) {
                return false;
            }
        }

        /** check that the flow in the sink is the same as the flow out of the source */
        if (inflow[sinkLabel] != outflow[sourceLabel]) { // 1)
            return false;
        }

        return true;
	}

	/**
	 * Gets the value of the flow: the total flow out from s or the total flow into t
	 * @return the value of the flow
	 */
	public int getValue() {
        int totalF = 0;
        /** total flow of sourse(s), same as total flow out of sink(t): */
        LinkedList<Vertex> neighbors = getAdjListByLabel(sourceLabel);
        for (int i = 0; i < neighbors.size(); i++) {
            totalF = totalF + getAdjMatrixEntry(source,neighbors.get(i)).getFlow();
        }
		return totalF;
	}

	/**
	 * Prints the flow.
	 * Display the flow through the network
	 */
	public void printFlow(int numStudents, int numProjects, int numLecturers) {
        // print students
        for (int s = 1; s <= numStudents; s++) {
            boolean isprinted = false;
            for (int p = 1 + numStudents; p <= numStudents + numProjects; p++) {
                Edge e = getAdjMatrixEntry(getVertexByIndex(s), getVertexByIndex(p));
                if (e!= null && e.getFlow() == 1) {
                    System.out.println("Student " + s + " is assigned to project " + (p -numStudents ));
                    isprinted = true; // the student is assigned a project
                }
            }
            // print the student if he/she has no project
            if (!isprinted) {
                System.out.println("Student " + s + " is unassigned");
            }
        }
        System.out.println();
        // print projects
        for (int p = numStudents + 1; p <= numStudents + numProjects; p++) {
            for (int l = 1 + numStudents + numProjects; l <= numStudents + numProjects + numLecturers; l++) {
                Edge e = getAdjMatrixEntry(getVertexByIndex(p), getVertexByIndex(l));
                if (e != null) {
                    System.out.print("Project " + (p - numStudents)
                            + " with capacity " + e.getCap()
                            + " is assigned to " + e.getFlow() + " student");
                    if (e.getFlow() == 1) {
                        System.out.println();
                    }
                    else {
                        System.out.println("s");
                    }
                }
            }
        }
        System.out.println();
        // print lecturers
        for (int l = numStudents + numProjects + 1; l <= numLecturers + numProjects + numStudents; l++) {
            Edge e = getAdjMatrixEntry(getVertexByIndex(l), sink);
            if (e != null) {
                System.out.print("Lecturer " + (l - numStudents - numProjects)
                        + " with capacity " + e.getCap()
                        + " is assigned to " + e.getFlow() + " student");
                if (e.getFlow() == 1) {
                    System.out.println();
                }
                else {
                    System.out.println("s"); // put 's' if there are 0 or more than 1 students
                }
            }
        }
	}

    /**
     * Updates the flow by adding/substracting the new flow derived after an iteration
     * of the Ford-Fulk algorithm.
     * @param resEdge: the edge from the residual graph that is part of the augm path
     *               we use this edge to get the original edge in the graph that is to be updated
     * @param newFlow: the new flow we are updating with
     * */
    public void updateFlow(Edge resEdge, int newFlow){
        Vertex residualVU = resEdge.getSourceVertex();
        Vertex residualVV = resEdge.getTargetVertex();
        Edge edge = getAdjMatrixEntry(residualVU, residualVV);
        /** we have a backward edge, have to substract the new flow */
        if (edge == null) {
            edge = getAdjMatrixEntry(residualVV,residualVU);
            edge.setFlow(edge.getFlow() - newFlow);
        }
        /** we have a forward edge, have to add the new flow */
        else {
            edge.setFlow(edge.getFlow() + newFlow);
        }
    }
}
