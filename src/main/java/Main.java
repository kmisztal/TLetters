import tletters.featureextraction.Zoning;
import tletters.glyph.Glyph;
import tletters.glyph.LanguageType;
import tletters.glyphclassification.GlyphClassifier;
import tletters.glyphextraction.GlyphExtractor;
import tletters.imagegeneration.ImageGenerator;
import tletters.imagescaling.ImageScaler;
import tletters.knnclassification.EuclideanDistanceMeter;
import tletters.knnclassification.KNNClassifier;

import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by mariuszbeltowski on 23/04/16.
 */
public class Main {

    public static void main(String[] args) {
        ImageGenerator imageGenerator = new ImageGenerator();
        String letters = "abcdefghijklmnoprstuwyzABCDEFGHIJKLMNOPRSTUWYZ";
        Font font = new Font("Arial", Font.PLAIN, 16);
        imageGenerator.generateImage(font, 48, letters, 0);
        GlyphExtractor glyphExtractor = new GlyphExtractor();
        glyphExtractor.setImage(imageGenerator.getGeneratedImage());
        ImageScaler imageScaler = new ImageScaler();
        Zoning zoning = new Zoning();
        final int[] i = {0};
        List<Glyph> glyphList = glyphExtractor.scalpel().stream()
                .map(imageScaler::scalImage)
                .map(image -> new Glyph(zoning.extractFeatures(image), LanguageType.GENERAL_PL, letters.charAt(i[0]++)))
                .collect(Collectors.toList());

        GlyphClassifier glyphClassifier = new GlyphClassifier(new KNNClassifier<Double>(2), new EuclideanDistanceMeter<Double>(), glyphList);
        String text = "Krzysztof Misztal";
        imageGenerator.generateImage(font, 24, text, 0);
        glyphExtractor.setImage(imageGenerator.getGeneratedImage());
        glyphExtractor.scalpel().stream()
                .map(imageScaler::scalImage)
                .map(zoning::extractFeatures)
                .map(glyphClassifier::classify)
                .forEach(System.out::print);
    }

}
