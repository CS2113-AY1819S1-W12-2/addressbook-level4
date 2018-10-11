//@@author Limminghong
package seedu.address.logic.commands;

import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NOTE;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import seedu.address.commons.util.FileEncryptor;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.CliSyntax;
import seedu.address.model.Model;
import seedu.address.model.UserPrefs;

/**
 * Exports CSV file into a directory from the address book.
 */
public class ExportCommand extends Command {
    public static final String COMMAND_WORD = CliSyntax.COMMAND_EXPORT;
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Exports the address book into a directory\n"
            + "Parameters: "
            + PREFIX_NOTE + "Directory "
            + PREFIX_NAME + "Name of File\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_NOTE + "C:\\Users\\USER\\ "
            + PREFIX_NAME + "export1";

    public static final String MESSAGE_FAILURE = "Directory does not exist.";
    public static final String MESSAGE_FILE_NAME_EXIST = "A file with the name %1$s exists in this directory.";
    public static final String MESSAGE_SUCCESS = "AddressBook is exported to %1$s with the name %2$s!";

    private String directory;
    private String fileName;
    private File file;

    public ExportCommand() {}

    public ExportCommand(String directory, String fileName, File file) {
        this.directory = directory;
        this.fileName = fileName;
        this.file = file;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        UserPrefs userPref = new UserPrefs();
        FileEncryptor fe = new FileEncryptor(userPref.getAddressBookFilePath().toString());
        File src = new File(userPref.getAddressBookFilePath().toString());

        if (fe.isLocked()) {
            throw new CommandException(FileEncryptor.MESSAGE_ADDRESS_BOOK_LOCKED);
        }

        try {
            Files.copy(src.toPath(), file.toPath());
            return new CommandResult(String.format(MESSAGE_SUCCESS, directory, fileName));
        } catch (IOException io) {
            throw new CommandException("ERROR");
        }
    }
}
