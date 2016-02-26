public class Main {
	
	/**
	 * The main method.
	 * @param args the name of the file containing the input network.
	 */
	public static void main(String args[]) throws Exception {

		// construct FordFulk object passing filename to constructor
		FordFulk fordFulk = new FordFulk (args[0]); // filename

		// read network from file given by first argument
		fordFulk.readNetworkFromFile();


		// apply Ford-Fulkerson algorithm to constructed network
		boolean success = fordFulk.fordFulkerson(0);
        if (!success) {
            System.out.println("No assignment exists that meets all the lecturer lower quotas");
            return;
        }
        if (success){
            fordFulk.fordFulkerson(-1);
            // check whether the assignment is a valid flow
            fordFulk.printResults();
        }
	}
}
