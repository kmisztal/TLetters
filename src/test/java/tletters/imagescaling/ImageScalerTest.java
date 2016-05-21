package tletters.imagescaling;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author arek
 */
public class ImageScalerTest {

    /**
     * Test for null parameters of scalImage method, of class ImageScaler.
     */
    @Test(expected=IllegalArgumentException.class)
    public void testScalImage_StringNull_StringNull() {
        System.out.println("scalImage(null, null)");
        String unicode = null;
        String fontName = null;
        ImageScaler instance = new ImageScaler();
        instance.scalImage(unicode, fontName);
    }
    /**
     * My comment:
     * Test failed,
     * NullPointerException was thrown instead of IllegalArgumentException,
     * should this be handled?
     */

    /**
     * Test of scalImage method, of class ImageScaler.
     * An untypical unicode character is tested, "\u1f170" (or "\u1F170" - both work) = NEGATIVE SQUARED LATIN CAPITAL LETTER A
     * and font is Arial
     * However, I don't have a reference image of this, so I just check whether any exception is thrown or not
     */
    @Test
    public void testScalImage_String_String() {
        System.out.println("scalImage(String, String)");
        String unicode = "\u1f170";
        String fontName = "Arial";
        ImageScaler instance = new ImageScaler();
        instance.scalImage(unicode, fontName);
    }
    /**
     * My comment:
     * Test passed but it appears that only 4-character hex codes are allowed here
     * so I get an "unknown character" which doesn't really look the same after processing
     * compare it with http://apps.timwhitlock.info/unicode/inspect/hex/1f17
     */

    /**
     * Test of scalImage method, of class ImageScaler.
     * Here, I'm testing whether the once created image will look the same after yet another cut
     * (but I really can't see a point in this method since I can write a new image under any hex I want using this class)
     */
    @Test
    public void testScalImage_String_BufferedImage() {
        System.out.println("scalImage(String, BufferedImage)");
        String unicode = "\u0065";
        String fontName = "Times New Roman";
        ImageScaler instance = new ImageScaler();
        instance.scalImage(unicode, fontName);
        BufferedImage src;
        BufferedImage copy;
        try {
            src = ImageIO.read(new File("0065.png"));
            instance.scalImage("\u0000", src);
            copy = ImageIO.read(new File("0000.png"));
            assertTrue(compareImages(src, copy));
        } catch (IOException ex) {
            Logger.getLogger(ImageScalerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /**
     * My comment: it's OK
     */

    /**
     * Additional method for comparing two BufferedImages.
     * It compares the image dimensions first, and then iterates over each pixel to
     * @param imgA
     * @param imgB
     * @return true if images are equal, false otherwise
     */
    public boolean compareImages(BufferedImage imgA, BufferedImage imgB) {
        int width = imgA.getWidth();
        int height = imgA.getHeight();
        if (width == imgB.getWidth() && height == imgB.getHeight()) {
            for (int y = 0; y < height; y++)
                for (int x = 0; x < width; x++)
                    if (imgA.getRGB(x, y) != imgB.getRGB(x, y))
                        return false;
        } else return false;
        return true;
    }

    /**
     * Test of cutImage method, of class ImageScaler.
     * (Well, I call scalImage(String, BufferedImage) but it does call the cutImage(BufferedImage) method.)
     * Gonna check what happens if I provide a blank image for it.
     * I'm expecting something like IndexOutOfBoundsException; anyhow, neither one is handled currently
     */
    @Test
    public void testCutImage_Blank() {
        System.out.println("cutImage(BufferedImage empty)");
        BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_4BYTE_ABGR);
        ImageScaler instance = new ImageScaler();
        instance.scalImage("\u0001", image);
    }

    /**
     * Test of cutImage method, of class ImageScaler.
     * Same as above but with an all-white image.
     */
    @Test
    public void testCutImage_AllWhite() {
        System.out.println("cutImage(BufferedImage allWhite)");
        BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_4BYTE_ABGR);
        for(int x=0; x<100; x++)
            for(int y=0; y<100; y++)
                image.setRGB(x, y, Color.WHITE.getRGB());
        ImageScaler instance = new ImageScaler();
        instance.scalImage("\u0002", image);
    }

    /**
     * Test of cutImage method, of class ImageScaler.
     * Same as above but with an all-black image.
     */
    @Test
    public void testCutImage_AllBlack() {
        System.out.println("cutImage(BufferedImage allBlack)");
        BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_4BYTE_ABGR);
        for(int x=0; x<100; x++)
            for(int y=0; y<100; y++)
                image.setRGB(x, y, Color.BLACK.getRGB());
        ImageScaler instance = new ImageScaler();
        instance.scalImage("\u0003", image);
    }
    /**
     * My comment regarding the last two tests:
     * Both do return an single-coloured 64x64px square images
     */
}