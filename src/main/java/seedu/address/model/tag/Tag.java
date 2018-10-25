//@@author LowGinWee
package seedu.address.model.tag;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import seedu.address.commons.core.index.Index;

/**
 * Represents a Tag in the address book.
 * Guarantees: immutable; name is valid as declared in {@link #isValidTagName(String)}
 */
public class Tag {
    public static final String MESSAGE_TAG_CONSTRAINTS = "Tags names should be alphanumeric,"
            + "followed by its priority(optional)(1 - 2)";
    public static final String TAG_VALIDATION_REGEX = "(((\\p{Alnum})+)(\\s([1-2]{1}))?)";
    public static final int PRIORITY_HIGH = 2;
    public static final int PRIORITY_MEDIUM = 1;
    public static final int PRIORITY_LOW = 0;

    public final String tagName;
    public final Index priority;

    /**
     * Constructs a {@code Tag}.
     * @param tagName A valid tag name.
     * @param priority Priority of Tag.
     */
    public Tag(String tagName, Index priority) {
        requireNonNull(tagName);
        checkArgument(isValidTagName(tagName), MESSAGE_TAG_CONSTRAINTS);
        this.tagName = tagName;
        this.priority = priority;
    }

    /**
     * Constructs a {@code Tag}.
     * @param tagName A valid tag name.
     */
    public Tag(String tagName) {
        requireNonNull(tagName);
        checkArgument(isValidTagName(tagName), MESSAGE_TAG_CONSTRAINTS);
        this.tagName = tagName;
        this.priority = Index.fromZeroBased(PRIORITY_LOW);
    }

    /**
     * Returns true if a given string is a valid tag name.
     */
    public static boolean isValidTagName(String test) {
        return test.matches(TAG_VALIDATION_REGEX);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Tag // instanceof handles nulls
                && tagName.equals(((Tag) other).tagName)
                && priority.equals(((Tag) other).priority)); // state check
    }

    @Override
    public int hashCode() {
        return (tagName).hashCode();
    }

    /**
     * Format state as text for viewing.
     */
    @Override
    public String toString() {
        String fullTag = tagName;
        if (!(priority.getZeroBased() == PRIORITY_LOW)) {
            fullTag += " " + priority.getZeroBased();
        }
        return fullTag;
    }
}
