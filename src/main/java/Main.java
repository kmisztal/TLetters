import tletters.featureextraction.ExtractionAlgorithm;
import tletters.featureextraction.Extractor;
import tletters.featureextraction.SqlExtractor;
import tletters.featureextraction.Zoning;
import tletters.glyph.Glyph;
import tletters.glyphclassification.GlyphClassifier;
import tletters.glyphextraction.GlyphExtractor;
import tletters.imagegeneration.ImageGenerator;
import tletters.imagescaling.ImageScaler;
import tletters.knnclassification.EuclideanDistanceMeter;
import tletters.knnclassification.KNNClassifier;

import java.awt.Font;
import java.util.List;

/**
 * Created by mariuszbeltowski on 23/04/16.
 */
public class Main {

    public static void main(String[] args) {
        Extractor extractor = new SqlExtractor("org.sqlite.JDBC", "src/main/resources/sqlite/zoning_combo");
        ImageGenerator imageGenerator = new ImageGenerator();
        Font font = new Font("Perpetua", Font.PLAIN, 16); //font unpresent in vectors database
        GlyphExtractor glyphExtractor = new GlyphExtractor();
        ImageScaler imageScaler = new ImageScaler();
        ExtractionAlgorithm zoning = new Zoning();
        List<Glyph> glyphList = extractor.load();
        GlyphClassifier glyphClassifier = new GlyphClassifier(new KNNClassifier<Double>(4), new EuclideanDistanceMeter<Double>(), glyphList);
        String text = "K r z y s z t o f M i s z t a l";
        imageGenerator.generateImage(font, 24, text, 0);
        glyphExtractor.setImage(imageGenerator.getGeneratedImage());
        glyphExtractor.scalpel().stream()
                .map(imageScaler::scalImage)
                .map(zoning::extractFeatures)
                .map(glyphClassifier::classify)
                .forEach(System.out::print);
    }

}
