
import java.util.ArrayList;


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

    public ArrayList<Integer> getMarker() {
        return marker;
    }

    public void setMarker(ArrayList<Integer> marker) {
        this.marker = marker;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    private ArrayList<Integer> marker;
    private String type;
    
    
   
}
