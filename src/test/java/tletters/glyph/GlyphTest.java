package tletters.glyph;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.Assert.assertTrue;

public class GlyphTest {

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Test
    public void testWillThrowExceptionWhenGlyphCaseDoesNotMatchCharacterCase() {
        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("Character case and glyphCase do not match");
        new Glyph(new double[0], LanguageType.GENERAL_PL, 'c').setGlyphCase(Glyph.GlyphCase.UPPER);
    }

    @Test
    public void testWillThrowExceptionWhenGlyphTypeDoesNotMatchCharacterType() {
        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("Character type and glyphType do not match");
        new Glyph(new double[0], LanguageType.GENERAL_PL, '1').setGlyphType(Glyph.GlyphType.LETTER);
    }

    @Test
    public void testWillThrowExceptionWhenGlyphTypeIsDigitAndGlyphCaseIsLower() {
        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("Character case and glyphCase do not match");
        new Glyph(new double[0], LanguageType.GENERAL_PL, '1').setGlyphCase(Glyph.GlyphCase.UPPER);
    }

    @Test
    public void testCanBeConstructedWithoutExceptionsFoValidTypeCaseCombination() {
        new Glyph(new double[0], LanguageType.GENERAL_PL, '7');
        new Glyph(new double[0], LanguageType.GENERAL_PL, 'a');
        new Glyph(new double[0], LanguageType.GENERAL_PL, 'A');
    }

    @Test
    public void testWillThrowExceptionWhenTryingToMakeDigitUppercase() {
        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("Character case and glyphCase do not match");
        new Glyph(new double[0], LanguageType.GENERAL_PL, '2').setGlyphCase(Glyph.GlyphCase.UPPER);
    }

    @Test
    public void testCanBeConstructedWithoutExceptionsForLowercaseDigit() {
        new Glyph(new double[0], LanguageType.GENERAL_PL, '9').setGlyphCase(Glyph.GlyphCase.LOWER);
    }

    @Test
    public void testWillReturnTrueWhenCheckingGlyphTypeAndCharacterTypeForNewGlyphObject() {

        try{
            Method testedMethod = Glyph.class.getDeclaredMethod("isCharacterTypeAndGlyphTypeMatching");
            testedMethod.setAccessible(true);
            Glyph g = new Glyph(new double[0], LanguageType.GENERAL_PL, '9');
            testedMethod.invoke(g);
            Boolean b;
            b = (Boolean)testedMethod.invoke(g);
            assertTrue(b);

        }catch(NoSuchMethodException e){
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testWillReturnTrueWhenCheckingGlyphCaseAndCharacterCaseForNewGlyphObject() {

        try{
            Method testedMethod = Glyph.class.getDeclaredMethod("isCharacterCaseAndGlyphCaseMatching");
            testedMethod.setAccessible(true);
            Glyph g = new Glyph(new double[0], LanguageType.GENERAL_PL, 'q');
            testedMethod.invoke(g);
            Boolean b;
            b = (Boolean)testedMethod.invoke(g);
            assertTrue(b);

        }catch(NoSuchMethodException e){
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testWillThrowExceptionWhenCharacterIsNotLetterNorDigit() {

        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("Character type and glyphType do not match");
        new Glyph(new double[0], LanguageType.GENERAL_PL, '~');

    }

}
