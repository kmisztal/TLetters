package tletters.featureextraction;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PropertiesExtractor extends Extractor {

    private static final String DEFAULT_DIR = "src/main/resources/properties/";
    private static final String DEFAULT_FILENAME = "features";
    private String path;

    public PropertiesExtractor() {
        super();
        path = DEFAULT_DIR + DEFAULT_FILENAME;
    }

    public PropertiesExtractor(String file) {
        super();
        path = DEFAULT_DIR + file;
    }

    @Override
    public Properties load() {
        try (FileInputStream in = new FileInputStream(path)) {
            properties.load(in);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(PropertiesExtractor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(PropertiesExtractor.class.getName()).log(Level.SEVERE, null, ex);
        }
        return properties;
    }

    @Override
    public void save(String featureName, double[] features) {
        properties.put(featureName, features);
        try (FileOutputStream out = new FileOutputStream(path)) {
            properties.store(out, null);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(PropertiesExtractor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(PropertiesExtractor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
