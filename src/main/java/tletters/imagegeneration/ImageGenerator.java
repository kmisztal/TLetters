package tletters.imagegeneration;

import tletters.image.ImageUtils;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class ImageGenerator {

    private BufferedImage image;

    public void generateImage(Font font, float fontSize, String text, float noisePercentage) {
        font = font.deriveFont(fontSize);
        image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        g2d.setFont(font);
        FontMetrics metrics = g2d.getFontMetrics();
        int height = metrics.getHeight() + 4;
        int width = metrics.stringWidth(text) + 4;
        g2d.dispose();
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        g2d = image.createGraphics();
        g2d.setRenderingHints(ImageUtils.RENDERING_PROPERTIES);
        g2d.setFont(font);
        g2d.setColor(Color.BLACK);
        g2d.setBackground(Color.WHITE);
        g2d.clearRect(0, 0, width, height);
        g2d.drawString(text, 0, metrics.getAscent() + 2);
        g2d.dispose();
        cropImage();
        generateNoise(noisePercentage);
    }

    public BufferedImage getGeneratedImage() {
        return image;
    }

    public void saveGeneratedImage() {
        try {
            ImageIO.write(image, "png", new File("Text.png"));
        } catch (IOException ex) {
        }
    }

    private void cropImage() {
        int top = 0;
        while (top < image.getHeight() - 1 && !checkHorizontalLine(top)) {
            top++;
        }
        int left = 0;
        while (left < image.getWidth() - 1 && !checkVerticalLine(left)) {
            left++;
        }
        int bottom = image.getHeight() - 1;
        while (bottom > top && !checkHorizontalLine(bottom)) {
            bottom--;
        }
        int right = image.getWidth() - 1;
        while (right > left && !checkVerticalLine(right)) {
            right--;
        }
        image = image.getSubimage(left, top, right - left + 1, bottom - top + 1);
    }

    private boolean checkHorizontalLine(int line) {
        int width = image.getWidth();
        for (int i = 0; i < width; i++) {
            if (ImageUtils.isBlack(image, i, line)) {
                return true;
            }
        }
        return false;
    }

    private boolean checkVerticalLine(int line) {
        int height = image.getHeight();
        for (int i = 0; i < height; i++) {
            if (ImageUtils.isBlack(image, line, i)) {
                return true;
            }
        }
        return false;
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
