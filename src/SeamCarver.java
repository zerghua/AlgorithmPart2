import java.awt.Color;
import edu.princeton.cs.algs4.Picture;

/**
 * Created by HuaZ on 1/17/2018.


 The SeamCarver API. Your task is to implement the following mutable data type:
 */
public class SeamCarver {
    private int width, height;
    private Pixel[][] pixel;

    private class Pixel{
        Color color;
        double energy;
        Pixel(Color color, double energy){
            this.color = color;
            this.energy = energy;
        }
    }

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if(picture == null ) throw new IllegalArgumentException("");
        width = picture.width();
        height = picture.height();

        pixel = new Pixel[height][width];

        // init color
        for(int row=0; row<height; row++) {
            for(int col=0;col<width;col++){
                pixel[row][col] = new Pixel(picture.get(col,row), 0);
            }
        }

        // init energy
        for(int row=0; row<height; row++) {
            for(int col=0;col<width;col++){
                pixel[row][col].energy = energy(col, row);
            }
        }
    }

    // current picture
    public Picture picture() {
        Picture ret = new Picture(width, height);
        for(int row=0; row<height; row++) {
            for(int col=0;col<width;col++){
                ret.set(col,row, pixel[row][col].color);
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

        // math calculation of energy
        Color left = pixel[y][x-1].color, right = pixel[y][x+1].color,
                top = pixel[y-1][x].color, down = pixel[y+1][x].color;
        double ret = Math.pow(left.getBlue() - right.getBlue(), 2) +
                     Math.pow(left.getRed() - right.getRed(), 2) +
                     Math.pow(left.getGreen() - right.getGreen(), 2) +
                     Math.pow(top.getBlue() - down.getBlue(), 2) +
                     Math.pow(top.getRed() - down.getRed(), 2) +
                     Math.pow(top.getGreen() - down.getGreen(), 2);

        return Math.sqrt(ret);
    }


    // has to pass width and height
    // they can be shorter than actuall matrix
    private Pixel[][] transposeMatrix(Pixel[][] matrix, int width, int height){
        int t_height = width, t_width = height;
        Pixel[][] ret = new Pixel[t_height][t_width];
        for(int row=0; row<t_height; row++){
            for(int col =0;col<t_width; col++) {
                ret[row][col] = matrix[col][row];
            }
        }
        //System.out.println("\ninside : " + ret.length + "  "  + ret[0].length);
        return ret;
    }

    // sequence of indices for horizontal seam
    // transpose matrix, call findVerticalSeam and transpose it back
    // I had some trouble generate transpose matrix, due to unusual sequence of matrix.
    public int[] findHorizontalSeam() {
        Pixel[][] p = transposeMatrix(pixel, width(), height());
        int[] ret = findVerticalSeam(p, height(), width());
        return ret;
    }

    // sequence of indices for vertical seam
    // assuming energy[][] contains updated energy for each cell
    // core algorithm in this assignment
    public int[] findVerticalSeam() {
        return findVerticalSeam(pixel, width(), height());
    }

    // column x and row y
    private int[] findVerticalSeam(Pixel[][] p, int width, int height){
        if(height == 1) return new int[]{0};

        double[][] distTo = new double[height][width];
        int[][] edgeTo = new int[height][width]; // contains col number from above row, use it to trace back

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
                        if(distTo[row][col] > distTo[row-1][ncol] + p[row][col].energy){
                            distTo[row][col] = distTo[row-1][ncol] + p[row][col].energy;
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
        Pixel[][] p = transposeMatrix(pixel, width(), height());
        //printMatrix(p);
        removeVerticalSeam(seam, p, height());
        height -= 1;
        pixel = transposeMatrix(p, height(), width());
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        if(seam == null || seam.length != height() || width() <= 1) throw new IllegalArgumentException("");
        removeVerticalSeam(seam, pixel, width());
        width -= 1;
    }

    private void removeVerticalSeam(int[] seam, Pixel[][] p, int width){
        // check int[] seam
        System.out.println("print seam:");
        for(int i=0; i < seam.length; i++){
            int col_index = seam[i];
            if(col_index< 0 || col_index >= width) throw new IllegalArgumentException("");
            if(i>0 && Math.abs(seam[i] - seam[i-1]) > 1) throw new IllegalArgumentException("");
            System.out.print(col_index + " ");
        }

        for(int row=0; row < seam.length; row++){
            int col_index = seam[row];
            System.arraycopy(p[row], col_index +1, p[row], col_index, width - col_index - 1);
        }

        System.out.println("inside width="+width);
        // update energy
        for(int row=0; row<height(); row++) {
            for(int col=seam[row]-1;col<=seam[row];col++){
                System.out.println("print coordinate: " + col + "  " + row);
                if(col >= 0 && col < width) p[row][col].energy = energy(col, row);
            }
        }
    }

    /*
    private void printMatrix(Pixel[][] p){
        System.out.println("\n");

        for(int i=0; i<p.length;i++){
            System.out.println("");
            for(int j=0;j<p[0].length;j++){
                System.out.format("%7.2f  ", p[i][j].energy );
            }
        }
        System.out.println("\n");
    }
    */
}
