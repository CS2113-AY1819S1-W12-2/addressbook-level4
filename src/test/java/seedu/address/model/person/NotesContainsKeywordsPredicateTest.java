//@@author lws803
package seedu.address.model.person;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import seedu.address.testutil.PersonBuilder;

public class NotesContainsKeywordsPredicateTest {
    @Test
    public void equals() {
        List<String> firstPredicateKeywordList = Collections.singletonList("Testing something");
        List<String> secondPredicateKeywordList = Arrays.asList("Testing something", "wow cool");

        NoteContainsKeywordsPredicate firstPredicate =
                new NoteContainsKeywordsPredicate(firstPredicateKeywordList);
        NoteContainsKeywordsPredicate secondPredicate =
                new NoteContainsKeywordsPredicate(secondPredicateKeywordList);

        // same object -> returns true
        assertTrue(firstPredicate.equals(firstPredicate));

        // same values -> returns true
        NoteContainsKeywordsPredicate firstPredicateCopy =
                new NoteContainsKeywordsPredicate(firstPredicateKeywordList);
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
        NoteContainsKeywordsPredicate predicate =
                new NoteContainsKeywordsPredicate(Collections.singletonList("note"));
        // New person with no position predicate
        assertFalse(predicate.test(new PersonBuilder().withName("Alice Bob").build()));
    }

}
