//@@author LowGinWee
package seedu.address.storage;

import static org.junit.Assert.assertEquals;
import static seedu.address.storage.XmlAdaptedActivity.MISSING_FIELD_MESSAGE_FORMAT;
import static seedu.address.testutil.TypicalActivity.ACTIVITY_TASK_3;

import java.util.Date;

import org.junit.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.schedule.Activity;
import seedu.address.testutil.Assert;


public class XmlAdaptedActivityTest {
    private static final String INVALID_ACTIVITY = "";

    private static final Date VALID_DATE = ACTIVITY_TASK_3.getDate();
    private static final String VALID_ACTIVITY = ACTIVITY_TASK_3.getActivityName();

    @Test
    public void toModelType_validActivity_returnsActivity() throws Exception {
        XmlAdaptedActivity activity = new XmlAdaptedActivity(ACTIVITY_TASK_3);
        assertEquals(ACTIVITY_TASK_3, activity.toModelType());
    }

    @Test
    public void toModelType_nullDate_throwsIllegalValueException() {
        XmlAdaptedActivity activity = new XmlAdaptedActivity(null, VALID_ACTIVITY);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Date.class.getSimpleName());
        Assert.assertThrows(IllegalValueException.class, expectedMessage, activity::toModelType);
    }

    @Test
    public void toModelType_nullActivity_throwsIllegalValueException() {
        XmlAdaptedActivity activity = new XmlAdaptedActivity(VALID_DATE, null);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Activity.class.getSimpleName());
        Assert.assertThrows(IllegalValueException.class, expectedMessage, activity::toModelType);
    }

    @Test
    public void toModelType_invalidActivity_throwsIllegalValueException() {
        XmlAdaptedActivity activity = new XmlAdaptedActivity(VALID_DATE, INVALID_ACTIVITY);
        String expectedMessage = String.format(Activity.MESSAGE_ACTIVITY_CONSTRAINTS);
        Assert.assertThrows(IllegalValueException.class, expectedMessage, activity::toModelType);
    }

}
