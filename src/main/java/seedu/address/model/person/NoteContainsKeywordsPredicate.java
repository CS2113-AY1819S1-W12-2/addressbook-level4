//@@author lws803
package seedu.address.model.person;

import java.util.List;
import java.util.function.Predicate;

import seedu.address.commons.util.StringUtil;

/**
 * Tests that a {@code Person}'s {@code Note} matches any of the keywords given.
 */
public class NoteContainsKeywordsPredicate implements Predicate<Person> {


    private final List<String> keywords;

    public NoteContainsKeywordsPredicate(List<String> keywords) {
        this.keywords = keywords;
    }

    @Override
    public boolean test(Person person) {
        return keywords.stream()
                .anyMatch(keyword -> StringUtil.containsWordIgnoreCase(person.getNote().value, keyword));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof NoteContainsKeywordsPredicate // instanceof handles nulls
                && keywords.equals(((NoteContainsKeywordsPredicate) other).keywords)); // state check
    }


}
