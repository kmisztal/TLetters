package tletters.algorithmcompare;

import tletters.featureextraction.SqlExtractor;
import tletters.glyph.Glyph;

import java.util.List;

public class GlyphListProviderImpl implements GlyphListProvider {
    public List<Glyph> provide (String extractionAlorithmName){
        return new SqlExtractor("org.sqlite.JDBC","src/main/resources/sqlite/"+extractionAlorithmName).load();
    }
}
