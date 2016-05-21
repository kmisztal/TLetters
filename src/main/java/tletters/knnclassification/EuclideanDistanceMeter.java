package tletters.knnclassification;

import java.util.Collection;
import java.util.Iterator;

/**
 * @author Piotr
 */
public class EuclideanDistanceMeter<VAL extends Number> implements DistanceMeter<VAL> {

    @Override
    public double distance(Collection<VAL> lhs, Collection<VAL> rhs) {
        Iterator<VAL> itLhs = lhs.iterator();
        Iterator<VAL> itRhs = rhs.iterator();
        double result = 0.0;
        double diff;
        while (itLhs.hasNext() && itRhs.hasNext()) {
            diff = itLhs.next().doubleValue() - itRhs.next().doubleValue();
            result += diff * diff;
        }
        //becouse of the fact that distances are only compared to each other in knn algorithm we can omit commputing sqrt
        //return Math.sqrt(result);
        return result;
    }

}
