package tletters.image;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Tomek on 2016-05-18.
 */
public class ImageUtils {

    public static final Map<RenderingHints.Key, Object> RENDERING_PROPERTIES = new HashMap<>();

    static {
        RENDERING_PROPERTIES.put(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        RENDERING_PROPERTIES.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        RENDERING_PROPERTIES.put(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        RENDERING_PROPERTIES.put(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        RENDERING_PROPERTIES.put(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        RENDERING_PROPERTIES.put(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        RENDERING_PROPERTIES.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        RENDERING_PROPERTIES.put(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
    }

    public static BufferedImage cropImage(BufferedImage image) {
        int width = image.getWidth(), height = image.getHeight();
        int left = 0, top = 0;
        int right = width - 1;
        int bottom = height - 1;
        while (top < height - 1 && !checkHorizontalLine(image, top)) {
            top++;
        }
        while (left < width - 1 && !checkVerticalLine(image, left)) {
            left++;
        }
        while (bottom > top && !checkHorizontalLine(image, bottom)) {
            bottom--;
        }
        while (right > left && !checkVerticalLine(image, right)) {
            right--;
        }
        return image.getSubimage(left, top, right - left + 1, bottom - top + 1);
    }

    public static BufferedImage generateText(Font font, String text) {
        BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = image.createGraphics();
        graphics.setFont(font);
        FontMetrics metrics = graphics.getFontMetrics();
        int height = metrics.getHeight() + 4;
        int width = metrics.stringWidth(text) + 4;
        graphics.dispose();
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        graphics = image.createGraphics();
        graphics.setBackground(Color.WHITE);
        graphics.setColor(Color.BLACK);
        graphics.setFont(font);
        graphics.setRenderingHints(ImageUtils.RENDERING_PROPERTIES);
        graphics.clearRect(0, 0, width, height);
        graphics.drawString(text, 0, metrics.getAscent() + 2);
        graphics.dispose();
        return image;
    }

    public static boolean isBlack(BufferedImage image, int i, int j) {
        //return image.getRGB(i, j) <= -15000000 && image.getRGB(i, j) > -17000000;
        return image.getRGB(i, j) != -1;
    }

    private static boolean checkHorizontalLine(BufferedImage image, int line) {
        int width = image.getWidth();
        for (int i = 0; i < width; i++) {
            if (ImageUtils.isBlack(image, i, line)) {
                return true;
            }
        }
        return false;
    }

    private static boolean checkVerticalLine(BufferedImage image, int line) {
        int height = image.getHeight();
        for (int i = 0; i < height; i++) {
            if (ImageUtils.isBlack(image, line, i)) {
                return true;
            }
        }
        return false;
    }

}
