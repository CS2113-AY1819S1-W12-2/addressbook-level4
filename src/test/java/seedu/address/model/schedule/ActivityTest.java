//@@author LowGinWee
package seedu.address.model.schedule;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.junit.Test;

import seedu.address.testutil.Assert;

public class ActivityTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> new Activity(null, null));
    }

    @Test
    public void constructor_invalidActivity_throwsIllegalArgumentException() {
        String invalidActivity = "";
        Date validDate = Activity.toDate(01, 01, 2000);
        Assert.assertThrows(IllegalArgumentException.class, () -> new Activity(validDate, invalidActivity));
    }

    @Test
    public void isValidDate() {
        // null date
        Assert.assertThrows(NullPointerException.class, () -> Activity.isValidDate(null));

        // invalid date
        assertFalse(Activity.isValidDate("")); // empty string
        assertFalse(Activity.isValidDate(" ")); // spaces only
        assertFalse(Activity.isValidDate("01 /02/2000")); // spaces in between date
        assertFalse(Activity.isValidDate("30/02/2000")); // invalid date i.e. 30th feb
        assertFalse(Activity.isValidDate("01/99/2000")); // invalid month
        assertFalse(Activity.isValidDate("99/01/2000")); // invalid day

        // valid date
        assertTrue(Activity.isValidDate("01/01/2000")); //valid date
        assertTrue(Activity.isValidDate("29/02/2020")); //leap day
    }

    @Test
    public void isValidActivityName() {
        // null ActivityName
        Assert.assertThrows(NullPointerException.class, () -> Activity.isValidActivity(null));

        // invalid ActivityName
        assertFalse(Activity.isValidActivity("")); // empty string
        assertFalse(Activity.isValidActivity(" ")); // spaces only
        assertFalse(Activity.isValidActivity("@")); // non-alphanumeric characters (excluding fullstops)
        assertFalse(Activity.isValidActivity("is this a bug?")); // contains non-alphanumeric characters


        // valid ActivityName
        assertTrue(Activity.isValidActivity("report")); // alphabets only
        assertTrue(Activity.isValidActivity("578")); // numbers only
        assertTrue(Activity.isValidActivity("...")); // fullstops only
        assertTrue(Activity.isValidActivity("do report 5.")); // alphabets with numbers and full stop
        assertTrue(Activity.isValidActivity("Hire Toilet Cleaner")); // with capital letters
        assertTrue(Activity.isValidActivity("Toilet CLeaner...  the 2nd of the 100th floor...")); // long string
    }
}
