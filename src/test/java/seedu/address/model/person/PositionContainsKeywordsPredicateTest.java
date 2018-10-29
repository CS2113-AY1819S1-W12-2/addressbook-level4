//@@author lws803
package seedu.address.model.person;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import seedu.address.testutil.PersonBuilder;

public class PositionContainsKeywordsPredicateTest {

    @Test
    public void equals() {
        List<String> firstPredicateKeywordList = Collections.singletonList("secretary");
        List<String> secondPredicateKeywordList = Arrays.asList("secretary", "cleaner");

        PositionContainsKeywordsPredicate firstPredicate =
                new PositionContainsKeywordsPredicate(firstPredicateKeywordList);
        PositionContainsKeywordsPredicate secondPredicate =
                new PositionContainsKeywordsPredicate(secondPredicateKeywordList);

        // same object -> returns true
        assertTrue(firstPredicate.equals(firstPredicate));

        // same values -> returns true
        PositionContainsKeywordsPredicate firstPredicateCopy =
                new PositionContainsKeywordsPredicate(firstPredicateKeywordList);
        assertTrue(firstPredicate.equals(firstPredicateCopy));

        // different types -> returns false
        assertFalse(firstPredicate.equals(1));

        // null -> returns false
        assertFalse(firstPredicate.equals(null));

        // different person -> returns false
        assertFalse(firstPredicate.equals(secondPredicate));
    }

    @Test
    public void nonExistenceTest() {
        PositionContainsKeywordsPredicate predicate =
                new PositionContainsKeywordsPredicate(Collections.singletonList("secretary"));
        // New person with no position predicate
        assertFalse(predicate.test(new PersonBuilder().withName("Alice Bob").build()));

    }

}
