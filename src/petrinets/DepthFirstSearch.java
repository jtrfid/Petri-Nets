package petrinets;


/*
 * Interface of DepthFirstSearch which is implemented in DepthFirstSearchLinkList
 */


/**
 *
 * @author Lea Vega
 */
public interface DepthFirstSearch {
    public void add(Node node1,Node node2); //Adds a pair of vertices   
    public String remove(Node node1,Node node2); //Removes a pair of vertices
    public void pathFrom(Node root); //Walking through all the nodes of the graph starting from the specified node 
    public void cleanVisited(); //Cleans all information about visited nodes
    
    public String totalPathFrom(Node root); // Return a string with the path from the specificated node
    
    
}
