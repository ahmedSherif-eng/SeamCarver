/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Picture;

public class SeamCarver {
    private Picture picture;
    private double[][] Energy;
    private int col;
    private int row;

    public SeamCarver(Picture picture) {
        this.picture = picture;
        col = width();
        row = height();
        Energy = new double[col][row];
        for (int i = 0; i < row; i++)
            for (int j = 0; j < col; j++)
                Energy[j][i] = -1;
        initEnergy();
    }

    private void initEnergy() {
        for (int i = 0; i < col; i++) {
            Energy[i][0] = 1000;
            Energy[i][row - 1] = 1000;
        }
        for (int i = 1; i < row - 1; i++) {
            Energy[0][i] = 1000;
            Energy[col - 1][i] = 1000;
        }
    }

    // current picture
    public Picture picture() {
        return this.picture;
    }

    // width of current picture
    public int width() {
        return picture.width();
    }

    // height of current picture
    public int height() {
        return picture.height();
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (Energy[x][y] == -1) {
            double deltaX = getDeltaX(x, y);
            double deltaY = getDeltaY(x, y);
            double energy = Math.sqrt(deltaX + deltaY);
            Energy[x][y] = energy;
            return energy;
        }
        return Energy[x][y];
    }

    private double getDeltaX(int x, int y) {
        int rgbNext = picture.getRGB(x + 1, y);
        int rN = (rgbNext >> 16) & 0xFF;
        int gN = (rgbNext >> 8) & 0xFF;
        int bN = rgbNext & 0xFF;
        int rgbPrev = picture.getRGB(x - 1, y);
        int rP = (rgbPrev >> 16) & 0xFF;
        int gP = (rgbPrev >> 8) & 0xFF;
        int bP = rgbPrev & 0xFF;
        int rX = rN - rP;
        int gX = gN - gP;
        int bX = bN - bP;
        double deltaX = Math.pow(rX, 2) + Math.pow(gX, 2) + Math.pow(bX, 2);
        return deltaX;
    }

    private double getDeltaY(int x, int y) {
        int rgbNext = picture.getRGB(x, y + 1);
        int rN = (rgbNext >> 16) & 0xFF;
        int gN = (rgbNext >> 8) & 0xFF;
        int bN = rgbNext & 0xFF;
        int rgbPrev = picture.getRGB(x, y - 1);
        int rP = (rgbPrev >> 16) & 0xFF;
        int gP = (rgbPrev >> 8) & 0xFF;
        int bP = rgbPrev & 0xFF;
        int rY = rN - rP;
        int gY = gN - gP;
        int bY = bN - bP;
        double deltaY = Math.pow(rY, 2) + Math.pow(gY, 2) + Math.pow(bY, 2);
        return deltaY;
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {

    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {

    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {

    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {

    }

    private void printEnergy() {
        for (int r = 0; r < row; r++) {
            for (int c = 0; c < col; c++)
                System.out.print(Energy[c][r] + " ");
            System.out.print("\n");
        }
    }

    public static void main(String[] args) {
      /*  SeamCarver obj = new SeamCarver(new Picture(10, 10));
        obj.printEnergy();
    */
    }
}
