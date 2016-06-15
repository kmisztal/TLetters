package tletters.algorithmcompare;

import tletters.glyph.Glyph;

import java.util.List;

public interface GlyphListProvider {
    public List<Glyph> provide (String extractionAlorithmName);
}
