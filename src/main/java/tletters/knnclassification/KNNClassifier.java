package tletters.knnclassification;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * @param <VAL>
 * @author Piotr
 */
public class KNNClassifier<VAL extends Number> implements Classifier<VAL> {

    private Collection<Collection<VAL>> descriptions;
    private Collection<Integer> classes;
    private DistanceMeter<VAL> distanceMeter;
    private int K;

    public KNNClassifier(int K) {
        this.K = K;
    }

    @Override
    public void fit(Collection<Collection<VAL>> descriptions, Collection<Integer> classes, DistanceMeter<VAL> distanceMeter) {
        this.descriptions = descriptions;
        this.classes = classes;
        this.distanceMeter = distanceMeter;
    }

    @Override
    public int predict(Collection<VAL> description) {
        SortedSet<Pair> neighbours = new TreeSet<>((Pair o1, Pair o2) -> o1.distance.compareTo(o2.distance));
        Iterator<Collection<VAL>> itDescriptions = descriptions.iterator();
        Iterator<Integer> itClasses = classes.iterator();
        double dist;
        int objClass;
        while (itDescriptions.hasNext() && itClasses.hasNext()) {
            dist = distanceMeter.distance(description, itDescriptions.next());
            objClass = itClasses.next();
            if (neighbours.size() < K) {
                neighbours.add(new Pair(dist, objClass));
            } else if (dist < neighbours.last().distance) {
                neighbours.add(new Pair(dist, objClass));
                //can be ommited, but it changes complexity from O(n*logn) to O(n*logK)
                neighbours.remove(neighbours.last());
            }
        }
        // count classes
        HashMap<Integer, Integer> classCounter = new HashMap<>();
        for (Pair p : neighbours) {
            Integer valueWrapper = classCounter.get(p.objClass);
            if (valueWrapper == null) {
                classCounter.put(p.objClass, new Integer(1));
            } else {
                valueWrapper++;
            }
        }
        // find most common class
        int maxCount = 1;
        int commonClass = neighbours.first().objClass;
        for (Pair p : neighbours) {
            Integer valueWrapper = classCounter.get(p.objClass);
            if (valueWrapper > maxCount) {
                maxCount = valueWrapper;
                commonClass = p.objClass;
            }
        }
        // might be different due to solving cases of equal number of neighbour classes
        return commonClass;
    }

    private class Pair {

        public Double distance;
        public int objClass;

        public Pair(double distance, int objClass) {
            this.distance = distance;
            this.objClass = objClass;
        }

    }

}
