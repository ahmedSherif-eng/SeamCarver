/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Picture;

public class SeamCarver {
    private Picture picture;
    private double[][] Energy;
    private double[][] distanceTo;
    private int col;
    private int row;

    public SeamCarver(Picture picture) {
        if (picture == null)
            throw new IllegalArgumentException();
        this.picture = picture;
        col = width();
        row = height();
        Energy = new double[col][row];
        distanceTo = new double[col][row];
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
        for (int x = 1; x < col - 1; x++)
            for (int y = 1; y < row - 1; y++)
                energy(x, y);
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
        if (x >= col || y >= row)
            throw new IllegalArgumentException();
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
    /*
     * each vertix has an edge with
     *           (x,y+1)
     *           (x+1,y+1)
     *           (x-1,y+1)
     * */
    public int[] findHorizontalSeam() {
        Picture transposed = new Picture(picture.height(), picture.width());
        for (int x = 0; x < col; x++)
            for (int y = 0; y < row; y++)
                transposed.set(y, x, picture.get(x, y));
        transposed.save("xx.png");
        SeamCarver flipped = new SeamCarver(transposed);
        return flipped.findVerticalSeam();


    }

    // Function to manually transpose the picture

    private void relax(int x, int y) {
        if (distanceTo[x][y] >= Energy[x][y] + distanceTo[x][y - 1])
            distanceTo[x][y] = Energy[x][y] + distanceTo[x][y - 1];
        else if (distanceTo[x][y] >= Energy[x][y] + distanceTo[x - 1][y - 1])
            distanceTo[x][y] = Energy[x][y] + distanceTo[x - 1][y - 1];
        else if (distanceTo[x][y] >= Energy[x][y] + distanceTo[x + 1][y - 1])
            distanceTo[x][y] = Energy[x][y] + distanceTo[x + 1][y - 1];
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        int i;
        int x;
        int y;
        int[] seam = new int[row];
        for (i = 0; i < col; i++)
            for (int j = 0; j < row; j++)
                distanceTo[i][j] = Double.POSITIVE_INFINITY;
        for (i = 0; i < col; i++) {
            distanceTo[i][0] = 1000;
            distanceTo[i][row - 1] = 1000;
        }
        for (i = 1; i < row - 1; i++) {
            distanceTo[0][i] = 1000;
            distanceTo[col - 1][i] = 1000;
        }
        for (y = 1; y < row - 1; y++)
            for (x = 1; x < col - 1; x++)
                relax(x, y);
        double min = Double.POSITIVE_INFINITY;
        for (int j = 1; j < row; j++) {
            for (i = 0; i < col; i++)
                if (distanceTo[i][j] < min) {
                    min = distanceTo[i][j];
                    seam[j] = i;
                    if (j == 1)
                        seam[0] = i;
                }
        }
        return seam;
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        if (seam == null || seam.length > row)
            throw new IllegalArgumentException();
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        if (seam == null || seam.length > row)
            throw new IllegalArgumentException();
    }

    private void printEnergy() {
        for (int r = 0; r < row; r++) {
            for (int c = 0; c < col; c++)
                System.out.print(Energy[c][r] + " ");
            System.out.print("\n");
        }
    }

    public static void main(String[] args) {
        SeamCarver obj = new SeamCarver(new Picture("HJocean.png"));
        obj.findHorizontalSeam();


    }
}
