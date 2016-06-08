package tletters.featureextraction;

import tletters.glyph.Glyph;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public abstract class Extractor {

    protected List<Glyph> glyphs;

    public Extractor() {
        glyphs = new ArrayList<>();
    }

    public void extract(ExtractionAlgorithm algorithm, BufferedImage image, String featureName) {
        double[] features = algorithm.extractFeatures(image);
        save(featureName, features);
    }

    public abstract List<Glyph> load();

    public abstract void save(String featureName, double[] features);

}