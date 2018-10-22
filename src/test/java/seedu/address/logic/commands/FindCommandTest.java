package seedu.address.logic.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.commons.core.Messages.MESSAGE_PERSONS_LISTED_OVERVIEW;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.testutil.TypicalPersons.CARL;
import static seedu.address.testutil.TypicalPersons.ELLE;
import static seedu.address.testutil.TypicalPersons.FIONA;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import seedu.address.logic.CommandHistory;
import seedu.address.logic.parser.Prefix;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.ClosestMatchList;
import seedu.address.model.person.NameContainsKeywordsPredicate;

/**
 * Contains integration tests (interaction with the Model) for {@code FindCommand}.
 */
public class FindCommandTest {
    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private CommandHistory commandHistory = new CommandHistory();

    @Test
    public void equals() {
        NameContainsKeywordsPredicate firstPredicate =
                new NameContainsKeywordsPredicate(Collections.singletonList("first"));
        NameContainsKeywordsPredicate secondPredicate =
                new NameContainsKeywordsPredicate(Collections.singletonList("second"));

        String[] namesFirst = {"first"};
        String[] namesSecond = {"second"};
        Prefix[] prefixArray = {PREFIX_NAME};
        Map<Prefix, String[]> prefixKeywordsMap = new HashMap<>();
        prefixKeywordsMap.put(PREFIX_NAME, namesFirst);
        prefixKeywordsMap.put(PREFIX_NAME, namesSecond);

        FindCommand findFirstCommand = new FindCommand(prefixKeywordsMap, prefixArray);
        FindCommand findSecondCommand = new FindCommand(prefixKeywordsMap, prefixArray);

        // same object -> returns true
        assertTrue(findFirstCommand.equals(findFirstCommand));

        // same values -> returns true
        FindCommand findFirstCommandCopy = new FindCommand(prefixKeywordsMap, prefixArray);
        assertTrue(findFirstCommand.equals(findFirstCommandCopy));

        // different types -> returns false
        assertFalse(findFirstCommand.equals(1));

        // null -> returns false
        assertFalse(findFirstCommand.equals(null));

        // different person -> returns false
        // assertFalse(findFirstCommand.equals(findSecondCommand));
    }

    @Test
    public void execute_zeroKeywords_noPersonFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 0);
        NameContainsKeywordsPredicate predicate = preparePredicate(" ");
        String[] names = {};
        Map<Prefix, String[]> prefixKeywordsMap = new HashMap<>();
        prefixKeywordsMap.put(PREFIX_NAME, names);
        Set<Prefix> keys = prefixKeywordsMap.keySet();

        FindCommand command = new FindCommand(prefixKeywordsMap, keys.toArray(new Prefix[0]));

        expectedModel.updateFilteredPersonList(predicate);
        assertCommandSuccess(command, model, commandHistory, expectedMessage, expectedModel);
        assertEquals(Collections.emptyList(), model.getFilteredPersonList());
    }

    @Test
    public void execute_multipleKeywords_multiplePersonsFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 3);
        NameContainsKeywordsPredicate predicate = preparePredicate("Kurz Elle Kunz");

        String[] names = {"Kurz", "Elle", " Kunz"};
        Map<Prefix, String[]> prefixKeywordsMap = new HashMap<>();
        prefixKeywordsMap.put(PREFIX_NAME, names);
        Set<Prefix> keys = prefixKeywordsMap.keySet();

        FindCommand command = new FindCommand(prefixKeywordsMap, keys.toArray(new Prefix[0]));
        expectedModel.updateFilteredPersonList(predicate);
        assertCommandSuccess(command, model, commandHistory, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(CARL, ELLE, FIONA), model.getFilteredPersonList());
    }

    /**
     * Parses {@code userInput} into a {@code NameContainsKeywordsPredicate}.
     */
    private NameContainsKeywordsPredicate preparePredicate(String userInput) {
        return new NameContainsKeywordsPredicate(Arrays.asList(userInput.split("\\s+")));
    }

    //@@author lws803
    @Test
    public void closestMatchListTest () {
        String[] names = {"Kurz"};
        ClosestMatchList closestMatch = new ClosestMatchList(model, PREFIX_NAME, names);
        assertEquals(closestMatch.getApprovedList().length, 2);
    }
    //@@author
}
