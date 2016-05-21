package tletters.knnclassification;

import java.util.Collection;

/**
 * @param <VAL>
 * @author Piotr
 */
public interface Classifier<VAL extends Number> {

    void fit(Collection<Collection<VAL>> descriptions, Collection<Integer> classes, DistanceMeter<VAL> distanceMeter);

    int predict(Collection<VAL> description);

}
