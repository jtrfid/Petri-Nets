package petrinets;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

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
    int post[][] = {{1, 0, 0}, {1, 1, 0}, {0, 1, 1}}; //matriz post
    int pre[][] = {{1, 1, 0}, {0, 0, 1}, {0, 0, 1}}; //matriz pre
    Node n0; //nodo inicial
    int p, t; //tamaños de los lugares y las transiciones 
    ArrayList<Integer> m0; //marcdo inicial
    ArrayList<Node> rp; //arreglo con los nodos
    ArrayList<Node> procesados;
    DepthFirstSearchLinkList g;
    int size = 0; //Numero de nodos creados que ademas almacenara el Id del nodo

    public PetriNetworks() {
        m0 = new ArrayList<>();
        rp = new ArrayList<>();
        procesados = new ArrayList<>();
        g= new DepthFirstSearchLinkList(10);
        
        try (Scanner s = new Scanner(System.in)) {
            System.out.print("Introduce the number of places: ");
            try {
                p = Integer.parseInt(s.nextLine());
            } catch (Exception e) {
                throw new NumberFormatException("Error: No es un numero");
            }
            System.out.print("Introduce the number of transitions: ");
            try {
                t = Integer.parseInt(s.nextLine());
            } catch (Exception e) {
                throw new NumberFormatException("Error: No es un numero");
            }

            c = new int[p][t];

            m0.add(1);
            m0.add(0);
            m0.add(0);

            // fillpre(s);
            // fillpost(s);
        }
        fillc();

    }

    private void fillpre(Scanner s) {
        System.out.println("Introduce pre for each place");
        for (int j = 0; j < t; j++) {
            System.out.println("Transition" + (j + 1));
            for (int i = 0; i < p; i++) {
                System.out.print("p" + (i + 1) + ": ");
                try {
                    pre[i][j] = Integer.parseInt(s.nextLine());
                } catch (Exception e) {
                    throw new NumberFormatException("Error: No es un numero");
                }
            }
        }
    }

    private void fillpost(Scanner s) {
        System.out.println("Introduce post for each place");
        for (int j = 0; j < t; j++) {
            System.out.println("Transition" + (j + 1));
            for (int i = 0; i < p; i++) {
                System.out.print("p" + (i + 1) + ": ");
                try {
                    post[i][j] = Integer.parseInt(s.nextLine());
                } catch (Exception e) {
                    throw new NumberFormatException("Error: No es un numero");
                }
            }
        }
    }

    private void fillc() {
        for (int i = 0; i < p; i++) {
            for (int j = 0; j < t; j++) {
                c[i][j] = post[i][j] - pre[i][j];
            }
        }
    }

    private void printMatriz(int[][] m) {
        int i = 1;
        System.out.println("Matriz{");
        for (int[] m1 : m) {
            System.out.print("  p" + (i++) + "[");
            int j;
            for (j = 0; j < m1.length - 1; j++) {
                System.out.print(m1[j] + ",");
            }
            System.out.print(m1[j] + "]\n");
        }
        System.out.println("}");
    }

    public void reachabilityGraph() {
        n0 = new Node(size++);
        n0.setMarker(m0);
        n0.setType('f');
        n0.setParent(null);

        rp.add(n0);
        procesados.add(n0);

        while (!procesados.isEmpty()) {
            Node nk = procesados.remove(0);

            if (nk.getType() == 'f') {
                //Buscar duplicado en rp
                for (int i = 0; i < rp.size(); i++) {
                    if (rp.get(i).getType() != 'f' && nk.getMarker().equals(rp.get(i).getMarker())) {
                        nk.setType('d');
                        cambiaTipo(nk,'d');
                        g.add(nk.getParent().get(0),rp.get(i));
                        break;
                    }
                }

                if (nk.getType() != 'd') {
                    //Transiciones habilitadas
                    ArrayList<Integer> vk = enableTransition(nk.getMarker());
                    if (vk.contains(1)) {
                        for (int j = 0; j < vk.size(); j++) {
                            if (vk.get(j) == 1) {
                                //se crea el nuevo nodo nz
                                Node nz = new Node(size++);
                                nz.getParent().add(nk);
                                nz.setMarker(firingCondition(nk.getMarker(), j));

                                //si mk en pi = w entonces mz en pi = w
                                for (int i = 0; i < nk.getMarker().size(); i++) {
                                    if (nk.getMarker().get(i) == Integer.MAX_VALUE) {
                                        nz.getMarker().set(i, Integer.MAX_VALUE);
                                    }
                                }

                                nz.setMarker(buscaMayor(nz, nz));
                                nz.setTransition(j);
                                nz.setType('f');

                                nk.getChildren().add(nz);
                                g.add(nk, nz);
                                
                                rp.add(nz);
                                procesados.add(nz);
                            }
                        }
                        nk.setType('e');
                        cambiaTipo(nk, 'e');
                        asignaNiños(nk, nk.getChildren());

                    } else {
                        nk.setType('t');
                        cambiaTipo(nk, 't');
                    }
                }
            }

        }
    }

    private void asignaNiños(Node nk, ArrayList<Node> children) {
        for (int i = 0; i < rp.size(); i++) {
            if (rp.get(i).getId() == nk.getId()) {
                rp.get(i).setChildren(children);
            }
        }

    }

    private void cambiaTipo(Node nk, char t) {
        for (int i = 0; i < rp.size(); i++) {
            if (rp.get(i).getId() == nk.getId()) {
                rp.get(i).setType(t);
            }
        }
    }

    private ArrayList<Integer> buscaMayor(Node nk, Node nz) {
        ArrayList<Integer> marker = nz.getMarker();

        for (int j = 0; j < nk.getParent().size(); j++) {
            boolean mayornz = true;

            for (int i = 0; i < nk.getParent().get(j).getMarker().size(); i++) {
                if (nz.getMarker().get(i) < nk.getParent().get(j).getMarker().get(i)) {
                    mayornz = false;
                    break;
                }
            }
            if (mayornz) {
                for (int a = 0; a < nk.getMarker().size(); a++) {
                    if (nz.getMarker().get(a) > nk.getParent().get(j).getMarker().get(a)) {
                        marker.set(a, Integer.MAX_VALUE);
                    }
                }
            }
        }

        return marker;
    }

    private ArrayList<Integer> enableTransition(ArrayList<Integer> m) {
        ArrayList<Integer> vk = new ArrayList(); //Firing vector
        boolean isGreater = false;
        for (int j = 0; j < t; j++) {
            for (int i = 0; i < m.size(); i++) {
                if (m.get(i) >= pre[i][j]) {
                    isGreater = true;
                } else {
                    isGreater = false;
                    break;
                }
            }
            if (isGreater) {
                vk.add(1);
            } else {
                vk.add(0);
            }
        }
        return vk;
    }

    //funcion que dispara todas las transiciones activas
    private ArrayList<Integer> firingCondition(ArrayList<Integer> m, ArrayList<Integer> vk) {
        ArrayList<Integer> result = new ArrayList<>();
        ArrayList<Integer> multiplicacion = multiMatrix(c, vk);
        for (int i = 0; i < m.size(); i++) {
            result.add(m.get(i) + multiplicacion.get(i));
        }
        return result;
    }

    //funcion que dispara la transicion tj
    private ArrayList<Integer> firingCondition(ArrayList<Integer> m, int tj) {
        ArrayList<Integer> result = new ArrayList<>();
        ArrayList<Integer> vk = new ArrayList<>();
        for (int i = 0; i < t; i++) {
            if (i == tj) {
                vk.add(1);
            } else {
                vk.add(0);
            }
        }
        ArrayList<Integer> multiplicacion = multiMatrix(c, vk);

        for (int i = 0; i < m.size(); i++) {
            result.add(m.get(i) + multiplicacion.get(i));
        }

        for (int i = 0; i < result.size(); i++) {
            if (result.get(i) < 0) {
                result.set(i, Integer.MAX_VALUE);
            }
        }

        return result;
    }

    private ArrayList<Integer> multiMatrix(int[][] m, ArrayList<Integer> vk) {
        ArrayList<Integer> result = new ArrayList<>();
        int i = 0;
        for (int[] mi : m) {
            int sum = 0;
            for (int j = 0; j < mi.length; j++) {
                sum += (m[i][j] * vk.get(j));
            }
            result.add(sum);
            i++;
        }
        return result;
    }

    private boolean reversibility() {
       String path;
     
        for (int i = 1; i < rp.size(); i++) {
            path = g.totalPathFrom((Node) rp.get(i));
            if (!path.contains("0")) { //If return to the initial state
                return false;
            }
            g.cleanVisited(); //Clean de path 
        }

        return true;
    }

    private boolean boundedness() {
        for (int i = 0; i < rp.size(); i++) {
            if(rp.get(i).getMarker().contains(Integer.MAX_VALUE)){
                return false;
        }
        }
        return true;
    }

    private boolean liveness() {
        ArrayList<Integer> transitions = new ArrayList();

        for (int i = 0; i < rp.size(); i++) {
            if (this.rp.get(i).getType() == 't') {
                return false;
            }

            if (!transitions.contains(this.rp.get(i).getTransition())) {
                transitions.add(this.rp.get(i).getTransition());
            }
        }

        if (transitions.size() != t) {
            return false;
        }

        return true;
    }

    public void printGraph() {
        for (int i = 0; i < rp.size(); i++) {
            System.out.println(rp.get(i).getMarker() + " " + rp.get(i).getType() + "T" + rp.get(i).getTransition());
        }
        
        System.out.println("Liveness: "+liveness());
        System.out.println("Boundedness: "+boundedness());
        System.out.println("Reversibility: "+reversibility());
        
    }

    
    
    
    public static void main(String[] ar) {
        PetriNetworks pn = new PetriNetworks();
        pn.reachabilityGraph();
        pn.printGraph();

         GraphViz gv = new GraphViz();
         gv.addln(gv.start_graph());
         gv.addln("node [shape=box];\n"
         + " node [fillcolor=\"white\"];\n"
         + " node [color=\"black\"];\n"
         + " edge [color=\"black\"];");

         String arbol=pn.g.toString();
         String array []= arbol.split("\n");
         
         for(int i=0;i<array.length;i++){
             String nodos[]=array[i].split(" ");
             
             for(int j=1;j<nodos.length;j++){
                gv.addln("\""+pn.g.buscaNodo(Integer.parseInt(nodos[0])).getMarker()+"\" ->\""+
                        pn.g.buscaNodo(Integer.parseInt(nodos[j])).getMarker()+"\"[label=\""+
                        pn.g.buscaNodo(Integer.parseInt(nodos[j])).getTransition()+"\"];");
             }
         }
           
         gv.addln(gv.end_graph());

        String type = "png";
         File out = new File("out." + type);    // Windows
         gv.writeGraphToFile(gv.getGraph(gv.getDotSource(), type), out);
       
    }
}
