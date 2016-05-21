package tletters.font;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * Created by mdomagala on 4/25/16.
 */
public class FontChecker {

    public static final char[] SPECIAL_CHARS = {'\u0261', '\u0260', '\u0263', '\u0262', '\u0281', '\u0280', '\u0322',
            '\u0321', '\u0324', '\u0323', '\u0243', '\u0211', '\u0347', '\u0346', '\u0378', '\u0377', '\u0380', '\u0379'};

    public static boolean checkFont(String name) throws IOException, FontFormatException {
        Font font = Font.createFont(Font.TRUETYPE_FONT, new FileInputStream("src/main/resources/fonts/" + name));
        for (char c : SPECIAL_CHARS) {
            if (!font.canDisplay(c)) {
                return false;
            }
        }
        return true;
    }

    public static void checkAllFonts() throws IOException, FontFormatException {
        final String newPath = "src/main/resources/fontsWithPolishCharacters/";
        final String startPath = "src/main/resources/fonts/";
        Path destination, source;
        String fileName;
        if (!Files.exists(Paths.get(newPath))) {
            Files.createDirectory(Paths.get(newPath));
        }
        final DirectoryStream<Path> paths = Files.newDirectoryStream(Paths.get(startPath),
                (Path entry) -> entry.getFileName().toString().endsWith(".ttf") || entry.getFileName().toString().endsWith(".otf"));
        for (Path path : paths) {
            fileName = path.getFileName().toString();
            if (checkFont(fileName)) {
                source = Paths.get(startPath + fileName);
                destination = Paths.get(newPath + fileName);
                Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);
            }
        }
    }

}
