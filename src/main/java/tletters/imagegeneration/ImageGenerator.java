package tletters.imagegeneration;

import tletters.image.ImageUtils;

import java.awt.Font;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ImageGenerator {

    private BufferedImage image;

    public void generateImage(Font font, float fontSize, String text, float noisePercentage) {
        if (font == null || text == null || text.equals("")) {
            throw new IllegalArgumentException("Font and text can not be empty or null!");
        }
        if (fontSize < 0 || noisePercentage < 0) {
            throw new IllegalArgumentException("FontSize and noisePercentage can not be negative!");
        }
        font = font.deriveFont(fontSize);
        image = ImageUtils.generateText(font, text);
        image = ImageUtils.cropImage(image);
        generateNoise(noisePercentage);
    }

    public BufferedImage getGeneratedImage() {
        return image;
    }

    public void saveGeneratedImage() {
        try {
            ImageIO.write(image, "png", new File("Text.png"));
        } catch (IOException ex) {
            Logger.getLogger(ImageGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void generateNoise(float noisePercentage) {
        int height = image.getHeight();
        int width = image.getWidth();
        BufferedImage noisyImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Raster source = image.getRaster();
        WritableRaster output = noisyImage.getRaster();
        int currentValue;
        double newValue;
        double gaussian;
        int bands = output.getNumBands();
        Random generator = new java.util.Random();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                gaussian = generator.nextGaussian();
                for (int b = 0; b < bands; b++) {
                    currentValue = source.getSample(i, j, b);
                    newValue = noisePercentage * gaussian + currentValue;
                    if (newValue < 0) {
                        newValue = 0.0;
                    } else if (newValue > 255) {
                        newValue = 255.0;
                    }
                    output.setSample(i, j, b, (int) (newValue));
                }
            }
        }
        image = noisyImage;
    }

}
