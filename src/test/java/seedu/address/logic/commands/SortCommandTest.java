package seedu.address.logic.commands;

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
