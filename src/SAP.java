/**
 * Created by Hua on 3/6/2017.
 * <p/>
 * Corner cases.
 * All methods should throw a java.lang.NullPointerException if any argument is null.
 * All methods should throw a java.lang.IndexOutOfBoundsException if any argument vertex is
 * invalid—not between 0 and G.V() - 1.
 * <p/>
 * <p/>
 * Performance requirements.
 * All methods (and the constructor) should take time at most proportional to E + V in the worst case, where E and V are
 * the number of edges and vertices in the digraph, respectively. Your data type should use space proportional to E + V.
 * <p/>
 * <p/>
 * Length = 4 ancestor = 1
 * Length = 3 ancestor = 5
 * Length = 4 ancestor = 0
 * Length = -1 ancestor = -1
 * Time                     = 0.023
 */

import edu.princeton.cs.algs4.*;

import java.util.Arrays;
import java.util.LinkedList;

public class SAP {
    private Digraph G;

    private class Node {
        Iterable<Integer> v, w;

    }

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        this.G = new Digraph(G);
    }

    private Iterable<Integer> toIterable(int v) {
        return new LinkedList<>(Arrays.asList(v));
    }


    // return first is vertex, second is minLength
    private int[] getMinCommonAncestor(Iterable<Integer> v, Iterable<Integer> w) {
        checkBounds(v);
        checkBounds(w);
        BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(G, w);
        boolean[] isChecked = new boolean[G.V()]; //optimization, also important, or will be infinite loop.
        Queue<Integer> q = new Queue<>();
        for (int vertex : v) q.enqueue(vertex);
        for (int vertex : w) q.enqueue(vertex);

        int minDistance = Integer.MAX_VALUE;
        int vertex = -1;
        while (!q.isEmpty()) {
            int cur = q.dequeue();
            isChecked[cur] = true;
            if (bfsV.hasPathTo(cur) && bfsW.hasPathTo(cur) && bfsV.distTo(cur) + bfsW.distTo(cur) < minDistance) {
                minDistance = bfsV.distTo(cur) + bfsW.distTo(cur);
                vertex = cur;
                return new int[]{vertex, minDistance};  //maybe buggy.
            }

            for (int adj : G.adj(cur)) {
                if (!isChecked[adj]) q.enqueue(adj);
            }
        }
        return new int[]{vertex, minDistance};
    }


    /*
    // alternating queue, not significant runtime improvement
    private int[] getMinCommonAncestor(Iterable<Integer> v, Iterable<Integer> w) {
        checkBounds(v);
        checkBounds(w);
        BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(G, w);
        boolean[] isChecked = new boolean[G.V()]; //optimization, also important, or will be infinite loop.
        Queue<Integer> qv = new Queue<>();
        Queue<Integer> qw = new Queue<>();
        for (int vertex : v) qv.enqueue(vertex);
        for (int vertex : w) qw.enqueue(vertex);

        int minDistance = Integer.MAX_VALUE;
        int vertex = -1;
        boolean isFirst = false;
        while(!qv.isEmpty() && !qw.isEmpty()){
            if(isFirst){
                isFirst = false;
                int cur = qv.dequeue();
                isChecked[cur] = true;
                if (bfsV.hasPathTo(cur) && bfsW.hasPathTo(cur) && bfsV.distTo(cur) + bfsW.distTo(cur) < minDistance){
                    minDistance = bfsV.distTo(cur) + bfsW.distTo(cur);
                    vertex = cur;
                    return new int[]{vertex, minDistance};
                }
                for (int adj : G.adj(cur)) {
                    if (!isChecked[adj]) qv.enqueue(adj);
                }

            }else{
                isFirst = true;
                int cur = qw.dequeue();
                isChecked[cur] = true;
                if (bfsV.hasPathTo(cur) && bfsW.hasPathTo(cur) && bfsV.distTo(cur) + bfsW.distTo(cur) < minDistance){
                    minDistance = bfsV.distTo(cur) + bfsW.distTo(cur);
                    vertex = cur;
                    return new int[]{vertex, minDistance};
                }
                for (int adj : G.adj(cur)) {
                    if (!isChecked[adj]) qw.enqueue(adj);
                }
            }
        }
        return new int[]{vertex, minDistance};
    }
    */

    private void checkBounds(Iterable<Integer> w) {
        if (w == null) throw new java.lang.NullPointerException();
        for (int v : w) {
            if (v < 0 || v >= G.V()) throw new java.lang.IndexOutOfBoundsException();
        }
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        int[] ret = getMinCommonAncestor(toIterable(v), toIterable(w));
        if (ret[0] == -1) return -1;
        return ret[1];
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        int[] ret = getMinCommonAncestor(toIterable(v), toIterable(w));
        if (ret[0] == -1) return -1;
        return ret[0];
    }


    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        int[] ret = getMinCommonAncestor(v, w);
        if (ret[0] == -1) return -1;
        return ret[1];
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        int[] ret = getMinCommonAncestor(v, w);
        if (ret[0] == -1) return -1;
        return ret[0];
    }

    // do unit testing of this class
    // before optimization time: 0.022
    public static void main(String[] args) {
        Stopwatch time = new Stopwatch();
        In in = new In(args[0]);
        Digraph graph = new Digraph(in);
        SAP sap = new SAP(graph);
        System.out.println("Length = " + sap.length(3, 11) + " ancestor = " + sap.ancestor(3, 11));
        System.out.println("Length = " + sap.length(9, 12) + " ancestor = " + sap.ancestor(9, 12));
        System.out.println("Length = " + sap.length(7, 2) + " ancestor = " + sap.ancestor(7, 2));
        System.out.println("Length = " + sap.length(1, 6) + " ancestor = " + sap.ancestor(1, 6));
        System.out.println(String.format("%-25s= ", "Time") + time.elapsedTime());
    }
}
