//@@author LowGinWee
package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import java.util.Objects;

/**
 * Represents a Person's KPI(Key Performance Index) in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidKpi(String)}
 */
public class Kpi {
    public static final String SORT_ATTRIBUTE = "kpi";


    public static final String MESSAGE_KPI_CONSTRAINTS = "KPI score should be a number from 0 - 5";
    public static final String KPI_VALIDATION_REGEX = "([0-4]{1}(\\.[0-9]+)?)|([5]{1}(\\.[0]+)?)";
    public final String value;

    /**
     * Constructs a {@code KPI}.
     *
     * @param kpi A valid KPI score.
     */
    public Kpi(String kpi) {
        requireNonNull(kpi);
        checkArgument(isValidKpi(kpi), MESSAGE_KPI_CONSTRAINTS);
        value = kpi;
    }

    public Kpi() {
        this.value = null;
    }

    /**
     * Returns true if a KPI has been assigned to the person.
     */
    public boolean doesExist() {
        if (value != null) {
            return true;
        }
        return false;
    }

    /**
     * Returns true if a given string is a valid KPI score.
     */
    public static boolean isValidKpi(String test) {
        return test.matches(KPI_VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        if (!doesExist() && !((Kpi) other).doesExist()) {
            return true;
        }
        return other == this // short circuit if same object
                || (other instanceof Kpi // instanceof handles nulls
                && Objects.equals(this.value, ((Kpi) other).value));
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}

