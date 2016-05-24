/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tletters.knnclassification;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Piotr
 */
public class KNNClassifierTest {

    public KNNClassifierTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }


    /**
     * GlyphClassifier of predict method, of class KNNClassifier.
     */
    @Test
    public void testPredict() {
        System.out.println("KNNClassifier.predict glyphclassification");
        {
            DistanceMeter<Double> dm = new EuclideanDistanceMeter();

            Classifier<Double> classifier = new KNNClassifier<>(5);
            Double[] fv = {1., 2., 3.};
            ArrayList<Double> fvArr = new ArrayList<>(Arrays.asList(fv));
            List<Collection<Double>> descriptions = new ArrayList<>();
            descriptions.add(fvArr);

            Integer[] cl = {1};
            ArrayList<Integer> classes = new ArrayList<>(Arrays.asList(cl));

            classifier.fit(descriptions, classes, dm);

            Double[] fv_q = {1., 2., 2.};
            ArrayList<Double> fv_question = new ArrayList<>(Arrays.asList(fv_q));

            Integer qclass = classifier.predict(fv_question);

            Integer expResult = 1;
            assertEquals(expResult, qclass);
        }

        {
            DistanceMeter<Double> dm = new EuclideanDistanceMeter();

            Classifier<Double> classifier = new KNNClassifier<>(2);
            List<Collection<Double>> descriptions = new ArrayList<>();

            Double[] fv = {1., 2., 3.};
            ArrayList<Double> fvArr = new ArrayList<>(Arrays.asList(fv));
            descriptions.add(fvArr);
            Double[] fv2 = {3., 3., 3.};
            ArrayList<Double> fvArr2 = new ArrayList<>(Arrays.asList(fv2));
            descriptions.add(fvArr2);
            Double[] fv3 = {1., 1., 1.};
            ArrayList<Double> fvArr3 = new ArrayList<>(Arrays.asList(fv3));
            descriptions.add(fvArr3);
            Double[] fv4 = {0., 1., 0.};
            ArrayList<Double> fvArr4 = new ArrayList<>(Arrays.asList(fv4));
            descriptions.add(fvArr4);

            Integer[] cl = {1, 2, 3, 3};
            ArrayList<Integer> classes = new ArrayList<>(Arrays.asList(cl));

            classifier.fit(descriptions, classes, dm);

            Double[] fv_q = {1., 0., 1.};
            ArrayList<Double> fv_question = new ArrayList<>(Arrays.asList(fv_q));

            Integer qclass = classifier.predict(fv_question);

            Integer expResult = 3;
            assertEquals(expResult, qclass);
        }

    }

}
