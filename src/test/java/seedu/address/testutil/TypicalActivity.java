package seedu.address.testutil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import seedu.address.model.AddressBook;
import seedu.address.model.schedule.Activity;

/**
 * A utility class containing a list of {@code Activity} objects to be used in tests.
 */
public class TypicalActivity {
    public static final Date DATE_1 = Activity.toDate(01, 01, 2018);
    public static final Date DATE_2 = Activity.toDate(02, 01, 2018);
    public static final Date DATE_3 = Activity.toDate(03, 01, 2018);

    public static final Activity ACTIVITY_TASK_1 = new Activity(DATE_1, "Write a report.");

    public static final Activity ACTIVITY_TASK_2 = new Activity(DATE_2, "Send a reminder to Bob.");

    public static final Activity ACTIVITY_TASK_3 = new Activity(DATE_3, "Give Alexia a raise.");

    public static AddressBook getTypicalAddressBook() {
        AddressBook ab = new AddressBook();
        for (Activity activity : getTypicalActivities()) {
            ab.addActivity(activity);
        }
        return ab;
    }

    public static List<Activity> getTypicalActivities() {
        return new ArrayList<>(Arrays.asList(ACTIVITY_TASK_1, ACTIVITY_TASK_2, ACTIVITY_TASK_3));
    }
}
