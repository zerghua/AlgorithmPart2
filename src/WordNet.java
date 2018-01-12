import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.Stopwatch;
import java.util.HashMap;
import java.util.HashSet;
import java.util.ArrayList;


/**
 * Created by Hua on 3/6/2017.
 * updated by Hua on 1/11/2018
*/

public class WordNet {
    private HashMap<String, HashSet<Integer>> map;
    private ArrayList<String> data;
    private SAP sap;
    private HashMap<Node, String> cacheAncestor;
    private HashMap<Node, Integer> cacheDistance;


    private class Node {
        String nounA, nounB;

        Node(String nounA, String nounB) {
            this.nounA = nounA;
            this.nounB = nounB;
        }

        @Override
        public int hashCode() {
            return this.nounA.hashCode() + this.nounB.hashCode();  //overflow?
        }

        @Override
        public boolean equals(Object o) {
            if (o == null) return false;
            if (!(o instanceof Node)) return false;

            Node other = (Node) o;
            if ((nounA.equals(other.nounA) && nounB.equals(other.nounB))
                    || (nounA.equals(other.nounB) && nounB.equals(other.nounA))
                    ) {
                return true;
            }
            return false;
        }
    }

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null) throw new java.lang.IllegalArgumentException();
        data = new ArrayList<>();
        int vertex = processSynsets(synsets);
        Digraph graph = processHypernyms(hypernyms, vertex);
        sap = new SAP(graph);
        cacheAncestor = new HashMap<>();
        cacheDistance = new HashMap<>();
    }


    // filename of synsets
    private int processSynsets(String filename) {
        int numOfVertex = 0;
        In in = new In(filename);
        map = new HashMap<>();
        while (!in.isEmpty()) {
            String[] line = in.readLine().trim().split(",");
            int id = Integer.parseInt(line[0].trim());
            String synset = line[1].trim();
            String[] words = synset.split("\\s+");  // split by spaces, also works for multiple spaces
            data.add(id, synset);
            for (String w : words) {
                if (!map.containsKey(w)) map.put(w, new HashSet<Integer>());
                map.get(w).add(id);
            }
            numOfVertex++;
        }
        return numOfVertex;
    }

    // filename of hypernyms
    private Digraph processHypernyms(String filename, int numOfVertex) {
        In in = new In(filename);
        Digraph graph = new Digraph(numOfVertex);

        while (!in.isEmpty()) {
            String[] line = in.readLine().trim().split(",");
            int v = Integer.parseInt(line[0]);
            for (int i = 1; i < line.length; i++) {
                graph.addEdge(v, Integer.parseInt(line[i]));
            }
        }

        // throw IllegalArgumentException when detects a cycle and over one root
        if(!isRootedDag(graph)) throw new IllegalArgumentException("");

        return graph;
    }

    private boolean isRootedDag(Digraph graph){
        /*
        int[] isMarked = new int[graph.V()];  // 0 not visited, 1 visited, 2 currently visit
        HashSet<Integer> roots = new HashSet();
        for(int v=0; v<graph.V(); v++){
            if(isMarked[v] == 0) dfs(v, graph, isMarked, roots);
        }
        */

        if(new DirectedCycle(graph).hasCycle()) {
            System.out.println("find cycle");
            return false;
        }

        // count number of roots
        int count = 0;
        for(int v=0; v<graph.V(); v++){
            if(graph.outdegree(v) == 0) count++;
            if(count > 1) {
                System.out.println("Find more than one root");
                return false;
            }
        }
        return true;
    }

    // Topological sort to detect cycle and rule out multiple roots
    // 0 not visited, 1 visited, 2 currently visit
    private void dfs(int v, Digraph graph, int[] isMarked, HashSet<Integer> roots ){
        if(isMarked[v] == 1) return;
        if(isMarked[v] == 2) {
            System.out.println(v);
            throw new IllegalArgumentException("find cycle");
        }

        if(graph.outdegree(v) == 0) roots.add(v);
        if(roots.size() > 1) throw new IllegalArgumentException("more than one root");

        isMarked[v] = 1;
        for(int adj : graph.adj(v)){
            isMarked[adj] = 2;
            dfs(adj, graph, isMarked, roots);
            isMarked[adj] = 1;
        }
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return map.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null) throw new java.lang.IllegalArgumentException();
        return map.containsKey(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (nounA == null || nounB == null) throw new java.lang.IllegalArgumentException();

        Node node = new Node(nounA, nounB);
        if (!cacheDistance.containsKey(node)) {
            cacheDistance.put(node, sap.length(map.get(nounA), map.get(nounB)));
        }
        return cacheDistance.get(node);
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (nounA == null || nounB == null) throw new java.lang.IllegalArgumentException();

        Node node = new Node(nounA, nounB);
        if (!cacheAncestor.containsKey(node)) {
            int root_id = sap.ancestor(map.get(nounA), map.get(nounB));
            cacheAncestor.put(node, data.get(root_id));
        }
        return cacheAncestor.get(node);
    }

    // do unit testing of this class
    // Time                     = 0.994
    // Time                     = 0.903  cache SAP rather than cache graph
    // Time                     = 0.882  early return in SAP getMinCommonAncestor
    public static void main(String[] args) {
        /*
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length   = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            System.out.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
        */

        ///*
        Stopwatch time = new Stopwatch();
        WordNet wordnet = new WordNet(args[0], args[1]);

        boolean isFoundOneWordMapMultipleId = false;
        for (String s : wordnet.map.keySet()) {
            if (wordnet.map.get(s).size() > 1) isFoundOneWordMapMultipleId = true;
        }
        if (isFoundOneWordMapMultipleId) System.out.println("Found one Word maps to multiple id");

        System.out.println("map size: " + wordnet.map.size());
        System.out.println("data size = " + wordnet.data.size());


        unitTestAncestor("worm", "bird", "animal", wordnet);
        unitTestDistance("worm", "bird", 5, wordnet);
        unitTestAncestor("individual", "edible_fruit", "physical_entity", wordnet);
        unitTestDistance("individual", "edible_fruit", 7, wordnet);

        unitTestDistance("white_marlin", "mileage", 23, wordnet);
        unitTestDistance("Black_Plague", "black_marlin", 33, wordnet);
        unitTestDistance("American_water_spaniel", "histology", 27, wordnet);
        unitTestDistance("Brown_Swiss", "barrel_roll", 29, wordnet);

        unitTestDistance("worm", "worm", 0, wordnet);
        System.out.println(String.format("%-25s= ", "Time") + time.elapsedTime());

        //*/
    }

    private static void unitTestAncestor(String word1, String word2, String correctAncestor, WordNet wordnet) {
        if (wordnet.sap(word1, word2).equals(correctAncestor)) {
            System.out.println("Correct ancestor between " + word1 + " and " + word2);
        } else {
            System.out.println("Wrong!! ancestor between " + word1 + " and " + word2);
        }
    }

    private static void unitTestDistance(String word1, String word2, int correctDistance, WordNet wordnet) {
        if (wordnet.distance(word1, word2) == correctDistance) {
            System.out.println("Correct distance between " + word1 + " and " + word2);
        } else {
            System.out.println("Wrong!! distance between " + word1 + " and " + word2);
        }
    }
}
