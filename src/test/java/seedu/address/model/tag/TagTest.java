package seedu.address.model.tag;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import seedu.address.testutil.Assert;

public class TagTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> new Tag(null));
    }

    @Test
    public void constructor_invalidTagName_throwsIllegalArgumentException() {
        String invalidTagName = "";
        Assert.assertThrows(IllegalArgumentException.class, () -> new Tag(invalidTagName));
    }

    @Test
    public void isValidTagName() {
        // null tag name
        Assert.assertThrows(NullPointerException.class, () -> Tag.isValidTagName(null));

        // invalid Note
        assertFalse(Tag.isValidTagName("")); // empty string
        assertFalse(Tag.isValidTagName(" ")); // spaces only
        assertFalse(Tag.isValidTagName("@")); // non-alphanumeric characters
        assertFalse(Tag.isValidTagName("is this a bug")); // multiple words
        assertFalse(Tag.isValidTagName("boss 3")); // invalid priority
        assertFalse(Tag.isValidTagName("boss 0")); // invalid priority


        // valid Note
        assertTrue(Tag.isValidTagName("boss")); // alphabets only
        assertTrue(Tag.isValidTagName("5")); // single number
        assertTrue(Tag.isValidTagName("556")); // numbers only
        assertTrue(Tag.isValidTagName("iStHiSaBuG")); // capitalization
        assertTrue(Tag.isValidTagName("iStHiSaBuG123")); // alphabets with numbers

        assertTrue(Tag.isValidTagName("iStHiSaBuG123 1")); // with priority medium
        assertTrue(Tag.isValidTagName("iStHiSaBuG123 2")); // with priority high
        assertTrue(Tag.isValidTagName("123 2")); // numbers only with priority high
    }

}
