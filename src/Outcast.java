import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

/**
 * Created by Hua on 3/6/2017.

 Outcast detection.
 Given a list of wordnet nouns A1, A2, ..., An, which noun is the least related to the others? To identify an outcast,
 compute the sum of the distances between each noun and every other one:

 di   =   dist(Ai, A1)   +   dist(Ai, A2)   +   ...   +   dist(Ai, An)

 and return a noun At for which dt is maximum.

 Implement an immutable data type Outcast with the following API:

 Assume that argument to outcast() contains only valid wordnet nouns (and that it contains at least two such nouns).

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

    // see test client below
    public static void main(String[] args){
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}
