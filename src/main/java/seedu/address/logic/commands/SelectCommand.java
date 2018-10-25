package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;

import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.commons.events.ui.JumpToListRequestEvent;
import seedu.address.commons.util.FileEncryptor;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.CliSyntax;
import seedu.address.model.Model;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;

/**
 * Selects a person identified using it's displayed index from the address book.
 */
public class SelectCommand extends Command {

    public static final String COMMAND_WORD = CliSyntax.COMMAND_SELECT;

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Selects single or multiple persons identified by the index numbers used "
            + "in the displayed person list.\n"
            + "Parameters: INDEX INDEX ... INDEX   or   START - END, ... , START - END\n"
            + "Example: " + COMMAND_WORD + " 1 2 4 5   or   1 - 3, 5 - 9";

    public static final String MESSAGE_SELECT_PERSON_SUCCESS_MULTIPLE = "Selected %1$s persons.";
    public static final String MESSAGE_SELECT_PERSON_SUCCESS_SINGLE = "Selected %1$s person.";

    private final ArrayList<Index> targetIndex;

    public SelectCommand(ArrayList<Index> targetIndex) {
        this.targetIndex = new ArrayList<>(targetIndex);
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        UserPrefs userPref = new UserPrefs();
        FileEncryptor fe = new FileEncryptor(userPref.getAddressBookFilePath().toString());

        if (fe.isLocked()) {
            throw new CommandException(FileEncryptor.MESSAGE_ADDRESS_BOOK_LOCKED);
        }

        requireNonNull(model);

        List<Person> filteredPersonList = model.getFilteredPersonList();

        for (Index index : targetIndex) {
            if (index.getZeroBased() >= filteredPersonList.size()) {
                throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
            }
        }

        EventsCenter.getInstance().post(new JumpToListRequestEvent(targetIndex));
        int selectedCount = targetIndex.size();
        if (selectedCount <= 1) {
            return new CommandResult(String.format(MESSAGE_SELECT_PERSON_SUCCESS_SINGLE, selectedCount));
        }
        return new CommandResult(String.format(MESSAGE_SELECT_PERSON_SUCCESS_MULTIPLE, selectedCount));

    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof SelectCommand // instanceof handles nulls
                && targetIndex.equals(((SelectCommand) other).targetIndex)); // state check
    }
}
