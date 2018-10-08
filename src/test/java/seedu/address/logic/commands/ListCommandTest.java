package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;
import static seedu.address.testutil.TypicalPersons.getTypicalPersons;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import seedu.address.logic.CommandHistory;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;

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
                new ListCommand(ListCommand.TYPE_ALL, new ArrayList<>()),
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
                new ListCommand(ListCommand.TYPE_ALL, new ArrayList<>()),
                model,
                commandHistory,
                expectedMessage, expectedModel);
    }
}
