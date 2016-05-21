package tletters.imagescaling;

import tletters.image.ImageUtils;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

public class ImageScaler {

    private boolean checkHorizontalLine(BufferedImage image, int line, int width) {
        for (int i = 0; i < width; i++) {
            if (ImageUtils.isBlack(image, i, line)) {
                return true;
            }
        }
        return false;
    }

    private boolean checkVerticalLine(BufferedImage image, int line, int height) {
        for (int i = 0; i < height; i++) {
            if (ImageUtils.isBlack(image, line, i)) {
                return true;
            }
        }
        return false;
    }

    private BufferedImage cutImage(BufferedImage image) {
        int width = image.getWidth(), height = image.getHeight();
        int left = 0, top = 0;
        int right = width - 1;
        int bottom = height - 1;
        while (top < height - 1 && !checkHorizontalLine(image, top, width)) {
            top++;
        }
        while (left < width - 1 && !checkVerticalLine(image, left, height)) {
            left++;
        }
        while (bottom > top && !checkHorizontalLine(image, bottom, width)) {
            bottom--;
        }
        while (right > left && !checkVerticalLine(image, right, height)) {
            right--;
        }
        return image.getSubimage(left, top, right - left + 1, bottom - top + 1);
    }

    private BufferedImage getScalImage(BufferedImage image) {
        BufferedImage imageAfterScal = new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphicAfterScal = imageAfterScal.createGraphics();
        try {
            graphicAfterScal.setRenderingHints(ImageUtils.RENDERING_PROPERTIES);
            graphicAfterScal.drawImage(image, 0, 0, 64, 64, null);
        } finally {
            graphicAfterScal.dispose();
        }
        return imageAfterScal;
    }

    private void saveScalImage(BufferedImage image, String unicode) {
        char unicodeChar = unicode.charAt(0);
        String hex = String.format("%04x", (int) unicodeChar);
        File outputfile = new File(hex + ".png");
        try {
            ImageIO.write(getScalImage(image), "png", outputfile);
        } catch (IOException ex) {
            Logger.getLogger(ImageScaler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void scalImage(String unicode, String fontName) {
        if (unicode == null || unicode.equals("") || fontName == null || fontName.equals("")) {
            throw new IllegalArgumentException("Unicode and fontName can not be empty or null!");
        }
        BufferedImage imageBeforeScal = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphic = imageBeforeScal.createGraphics();
        Font font = null;
        String regex = "([A-Z|a-z]:\\\\[^*|\"<>?\\n]*)|(\\\\\\\\.*?\\\\.*)";
        File fontFile = new File(fontName);
        if (Pattern.matches(regex, fontName)) {
            try {
                font = Font.createFont(Font.TRUETYPE_FONT, fontFile).deriveFont(50f);
                GraphicsEnvironment graphicsEnviroment = GraphicsEnvironment.getLocalGraphicsEnvironment();
                graphicsEnviroment.registerFont(Font.createFont(Font.TRUETYPE_FONT, fontFile));
            } catch (IOException | FontFormatException e) {
                System.out.println("Wrong font format!");
            }
        } else {
            font = new Font(fontName, Font.PLAIN, 50);
        }
        graphic.setFont(font);
        FontMetrics fontMetrics = graphic.getFontMetrics();
        int width = fontMetrics.stringWidth(unicode) + 4;
        int height = fontMetrics.getHeight() + 4;
        graphic.dispose();
        imageBeforeScal = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        graphic = imageBeforeScal.createGraphics();
        graphic.setRenderingHints(ImageUtils.RENDERING_PROPERTIES);
        graphic.setColor(Color.BLACK);
        graphic.setFont(font);
        int x = 0;
        int y = fontMetrics.getAscent() + 2;
        graphic.setBackground(Color.WHITE);
        graphic.clearRect(0, 0, width, height);
        graphic.drawString(unicode, x, y);
        graphic.dispose();
        imageBeforeScal = cutImage(imageBeforeScal);
        saveScalImage(imageBeforeScal, unicode);
    }

    public void scalImage(String unicode, BufferedImage image) {
        if (unicode == null || unicode.equals("") || image == null) {
            throw new IllegalArgumentException("Unicode and image can not be empty or null!");
        }
        image = cutImage(image);
        saveScalImage(image, unicode);
    }

    public BufferedImage scalImage(BufferedImage image) {
        if (image == null) {
            throw new IllegalArgumentException("Image can not be null!");
        }
        image = cutImage(image);
        return getScalImage(image);
    }

}
