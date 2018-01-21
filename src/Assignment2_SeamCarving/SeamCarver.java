import java.awt.Color;
import edu.princeton.cs.algs4.Picture;

/**
 * Created by HuaZ on 1/17/2018.


 The SeamCarver API. Your task is to implement the following mutable data type:

 v1: Pixel class holds both Color and energy of each cell, but failed to pass memory tests.
 v2: only store Color[][], will compute energy on the fly.
 v3: store Color[][] will failed on memory tests as well. store Color as RGB int32 in int[][] and do conversion.

 ASSESSMENT SUMMARY Compilation:
 PASSED API: PASSED
 Findbugs: PASSED
 PMD: PASSED
 Checkstyle: FAILED (0 errors, 143 warnings)
 Correctness: 31/31 tests passed
 Memory: 6/6 tests passed
 Timing: 19/17 tests passed
 Aggregate score: 102.35%
 [Compilation: 5%, API: 5%, Findbugs: 0%, PMD: 0%, Checkstyle: 0%, Correctness: 60%, Memory: 10%, Timing: 20%]


 */
public class SeamCarver {
    private int width, height;
    private int[][] pixelRGB;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if(picture == null ) throw new IllegalArgumentException("");
        width = picture.width();
        height = picture.height();
        pixelRGB = new int[height][width];

        // convert picture to int[][] matrix
        for(int row=0; row<height; row++) {
            for(int col=0;col<width;col++){
                pixelRGB[row][col] = picture.getRGB(col,row);
            }
        }
    }

    // current picture
    public Picture picture() {
        Picture ret = new Picture(width, height);
        for(int row=0; row<height; row++) {
            for(int col=0;col<width;col++){
                ret.set(col,row, new Color(pixelRGB[row][col]));
            }
        }
        return ret;
    }

    // width of current picture
    public int width() {
        return width;
    }

    // height of current picture
    public int height() {
        return height;
    }

    // energy of pixel at column x and row y
    // passed unit test, printEnergy.java
    public double energy(int x, int y) {
        if(x <0 || x>=width || y<0 || y>=height) {
            throw new IllegalArgumentException(String.format("x=%d width=%d, y=%d height=%d",x,width,y,height));
        }
        if(x == 0 || x == width - 1 || y == 0 || y == height -1 ) return 1000;  // border are defined as 1000

        //System.out.println(String.format("in energy x=%d width=%d, y=%d height=%d",x,width,y,height));
        // math calculation of energy

        Color left = new Color(pixelRGB[y][x-1]), right = new Color(pixelRGB[y][x+1]),
                top = new Color(pixelRGB[y-1][x]), down = new Color(pixelRGB[y+1][x]);
        double ret = Math.pow(left.getBlue() - right.getBlue(), 2) +
                     Math.pow(left.getRed() - right.getRed(), 2) +
                     Math.pow(left.getGreen() - right.getGreen(), 2) +
                     Math.pow(top.getBlue() - down.getBlue(), 2) +
                     Math.pow(top.getRed() - down.getRed(), 2) +
                     Math.pow(top.getGreen() - down.getGreen(), 2);

        return Math.sqrt(ret);
    }


    // they can be shorter than actually matrix
    // transpose original matrix
    private void transposeMatrix(){
        int t_height = width(), t_width = height();
        int[][] ret = new int[t_height][t_width];
        for(int row=0; row<t_height; row++){
            for(int col =0;col<t_width; col++) {
                ret[row][col] = pixelRGB[col][row];
            }
        }
        pixelRGB = ret;
        swapWeightAndHeight();
    }

    // compute energy[][], need to compute every cell? can optimize?
    private double[][] computeEnergy(){
        double[][] energy = new double[height][width];
        for(int row=0; row<height; row++) {
            for(int col=0;col<width;col++){
                energy[row][col] = energy(col,row);
            }
        }
        return energy;
    }

    private void swapWeightAndHeight(){
        int tmp = height;
        height = width;
        width = tmp;
    }

    // sequence of indices for horizontal seam
    // transpose matrix, call findVerticalSeam and transpose it back
    // I had some trouble generate transpose matrix, due to unusual sequence of matrix.
    // solved by create matrix as normal, but pass col first in some function calls.
    public int[] findHorizontalSeam() {
        transposeMatrix();
        int[] ret = findVerticalSeam();
        transposeMatrix();
        return ret;
    }


    // sequence of indices for vertical seam
    // need compute energy[][] on the fly
    // core algorithm in this assignment
    public int[] findVerticalSeam() {
        if(height == 1) return new int[]{0};

        double[][] distTo = new double[height][width];
        int[][] edgeTo = new int[height][width]; // contains col number from above row, use it to trace back
        double[][] energy = computeEnergy();

        // init distTo
        for(int row=0; row<height; row++) {
            for(int col=0;col<width;col++){
                if(row == 0) distTo[row][col] = 1000;  // first row
                else distTo[row][col] = Integer.MAX_VALUE;
            }
        }

        // core Shortest path
        for(int row=1; row<height; row++) {
            for(int col=0;col<width;col++) {
                // check 3 neighbour cols in above row
                for(int ncol = col-1; ncol <= col+1; ncol++){
                    if(ncol >=0 && ncol < width){
                        if(distTo[row][col] > distTo[row-1][ncol] + energy[row][col]){
                            distTo[row][col] = distTo[row-1][ncol] + energy[row][col];
                            edgeTo[row][col] = ncol;
                        }
                    }
                }
            }
        }

        // find min distTo in last row
        int minLastCol = 0;
        double minDist = Double.MAX_VALUE;
        for(int col=0; col<width; col++){
            if(minDist > distTo[height-1][col]){
                minDist = distTo[height-1][col];
                minLastCol = col;
            }
        }

        // trace back
        int[] ret  = new int[height];
        for(int row = height-1; row >=0; row--){
            ret[row] = minLastCol;
            minLastCol = edgeTo[row][minLastCol];
        }
        return ret;
    }

    // remove horizontal seam from current picture
    // transpose and call removeVerticalSeam and transpose it back
    public void removeHorizontalSeam(int[] seam) {
        if(seam == null || seam.length != width() || height() <= 1) throw new IllegalArgumentException("");
        transposeMatrix();
        removeVerticalSeam(seam);
        transposeMatrix();
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        if(seam == null || seam.length != height() || width() <= 1) throw new IllegalArgumentException("");

        // check int[] seam
        for(int i=0; i < seam.length; i++){
            int col_index = seam[i];
            if(col_index< 0 || col_index >= width) throw new IllegalArgumentException("");
            if(i>0 && Math.abs(seam[i] - seam[i-1]) > 1) throw new IllegalArgumentException("");
        }

        // remove seam in each row
        for(int row=0; row < seam.length; row++){
            int col_index = seam[row];
            System.arraycopy(pixelRGB[row], col_index +1, pixelRGB[row], col_index, width - col_index - 1);
        }

        width -= 1;
    }
}
