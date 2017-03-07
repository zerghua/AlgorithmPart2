/**
 * Created by Hua on 3/6/2017.

 Corner cases.
 All methods should throw a java.lang.NullPointerException if any argument is null.
 All methods should throw a java.lang.IndexOutOfBoundsException if any argument vertex is
 invalid—not between 0 and G.V() - 1.


 Performance requirements.
 All methods (and the constructor) should take time at most proportional to E + V in the worst case, where E and V are
 the number of edges and vertices in the digraph, respectively. Your data type should use space proportional to E + V.

 */

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;

public class SAP {
    Digraph G ;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G){
        this.G = new Digraph(G);
    }

    // return first is vertex, second is minLength
    private int[] getMinCommonAncestor(int v, int w){
        if(v < 0 || v >= G.V() || w < 0 || w >= G.V()) throw new java.lang.IndexOutOfBoundsException("");
        BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(G, w);
        boolean[] isChecked = new boolean[G.V()]; //optimization, also important, or will be infinite loop.
        Queue<Integer> q = new Queue<Integer>();
        q.enqueue(v);
        q.enqueue(w);
        int minDistance = Integer.MAX_VALUE;
        int vertex=-1;
        while(!q.isEmpty()){
            int cur = q.dequeue();
            if(bfsV.hasPathTo(cur) && bfsW.hasPathTo(cur)){
                if( bfsV.distTo(cur) + bfsW.distTo(cur) < minDistance){
                    minDistance = bfsV.distTo(cur) + bfsW.distTo(cur);
                    vertex = cur;
                }
            }
            isChecked[cur] = true;

            for(int adj : G.adj(cur)){
                if(!isChecked[adj]) q.enqueue(adj);
            }
        }
        return new int[]{vertex, minDistance};
    }


    // return first is vertex, second is minLength
    private int[] getMinCommonAncestor(Iterable<Integer> v, Iterable<Integer> w){
        if(v == null || w == null) throw new java.lang.NullPointerException();

        BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(G, w);
        boolean[] isChecked = new boolean[G.V()]; //optimization, also important, or will be infinite loop.
        Queue<Integer> q = new Queue<Integer>();
        for(int vertex: v){
            if(vertex < 0 || vertex >= G.V()) throw new java.lang.IndexOutOfBoundsException("");
            q.enqueue(vertex);
        }

        for(int vertex: w){
            if(vertex < 0 || vertex >= G.V()) throw new java.lang.IndexOutOfBoundsException("");
            q.enqueue(vertex);
        }

        int minDistance = Integer.MAX_VALUE;
        int vertex=-1;
        while(!q.isEmpty()){
            int cur = q.dequeue();
            if(bfsV.hasPathTo(cur) && bfsW.hasPathTo(cur)){
                if( bfsV.distTo(cur) + bfsW.distTo(cur) < minDistance){
                    minDistance = bfsV.distTo(cur) + bfsW.distTo(cur);
                    vertex = cur;
                }
            }
            isChecked[cur] = true;

            for(int adj : G.adj(cur)){
                if(!isChecked[adj]) q.enqueue(adj);
            }
        }
        return new int[]{vertex, minDistance};
    }


    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w){
        int[] ret = getMinCommonAncestor(v, w);
        if(ret[0] == -1) return -1;
        return ret[1];
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w){
        int[] ret = getMinCommonAncestor(v, w);
        if(ret[0] == -1) return -1;
        return ret[0];
    }


    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w){
        int[] ret = getMinCommonAncestor(v, w);
        if(ret[0] == -1) return -1;
        return ret[1];
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w){
        int[] ret = getMinCommonAncestor(v, w);
        if(ret[0] == -1) return -1;
        return ret[0];
    }

    // do unit testing of this class
    public static void main(String[] args){
        In in = new In(args[0]);
        Digraph graph = new Digraph(in);
        SAP sap = new SAP(graph);
        System.out.println("Length = " + sap.length(3,11)  + " ancestor = "+ sap.ancestor(3,11));
        System.out.println("Length = " + sap.length(9,12)  + " ancestor = "+ sap.ancestor(9,12));
        System.out.println("Length = " + sap.length(7,2)  + " ancestor = "+ sap.ancestor(7,2));
        System.out.println("Length = " + sap.length(1,6)  + " ancestor = "+ sap.ancestor(1,6));
    }
}
