package petrinets;


import java.util.ArrayList;

/*
 * Implementation of the Depth First Search using Adjacency Link List Representation with a queue
 */


/**
 *
 * @author Lea Vega
 */
public class DepthFirstSearchLinkList implements DepthFirstSearch{
    Node tree[]; //Array of the linked list
    boolean visited[]; //Array that store if the node is visited
    private final static int DEFAULT_SIZE=10;
    int size=0;
    String path="";
    
    
     //Creates the tree with the default size
    public DepthFirstSearchLinkList(){
        tree=new Node[DEFAULT_SIZE];
        visited=new boolean[DEFAULT_SIZE];
        size=DEFAULT_SIZE;
    }
    
    //Creates the tree with a specified size
    public DepthFirstSearchLinkList(int tam){
        tam=tam+1;
        
        tree=new Node[tam];
        visited=new boolean[tam];
        size=tam;
    }

    //Adds a pair of vertices
    @Override
    public void add(Node node1, Node node2) {
        size++;
        
        if(size>=tree.length){
            Node temp[]=new Node[tree.length*2];
            System.arraycopy(tree, 0, temp, 0, tree.length);
        }
        
        Node temp;
        Node newNode1=new Node(); 
        newNode1.setId(node1.getId());
        
        Node newNode2=new Node();
        newNode2.setId(node2.getId());
        
        //Store in the matrix its adjacent node 
        if(tree[node1.getId()]==null){  //If is the first node
            tree[node1.getId()]=newNode2;
        }
        else{ 
            temp=tree[node1.getId()];
            newNode2.setNext(temp);
            tree[node1.getId()]=newNode2;
        }
        
        //Store in the matrix its adjacent node 
        /*if(tree[node2.getId()]==null){ 
            tree[node2.getId()]=newNode1;
        }
        else{ 
            temp=tree[node2.getId()];
            newNode1.setNext(temp);
            tree[node2.getId()]=newNode1;
        }*/
    }

    
    //Returns a Removes a pair of vertices
    @Override
    public String remove(Node node1,Node node2){
        
         Node temp,before=null;
         boolean flag=false;
         int actual;
         
         temp=tree[node1.getId()];
         actual=temp.getId();
         
         //Remove the relationship from node1 to node2
         while(!flag){
           if(actual==node2.getId()){
                if(temp.getNext()==null){
                    tree[node1.getId()]=null;
                    flag=true;
                }
                else{ 
                   if(before!=null){ 
                    before.setNext(temp.getNext());
                    tree[node1.getId()]=before;
                   }else{
                    tree[node1.getId()]=temp.getNext();
                   }
                   flag=true;
             }     
            }
           else{
               if(temp.getNext()!=null){
                 before=temp;
                 temp=temp.getNext();
                 actual=temp.getId();
               }
               
           }
         }
           
         temp=tree[node2.getId()];
         before=null;
         actual=temp.getId();
         flag=false;
        
         //Remove the relationship from node2 to node1
         while(!flag){
           if(actual==node1.getId()){
                  if(temp.getNext()==null){
                    tree[node2.getId()]=null;
                    flag=true;
                }
                else{ 
                   if(before!=null){ 
                    before.setNext(temp.getNext());
                    tree[node2.getId()]=before;
                   }else{
                    tree[node2.getId()]=temp.getNext();
                   }
                   flag=true;
             }     
         }else{
               if(temp.getNext()!=null){
                 before=temp;
                 temp=temp.getNext();
                 actual=temp.getId();
               }
               
           }
         }
       size--;
       
       return "("+node1.getId()+","+node2.getId()+")";
    }
    
    @Override
    public String totalPathFrom(Node root) {
        pathFrom(root);
        return path;
    }

    
    //Walking through all the nodes of the graph starting from the specified node 
    @Override
   public void pathFrom(Node root){
      int actual=root.getId();
      int index;
      Node temp;
       
      if(visited[actual]==true){ //If the node was visited returns
         return;
      }
      else{
         visited[actual]=true;  
         System.out.println("Node visited: "+actual);
         path=path+actual+",";
         
         if(tree[actual]==null){ //Does have adjencety nodes
            return;
         }
         else{
            temp=tree[actual]; //Store the actual node
            
            while(temp!=null){ // Go over its adjencenty nodes
               index=temp.getId();

               if(visited[index]==true){ //If it was visited
                  return;
               }
               else{
                  pathFrom(temp); 
                  temp=temp.getNext(); //Stack of the adjencety nodes by visiting
               }
            }
         }
      }
    }  
    
    //Clean the array visited
    @Override
    public void cleanVisited() {
        for (int a =0; a< visited.length;a++) {
            visited[a]=false;
        }
        path="";
        
    }
    
    // Returns the string representation of the linked list
    @Override
    public String toString(){
        Node temp;
        String cadena="";
        
        for(int i=0;i<size;i++){   
            temp=tree[i];
            cadena=cadena+"i= "+i+"|";
            while(temp!=null){
                cadena=cadena+temp.getId() +" "; 
                temp=temp.getNext();
            }
            cadena=cadena+"\n";
           }
        
        return cadena; 
    }	
    
     
  /*   
   public static void main(String ar[]){ 
         DepthFirstSearchLinkList a= new  DepthFirstSearchLinkList(6);
         Node q1=new Node(1);
         Node q2=new Node(2);
         Node q3=new Node(3);
         Node q4=new Node(4);
         Node q5=new Node(5);
         
         
            a.add(q1,q2);
            a.add(q1,q3);
            a.add(q2,q4);
            a.add(q3,q4);
            a.add(q4,q5);
            a.add(q5,q2);
            a.add(q5,q3);
     
           System.out.println(a.toString());
           
           System.out.println(a.totalPathFrom(q2));
           
      }
*/

   
   
}