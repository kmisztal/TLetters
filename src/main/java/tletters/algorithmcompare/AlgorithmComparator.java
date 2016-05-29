package tletters.algorithmcompare;

import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import tletters.featureextraction.ExtractionAlgorithm;
import tletters.glyph.Glyph;
import tletters.glyph.LanguageType;
import tletters.glyphclassification.GlyphClassifier;
import tletters.glyphextraction.GlyphExtractor;
import tletters.imagegeneration.ImageGenerator;
import tletters.imagescaling.ImageScaler;
import tletters.knnclassification.Classifier;
import tletters.knnclassification.EuclideanDistanceMeter;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AlgorithmComparator {

    public static final int DEFAULT_DATA_AMOUNT = 20;
    public static String ALPHABET = "abcdefghijklmnoprstuwyzABCDEFGHIJKLMNOPRSTUWYZ";
    public static String DEFAULT_REGEX = "[a-zA-z]{3,10}";
    public static boolean DEFAULT_ISNAMES = true;

    private final Map<String, ExtractionAlgorithm> algorithmMap;
    private final List<Font> fonts;
    private List<Glyph> glyphList;
    private int dataAmount = DEFAULT_DATA_AMOUNT;
    private String regex = DEFAULT_REGEX;
    private boolean isNames = DEFAULT_ISNAMES;

    public AlgorithmComparator(
            Map<String, ExtractionAlgorithm> algorithmMap,
            List<Font> fonts) {
        this.algorithmMap = algorithmMap;
        this.fonts = fonts;
    }

    /**
     * @param isNames    set true if you want to generate names (default in class fields)
     * @param regex      if names==false provide a regex (default in class fields)
     * @param dataAmount number of text tests per font
     */
    public void setDataOptions(boolean isNames, String regex, int dataAmount) {
        this.isNames = isNames;
        this.regex = regex;
        this.dataAmount = dataAmount;
    }

    /**
     * Tests speed of preparing font data.
     * Needs to be run before recognition
     *
     * @return map: key - algorithm name, value time in ms
     */
    public Map<String, Long> comparePreparation() {
        Map<String, Long> times = new HashMap<>();
        for (Map.Entry<String, ExtractionAlgorithm> algorithmEntry : algorithmMap.entrySet()) {
            Long startTime = System.currentTimeMillis();
            fonts.forEach(font -> processOnePreparation(font, algorithmEntry.getValue()));
            times.put(algorithmEntry.getKey(), System.currentTimeMillis() - startTime);
        }
        return times;
    }

    /**
     * Tests speed of recognition using different algorithms, but only one classifier
     *
     * @return map: key - algorithm name, value - RecognitionTestResult
     */
    public Map<String, RecognitionTestResult> compareRecognitionUsingOneClassifier(Classifier classifier) {
        List<String> testData = generateTestTexts();
        /*uncomment to see test data
        System.out.println(Arrays.toString(testData.toArray()));
        */
        Map<String, RecognitionTestResult> results = new HashMap<>();
        for (Map.Entry<String, ExtractionAlgorithm> algorithmEntry : algorithmMap.entrySet()) {
            Long startTime = System.currentTimeMillis();
            int successes = 0;
            for (Font font : fonts) {
                for (String text : testData) {
                    String resultText = recognize(algorithmEntry.getValue(), classifier, font, text, 24);
                    if (resultText.equals(text)) successes++;
                }
            }
            Float successRate = (float) successes / (testData.size() * fonts.size());
            results.put(algorithmEntry.getKey(),
                    new RecognitionTestResult(System.currentTimeMillis() - startTime, successRate));
        }
        return results;
    }

    public void processOnePreparation(Font font, ExtractionAlgorithm algorithm) {
        ImageGenerator imageGenerator = new ImageGenerator();
        imageGenerator.generateImage(font, 48, ALPHABET, 0);
        GlyphExtractor glyphExtractor = new GlyphExtractor();
        glyphExtractor.setImage(imageGenerator.getGeneratedImage());
        ImageScaler imageScaler = new ImageScaler();
        final int[] i = {0};
        glyphList = glyphExtractor.scalpel().stream()
                .map(imageScaler::scalImage)
                .map(image -> new Glyph(algorithm.extractFeatures(image), LanguageType.GENERAL_PL, ALPHABET.charAt(i[0]++)))
                .collect(Collectors.toList());
    }

    public String recognize(ExtractionAlgorithm algorithm, Classifier classifier,
                            Font font, String text, float fontSize) {
        return recognize(algorithm, classifier, font, text, fontSize, 0);
    }

    public String recognize(ExtractionAlgorithm algorithm, Classifier classifier,
                            Font font, String text, float fontSize, float noisePercentage) {
        GlyphClassifier glyphClassifier = new GlyphClassifier(classifier, new EuclideanDistanceMeter<Double>(), glyphList);
        ImageGenerator imageGenerator = new ImageGenerator();
        GlyphExtractor glyphExtractor = new GlyphExtractor();
        ImageScaler imageScaler = new ImageScaler();
        imageGenerator.generateImage(font, fontSize, text, noisePercentage);
        glyphExtractor.setImage(imageGenerator.getGeneratedImage());
        StringBuilder result = new StringBuilder();
        glyphExtractor.scalpel().stream()
                .map(imageScaler::scalImage)
                .map(algorithm::extractFeatures)
                .map(glyphClassifier::classify)
                .forEach(result::append);
        return result.toString();
    }

    private List<String> generateTestTexts() {
        if (!isNames) {
            Faker faker = new Faker();
            return Stream.generate(() -> faker.regexify(regex)).limit(dataAmount).collect(Collectors.toList());
        } else {
            Name name = new Faker().name();
            return Stream.generate(name::firstName).limit(dataAmount).collect(Collectors.toList());
        }
    }
}