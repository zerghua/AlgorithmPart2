import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Stopwatch;

/**
 * Created by Hua on 3/6/2017.

 Outcast detection.
 Given a list of wordnet nouns A1, A2, ..., An, which noun is the least related to the others? To identify an outcast,
 compute the sum of the distances between each noun and every other one:

 di   =   dist(Ai, A1)   +   dist(Ai, A2)   +   ...   +   dist(Ai, An)

 and return a noun At for which dt is maximum.

 Implement an immutable data type Outcast with the following API:

 Assume that argument to outcast() contains only valid wordnet nouns (and that it contains at least two such nouns).


 Optional Optimizations
 1. The bottleneck operation is re-initializing arrays of length V to perform the BFS computations.
 This must be done once for the first BFS computation, but if you keep track of which array entries change,
 you can reuse the same array from computation to computation (re-initializing only those entries that changed in the
 previous computation). This leads to a dramatic savings when only a small number of entries change (and this is the
 typical case for the wordnet digraph). Note that if you have any other loops that iterates through all of the vertices,
 then you must eliminate those to achieve a sublinear running time. (An alternative is to replace the arrays with symbol
 tables, where, in constant time, the constructor initializes the value associated with every key to be null.)

 2. If you run the two breadth-first searches from v and w in lockstep (alternating back and forth between exploring
 vertices in each of the two searches), then you can terminate the BFS from v (or w) as soon as the distance exceeds the
 length of the best ancestral path found so far.

 3. Implement a software cache of recently computed length() and ancestor() queries.



 */
public class Outcast {
    private WordNet wordnet;

    // constructor takes a WordNet object
    public Outcast(WordNet wordnet){
        this.wordnet = wordnet;
    }

    // given an array of WordNet nouns, return an outcast
    public String outcast(String[] nouns){
        int maxDistance = 0;
        String candidate = null;
        String[] words = nouns.clone();
        for(String w: words){
            int dist = 0;
            for(String v: words){
                dist += wordnet.distance(w,v);
            }
            if(dist > maxDistance){
                maxDistance = dist;
                candidate = w;
            }
        }
        return candidate;
    }

    // see test client below, expect
    // wordnet\outcast5.txt: table
    // wordnet\outcast8.txt: bed
    // wordnet\outcast11.txt: potato

    // before optimization 1.8s
    public static void main(String[] args){
        Stopwatch time = new Stopwatch();
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
        System.out.println(String.format("%-25s= ","Time") + time.elapsedTime());
    }
}
