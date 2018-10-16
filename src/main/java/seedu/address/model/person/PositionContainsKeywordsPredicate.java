//@@author lws803
package seedu.address.model.person;

import java.util.List;
import java.util.function.Predicate;

import seedu.address.commons.util.StringUtil;

/**
 * Tests that a {@code Person}'s {@code Position} matches any of the keywords given.
 */
public class PositionContainsKeywordsPredicate implements Predicate<Person> {

    private final List<String> keywords;

    public PositionContainsKeywordsPredicate(List<String> keywords) {
        this.keywords = keywords;
    }

    @Override
    public boolean test(Person person) {
        return keywords.stream()
                .anyMatch(keyword -> StringUtil.containsWordIgnoreCase(person.getPosition().value, keyword));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof PositionContainsKeywordsPredicate // instanceof handles nulls
                && keywords.equals(((PositionContainsKeywordsPredicate) other).keywords)); // state check
    }

}
