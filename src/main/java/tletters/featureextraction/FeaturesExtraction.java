package tletters.featureextraction;

import tletters.glyph.Glyph;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Optional;

public class FeaturesExtraction {

    private Extractor extractor;
    private ExtractionAlgorithm algorithm;

    public FeaturesExtraction(Extractor extractor, ExtractionAlgorithm algorithm) {
        this.extractor = extractor;
        this.algorithm = algorithm;
    }

    public void extractSave(BufferedImage image, String featureName) {
        extractor.extract(algorithm, image, featureName);
    }

    public Optional<List<Glyph>> loadExtracted() {
        Optional<List<Glyph>> features = Optional.ofNullable(extractor.load());
        return features;
    }

}
