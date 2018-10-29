package seedu.address.storage;

import java.util.Date;
import java.util.Objects;
import javax.xml.bind.annotation.XmlElement;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.schedule.Activity;

/**
 * JAXB-friendly version of the Activity.
 */
public class XmlAdaptedActivity {
    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Activity's %s field is missing!";

    @XmlElement
    private Date date;
    @XmlElement
    private String activity;

    /**
     * Constructs an XmlAdaptedActivity.
     * This is the no-arg constructor that is required by JAXB.
     */
    public XmlAdaptedActivity() {
    }

    /**
     * Constructs an {@code XmlAdaptedActivity} with the given activity.
     */
    public XmlAdaptedActivity(Date date, String activity) {
        this.date = date;
        this.activity = activity;
    }

    /**
     * Converts a given activity into this class for JAXB use.
     *
     * @param source future changes to this will not affect the created XmlAdaptedPerson
     */
    public XmlAdaptedActivity(Activity source) {
        this.date = source.getDate();
        this.activity = source.getActivityName();
    }

    /**
     * Converts this jaxb-friendly adapted activity object into the model's Person object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted activity
     */
    public Activity toModelType() throws IllegalValueException {
        if (date == null) {
            throw new IllegalValueException((String.format(MISSING_FIELD_MESSAGE_FORMAT, Date.class.getSimpleName())));
        }
        if (activity == null) {
            throw new IllegalValueException((String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    Activity.class.getSimpleName())));
        }
        if (!Activity.isValidActivity(activity)) {
            throw new IllegalValueException(Activity.MESSAGE_ACTIVITY_CONSTRAINTS);
        }
        return new Activity(date, activity);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof XmlAdaptedActivity)) {
            return false;
        }

        XmlAdaptedActivity otherActivity = (XmlAdaptedActivity) other;
        return Objects.equals(date, otherActivity.date)
                && Objects.equals(activity, otherActivity.activity);
    }
}
