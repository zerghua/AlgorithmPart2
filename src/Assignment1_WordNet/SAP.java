/**
 * Created by Hua on 3/6/2017.
 * updated by Hua on 1/11/2018
 */
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.Stopwatch;
import java.util.Arrays;
import java.util.LinkedList;

public class SAP {
    private Digraph G;
    private DeluxeBFS bfsV, bfsW;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        this.G = new Digraph(G);
        bfsV = new DeluxeBFS(G);
        bfsW = new DeluxeBFS(G);
    }

    private Iterable<Integer> toIterable(int v) {
        return new LinkedList<>(Arrays.asList(v));
    }


    /*
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
            }

            for (int adj : G.adj(cur)) {
                if (!isChecked[adj]) q.enqueue(adj);
            }
        }
        return new int[]{vertex, minDistance};
    }
    */

    ///*
    private int[] getMinCommonAncestor(Iterable<Integer> v, Iterable<Integer> w) {
        checkBounds(v);
        checkBounds(w);
        bfsV.runBFS(G, v);
        bfsW.runBFS(G, w);
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
            }

            for (int adj : G.adj(cur)) {
                if (!isChecked[adj]) q.enqueue(adj);
            }
        }
        return new int[]{vertex, minDistance};
    }
    //*/

    private void checkBounds(Iterable<Integer> w) {
        if (w == null) throw new java.lang.IllegalArgumentException();
        for (int v : w) {
            if (v < 0 || v >= G.V()) throw new java.lang.IllegalArgumentException();
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
