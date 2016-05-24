package tletters.imagescaling;

import tletters.image.ImageUtils;

import javax.imageio.ImageIO;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

public class ImageScaler {

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
        Font font = null;
        String regex = "([A-Z|a-z]:\\\\[^*|\"<>?\\n]*)|(\\\\\\\\.*?\\\\.*)";
        File fontFile = new File(fontName);
        if (Pattern.matches(regex, fontName)) {
            try {
                font = Font.createFont(Font.TRUETYPE_FONT, fontFile).deriveFont(50f);
                GraphicsEnvironment graphicsEnviroment = GraphicsEnvironment.getLocalGraphicsEnvironment();
                graphicsEnviroment.registerFont(Font.createFont(Font.TRUETYPE_FONT, fontFile));
            } catch (IOException | FontFormatException ex) {
                Logger.getLogger(ImageScaler.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            font = new Font(fontName, Font.PLAIN, 50);
        }
        BufferedImage imageBeforeScal = ImageUtils.generateText(font, unicode);
        imageBeforeScal = ImageUtils.cropImage(imageBeforeScal);
        saveScalImage(imageBeforeScal, unicode);
    }

    public void scalImage(String unicode, BufferedImage image) {
        if (unicode == null || unicode.equals("") || image == null) {
            throw new IllegalArgumentException("Unicode and image can not be empty or null!");
        }
        image = ImageUtils.cropImage(image);
        saveScalImage(image, unicode);
    }

    public BufferedImage scalImage(BufferedImage image) {
        if (image == null) {
            throw new IllegalArgumentException("Image can not be null!");
        }
        image = ImageUtils.cropImage(image);
        return getScalImage(image);
    }

}
