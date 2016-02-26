package networkFlow;
import java.util.*;

/**
 * The Class ResidualGraph.
 * Represents the residual graph corresponding to a given network.
 */
public class ResidualGraph extends Network {

    protected Edge [][] residualAdjMatrix;

    protected ArrayList<LinkedList<Vertex>> residualAdjLists;
    /**
	 * Instantiates a new ResidualGraph object.
	 * Builds the residual graph corresponding to the given network net.
	 * Residual graph has the same number of vertices as net.
	 * @param net the network
	 */
	public ResidualGraph (Network net) throws Exception {
		super(net.numVertices);
        // initialize residualAdjMatrix
        residualAdjMatrix = new Edge [numVertices][numVertices];
        residualAdjLists = new ArrayList<>();
        for (int u = 0; u < numVertices; u++) {
            residualAdjLists.add(new LinkedList<>());
            for (int v = 0; v < numVertices; v++)
                residualAdjMatrix[u][v] = null;
        }

        for (int u = 0; u < numVertices; u ++) {
            LinkedList<Vertex> neighbors = net.getAdjList(getVertexByIndex(u));
            for (int v = 0; v < neighbors.size(); v ++) {
                Vertex currN = neighbors.get(v);
                if (u != currN.getLabel() ) {
                    Edge edge = net.getAdjMatrixEntry(getVertexByIndex(u), currN);
                    //System.out.println("(u,v): (" + u + "," + currN.getLabel() + ") edge cap: " + edge.getCap() + " flow: " + edge.getFlow());
                    setResidualEdges(edge);
                }
            }
        }
	}

    private void setResidualEdges(Edge e) {
        Vertex vu = e.getSourceVertex();
        Vertex vv = e.getTargetVertex();
        // residual edge is a backward edge and it is only 1:
        if (e.getFlow() == e.getCap()) {
            //System.out.println("f = c, backward edge");
            Edge resBackEdge = new Edge(vv,vu,e.getFlow());
            residualAdjMatrix[vv.getLabel()][vu.getLabel()] = resBackEdge;
            residualAdjLists.get(vv.getLabel()).addLast(vu);
        }
        // residual graph has forward edge with flow = cap - flow
        // residual graph has backward edge with flow = flow
        else if (e.getFlow() < e.getCap() && e.getFlow() > 0) {
            //System.out.println("f < c, and f > 0 backward and forward edge");
            int newFlow = e.getCap() - e.getFlow();
            Edge resForwEdge = new Edge(vu,vv,newFlow);
            Edge resBackEdge = new Edge(vv,vu,e.getFlow());
            residualAdjMatrix[vv.getLabel()][vu.getLabel()] = resBackEdge;
            residualAdjMatrix[vu.getLabel()][vv.getLabel()] = resForwEdge;
            residualAdjLists.get(vv.getLabel()).addLast(vu);
            residualAdjLists.get(vu.getLabel()).addLast(vv);
        }
        // residual edge is one forward edge
        else if (e.getFlow() == 0) {
            //System.out.println("f = 0, forward edge");
            int newFlow = e.getCap();
            Edge resForwEdge = new Edge(vu,vv,newFlow);
            residualAdjMatrix[vu.getLabel()][vv.getLabel()] = resForwEdge;
            residualAdjLists.get(vu.getLabel()).addLast(vv);
        }
    }

	/**
	 * Find an augmenting path if one exists.
	 * Determines whether there is a directed path from the source to the sink in the residual
	 * graph -- if so, return a linked list containing the edges in the augmenting path in the
     * form (s,v_1), (v_1,v_2), ..., (v_{k-1},v_k), (v_k,t); if not, return an empty linked list.
	 * @return the linked list
	 */
    // do bfs to find the shortest augmenting path
	public LinkedList<Edge> findAugmentingPath () {
        int[] pred = bfs(); // pred[i] takes the value of the label of the node that is before the node with label i in the search

        // start from sink and backtrack until finding the source
        LinkedList<Vertex> path = new LinkedList<>();
        int currLabel = sinkLabel;
        if (pred[currLabel] == -1) { // this means we didn't manage to reach the sink, i.e. there is no augm path
            return null;
        }
        while(currLabel != sourceLabel) {
            path.addFirst(getVertexByIndex(currLabel));
            currLabel = pred[currLabel];
        }
        path.addFirst(source);
        LinkedList<Edge> augmPath = new LinkedList<>();
        for (int u = 0, v = 1; u < path.size() - 1 && v < path.size(); u ++, v++) {
            Vertex vu = path.get(u);
            Vertex vv = path.get(v);
            augmPath.add(residualAdjMatrix[vu.getLabel()][vv.getLabel()]);
        }
		return augmPath;
	}

    /** bfs modified to return an array of size numVertices,
     * where the i-th entry of the array has the label of
     * the predecessor node of the node labeled as i after breadth first search */
    private int[] bfs() {
        LinkedList<Vertex> q = new LinkedList<>();
        int pred[] = new int[numVertices]; // the predecessor of each node during bfs
        /**initialize pred, everything is -1 initially. There is no entry for the source */
        for (int i = 0; i < numVertices; i++) {
            /** the value of pred is used also to check whether a node
             * is already visited.
             * if pred[i] == -1: vertex i not visited
             * else: vertex i is already visited
             * As initially all nodes are not visited, we put -1 as value for all nodes
             * We do not put -1 for pred[0], as this is the starting node
            */
            pred[i] = -1;
        }
        q.addLast(source);
        /** classic bfs, with small modification */
        while(q.size() > 0) {
            Vertex currV = q.removeFirst();
            LinkedList<Vertex> neighbors = residualAdjLists.get(currV.getLabel());
            for (int j = 0; j < neighbors.size(); j++) {
                Vertex v = neighbors.get(j);
                if (pred[v.getLabel()] == -1) { // if the vertex is not visited yet
                    pred[v.getLabel()] = currV.getLabel(); // this is the modification of bfs
                    q.addLast(v);
                }
            }
        }
        return pred;
    }

    private void printResG() {
        for (int u = 0; u < numVertices; u++) {
            LinkedList<Vertex> neighbors = getResAdjList(getVertexByIndex(u));
            for (int v = 0; v < neighbors.size(); v++) {
                Vertex currN = neighbors.get(v);
                if (u != currN.getLabel()) {
                    Edge edge = getResAdjMatrixEntry(getVertexByIndex(u), currN);
                    System.out.println("(u,v): (" + u + "," + currN.getLabel() + ") edge cap: " + edge.getCap() + " flow: " + edge.getFlow());
                }
            }
        }
    }

    /**
     * Gets the adjacency list for a given vertex vv.
     *
     * @param vv the given vertex
     * @return the adjacency list
     */
    public LinkedList<Vertex> getResAdjList(Vertex vv) {
        return residualAdjLists.get(vv.getLabel());
    }

    /**
     * Gets the adjacency list for a given vertex with label v.
     * @param v the vertex label
     * @return the adjacency list
     */
    public LinkedList<Vertex> getResAdjListByLabel(int v) {
        return residualAdjLists.get(v);
    }

    /**
     * Gets the adjacency matrix entry corresponding to vertices uu and vv.
     *
     * @param uu the first vertex
     * @param vv the second vertex
     * @return the adjacency matrix entry
     */
    public Edge getResAdjMatrixEntry(Vertex uu, Vertex vv) {
        return residualAdjMatrix[uu.getLabel()][vv.getLabel()];
    }
}
