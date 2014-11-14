
import java.io.File;
import java.util.HashSet;



/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Lea Vega
 */
public class PetriNetworks {
    int c[][]; //Incidence matrix 
    int post[][];
    int pre[][];
    int m0[];
    
    public void reachabilityGraph(){
    
      
    }
    
    private HashSet<Integer> enableTransition(HashSet<Integer> m){
        
        return null;
    }
    
    private HashSet<Integer> firingCondition(HashSet<Integer> m, HashSet<Integer> vk){
    
        return null;
    }
    
    private boolean reversibility(){
    
      return true;
    }
    
    private boolean boundedness(){
    
      return true;
    }
    
    private boolean liveness(){
    
      return true;
    }
    
    
    
    
    
    
    
    
    
    
    public static void main(String[] ar){
     GraphViz gv = new GraphViz();
      gv.addln(gv.start_graph());
      gv.addln( "node [shape=plain];\n" +
                " node [fillcolor=\"#EEEEEE\"];\n" +
                " node [color=\"#EEEEEE\"];\n" +
                " edge [color=\"#31CEF0\"];");
      
      
      gv.addln("\"[09876]\" -> B [label=\"asddsfds\"];");
      gv.addln(" t1 -> C;");
      gv.addln(gv.end_graph());
      
       String type = "png";
       File out = new File("out." + type);    // Windows
       gv.writeGraphToFile( gv.getGraph(gv.getDotSource(), type ), out );
    
    }
        
   
}
