package seedu.address.model.person;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import seedu.address.testutil.Assert;


public class PositionTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> new Position(null));
    }

    @Test
    public void constructor_invalidPosition_throwsIllegalArgumentException() {
        String invalidPosition = "";
        Assert.assertThrows(IllegalArgumentException.class, () -> new Position(invalidPosition));
    }

    @Test
    public void isValidPosition() {
        // null Position
        Assert.assertThrows(NullPointerException.class, () -> Position.isValidPosition(null));

        // invalid Position
        assertFalse(Position.isValidPosition("")); // empty string
        assertFalse(Position.isValidPosition(" ")); // spaces only
        assertFalse(Position.isValidPosition("@")); // non-alphanumeric characters
        assertFalse(Position.isValidPosition("Boss^")); // contains non-alphanumeric characters


        // valid Position
        assertTrue(Position.isValidPosition("boss")); // alphabets only
        assertTrue(Position.isValidPosition("578")); // numbers only
        assertTrue(Position.isValidPosition("boss 5")); // alphabets with numbers
        assertTrue(Position.isValidPosition("Toilet Cleaner")); // with capital letters
        assertTrue(Position.isValidPosition("Toilet CLeaner the 2nd of the hundredth floor")); // long floats
    }
}
