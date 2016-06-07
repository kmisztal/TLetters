package tletters.featureextraction;

import java.awt.image.BufferedImage;
import java.util.Properties;

public abstract class Extractor {

    protected Properties properties;

    public Extractor() {
        properties = new Properties();
    }

    public void extract(ExtractionAlgorithm algorithm, BufferedImage image, String featureName) {
        double[] features = algorithm.extractFeatures(image);
        save(featureName, features);
    }

    abstract Properties load();

    abstract void save(String featureName, double[] features);

}