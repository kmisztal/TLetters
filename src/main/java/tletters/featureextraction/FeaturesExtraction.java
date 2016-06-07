package tletters.featureextraction;

import java.awt.image.BufferedImage;
import java.util.Optional;
import java.util.Properties;

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

    public Optional<Properties> loadExtracted() {
        Optional<Properties> features = Optional.ofNullable(extractor.load());
        return features;
    }

}
