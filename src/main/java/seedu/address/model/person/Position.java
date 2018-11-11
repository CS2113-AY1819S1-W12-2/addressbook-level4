//@@author LowGinWee
package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Person's Position in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidPosition(String)}
 */
public class Position {
    public static final String MESSAGE_POSITION_CONSTRAINTS = "Positions should only contain "
            + "alphanumeric characters and spaces";

    public static final String POSITION_VALIDATION_REGEX = "[\\p{Alnum}][\\p{Alnum} ]*";

    public final String value;

    /**
     * Constructs a {@code Position}.
     *
     * @param position A valid position name.
     */
    public Position(String position) {
        requireNonNull(position);
        checkArgument(isValidPosition(position), MESSAGE_POSITION_CONSTRAINTS);
        value = position;
    }

    /**
     * No position provided
     */
    public Position() {
        value = null;
    }

    /**
     * Returns true if a position has been assigned to the person.
     */
    public boolean doesExist() {
        if (value != null) {
            return true;
        }
        return false;
    }

    /**
     * Returns true if a given string is a valid position.
     */
    public static boolean isValidPosition(String test) {
        return test.matches(POSITION_VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        if (!doesExist() && !((Position) other).doesExist()) {
            return true;
        }
        return other == this // short circuit if same object
                || (other instanceof Position // instanceof handles nulls
                && value.equals(((Position) other).value)); // state check
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
