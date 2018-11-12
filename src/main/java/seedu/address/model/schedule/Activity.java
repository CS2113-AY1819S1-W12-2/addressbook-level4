//@@author LowGinWee
package seedu.address.model.schedule;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Represents an Activity in the schedule.
 */
public class Activity {
    public static final String DATE_VALIDATION_REGEX = "\\d{2}/\\d{2}/\\d{4}";
    public static final String ACTIVITY_VALIDATION_REGEX = "([\\p{Alnum}]|\\.)([\\p{Alnum} ]|\\.\\s*)*";
    public static final String MESSAGE_DATE_CONSTRAINTS = "Date should be in \"DD/MM/YYYY\" and must be a valid date.";
    public static final String MESSAGE_ACTIVITY_CONSTRAINTS = "Task name should only contain alphanumeric characters,"
            + "spaces and fullstops.";

    private final Date date;
    private final String activityName;

    /**
     * Creates an Activity.
     * @param date A valid date.
     * @param activity Activity string.
     */
    public Activity(Date date, String activity) {
        requireNonNull(activity);
        requireNonNull(date);
        checkArgument(isValidActivity(activity), MESSAGE_ACTIVITY_CONSTRAINTS);
        this.date = date;
        this.activityName = activity;
    }

    /**
     * @return Date of activity.
     */
    public Date getDate() {
        return date;
    }

    /**
     * @return Activity name.
     */
    public String getActivityName() {
        return activityName;
    }

    /**
     * Converts day, month and year specified to a {@code Date} object
     * @param day A valid day of the month
     * @param month A valid month of the year
     * @param year A valid year.
     * @return {@code Date} of activity.
     */
    public static Date toDate(int day, int month, int year) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, day);
        cal.set(Calendar.MONTH, --month); //index of month start from 0. i.e. jan == 0.
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date date = cal.getTime();
        return date;
    }

    /**
     * @return {@code String} of date in "DAY dd/mm/yyyy" format.
     */
    public static String getDateString (Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        String dayOfWeek = new SimpleDateFormat("EE", Locale.ENGLISH).format(date);
        int month = cal.get(Calendar.MONTH);
        return dayOfWeek + " " + cal.get(Calendar.DATE) + "/" + ++month
                + "/" + cal.get(Calendar.YEAR);
    }


    /**
     * Checks if specified date is valid
     */
    public static boolean isValidDate(String test) {
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        format.setLenient(false);
        try {
            format.parse(test);
        } catch (ParseException e) {
            return false;
        }
        return test.matches(DATE_VALIDATION_REGEX);
    }

    /**
     * Checks if specified Activity name is valid
     */
    public static boolean isValidActivity(String test) {
        return test.matches(ACTIVITY_VALIDATION_REGEX);
    }
    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Activity // instanceof handles nulls
                && activityName.equals(((Activity) other).activityName) // state check
                && date.equals(((Activity) other).date)); // state check
    }

    @Override
    public int hashCode() {
        return (activityName + date).hashCode();
    }
}
