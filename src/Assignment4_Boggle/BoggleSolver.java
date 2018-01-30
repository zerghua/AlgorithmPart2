import java.util.HashSet;
import java.util.HashMap;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;


/**
 * Created by HuaZ on 1/30/2018.


 ASSESSMENT SUMMARY

 Compilation: PASSED
 API: PASSED
 Findbugs: PASSED
 PMD: PASSED
 Checkstyle: FAILED (0 errors, 55 warnings)
 Correctness: 13/13 tests passed
 Memory: 3/3 tests passed
 Timing: 9/9 tests passed

 Aggregate score: 100.00%
 [Compilation: 5%, API: 5%, Findbugs: 0%, PMD: 0%, Checkstyle: 0%, Correctness: 60%, Memory: 10%, Timing: 20%]


 Test 2: timing getAllValidWords() for 5.0 seconds using dictionary-yawl.txt
        (must be <= 2x reference solution)

 - reference solution calls per second: 8563.51
 - student solution calls per second: 5353.80
 - reference / student ratio: 1.60

 => passed student <= 10000x reference
 => passed student <= 25x reference
 => passed student <= 10x reference
 => passed student <= 5x reference
 => passed student <= 2x reference
 Total: 9/9 tests passed!

 */

public class BoggleSolver {
    private class TrieNode{
        TrieNode[] children;
        boolean isEnd;
        public TrieNode() {
            children = new TrieNode[26];
        }

        public boolean containsKey(char c){
            return children[c - 'A'] != null;
        }

        public TrieNode getChild(char c){
            return children[c - 'A'];
        }

        public void setChild(char c, TrieNode childNode){
            children[c - 'A'] = childNode;
        }
    }

    private class Trie {
        private TrieNode root;

        public Trie() {
            root = new TrieNode();
        }

        // Inserts a word into the trie.
        public void insert(String word) {
            TrieNode node = root;
            for(int i=0;i<word.length();i++){
                char c = word.charAt(i);
                if(!node.containsKey(c)){
                    node.setChild(c, new TrieNode());
                }
                node = node.getChild(c);
            }
            node.isEnd = true;
        }

        public TrieNode getNode(String word){
            TrieNode node = root;
            for(int i=0;i<word.length();i++) {
                char c = word.charAt(i);
                if(node.containsKey(c))node = node.getChild(c);
                else return null;
            }
            return node;
        }

        // Returns if the word is in the trie.
        public boolean contains(String word) {
            TrieNode node = getNode(word);
            return node != null && node.isEnd;
        }

        // Returns if there is any word in the trie
        // that starts with the given prefix.
        public boolean startsWith(String prefix) {
            TrieNode node = getNode(prefix);
            return node != null;
        }
    }

    private Trie tr;
    private HashMap<Integer, Integer> scoreMap;

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary){
        scoreMap = new HashMap<Integer, Integer>();
        scoreMap.put(0, 0);
        scoreMap.put(1, 0);
        scoreMap.put(2, 0);
        scoreMap.put(3, 1);
        scoreMap.put(4, 1);
        scoreMap.put(5, 2);
        scoreMap.put(6, 3);
        scoreMap.put(7, 5);
        scoreMap.put(8, 11); // length larger than 8 get the same score as 8

        tr = new Trie();
        for(String s : dictionary) tr.insert(s);
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board){
        HashSet<String> set = new HashSet<String>();
        int row = board.rows(), col = board.cols();
        for(int i=0; i<row; i++){
            for(int j=0; j<col; j++){
                dfs(new boolean[row][col], board, i, j, "", set);
            }
        }
        return set;
    }

    private void dfs(boolean[][] isUsed, BoggleBoard board, int x, int y, String word, HashSet<String> set){
        isUsed[x][y] = true;
        char c = board.getLetter(x,y);
        if(c == 'Q') word += "QU";
        else word += c;

        TrieNode node = tr.getNode(word);                         // performance optimization
        if(node != null) {   // == tr.startsWith(word);           // performance optimization
            //if(word.length() >2 && tr.contains(word)) set.add(word);
            if(word.length() >2 && node.isEnd) set.add(word);     // performance optimization

            // check 8 neighbours
            for (int i = x - 1; i <= x + 1; i++) {
                for (int j = y - 1; j <= y + 1; j++) {
                    if (i < 0 || i >= board.rows() || j < 0 || j >= board.cols() || isUsed[i][j]) continue;
                    dfs(isUsed, board, i, j, word, set);
                }
            }
        }

        isUsed[x][y] = false;  // backtrack, important
    }



    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word){
        if(word == null || !tr.contains(word)) return 0;
        if(word.length() >=8) return scoreMap.get(8);
        return scoreMap.get(word.length());
    }

    public static void main(String[] args) {
        //String dicFile = "boggle/dictionary-algs4.txt";
        //String boardFile = "boggle/board4x4.txt";

        String dicFile = "boggle/dictionary-algs4.txt";
        String boardFile = "boggle/board-q.txt";

        //String dicFile = "boggle/dictionary-common.txt";
        //String boardFile = "boggle/3x3.txt";

        In in = new In(dicFile);
        BoggleBoard board = new BoggleBoard(boardFile);

        //In in = new In(args[0]);
        //BoggleBoard board = new BoggleBoard(args[1]);

        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word + "  " + solver.scoreOf(word));
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }

}
