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

    public static final char[] SPECIAL_CHARS = {'\u0105', '\u0104', '\u0107', '\u0106', '\u0119', '\u0118', '\u0142',
            '\u0141', '\u0144', '\u0143', '\u00F3', '\u00D3', '\u015B', '\u015A', '\u017A', '\u0179', '\u017C', '\u017B'};

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
