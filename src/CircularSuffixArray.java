import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by HuaZ on 2/5/2018.
 */
public class CircularSuffixArray {
    private String str;     // TODO improve space from 2N to N, with modular ops.
    private int n;
    private ArrayList<Integer> list;

    // circular suffix array of s
    public CircularSuffixArray(String s){
        if(s == null) throw new IllegalArgumentException();

        str = s + s;
        n = s.length();
        list = new ArrayList<Integer>();
        for(int i=0; i< n; i++) list.add(i);

        // core code here. reference sort circular suffix array.
        Collections.sort(list, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                for(int i=0; i<n; i++){
                    if(str.charAt(o1+i) < str.charAt(o2+i)) return -1;
                    else if (str.charAt(o1+i) > str.charAt(o2+i)) return 1;
                }
                return 0;
            }
        });

    }

    // length of s
    public int length(){
        return n;
    }

    // returns index of ith sorted suffix
    public int index(int i){
        if(i<0 || i>=n) throw new java.lang.IllegalArgumentException();
        return list.get(i);
    }

    // unit testing (required)
    /*
    output should be
        ABRACADABRA!
        length = 12
        11
        10
        7
        0
        3
        5
        8
        1
        4
        6
        9
        2
     */
    public static void main(String[] args){
        String s = "ABRACADABRA!";
        CircularSuffixArray c = new CircularSuffixArray(s);
        System.out.println(s);
        System.out.println("length = " + c.length());
        for(int i=0; i<c.length(); i++){
            System.out.println(c.index(i));
        }
    }



}
