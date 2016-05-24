package tletters.imagegeneration;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import javax.imageio.ImageIO;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ImageGeneratorTest {

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Test
    public void testWillThrowExceptionWhenFontSizeIsNegative() {
        expectedEx.expect(IllegalArgumentException.class);
        ImageGenerator generator = new ImageGenerator();
        Font font = new Font("Verdana", Font.BOLD, 0);
        generator.generateImage(font, -5, "TEST", 30);
    }

    @Test
    public void testWillThrowExceptionWhenImageIsNullAndFileWasNotCreated() {
        expectedEx.expect(IllegalArgumentException.class);
        ImageGenerator generator = new ImageGenerator();
        generator.saveGeneratedImage();
    }

    @Test
    public void testFileWasCreated() {
        ImageGenerator generator = new ImageGenerator();
        Font font = new Font("Verdana", Font.BOLD, 0);
        generator.generateImage(font, 5, "TEST", 30);
        generator.saveGeneratedImage();
        assertTrue(new File("Text.png").exists());
    }

    @Test
    public void testBufferedImageWasCreated() {
        ImageGenerator generator = new ImageGenerator();
        Font font = new Font("Verdana", Font.BOLD, 0);
        generator.generateImage(font, 50, "TEST", 30);
        assertTrue(generator.getGeneratedImage() != null);
    }

    public static byte[] getBytes(BufferedImage img) throws IOException {
        ByteArrayOutputStream byar = new ByteArrayOutputStream();
        try {
            ImageIO.write(img, "png", byar);
        } finally {
            byar.close();
        }
        return byar.toByteArray();
    }

    @Test
    public void testRandomGenerateNoise() throws IOException {
        ImageGenerator generator = new ImageGenerator();
        Font font = new Font("Verdana", Font.BOLD, 0);

        generator.generateImage(font, 50, "TEST", 30);
        BufferedImage imageFirst = generator.getGeneratedImage();
        byte[] imageFirstBytes = getBytes(imageFirst);

        generator.generateImage(font, 50, "TEST", 30);
        BufferedImage imageSecond = generator.getGeneratedImage();
        byte[] imageSecondBytes = getBytes(imageSecond);

        assertFalse(Arrays.equals(imageFirstBytes, imageSecondBytes));
    }
}
