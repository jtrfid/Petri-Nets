package petrinets;

import java.io.File;
import java.util.ArrayList;
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
    int post[][]; //matriz post
    int pre[][]; //matriz pre
    Node n0; //nodo inicial puede no ser necesario aki
    int p, t; //tamaños de los lugares y las transiciones 
    ArrayList<Integer> m0; //marcdo inicial
    ArrayList<Node> rp; //arreglo con los nodos 
    DepthFirstSearchLinkList g = new DepthFirstSearchLinkList(6);
    int size = 0; //Numero de nodos creados que ademas almacenara el Id del nodo

    public void PetriNetworks(Node root) {
        n0 = root;
        size++;
        m0 = new ArrayList<>();
        rp = new ArrayList<>();
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
            System.out.println("Introduce the initial marking");
            String cadena = s.nextLine();
            String[] chars = cadena.split("\\s+");
            for (String char1 : chars) {
                m0.add(Integer.parseInt(char1));
            }
            pre = new int[p][t];
            post = new int[p][t];
            c = new int[p][t];
            fillpre(s);
            fillpost(s);
        }
        fillc();

    }

    private void fillpre(Scanner s) {
        System.out.println("Introduce pre for each place");
        for (int i = 0; i < p; i++) {
            for (int j = 0; j < t; j++) {
                System.out.print("p" + (i + 1) + "[" + (j + 1) + "]: ");
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
        for (int i = 0; i < p; i++) {
            for (int j = 0; j < t; j++) {
                System.out.print("p" + (i + 1) + "[" + (j + 1) + "]: ");
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
        /*Pedro cada vez que se genera una transicion, creas un nodo y le pones de id el size y  aumentas el size
         * y lo agregues al grafo g como una arista
         */

        Node nuevo = new Node(size);
        size++;

        //Falta agregarlo como arista 
    }

    private ArrayList<Integer> enableTransition(ArrayList<Integer> m) {
        ArrayList<Integer> vk = new ArrayList(); //Firing vector
        boolean bandera = false;
        for (int i = 0; i < m.size(); i++) {
            for (int j = 0; j < t; j++) {
                if (m.get(i) < pre[i][j]) {
                    bandera = true;
                }
            }
            if (!bandera) {
                vk.add(1);
            } else {
                vk.add(0);
            }
        }
        return vk;
    }

    private ArrayList<Integer> firingCondition(ArrayList<Integer> m, ArrayList<Integer> vk) {
        ArrayList<Integer> result = new ArrayList<>();
        ArrayList<Integer> multiplicacion = multiMatrix(c, vk);
        for (int i = 0; i < m.size(); i++) {
            result.add(m.get(i) + multiplicacion.get(i));
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

    private boolean reversibility(ArrayList rp) {
        String path;

        for (int i = 1; i < rp.size(); i++) {
            path = g.totalPathFrom((Node) rp.get(i));
            if (!path.contains("1")) { //If return to the initial state
                return false;
            }
            g.cleanVisited(); //Clean de path 
        }

        return true;
    }

    private boolean boundedness() {

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

    public static void main(String[] ar) {
        GraphViz gv = new GraphViz();
        gv.addln(gv.start_graph());
        gv.addln("node [shape=plain];\n"
                + " node [fillcolor=\"#EEEEEE\"];\n"
                + " node [color=\"#EEEEEE\"];\n"
                + " edge [color=\"#31CEF0\"];");

        gv.addln("\"[09876]\" -> B [label=\"asddsfds\"];");
        gv.addln(" t1 -> C;");
        gv.addln(gv.end_graph());

        String type = "png";
        File out = new File("out." + type);    // Windows
        gv.writeGraphToFile(gv.getGraph(gv.getDotSource(), type), out);

    }

}
