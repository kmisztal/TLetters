package tletters.font;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.awt.FontFormatException;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.*;


public class FontCheckerTest {

    private static final String fontsWithPolishCharactersPath = "src/main/resources/fontsWithPolishCharacters/";

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void testCheckFontWithoutPolishCharacters() throws Exception {
        FontChecker fontChecker = new FontChecker();
        String fontName = "Akura Popo.ttf";
        assertFalse(fontChecker.checkFont(fontName));
    }

    @Test
    public void testCheckFontWithPolishCharacters() throws IOException, FontFormatException {
        FontChecker fontChecker = new FontChecker();
        String fontName = "Alegreya-Black.otf";
        assertTrue(fontChecker.checkFont(fontName));
    }

    @Test
    public void testWillThrowExceptionIfNameIsWrong() throws IOException, FontFormatException {
        exception.expect(IOException.class);
        FontChecker fontChecker = new FontChecker();
        String fontName = "Wrong File Name.ttf";
        fontChecker.checkFont(fontName);

    }

    @Test
    public void testContentOfFontsWithPolishCharactersDirectory() throws IOException, FontFormatException {
        String fileName;
        FontChecker fontChecker = new FontChecker();

        final DirectoryStream<Path> paths = Files.newDirectoryStream(Paths.get(fontsWithPolishCharactersPath));

        for (Path path : paths) {
            fileName = path.getFileName().toString();
            assertTrue(fontChecker.checkFont(fileName));
        }
    }
}