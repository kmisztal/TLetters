package tletters.glyph;

public class Glyph {

    private double[] featureVector;
    private GlyphCase glyphCase;
    private GlyphType glyphType;
    private LanguageType glyphLanguage;
    private final char character;

    public enum GlyphCase {
        UPPER, LOWER
    }

    public enum GlyphType {
        DIGIT, LETTER
    }

    public Glyph(double[] featureVector, LanguageType glyphLanguage, char character) {
        this.featureVector = featureVector;
        this.glyphCase = Character.isUpperCase(character) ? GlyphCase.UPPER : GlyphCase.LOWER;
        this.glyphType = Character.isAlphabetic(character) ? GlyphType.LETTER : GlyphType.DIGIT;
        this.glyphLanguage = glyphLanguage;
        this.character = character;
        this.validateGlyph();
    }

    private void validateGlyph() {
        if (!this.isCharacterTypeAndGlyphTypeMatching()) {
            throw new IllegalArgumentException("Character type and glyphType do not match");
        }
        if (!this.isCharacterCaseAndGlyphCaseMatching()) {
            throw new IllegalArgumentException("Character case and glyphCase do not match");
        }
    }

    private boolean isCharacterTypeAndGlyphTypeMatching() {
        if (this.glyphType == GlyphType.DIGIT) {
            return Character.isDigit(this.character);
        } else if (this.glyphType == GlyphType.LETTER) {
            return Character.isLetter(this.character);
        }
        throw new IllegalArgumentException("Invalid glyph type");
    }

    private boolean isCharacterCaseAndGlyphCaseMatching() {
        if (this.glyphCase == GlyphCase.UPPER) {
            return Character.isUpperCase(this.character);
        } else if (this.glyphCase == GlyphCase.LOWER) {
            return Character.isLowerCase(this.character) || Character.isDigit(this.character);
        }
        throw new IllegalArgumentException("Invalid glyph case");
    }

    public double[] getFeatureVector() {
        return featureVector;
    }

    public void setFeatureVector(double[] featureVector) {
        this.featureVector = featureVector;
    }

    public GlyphCase getGlyphCase() {
        return glyphCase;
    }

    public void setGlyphCase(GlyphCase glyphCase) {
        this.glyphCase = glyphCase;
        this.validateGlyph();
    }

    public GlyphType getGlyphType() {
        return glyphType;
    }

    public void setGlyphType(GlyphType glyphType) {
        this.glyphType = glyphType;
        this.validateGlyph();
    }

    public LanguageType getGlyphLanguage() {
        return glyphLanguage;
    }

    public void setGlyphLanguage(LanguageType glyphLanguage) {
        this.glyphLanguage = glyphLanguage;
    }

    public char getCharacter() {
        return character;
    }

}
