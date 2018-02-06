import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;
import java.util.LinkedList;


/**
 * Created by HuaZ on 2/5/2018.

 ASSESSMENT SUMMARY
 Compilation: PASSED
 API: PASSED
 Findbugs: FAILED (2 warnings)
 PMD: PASSED
 Checkstyle: FAILED (0 errors, 78 warnings)
 Correctness: 64/64 tests passed
 Memory: 10/10 tests passed
 Timing: 159/159 tests passed
 Aggregate score: 100.00%
 [Compilation: 5%, API: 5%, Findbugs: 0%, PMD: 0%, Checkstyle: 0%, Correctness: 60%, Memory: 10%, Timing: 20%]

 */
public class MoveToFront {
    private static void initList(LinkedList<Integer> list){
        for(int i=0; i<256; i++) list.add(i);
    }

    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode(){
        String s = BinaryStdIn.readString();
        //String s = "ABRACADABRA!";
        LinkedList<Integer> list = new LinkedList<Integer>();
        initList(list);
        char[] c = s.toCharArray();
        for(int i=0; i<c.length; i++){
            int index = list.indexOf((int) c[i]);
            //System.out.print(Integer.toHexString(index) + " ");
            BinaryStdOut.write((char)index, 8);
            list.addFirst(list.remove(index));
        }
        BinaryStdOut.close();
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode(){
        String s = BinaryStdIn.readString();
        LinkedList<Integer> list = new LinkedList<Integer>();
        initList(list);
        char[] c = s.toCharArray();
        for(int i=0; i<c.length; i++){
            int e = list.remove((int)c[i]);
            list.addFirst(e);
            BinaryStdOut.write((char) e, 8);
        }
        BinaryStdOut.close();
    }

    // if args[0] is '-', apply move-to-front encoding
    // if args[0] is '+', apply move-to-front decoding
    public static void main(String[] args){
        //encode();
        if      (args[0].equals("-")) encode();
        else if (args[0].equals("+")) decode();
    }
}
