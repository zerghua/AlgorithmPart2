import edu.princeton.cs.algs4.In;

/**
 * Created by Hua on 3/6/2017.

 WordNet is a semantic lexicon for the English language that is used extensively by computational linguists and
 cognitive scientists; for example, it was a key component in IBM's Watson. WordNet groups words into sets of synonyms
 called synsets and describes semantic relationships between them. One such relationship is the is-a relationship, which
 connects a hyponym (more specific synset) to a hypernym (more general synset). For example, animal is a hypernym of
 both bird and fish; bird is a hypernym of eagle, pigeon, and seagull.


 The WordNet input file formats.
 We now describe the two data files that you will use to create the wordnet digraph.
 The files are in CSV format: each line contains a sequence of fields, separated by commas.


 List of noun synsets.
 The file synsets.txt lists all the (noun) synsets in WordNet. The first field is the synset id (an integer),
 the second field is the synonym set (or synset), and the third field is its dictionary definition (or gloss).
 For example, the line

 36,AND_circuit AND_gate,a circuit in a computer that fires only when all of its inputs fire

 means that the synset { AND_circuit, AND_gate } has an id number of 36 and it's gloss is a circuit in a computer
 that fires only when all of its inputs fire. The individual nouns that comprise a synset are separated by spaces (and
 a synset element is not permitted to contain a space). The S synset ids are numbered 0 through S − 1; the id numbers
 will appear consecutively in the synset file.



 List of hypernyms.
 The file hypernyms.txt contains the hypernym relationships: The first field is a synset id; subsequent fields are the
 id numbers of the synset's hypernyms. For example, the following line

 164,21012,56099

 means that the the synset 164 ("Actifed") has two hypernyms: 21012 ("antihistamine") and 56099 ("nasal_decongestant"),
 representing that Actifed is both an antihistamine and a nasal decongestant. The synsets are obtained from the
 corresponding lines in the file synsets.txt.

 164,Actifed,trade name for a drug containing an antihistamine and a decongestant...
 21012,antihistamine,a medicine used to treat allergies...
 56099,nasal_decongestant,a decongestant that provides temporary relief of nasal...



 Corner cases.
 All methods and the constructor should throw a java.lang.NullPointerException if any argument is null.
 The constructor should throw a java.lang.IllegalArgumentException if the input does not correspond to a rooted DAG.
 The distance() and sap() methods should throw a java.lang.IllegalArgumentException unless both of the noun arguments
 are WordNet nouns.

 Performance requirements.
 Your data type should use space linear in the input size (size of synsets and hypernyms files).
 The constructor should take time linearithmic (or better) in the input size.
 The method isNoun() should run in time logarithmic (or better) in the number of nouns.
 The methods distance() and sap() should run in time linear in the size of the WordNet digraph.
 For the analysis, assume that the number of nouns per synset is bounded by a constant.


 Shortest ancestral path.
 An ancestral path between two vertices v and w in a digraph is a directed path from v to a common ancestor x, together
 with a directed path from w to the same ancestor x. A shortest ancestral path is an ancestral path of minimum total
 length. For example, in the digraph at left (digraph1.txt), the shortest ancestral path between 3 and 11 has length 4
 (with common ancestor 1). In the digraph at right (digraph2.txt), one ancestral path between 1 and 5 has length 4 (with
 common ancestor 5), but the shortest ancestral path has length 2 (with common ancestor 0).


 Measuring the semantic relatedness of two nouns.
 Semantic relatedness refers to the degree to which two concepts are related. Measuring semantic relatedness is a
 challenging problem. For example, most of us agree that George Bush and John Kennedy (two U.S. presidents) are more
 related than are George Bush and chimpanzee (two primates). However, not most of us agree that George Bush and
 Eric Arthur Blair are related concepts. But if one is aware that George Bush and Eric Arthur Blair (aka George Orwell)
 are both communicators, then it becomes clear that the two concepts might be related.


 We define the semantic relatedness of two wordnet nouns A and B as follows:

 distance(A, B) = distance is the minimum length of any ancestral path between any synset v of A and any synset w of B.

 This is the notion of distance that you will use to implement the distance() and sap() methods in the WordNet data type.


 */
public class WordNet {
    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms){
        In in = new In(synsets);
    }

    // returns all WordNet nouns
    public Iterable<String> nouns(){
        return null;
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word){
        return false;
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB){
        return 1;

    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB){
        return null;
    }

    // do unit testing of this class
    public static void main(String[] args){

    }
}