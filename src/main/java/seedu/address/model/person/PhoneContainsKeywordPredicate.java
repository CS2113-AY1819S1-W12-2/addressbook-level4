//@@author lws803
package seedu.address.model.person;

import java.util.List;
import java.util.function.Predicate;

import seedu.address.commons.util.StringUtil;

/**
 * Tests that a {@code Person}'s {@code PhoneNumber} matches any of the keywords given.
 */
public class PhoneContainsKeywordPredicate implements Predicate<Person> {
    private final List<String> keywords;

    public PhoneContainsKeywordPredicate (List<String> keywords) {
        this.keywords = keywords;
    }

    @Override
    public boolean test(Person person) {
        return keywords.stream()
                .anyMatch(keyword -> StringUtil.containsWordIgnoreCase(person.getPhone().value, keyword));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof PhoneContainsKeywordPredicate // instanceof handles nulls
                && keywords.equals(((PhoneContainsKeywordPredicate) other).keywords)); // state check
    }
}
