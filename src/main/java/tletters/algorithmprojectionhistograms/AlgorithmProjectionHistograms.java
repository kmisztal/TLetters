package tletters.algorithmprojectionhistograms;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Monika on 2016-06-13.
 */
public class AlgorithmProjectionHistograms {
    private int[][] pixels;
    private int width, height;

    public AlgorithmProjectionHistograms(BufferedImage bufferedImage) {
        if (bufferedImage == null) {
            throw new IllegalArgumentException("Image cannot be null!");
        }

        width = bufferedImage.getWidth();
        height = bufferedImage.getHeight();

        pixels = getPixels(bufferedImage);
    }

    public List<List> computeVertical(){
        List<List> countPixels = new ArrayList();
        int count;
        for (int idx = 0; idx < width; idx++) {
            count = 0;
            countPixels.add(new ArrayList());
            for (int j = 0; j < height; j++) {
                if(pixels[idx][j] == 1){
                    count++;
                } else if(count != 0){
                    countPixels.get(idx).add(count);
                    count = 0;
                }
            }

            if(count != 0 ||  countPixels.get(idx).isEmpty()){
                countPixels.get(idx).add(count);
            }
        }

        return countPixels;
    }

    public List<List> computeHorizontal(){
        List<List> countPixels = new ArrayList();
        int count;
        for (int idx = 0; idx < height; idx++) {
            count = 0;
            countPixels.add(new ArrayList());
            for (int j = 0; j < width; j++) {
                if(pixels[j][idx] == 1){
                    count++;
                } else if(count != 0){
                    countPixels.get(idx).add(count);
                    count = 0;
                }
            }
            if(count != 0 ||  countPixels.get(idx).isEmpty()){
                countPixels.get(idx).add(count);
            }
        }

        return countPixels;
    }

    private int[][] getPixels(BufferedImage bufferedImage){

        int[][] pixelsArray = new int[width][height];

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int tmpColor = bufferedImage.getRGB(j, i);
                if (tmpColor == -1) {
                    pixelsArray[j][i] = 0;
                } else {
                    pixelsArray[j][i] = 1;
                }
            }
        }
        return pixelsArray;
    }

}
