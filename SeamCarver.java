/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Picture;

public class SeamCarver {
    private Picture picture;
    private double[][] energy;
    private double[][] distance;


    public SeamCarver(Picture picture) {
        if (picture == null)
            throw new IllegalArgumentException();
        this.picture = new Picture(picture);
        energy = new double[width()][height()];
        distance = new double[width()][height()];
        initEnergy();
    }

    private void initEnergy() {
        int col = width();
        int row = height();
        for (int i = 0; i < col; i++) {
            energy[i][0] = 1000;
            energy[i][row - 1] = 1000;
        }
        for (int i = 1; i < row - 1; i++) {
            energy[0][i] = 1000;
            energy[col - 1][i] = 1000;
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
        int col = width();
        int row = height();
        if (x >= col || y >= row || x < 0 || y < 0)
            throw new IllegalArgumentException();
        if (x > 0 && y > 0 && x < width() - 1 && y < height() - 1) {
            double deltaX = getDeltaX(x, y);
            double deltaY = getDeltaY(x, y);
            energy[x][y] = Math.sqrt(deltaX + deltaY);
            return energy[x][y];
        }
        energy[x][y] = 1000;
        return 1000;
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
        transpose();
        int[] seam = findVerticalSeam();
        transpose();
        return seam;
    }

    private void transpose() {
        Picture transposed = new Picture(picture.height(), picture.width());
        double[][] newEnergy = new double[picture.height()][picture.width()];
        double[][] newDistance = new double[picture.height()][picture.width()];
        for (int x = 0; x < picture.width(); x++)
            for (int y = 0; y < picture.height(); y++) {
                transposed.set(y, x, picture.get(x, y));
                newEnergy[y][x] = energy[x][y];
                newDistance[y][x] = distance[x][y];
            }
        energy = newEnergy;
        distance = newDistance;
        picture = transposed;
    }
    // Function to manually transpose the picture


    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        int[] seam = new int[height()];
        if (width() == 1 || height() == 1) {
            for (int x = 0; x < width(); x++) {
                for (int y = 0; y < height(); y++)
                    seam[y] = x;
            }
            return seam;
        }
        initEnergy();

        int height = picture.height() - 1;
        for (int i = 0; i < picture.width(); i++)
            distance[i][height] = energy[i][height];
        for (int y = height() - 2; y >= 0; y--) {
            for (int x = 0; x < width(); x++) {
                if (x == 0)
                    distance[x][y] = energy[x][y] + (Math.min(distance[x][y + 1],
                                                              distance[x + 1][y + 1]));
                else if (x == width() - 1)
                    distance[x][y] = energy[x][y] + Math.min(distance[x][y + 1],
                                                             distance[x - 1][y + 1]);
                else {
                    distance[x][y] = energy[x][y] + Math.min(
                            Math.min(distance[x][y + 1], distance[x - 1][y + 1]),
                            distance[x + 1][y + 1]);
                }
            }
        }
        int x;
        int minIndex = -1;
        double min = Double.POSITIVE_INFINITY;
        for (x = 0; x < width(); x++) {
            if (distance[x][0] < min) {
                min = distance[x][0];
                minIndex = x;
            }
        }
        seam[0] = minIndex;
        for (int y = 0; y < height() - 2; y++) {
            if (minIndex == width() - 1)
                seam[y + 1] = distance[minIndex - 1][y + 1] >= distance[minIndex][y + 1] ?
                              minIndex : minIndex - 1;
            else if (minIndex == 0)
                seam[y + 1] = distance[minIndex + 1][y + 1] >= distance[minIndex][y + 1] ?
                              minIndex : minIndex + 1;
            else {
                min = Math.min(distance[minIndex][y + 1], Math.min(distance[minIndex - 1][y + 1],
                                                                   distance[minIndex + 1][y + 1]));
                if (min == distance[minIndex][y + 1])
                    seam[y + 1] = minIndex;
                else if (min == distance[minIndex + 1][y + 1])
                    seam[y + 1] = minIndex + 1;
                else
                    seam[y + 1] = minIndex - 1;
            }
            minIndex = seam[y + 1];
        }

        seam[height() - 1] = seam[height() - 2];
        return seam;
    }


    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        if (seam == null)
            throw new IllegalArgumentException();
        checkValidity(seam);
        Picture newPicture = new Picture(width(), height() - 1);
        for (int x = 0; x < width(); x++) {
            for (int y = 0; y < height() - 1; y++) {
                if (seam[x] < 0)
                    throw new IllegalArgumentException();
                if (y < seam[x]) {
                    newPicture.setRGB(x, y, picture.getRGB(x, y));
                }
                else {
                    newPicture.setRGB(x, y, picture.getRGB(x, y + 1));
                }
            }
        }
        picture = newPicture;
        initEnergy();
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        if (seam == null)
            throw new IllegalArgumentException();
        checkValidity(seam);
        Picture newPicture = new Picture(width() - 1, height());
        for (int y = 0; y < height(); y++) {
            for (int x = 0; x < width() - 1; x++) {
                if (seam[y] < 0)
                    throw new IllegalArgumentException();
                if (x < seam[y]) {
                    newPicture.setRGB(x, y, picture.getRGB(x, y));
                }
                else {
                    newPicture.setRGB(x, y, picture.getRGB(x + 1, y));
                }
            }
        }
        picture = newPicture;
        initEnergy();
    }

    private void checkValidity(int[] seam) {
        if (width() < 1 || height() < 1) {
            throw new IllegalArgumentException(
                    "The width and height of the picture must be greatern than 1");
        }
        if (seam.length < 1) {
            throw new IllegalArgumentException("The seam size must be greater than 1.");
        }

        for (int i = 0; i < seam.length - 1; i++) {
            if (Math.abs(seam[i] - seam[i + 1]) > 1) {
                throw new IllegalArgumentException();
            }
        }
    }
    /*
    private void printEnergy() {
        int row = height();
        int col = width();
        for (int r = 0; r < row; r++) {
            for (int c = 0; c < col; c++)
                System.out.print(energy[c][r] + " ");
            System.out.print("\n");
        }
    }

    private void printDistanceTo() {
        int row = height();
        int col = width();
        for (int r = 0; r < row; r++) {
            for (int c = 0; c < col; c++)
                System.out.print(distance[c][r] + " ");
            System.out.print("\n");
        }
    }
    */

/*    public static void main(String[] args) {
        SeamCarver obj = new SeamCarver(new Picture("logo.png"));
        obj.printEnergy();
    }*/
}
