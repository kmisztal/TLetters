/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tletters.knnclassification;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Piotr
 */
public class EuclideanDistanceMeterTest {

    public EuclideanDistanceMeterTest() {
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
     * GlyphClassifier of distance method, of class EuclideanDistanceMeter.
     */
    @Test
    public void testDistance() {
        System.out.println("distance glyphclassification");
        {
            DistanceMeter instance = new EuclideanDistanceMeter();
            double expResult = 0.0;
            Double[] fv = {1., 2., 3.};
            ArrayList<Double> fvArr = new ArrayList<>(Arrays.asList(fv));
            Double[] fv2 = {1., 2., 3.};
            ArrayList<Double> fvArr2 = new ArrayList<>(Arrays.asList(fv2));
            double result = instance.distance(fvArr, fvArr2);
            assertEquals(expResult, result, 1e-10);
        }

        {
            DistanceMeter instance = new EuclideanDistanceMeter();
            double expResult = 40.0;
            Double[] fv = {-1., 2., -3.};
            ArrayList<Double> fvArr = new ArrayList<>(Arrays.asList(fv));
            Double[] fv2 = {1., 2., 3.};
            ArrayList<Double> fvArr2 = new ArrayList<>(Arrays.asList(fv2));
            double result = instance.distance(fvArr, fvArr2);
            assertEquals(expResult, result, 1e-10);
        }
    }

}
