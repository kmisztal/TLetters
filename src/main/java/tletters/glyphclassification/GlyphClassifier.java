package tletters.glyphclassification;

import tletters.glyph.Glyph;
import tletters.knnclassification.Classifier;
import tletters.knnclassification.DistanceMeter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class GlyphClassifier {

    private Classifier classifier;
    private DistanceMeter distanceMeter;
    private List<Glyph> glyphs;

    public GlyphClassifier(Classifier classifier, DistanceMeter distanceMeter, List<Glyph> glyphs) {
        this.classifier = classifier;
        this.distanceMeter = distanceMeter;
        this.glyphs = new ArrayList<>(glyphs);
    }

    public Classifier getClassifier() {
        return classifier;
    }

    public void setClassifier(Classifier classifier) {
        this.classifier = classifier;
    }

    public DistanceMeter getDistanceMeter() {
        return distanceMeter;
    }

    public void setDistanceMeter(DistanceMeter distanceMeter) {
        this.distanceMeter = distanceMeter;
    }

    public List<Glyph> getGlyphs() {
        return glyphs;
    }

    public void setGlyphs(List<Glyph> glyphs) {
        this.glyphs = glyphs;
    }

    private void classifierFit() {
        List<List<Double>> glyphVectors = new ArrayList<>(glyphs.size());
        List<Integer> glyphClasses = new ArrayList<>(glyphs.size());
        for (Glyph g : glyphs) {
            glyphVectors.add(new ArrayList<>(Arrays.stream(g.getFeatureVector()).boxed().collect(Collectors.toList())));
            glyphClasses.add((int) g.getCharacter());
        }
        classifier.fit(glyphVectors, glyphClasses, distanceMeter);
    }

    public char classify(double[] vector) {
        classifierFit();
        List<Double> listVector = new ArrayList<>();
        for (double d : vector) {
            listVector.add(d);
        }
        return (char) (classifier.predict(listVector));
    }

}
