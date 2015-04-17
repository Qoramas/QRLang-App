package qor.qrlang.tests;

import junit.framework.TestCase;

import qor.qrlang.Decompressor;

/**
 * Created by user on 17/04/2015.
 */
public class DecompressorTest extends TestCase {

    /**
     * Tests a valid compression of a QRLang Game
     */
    public void testValidDecompression(){
        Decompressor d = new Decompressor();

        String before = "DR.UP.";
        String after = " ;draw = function(){ }\n ;update = function(){ }\n";

        assertEquals(d.decompress(before), after);
    }

    /**
     * Tests various invalid compressions of a QRLang Game
     */
    public void testInvalidDecompression(){
        Decompressor d = new Decompressor();

        String invalidSymbol = "DR.UP._";
        String missingDraw = "UP.";
        String missingUpdate = "DR.";
        String badCurlyBracketCount = "DR.UP..";
        String badBracketCount = "DR.UP.LBRBLB";

        String errorBefore = " ;draw = function(){ ;Android.write('";
        String errorAfter = "',10,10,7) }\n ;update = function(){ }\n";

        assertEquals(d.decompress(invalidSymbol), errorBefore + "INVALID CHARACTER" + errorAfter);
        assertEquals(d.decompress(missingDraw), errorBefore + "NO DRAW METHOD" + errorAfter);
        assertEquals(d.decompress(missingUpdate), errorBefore + "NO UPDATE METHOD" + errorAfter);
        assertEquals(d.decompress(badCurlyBracketCount), errorBefore + "MISSING CURLY BRACKET" + errorAfter);
        assertEquals(d.decompress(badBracketCount), errorBefore + "MISSING BRACKET" + errorAfter);
    }


}
