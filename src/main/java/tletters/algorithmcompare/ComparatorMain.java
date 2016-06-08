package tletters.algorithmcompare;

import tletters.featureextraction.ExtractionAlgorithm;
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

        /*gather algorithms*/
        Map<String, ExtractionAlgorithm> algorithmMap = new HashMap<>();
        Zoning zoning = new Zoning();
        algorithmMap.put("Zoning", zoning);

        /*gather classifiers */
        Map<String, Classifier> classifierMap = new HashMap<>();
        KNNClassifier knnClassifier = new KNNClassifier<Double>(2);
        classifierMap.put("KNNClassifier", knnClassifier);

        AlgorithmComparator algorithmComparator = new AlgorithmComparator(algorithmMap, fonts);
//        algorithmComparator.setDataOptions(false,"[a-zA-Z]{3,8}",50);
        algorithmComparator.setDataOptions(true, "[a-zA-Z]{3,8}", 50);

        /*preparationTime test */
        System.out.println("Preparation time test:");
        for (Map.Entry<String, Long> entry : algorithmComparator.comparePreparation().entrySet()) {
            System.out.println("* " + entry.getKey() + " - " + entry.getValue() + " ms");
        }

        /*recognition test with one classifier*/
        System.out.println("Recognition using one classifier time tests:");
        for (Map.Entry<String, RecognitionTestResult> entry :
                algorithmComparator.compareRecognitionUsingOneClassifier(classifierMap.get("KNNClassifier")).entrySet()) {
            System.out.println("* " + entry.getKey() + " - " +
                    entry.getValue().getTime() + " ms with success rate " +
                    entry.getValue().getSuccessRate() * 100 + " percent");
        }
    }
}
