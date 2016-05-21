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
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

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
        List<BufferedImage> lettersList = glyphExtractor.scalpel();
        ImageScaler imageScaler = new ImageScaler();
        List<BufferedImage> scaledLettersList = new ArrayList<>();
        for (BufferedImage image : lettersList) {
            BufferedImage scaledImage = imageScaler.scalImage(image);
            scaledLettersList.add(scaledImage);
        }
        Zoning zoning = new Zoning();
        List<Glyph> glyphList = new ArrayList<>();
        int i = 0;
        for (BufferedImage image : scaledLettersList) {
            Character character = letters.charAt(i);
            glyphList.add(new Glyph(zoning.extractFeatures(image),
                    LanguageType.GENERAL_PL,
                    character));
            i++;
        }
        GlyphClassifier glyphClassifier = new GlyphClassifier(new KNNClassifier<Double>(2), new EuclideanDistanceMeter<Double>(), glyphList);
        String text = "Krzysztof Misztal";
        imageGenerator.generateImage(font, 24, text, 0);
        glyphExtractor.setImage(imageGenerator.getGeneratedImage());
        List<BufferedImage> textLettersList = glyphExtractor.scalpel();
        List<BufferedImage> scaledTextLettersList = new ArrayList<>();
        for (BufferedImage image : textLettersList) {
            BufferedImage scaledImage = imageScaler.scalImage(image);
            scaledTextLettersList.add(scaledImage);
        }
        List<double[]> textLettersVectorList = new ArrayList<>();
        for (BufferedImage image : scaledTextLettersList) {
            textLettersVectorList.add(zoning.extractFeatures(image));
        }
        for (double[] v : textLettersVectorList) {
            System.out.print(glyphClassifier.classify(v));
        }
    }

}
