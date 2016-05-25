package tletters.glyphextraction;

import tletters.image.ImageUtils;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;

public class GlyphExtractor {

    private List<BufferedImage> letters;

    private BufferedImage image;
    private List<BufferedImage> lines;
    private List<BufferedImage> joinedLines;

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    public List<BufferedImage> scalpel() {
        cutLineWithText();
        updateLinesWithAverageHeight();
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

    private void updateLinesWithAverageHeight() {
        OptionalDouble average = lines.stream().mapToDouble(BufferedImage::getHeight).average();
        joinLinesWhenNeeded(average.getAsDouble());
    }

    private void joinLinesWhenNeeded(double average) {
        double halfAverage = average / 2;
        joinedLines = new ArrayList<>();
        for (int i = 0; i < lines.size(); i++) {
            BufferedImage thisLine = lines.get(i);
            if (shouldLineBeJoined(halfAverage, thisLine) && nextLineExists(i)) {
                BufferedImage nextLine = lines.get(i + 1);
                BufferedImage joinedLine = joinLines(thisLine, nextLine);
                i++;
                joinedLines.add(joinedLine);
            } else {
                joinedLines.add(thisLine);
            }
        }
    }

    private BufferedImage joinLines(BufferedImage thisLine, BufferedImage nextLine) {
        BufferedImage joinedLine = new BufferedImage(thisLine.getWidth() + nextLine.getWidth(),
                thisLine.getHeight() + nextLine.getHeight(), thisLine.getType());

        Graphics joinedLinesGraphics = joinedLine.getGraphics();
        joinedLinesGraphics.drawImage(thisLine, 0, 0, null);
        joinedLinesGraphics.drawImage(nextLine, 0, thisLine.getHeight(), null);
        return joinedLine;
    }

    private boolean nextLineExists(int i) {
        return i + 1 < lines.size();
    }

    private boolean shouldLineBeJoined(double halfAverage, BufferedImage thisLine) {
        return thisLine.getHeight() < halfAverage;
    }

    private void cutLettersWithLines() {
        letters = new ArrayList<>();
        boolean add;
        for (int i = 0; i < joinedLines.size(); i++) {
            int point = 0;
            add = false;
            for (int x = 0; x < joinedLines.get(i).getWidth(); x++) {
                for (int y = 0; y < joinedLines.get(i).getHeight(); y++) {
                    if (ImageUtils.isBlack(joinedLines.get(i), x, y)) {
                        if (x == joinedLines.get(i).getWidth() - 1) {
                            add = true;
                        }
                        break;
                    }
                    if (x != 0 && joinedLines.get(i).getHeight() - 1 == y) {
                        if (x - point > 1) {
                            letters.add(joinedLines.get(i).getSubimage(point, 0, x - point + 1, joinedLines.get(i).getHeight()));
                        }
                        point = x;
                    }
                }
            }
            if (add) {
                letters.add(joinedLines.get(i).getSubimage(point, 0, joinedLines.get(i).getWidth() - point, joinedLines.get(i).getHeight()));
            }
        }
    }

}
