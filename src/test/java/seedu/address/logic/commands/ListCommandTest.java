package seedu.address.logic.commands;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.logic.commands.ListCommand.TYPE_ALL;
import static seedu.address.logic.commands.ListCommand.TYPE_TAG;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;
import static seedu.address.testutil.TypicalPersons.getTypicalPersons;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import org.junit.Before;
import org.junit.Test;

import seedu.address.logic.CommandHistory;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Kpi;
import seedu.address.model.tag.Tag;

/**
 * Contains integration tests (interaction with the Model) and unit tests for ListCommand.
 */
public class ListCommandTest {

    private Model model;
    private Model expectedModel;
    private CommandHistory commandHistory = new CommandHistory();

    @Before
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
    }

    @Test
    public void execute_listIsNotFiltered_showsSameList() {
        String expectedMessage =
                String.format(ListCommand.MESSAGE_SUCCESS, getTypicalPersons().size());
        assertCommandSuccess(
                new ListCommand(TYPE_ALL, new ArrayList<>()),
                model,
                commandHistory,
                expectedMessage,
                expectedModel);
    }

    @Test
    public void execute_listIsFiltered_showsEverything() {
        String expectedMessage =
                String.format(ListCommand.MESSAGE_SUCCESS, getTypicalPersons().size());
        showPersonAtIndex(model, INDEX_FIRST_PERSON);
        assertCommandSuccess(
                new ListCommand(TYPE_ALL, new ArrayList<>()),
                model,
                commandHistory,
                expectedMessage, expectedModel);
    }

    @Test
    public void execute_listTags_showsPersonsWithSpecifiedTags() {
        String expectedMessage =
                String.format(ListCommand.MESSAGE_SUCCESS, 3);
        expectedModel.updateFilteredPersonList(p -> p.getTags().contains(new Tag("friends")));

        assertCommandSuccess(
                new ListCommand(ListCommand.TYPE_TAG, new HashSet<>(Arrays.asList(new Tag("friends")))),
                model,
                commandHistory,
                expectedMessage,
                expectedModel);
    }

    @Test
    public void execute_listKpi_showsPersonsWithSpecifiedKpi() {
        String expectedMessage =
                String.format(ListCommand.MESSAGE_SUCCESS_SINGULAR, 0);
        expectedModel.updateFilteredPersonList(p -> p.getKpi().equals(new Kpi("0")));

        assertCommandSuccess(
                new ListCommand(ListCommand.TYPE_KPI, new ArrayList<>(Arrays.asList())),
                model,
                commandHistory,
                expectedMessage,
                expectedModel);
    }

    @Test
    public void equals() {
        ListCommand listAllCommand = new ListCommand(TYPE_ALL, new ArrayList<>());
        ListCommand listAllCommandCopy = new ListCommand(TYPE_ALL, new ArrayList<>());
        ListCommand listTagsCommand = new ListCommand(TYPE_TAG, new HashSet<>());

        // Equal commands -> return false
        assertTrue(listAllCommand.equals(listAllCommandCopy));

        // Unequal commands -> return false
        assertFalse(listAllCommand.equals(listTagsCommand));
    }
}
