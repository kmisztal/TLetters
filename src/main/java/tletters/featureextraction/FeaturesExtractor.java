package tletters.featureextraction;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Properties;

public class FeaturesExtractor {

    private static final String DEFAULT_DIR = "src/main/resources/properties/";
    private Properties properties;
    private String file;

    public FeaturesExtractor() throws IOException {
        properties = new Properties();
        file = DEFAULT_DIR + "defaultProperties";
        try (FileInputStream in = new FileInputStream(file)) {
            properties.load(in);
        } catch (FileNotFoundException e) {
            new File(file).createNewFile();
        }
    }

    public FeaturesExtractor(String file) throws IOException {
        this.file = DEFAULT_DIR + file;
        properties = new Properties();
        try (FileInputStream in = new FileInputStream(file)) {
            properties.load(in);
        } catch (FileNotFoundException e) {
            new File(this.file).createNewFile();
        }
    }

    public void extractFeatures(ExtractionAlgorithm algorithm, BufferedImage image, String name) throws IOException {
        double[] features = algorithm.extractFeatures(image);
        saveFeatures(name, features);
    }

    public void saveFeatures(String name, double[] features) throws IOException {
        properties.put(name, features);
        FileOutputStream out = new FileOutputStream(file);
        properties.store(out, null);
        out.close();
    }

}
