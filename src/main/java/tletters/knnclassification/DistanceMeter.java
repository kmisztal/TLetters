package tletters.knnclassification;

import java.util.Collection;

/**
 * @param <VAL>
 * @author Piotr
 */
public interface DistanceMeter<VAL extends Number> {

    double distance(Collection<VAL> lhs, Collection<VAL> rhs);

}
