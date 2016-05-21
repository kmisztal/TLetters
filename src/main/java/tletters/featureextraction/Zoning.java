package tletters.featureextraction;

import tletters.image.ImageUtils;

import java.awt.image.BufferedImage;

import static java.lang.Math.sqrt;
import static java.lang.StrictMath.pow;

/**
 * Created by Piotrek on 2016-04-30.
 * na podstawie http://www.ijcaonline.org/volume27/number4/pxc3874481.pdf
 */
public class Zoning implements ExtractionAlgorithm {

    public static final int DEF_COLUMNS = 4;
    public static final int DEF_ROWS = 4;
    public static final int DEF_ALPHA = 1;
    private final int columns;
    private final int rows;
    private final double alpha;

    public Zoning() {
        columns = DEF_COLUMNS;
        rows = DEF_ROWS;
        alpha = DEF_ALPHA;
    }

    public Zoning(int rows, int columns, double alpha) {
        this.columns = columns;
        this.rows = rows;
        this.alpha = alpha;
    }

    @Override
    public double[] extractFeatures(BufferedImage bufferedImage) {
        double[] res = new double[columns * rows];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                res[i * columns + j] = computeCellValue(i, j, bufferedImage);
            }
        }
        return res;
    }

    private Double computeCellValue(int row, int column, BufferedImage bufferedImage) {
        Double cellSum = 0.0;
        int cellWidth = (int) Math.ceil(bufferedImage.getWidth() / columns);
        int cellHeight = (int) Math.ceil(bufferedImage.getHeight() / rows); //TODO sprwdzic czy nie ma zaokroglania w dol
        int rowBeg = row * cellHeight;
        int colBeg = column * cellWidth;
        for (int i = rowBeg; i < Math.min((row + 1) * cellHeight, bufferedImage.getHeight()); i++) {
            for (int j = colBeg; j < Math.min((column + 1) * cellWidth, bufferedImage.getWidth()); j++) {
                if (ImageUtils.isBlack(bufferedImage, i, j)) {
                    cellSum += sqrt(pow((i - rowBeg), 2) + pow((j - colBeg), 2)) * 1.0 / sqrt(1 + pow(((j - colBeg + 1) / (i - rowBeg + 1)), 2));
                    //cos(arctan(x) zredukowany do 1/(sqrt (1+x^2))
                    //w celu uniknięcia dzielenia przez zero dla wyliczania theta użyłęm (y+1)/(x+1)
                    //zamiast y/x nie powinno byc problemow
                }
            }
        }
        return alpha * cellSum / getNumberOfPixels(row, column, bufferedImage, cellWidth, cellHeight);
    }

    private int getNumberOfPixels(int row, int column, BufferedImage bufferedImage, int cellWidth, int cellHeight) {
        return (Math.min((row + 1) * cellHeight, bufferedImage.getHeight()) - row * cellHeight)
                * (Math.min((column + 1) * cellWidth, bufferedImage.getWidth()) - column * cellWidth);
    }

}
