package petrinets;

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
    private ArrayList<Integer> marker;
    private char type;
    private Node parent;
    private int id; 
    private Node next;
    private int transition;
    
     
    public Node(){}
     
    public Node(int id){
        this.id=id;
    }
    

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Node getNext() {
        return next;
    }

    public void setNext(Node next) {
        this.next = next;
    }

    public int getTransition() {
        return transition;
    }

    public void setTransition(int transition) {
        this.transition = transition;
    }
    
    public ArrayList<Integer> getMarker() {
        return marker;
    }

    public void setMarker(ArrayList<Integer> marker) {
        this.marker = marker;
    }

    public char getType() {
        return type;
    }

    public void setType(char type) {
        this.type = type;
    }
   
    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }
}
