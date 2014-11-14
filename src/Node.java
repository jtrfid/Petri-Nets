
import java.util.HashSet;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author petergtam
 */
public class Node {
    private HashSet<Integer> marker;
    private String type;
    
    
    public HashSet<Integer> getMarker() {
        return marker;
    }

    public void setMarker(HashSet<Integer> marker) {
        this.marker = marker;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
