package tletters.algorithmcompare;

import tletters.featureextraction.SqlExtractor;
import tletters.glyph.Glyph;

import java.util.List;

public class GlyphListProviderImpl implements GlyphListProvider {
    public List<Glyph> provide (String extractionAlgorithmName){
        return new SqlExtractor("org.sqlite.JDBC","src/main/resources/sqlite/"+extractionAlgorithmName).load();
    }
}
