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
    int post[][];
    int pre[][]; 
    Node n0; 
    int p, t; //numbers of places and transitions
    ArrayList<Integer> m0; //initial marker
    ArrayList<Node> rp; 
    ArrayList<Node> procesados;
    DepthFirstSearchLinkList g;
    int size = 0; 

    public PetriNetworks(int p, int t, int pre[][], int pos[][], ArrayList<Integer> m0) {
        this.p = p;
        this.t = t;
        this.pre = pre;
        this.post = pos;
        this.m0 = m0;
        this.c = new int[this.p][this.t];
        
        g = new DepthFirstSearchLinkList(10);
        rp = new ArrayList<>();
        procesados = new ArrayList<>();

        fillc();
    }

    public PetriNetworks() {
        m0 = new ArrayList<>();
        rp = new ArrayList<>();
        procesados = new ArrayList<>();
        g = new DepthFirstSearchLinkList(10);

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
           

        }
        fillc();

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

        ArrayList<Node> y = new ArrayList();
        y.add(new Node(-1));
        n0.setParent(y);

        rp.add(n0);
        procesados.add(n0);

        while (!procesados.isEmpty()) {
            Node nk = procesados.remove(0);

            if (nk.getType() == 'f') {
                //Buscar duplicado en rp
                for (int i = 0; i < rp.size(); i++) {
                    if (rp.get(i).getType() != 'f' && nk.getMarker().equals(rp.get(i).getMarker())) {
                        nk.setType('d');
                        changeType(nk, 'd');
                        g.add(nk.getParent().get(0), rp.get(i));
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

                                nz.setMarker(buscaMayor(nk, nz));
                                nz.setTransition(j);
                                nz.setType('f');

                                nk.getChildren().add(nz);
                                g.add(nk, nz);

                                rp.add(nz);
                                procesados.add(nz);
                            }
                        }
                        nk.setType('e');
                        changeType(nk, 'e');
                        addChildren(nk, nk.getChildren());

                    } else {
                        nk.setType('t');
                        changeType(nk, 't');
                    }
                }
            }

        }
    }

    private void addChildren(Node nk, ArrayList<Node> children) {
        for (int i = 0; i < rp.size(); i++) {
            if (rp.get(i).getId() == nk.getId()) {
                rp.get(i).setChildren(children);
            }
        }

    }

    private void changeType(Node nk, char t) {
        for (int i = 0; i < rp.size(); i++) {
            if (rp.get(i).getId() == nk.getId()) {
                rp.get(i).setType(t);
            }
        }
    }

    private ArrayList<Integer> buscaMayor(Node nk, Node nz) {
        ArrayList<Integer> marker = nz.getMarker();

        boolean mayornz = true;

                
        for (int i = 0; i < nk.getMarker().size(); i++) {
            if (nz.getMarker().get(i) < nk.getMarker().get(i)) {
                mayornz = false;
                break;
            }
        }

        if (mayornz) {
            for (int a = 0; a < nk.getMarker().size(); a++) {
                if (nz.getMarker().get(a) > nk.getMarker().get(a)) {
                    marker.set(a, Integer.MAX_VALUE);
                }
            }
            return marker;
        }

        if (nk.getParent() != null) {
            for (int i = 0; i < nk.getParent().size(); i++) {
                return buscaMayor(nk.getParent().get(i), nz);
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

   public boolean reversibility() {
        String path;

        for (int i = 1; i < rp.size(); i++) {
            if (rp.get(i).getType() != 'd') {
                path = g.totalPathFrom((Node) rp.get(i));
                if (!path.contains("0")) { //If return to the initial state
                    return false;
                }
                g.cleanVisited(); //Clean de path 
            }
        }

        return true;
    }

    public boolean boundedness() {
        for (int i = 0; i < rp.size(); i++) {
            if (rp.get(i).getMarker().contains(Integer.MAX_VALUE)) {
                return false;
            }
        }
        return true;
    }

    public boolean liveness() {
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

    public String imprimirMarcado(ArrayList<Integer> marker) {
        String marker2 = "";

        for (int i = 0; i < marker.size(); i++) {
            if (marker.get(i) == Integer.MAX_VALUE) {
                marker2 = marker2 + "w ";
            } else {
                marker2 = marker2 + marker.get(i) + " ";
            }
        }
        return marker2;
    }

    public void printGraph() {
        for (int i = 0; i < rp.size(); i++) {
            System.out.println(rp.get(i).getMarker() + " " + rp.get(i).getType() + "t" + rp.get(i).getTransition());
        }

        System.out.println("Liveness: " + liveness());
        System.out.println("Boundedness: " + boundedness());
        System.out.println("Reversibility: " + reversibility());

        GraphViz gv = new GraphViz();
        gv.addln(gv.start_graph());
        gv.addln("node [shape=box];\n"
                + " node [fillcolor=\"white\"];\n"
                + " node [color=\"black\"];\n"
                + " edge [color=\"black\"];");

        for (int i = 0; i < rp.size(); i++) {
            for (int j = 0; j < rp.get(i).getChildren().size(); j++) {
                gv.addln("\"" + imprimirMarcado(rp.get(i).getMarker()) + "\" ->" + "\"" + imprimirMarcado(rp.get(i).getChildren().get(j).getMarker())
                        + "\" [label=\"t" + (rp.get(i).getChildren().get(j).getTransition() + 1) + "\"];");
            }
        }

        gv.addln(gv.end_graph());
        
        String type = "svg";
        File out = new File("out." + type);    // Windows
        gv.writeGraphToFile(gv.getGraph(gv.getDotSource(), type), out);
    }

    
}
