//@@author Limminghong
package seedu.address.logic.commands;

import java.nio.file.Path;

import seedu.address.commons.util.FileEncryptor;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.CliSyntax;
import seedu.address.model.Model;
import seedu.address.model.UserPrefs;

/**
 * Imports a CSV file of the address book from a directory.
 */
public class ImportCommand extends Command {
    public static final String COMMAND_WORD = CliSyntax.COMMAND_IMPORT;
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Imports the address book into a directory\n"
            + "Parameters: directory";
    public static final String MESSAGE_FAILURE = "DIRECTORY DOES NOT EXIST";
    public static final String MESSAGE_SUCCESS = "Directory exist :)!";

    private int flag = 0;

    public ImportCommand() {}

    public ImportCommand(Path path) {
        this.flag = 1;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        UserPrefs userPref = new UserPrefs();
        FileEncryptor fe = new FileEncryptor(userPref.getAddressBookFilePath().toString());

        if (fe.isLocked()) {
            throw new CommandException(FileEncryptor.MESSAGE_ADDRESS_BOOK_LOCKED);
        }

        if (flag == 1) {
            return new CommandResult(MESSAGE_SUCCESS);
        } else {
            return new CommandResult(MESSAGE_USAGE);
        }
    }
}
