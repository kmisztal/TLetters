package tletters.featureextraction;

import java.util.Arrays;

public class Features {

    private int id;
    private String name;
    private double[] features;

    public Features() {
    }

    public Features(int id, String featureName, double[] features) {
        this.id = id;
        this.name = featureName;
        this.features = features;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double[] getFeatures() {
        return features;
    }

    public void setFeatures(double[] features) {
        this.features = features;
    }

    @Override
    public String toString() {
        return "[" + id + "] [" + name + "] [" +
                Arrays.toString(features).replace('[', ' ').replace(']', ' ').replace(',', ' ').trim() +"]";
    }
}
