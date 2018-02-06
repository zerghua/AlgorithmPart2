import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;


/**
 * Created by HuaZ on 2/5/2018.
 */
public class BurrowsWheeler {
    // apply Burrows-Wheeler transform, reading from standard input and writing to standard output
    public static void transform(){
        //String s = "ABRACADABRA!";
        //System.out.println("input string=" + s);

        String s = BinaryStdIn.readString();
        CircularSuffixArray c = new CircularSuffixArray(s);

        for(int i=0; i<c.length(); i++) {
            if(c.index(i) == 0){
                BinaryStdOut.write(i);
                break;
            }
        }

        for(int i=0; i<c.length(); i++) {
            BinaryStdOut.write(s.charAt((c.index(i) + c.length() - 1) % c.length()));
        }
        BinaryStdOut.close();
    }

    // apply Burrows-Wheeler inverse transform, reading from standard input and writing to standard output
    // core code here.
    public static void inverseTransform(){
        int R = 256;

        int first = BinaryStdIn.readInt();
        String s = BinaryStdIn.readString();

        int n = s.length();
        int[] count = new int[R + 1], next = new int[n];

        // count frequency
        for (int i = 0; i < n; i++) count[s.charAt(i) + 1]++;

        // accumulate frequency
        for (int i = 0; i < R ; i++) count[i+1] += count[i];

        for (int i = 0; i < n; i++) next[count[s.charAt(i)]++] = i;

        for (int i = next[first], c = 0; c < n; i = next[i], c++) BinaryStdOut.write(s.charAt(i));

        BinaryStdOut.close();

    }

    // if args[0] is '-', apply Burrows-Wheeler transform
    // if args[0] is '+', apply Burrows-Wheeler inverse transform
    public static void main(String[] args) {
        if(args[0] == "-") transform();
        else if(args[0] == "+") inverseTransform();
    }
}
