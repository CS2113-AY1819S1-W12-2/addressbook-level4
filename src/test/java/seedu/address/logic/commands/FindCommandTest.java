package seedu.address.logic.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.commons.core.Messages.MESSAGE_PERSONS_LISTED_OVERVIEW;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_KPI;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NOTE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_POSITION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;
import static seedu.address.testutil.TypicalPersons.CARL;
import static seedu.address.testutil.TypicalPersons.ELLE;
import static seedu.address.testutil.TypicalPersons.FIONA;
import static seedu.address.testutil.TypicalPersons.WILSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

import org.junit.Test;

import seedu.address.logic.CommandHistory;
import seedu.address.logic.parser.Prefix;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.AddressContainsKeywordsPredicate;
import seedu.address.model.person.ClosestMatchList;
import seedu.address.model.person.EmailContainsKeywordsPredicate;
import seedu.address.model.person.KpiContainsKeywordPredicate;
import seedu.address.model.person.NameContainsKeywordsPredicate;
import seedu.address.model.person.NoteContainsKeywordsPredicate;
import seedu.address.model.person.Person;
import seedu.address.model.person.PhoneContainsKeywordPredicate;
import seedu.address.model.person.PositionContainsKeywordsPredicate;
import seedu.address.model.person.TagContainsKeywordsPredicate;

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

        String[] namesFirst = {"first", "second"};
        Prefix[] prefixArray = {PREFIX_NAME};
        Map<Prefix, String[]> prefixKeywordsMap = new HashMap<>();
        prefixKeywordsMap.put(PREFIX_NAME, namesFirst);

        FindCommand findFirstCommand = new FindCommand(prefixKeywordsMap, prefixArray);

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

    //@@author lws803
    @Test
    public void execute_zeroKeywords_noPersonFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 0);
        expectedMessage += combinedActualMatches(0, "{}");

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
        expectedMessage += combinedActualMatches(2, "{Elle, Kurz}");

        String[] names = {"Kurz", "Elle", " Kunz"};
        Map<Prefix, String[]> prefixKeywordsMap = new HashMap<>();
        prefixKeywordsMap.put(PREFIX_NAME, names);
        Set<Prefix> keys = prefixKeywordsMap.keySet();

        FindCommand command = new FindCommand(prefixKeywordsMap, keys.toArray(new Prefix[0]));
        expectedModel.updateFilteredPersonList(predicate);
        assertCommandSuccess(command, model, commandHistory, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(CARL, ELLE, FIONA), model.getFilteredPersonList());
    }

    @Test
    public void execute_multipleAttributes_multiplePersonsFound() {
        Map<Prefix, String[]> prefixKeywordMap = new HashMap<>();
        String[] names = {"Kurz", "Kunz"};
        String[] phones = {"95352563"};
        String[] emails = {"heinz@example.com"};
        String[] addresses = {"wall street"};
        String[] notes = {"Carl sample note"};
        String[] kpis = {"4.0"};
        String[] positions = {"Worker"};

        prefixKeywordMap.put(PREFIX_NAME, names);
        prefixKeywordMap.put(PREFIX_PHONE, phones);
        prefixKeywordMap.put(PREFIX_EMAIL, emails);
        prefixKeywordMap.put(PREFIX_NOTE, notes);
        prefixKeywordMap.put(PREFIX_ADDRESS, addresses);
        prefixKeywordMap.put(PREFIX_KPI, kpis);
        prefixKeywordMap.put(PREFIX_POSITION, positions);


        Set<Prefix> keys = prefixKeywordMap.keySet();
        Prefix[] types = keys.toArray(new Prefix[0]);
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 1);
        expectedMessage += combinedActualMatches(
                6,
                "{heinz@example.com, Kurz, Kunz, 4.0, 95352563, Worker}"
        );

        Predicate<Person> combinedPredicate = PREDICATE_SHOW_ALL_PERSONS;
        FindCommand command = new FindCommand(prefixKeywordMap, keys.toArray(new Prefix[0]));

        combinedPredicate = getPersonPredicate(prefixKeywordMap, types, combinedPredicate);

        expectedModel.updateFilteredPersonList(combinedPredicate);
        assertCommandSuccess(command, model, commandHistory, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(CARL), model.getFilteredPersonList());
    }

    @Test
    public void execute_emailSearch_multiplePersonsFound() {
        Map<Prefix, String[]> prefixKeywordMap = new HashMap<>();
        String[] emails = {"wow@gmail.com"};
        prefixKeywordMap.put(PREFIX_EMAIL, emails);

        Set<Prefix> keys = prefixKeywordMap.keySet();
        Prefix[] types = keys.toArray(new Prefix[0]);
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 1);
        expectedMessage += combinedActualMatches(1, "{wow@gmail.com}");

        Predicate<Person> combinedPredicate = PREDICATE_SHOW_ALL_PERSONS;
        FindCommand command = new FindCommand(prefixKeywordMap, keys.toArray(new Prefix[0]));

        combinedPredicate = getPersonPredicate(prefixKeywordMap, types, combinedPredicate);

        expectedModel.updateFilteredPersonList(combinedPredicate);
        assertCommandSuccess(command, model, commandHistory, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(WILSON), model.getFilteredPersonList());
    }

    /**
     * Gets the person's predicate based on attributes
     * @param prefixKeywordMap obtains the keywordMap of PREFIX_TYPE and arguments
     * @param types is a list of PREFIX types
     * @param combinedPredicate Combined predicate of all persons
     * @return
     */
    private Predicate<Person> getPersonPredicate(
            Map<Prefix,
            String[]> prefixKeywordMap,
            Prefix[] types,
            Predicate<Person> combinedPredicate) {

        for (Prefix type : types) {
            ClosestMatchList closestMatch = new ClosestMatchList(model, type, prefixKeywordMap.get(type));
            String[] approvedList = closestMatch.getApprovedList();

            if (type == PREFIX_PHONE) {
                combinedPredicate = combinedPredicate.and(
                        new PhoneContainsKeywordPredicate(Arrays.asList(approvedList))
                );
            } else if (type == PREFIX_NAME) {
                combinedPredicate = combinedPredicate.and(
                        new NameContainsKeywordsPredicate(Arrays.asList(approvedList))
                );
            } else if (type == PREFIX_ADDRESS) {
                combinedPredicate = combinedPredicate.and(
                        new AddressContainsKeywordsPredicate(Arrays.asList(approvedList))
                );
            } else if (type == PREFIX_EMAIL) {
                combinedPredicate = combinedPredicate.and(
                        new EmailContainsKeywordsPredicate(Arrays.asList(approvedList))
                );
            } else if (type == PREFIX_NOTE) {
                combinedPredicate = combinedPredicate.and(
                        new NoteContainsKeywordsPredicate(Arrays.asList(approvedList))
                );
            } else if (type == PREFIX_POSITION) {
                combinedPredicate = combinedPredicate.and(
                        new PositionContainsKeywordsPredicate(Arrays.asList(approvedList))
                );
            } else if (type == PREFIX_TAG) {
                combinedPredicate = combinedPredicate.and(
                        new TagContainsKeywordsPredicate(Arrays.asList(approvedList))
                );
            } else if (type == PREFIX_KPI) {
                combinedPredicate = combinedPredicate.and(
                        new KpiContainsKeywordPredicate(Arrays.asList(approvedList))
                );
            }
        }
        return combinedPredicate;
    }
    //@@author


    /**
     * Parses {@code userInput} into a {@code NameContainsKeywordsPredicate}.
     */
    private NameContainsKeywordsPredicate preparePredicate(String userInput) {
        return new NameContainsKeywordsPredicate(Arrays.asList(userInput.split("\\s+")));
    }

    //@@author lws803

    /**
     * Combined the actual matches. To be used together with results
     * @param keywordsMatched number of keywords matched
     * @param matchedKeywordsString keyword matched string
     * @return output String
     */
    private String combinedActualMatches(Integer keywordsMatched, String matchedKeywordsString) {
        StringBuilder output = new StringBuilder();
        output.append("\n");
        output.append(keywordsMatched);
        output.append(" keyword(s) matched:");
        output.append("\n");
        output.append(matchedKeywordsString);
        return output.toString();
    }


    @Test
    public void closestMatchListTest () {
        String[] names = {"Kurz"};
        ClosestMatchList closestMatch = new ClosestMatchList(model, PREFIX_NAME, names);
        assertEquals(closestMatch.getApprovedList().length, 2);
    }
    //@@author
}
