package tletters.glyphextraction;

import tletters.image.ImageUtils;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class GlyphExtractor {

    public List<BufferedImage> letters;

    private BufferedImage image;
    public List<BufferedImage> lines;

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    public List<BufferedImage> scalpel() {
        cutLineWithText();
        cutLettersWithLines();
        return letters;
    }

    private void cutLineWithText() {
        lines = new ArrayList<>();
        int point = 0;
        boolean add = false;
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                if (ImageUtils.isBlack(image, x, y)) {
                    if (y == image.getHeight() - 1) {
                        add = true;
                    }
                    break;
                }
                if (image.getWidth() - 1 == x && y != 0) {
                    if (y - point > 1) {
                        lines.add(image.getSubimage(0, point, image.getWidth(), y - point + 1));
                    }
                    point = y;
                }
            }
        }
        if (add) {
            lines.add(image.getSubimage(0, point, image.getWidth(), image.getHeight() - point));
        }
    }

    private void cutLettersWithLines() {
        letters = new ArrayList<>();
        boolean add;
        for (int i = 0; i < lines.size(); i++) {
            int point = 0;
            add = false;
            for (int x = 0; x < lines.get(i).getWidth(); x++) {
                for (int y = 0; y < lines.get(i).getHeight(); y++) {
                    if (ImageUtils.isBlack(lines.get(i), x, y)) {
                        if (x == lines.get(i).getWidth() - 1) {
                            add = true;
                        }
                        break;
                    }
                    if (x != 0 && lines.get(i).getHeight() - 1 == y) {
                        if (x - point > 1) {
                            letters.add(lines.get(i).getSubimage(point, 0, x - point + 1, lines.get(i).getHeight()));
                        }
                        point = x;
                    }
                }
            }
            if (add) {
                letters.add(lines.get(i).getSubimage(point, 0, lines.get(i).getWidth() - point, lines.get(i).getHeight()));
            }
        }
    }

}
