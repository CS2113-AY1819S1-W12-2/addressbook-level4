/**
 * package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showFirstPersonOnly;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.Before;
import org.junit.Test;

import seedu.address.logic.CommandHistory;
import seedu.address.logic.UndoRedoStack;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.user.UserCreds;
import seedu.address.model.user.UserPrefs;

public class SortCommandTest {
    private Model model;
    private Model expectedModel;
    private SortCommand sortCommand;

    @Before
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs(), new UserCreds());
        model.getUserCreds().validateCurrentSession(); // validate user
        expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs(), new UserCreds());
        expectedModel.getUserCreds().validateCurrentSession(); // validate user

        sortCommand = new SortCommand(SortCommand.PREFIX_NAME, false);
        sortCommand.setData(model, new CommandHistory(), new UndoRedoStack());
    }

    @Test
    public void execute_defaultSortCommand_unfilteredList() {
        assertCommandSuccess(sortCommand, model, String.format(sortCommand.MESSAGE_SUCCESS, SortCommand.PREFIX_NAME,
                "ascending"), expectedModel);
    }

    @Test
    public void execute_defaultSortCommand_filteredList() {
        showFirstPersonOnly(model);
        assertCommandSuccess(sortCommand, model, String.format(sortCommand.MESSAGE_SUCCESS, SortCommand.PREFIX_NAME,
                "ascending"), expectedModel);
    }
}
**/
/**
package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalPersons.getEmptyAddressBook;
import static seedu.address.testutil.TypicalPersons.getSortedAddressBook;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.CliSyntax;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;

//@@author peiying98 - incomplete!
/**
 * Contains integration tests (interaction with the Model) and unit tests for {@code DeleteCommand}.
 */

/**
public class SortCommandTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private Model emptyModel = new ModelManager(getEmptyAddressBook(), new UserPrefs());
    private Model modelSortedByNameInReverse = new ModelManager(getSortedAddressBook("name", true), new UserPrefs());
    private Model modelSortedByPhone = new ModelManager(getSortedAddressBook("phone", false), new UserPrefs());
    private Model modelSortedByPhoneInReverse = new ModelManager(getSortedAddressBook("phone", true), new UserPrefs());
    private Model modelSortedByEmail = new ModelManager(getSortedAddressBook("email", false), new UserPrefs());
    private Model modelSortedByEmailInReverse = new ModelManager(getSortedAddressBook("email", true), new UserPrefs());
    private Model modelSortedByAddress = new ModelManager(
            getSortedAddressBook("address", false), new UserPrefs());
    private Model modelSortedByAddressInReverse = new ModelManager(
            getSortedAddressBook("address", true), new UserPrefs());

    @Test
    public void constructor_nullSortOrder_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        new SortCommand(CliSyntax.PREFIX_NAME.getPrefix(), null);
    }

    @Test
    public void constructor_nullSortField_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        new SortCommand(null, true);
    }

    @Test
    public void execute_catchNoPersonsException() throws CommandException {
        thrown.expect(CommandException.class);
        prepareCommand("n/", false, emptyModel).execute();
    }

    @Test
    public void execute_sortByName_success() throws Exception {
        SortCommand sortCommand = prepareCommand("n/", false, model);
        String expectedMessage = String.format(SortCommand.MESSAGE_SORT_SUCCESS, "name", "ascending");
        assertCommandSuccess(sortCommand, model, expectedMessage, model);
    }

    @Test
    public void execute_sortByNameInReverseOrder_success() throws Exception {
        SortCommand sortCommand = prepareCommand("n/", true, model);
        String expectedMessage = String.format(SortCommand.MESSAGE_SORT_SUCCESS, "name", "descending");
        assertCommandSuccess(sortCommand, model, expectedMessage, modelSortedByNameInReverse);
    }

    @Test
    public void execute_sortByPhone_success() throws Exception {
        SortCommand sortCommand = prepareCommand("p/", false, model);
        String expectedMessage = String.format(SortCommand.MESSAGE_SORT_SUCCESS, "phone", "ascending");
        assertCommandSuccess(sortCommand, model, expectedMessage, modelSortedByPhone);
    }

    @Test
    public void execute_sortByPhoneInReverseOrder_success() throws Exception {
        SortCommand sortCommand = prepareCommand("p/", true, model);
        String expectedMessage = String.format(SortCommand.MESSAGE_SORT_SUCCESS, "phone", "descending");
        assertCommandSuccess(sortCommand, model, expectedMessage, modelSortedByPhoneInReverse);
    }

    @Test
    public void execute_sortByEmail_success() throws Exception {
        SortCommand sortCommand = prepareCommand("e/", false, model);
        String expectedMessage = String.format(SortCommand.MESSAGE_SORT_SUCCESS, "email", "ascending");
        assertCommandSuccess(sortCommand, model, expectedMessage, modelSortedByEmail);
    }

    @Test
    public void execute_sortByEmailInReverseOrder_success() throws Exception {
        SortCommand sortCommand = prepareCommand("e/", true, model);
        String expectedMessage = String.format(SortCommand.MESSAGE_SORT_SUCCESS, "email", "descending");
        assertCommandSuccess(sortCommand, model, expectedMessage, modelSortedByEmailInReverse);
    }

    @Test
    public void execute_sortByAddress_success() throws Exception {
        SortCommand sortCommand = prepareCommand("a/", false, model);
        String expectedMessage = String.format(SortCommand.MESSAGE_SORT_SUCCESS, "address", "ascending");
        assertCommandSuccess(sortCommand, model, expectedMessage, modelSortedByAddress);
    }

    @Test
    public void execute_sortByAddressInReverseOrder_success() throws Exception {
        SortCommand sortCommand = prepareCommand("a/", true, model);
        String expectedMessage = String.format(SortCommand.MESSAGE_SORT_SUCCESS, "address", "descending");
        assertCommandSuccess(sortCommand, model, expectedMessage, modelSortedByAddressInReverse);
    }
*/
    /**
     * Returns a {@code sortCommand} with the parameters {@code field and @code isReverseOrder}.
     */

/**
    private SortCommand prepareCommand(String field, boolean isReverseOrder, Model model) {
        SortCommand sortCommand = new SortCommand(field, isReverseOrder);
        return sortCommand;
    }


}
*/
