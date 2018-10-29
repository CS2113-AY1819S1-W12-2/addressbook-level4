//@@author lekoook
package seedu.address.model.autocomplete;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import seedu.address.logic.parser.CliSyntax;

public class AutoCompleteParserPairTest {

    @Test
    public void constructor_validArgs_success() {
        AutoCompleteParserPair pair = new AutoCompleteParserPair(CliSyntax.PREFIX_NAME, "  john");
        assertEquals(CliSyntax.PREFIX_NAME, pair.predictionType);
        assertEquals("john", pair.prefixValue);

        // argument contains only white space
        AutoCompleteParserPair emptyPair = new AutoCompleteParserPair(CliSyntax.PREFIX_NAME, "    ");
        assertEquals(CliSyntax.PREFIX_NAME, emptyPair.predictionType);
        assertEquals("    ", emptyPair.prefixValue);
    }

    @Test
    public void equals() {
        AutoCompleteParserPair namePair = new AutoCompleteParserPair(CliSyntax.PREFIX_NAME, "tom");
        AutoCompleteParserPair namePairCopy = new AutoCompleteParserPair(CliSyntax.PREFIX_NAME, "tom");
        AutoCompleteParserPair phonePair = new AutoCompleteParserPair(CliSyntax.PREFIX_PHONE, "999");
        AutoCompleteParserPair phonePairCopy = new AutoCompleteParserPair(CliSyntax.PREFIX_PHONE, "999");
        AutoCompleteParserPair addrPair = new AutoCompleteParserPair(CliSyntax.PREFIX_ADDRESS, "Mars");
        AutoCompleteParserPair addrPairCopy = new AutoCompleteParserPair(CliSyntax.PREFIX_ADDRESS, "Mars");

        // Matching pairs
        assertTrue(namePair.equals(namePairCopy));
        assertTrue(phonePair.equals(phonePairCopy));
        assertTrue(addrPair.equals(addrPairCopy));

        // Non matching pairs
        assertFalse(namePair.equals(phonePair));
        assertFalse(namePair.equals(addrPair));
        assertFalse(phonePair.equals(addrPair));
    }
}
