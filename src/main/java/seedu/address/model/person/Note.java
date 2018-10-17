//@@author LowGinWee
package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Note in the address book.
 * Guarantees: immutable; name is valid as declared in {@link #isValidNote(String)}
 */
public class Note {

    public static final String MESSAGE_NOTE_CONSTRAINTS = "Notes or Descriptions "
            + "should only contain alphanumeric characters and spaces";

    //TODO check regex and update to include fullstops.
    public static final String NOTE_VALIDATION_REGEX = "[\\p{Alnum}][\\p{Alnum} ]*";

    public final String value;

    /**
     * Constructs a {@code Note}
     */
    public Note() {
        value = null;
    }

    /**
     * Constructs a {@code Tag}.
     *
     * @param note A valid note provided.
     */
    public Note(String note) {
        requireNonNull(note);
        checkArgument(isValidNote(note), MESSAGE_NOTE_CONSTRAINTS);
        value = note;
    }

    /**
     * Returns true if a Note has been assigned to the person.
     */
    public boolean doesExist() {
        if (value != null) {
            return true;
        }
        return false;
    }

    /**
     * Returns true if a given string is a valid note.
     */
    public static boolean isValidNote(String test) {
        return test.matches(NOTE_VALIDATION_REGEX);
    }

    /**
     * Format state as text for viewing.
     */
    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Note // instanceof handles nulls
                && value.equals(((Note) other).value)); // state check
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}


