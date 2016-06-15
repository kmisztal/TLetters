package tletters.algorithmcompare;

import tletters.featureextraction.ExtractionAlgorithm;
import tletters.featureextraction.ZernikeMoments;
import tletters.featureextraction.Zoning;
import tletters.knnclassification.Classifier;
import tletters.knnclassification.KNNClassifier;

import java.awt.*;
import java.util.*;

public class ComparatorMain {
    public static void main(String[] args) {
        /*gather fonts*/
        java.util.List<Font> fonts = new ArrayList<>();
        fonts.add(new Font("Arial", Font.PLAIN, 24));
        fonts.add(new Font("Tahoma", Font.PLAIN, 24));
        fonts.add(new Font("Verdana", Font.PLAIN, 24));

        /*gather algorithms*/
        Map<String, ExtractionAlgorithm> algorithmMap = new HashMap<>();
        algorithmMap.put("zoning", new Zoning());
        algorithmMap.put("zernike",new ZernikeMoments());

        /*gather classifiers */
        Map<String, Classifier> classifierMap = new HashMap<>();
        classifierMap.put("KNNClassifier (2)", new KNNClassifier<Double>(2));
        classifierMap.put("KNNClassifier (5)", new KNNClassifier<Double>(5));

        GlyphListProvider glyphListProvider = new GlyphListProviderImpl();
        AlgorithmComparator algorithmComparator = new AlgorithmComparator(algorithmMap, fonts, glyphListProvider);
//        algorithmComparator.setDataOptions(false,"[a-zA-Z]{3,8}",50);
        algorithmComparator.setDataOptions(true, "[a-zA-Z]{3,8}", 50);

        /*preparationTime test */
        System.out.println("Preparation time test:");
        for (Map.Entry<String, Long> entry : algorithmComparator.comparePreparation().entrySet()) {
            System.out.println("* " + entry.getKey() + " - " + entry.getValue() + " ms");
        }
        System.out.println();
        /*recognition test with given classifiers */
        for(Map.Entry<String,Classifier> classifierEntry :classifierMap.entrySet()){
            System.out.println("Recognition using classifier \""+classifierEntry.getKey()+"\" time tests:");
            for (Map.Entry<String, RecognitionTestResult> entry :
                    algorithmComparator.compareRecognitionUsingOneClassifier(classifierEntry.getValue()).entrySet()) {
                System.out.println("* " + entry.getKey() + " - " +
                        entry.getValue().getTime() + " ms with success rate " +
                        entry.getValue().getSuccessRate() * 100 + " percent");
            }
        }

    }
}
