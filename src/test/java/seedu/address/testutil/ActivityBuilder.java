package seedu.address.testutil;

import java.util.Date;

import seedu.address.logic.parser.ParserUtil;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.schedule.Activity;

/**
 * A utility class to help with building Activity objects.
 */
public class ActivityBuilder {
    private static final String DEFAULT_ACTIVITY_NAME = "Attend Meeting";
    private static final String DEFAULT_DATE = "01/01/2018";

    private String activityName;
    private Date date;

    public ActivityBuilder() {
        try {
            date = ParserUtil.parseDate(DEFAULT_DATE);
        } catch (ParseException e) {
            date = null;
        }
        activityName = DEFAULT_ACTIVITY_NAME;
    }

    /**
     * Sets the {@code Date} of the {@code Activity} that we are building.
     * @param date Must be a valid {@code String} in the DD/MM/YYYY format.
     */
    public ActivityBuilder withDateString(String date) {
        try {
            this.date = ParserUtil.parseDate(date);
        } catch (ParseException e) {
            this.date = null;
        }
        return this;
    }

    /**
     * Sets the {@code activityName} of the {@code Activity} that we are building.
     */
    public ActivityBuilder withActivityName(String activityName) {
        this.activityName = activityName;
        return this;
    }

    public Activity build() {
        return new Activity(date, activityName);
    }

}
