//@@author LowGinWee
package seedu.address.model.person;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import seedu.address.testutil.Assert;

public class NoteTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> new Note(null));
    }

    @Test
    public void constructor_invalidNote_throwsIllegalArgumentException() {
        String invalidPosition = "";
        Assert.assertThrows(IllegalArgumentException.class, () -> new Note(invalidPosition));
    }

    @Test
    public void isValidNote() {
        // null Note
        Assert.assertThrows(NullPointerException.class, () -> Note.isValidNote(null));

        // invalid Note
        assertFalse(Note.isValidNote("")); // empty string
        assertFalse(Note.isValidNote(" ")); // spaces only
        assertFalse(Note.isValidNote("@")); // non-alphanumeric characters (excluding fullstops)
        assertFalse(Note.isValidNote("is this a bug?")); // contains non-alphanumeric characters


        // valid Note
        assertTrue(Note.isValidNote("boss")); // alphabets only
        assertTrue(Note.isValidNote("578")); // numbers only
        assertTrue(Note.isValidNote("...")); // fullstops only
        assertTrue(Note.isValidNote("boss 5.")); // alphabets with numbers and full stop
        assertTrue(Note.isValidNote("Toilet Cleaner")); // with capital letters
        assertTrue(Note.isValidNote("Toilet CLeaner ....     the 2nd of the hundredth floor...")); // long string
    }
}
