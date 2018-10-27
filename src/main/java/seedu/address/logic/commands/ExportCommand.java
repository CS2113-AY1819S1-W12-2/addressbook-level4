//@@author Limminghong
package seedu.address.logic.commands;

import static seedu.address.logic.parser.CliSyntax.PREFIX_DIRECTORY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_FILENAME;

import java.io.IOException;

import javafx.collections.ObservableList;
import seedu.address.commons.util.FileEncryptor;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.CliSyntax;
import seedu.address.model.Model;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.storage.CsvWriter;

/**
 * Exports CSV file into a directory from the address book.
 */
public class ExportCommand extends Command {
    public static final String COMMAND_WORD = CliSyntax.COMMAND_EXPORT;
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Exports the address book into a directory\n"
            + "Parameters: "
            + PREFIX_DIRECTORY + "Directory "
            + PREFIX_FILENAME + "Name of File\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_DIRECTORY + "C:\\Users\\USER\\ "
            + PREFIX_FILENAME + "export1";
    public static final String MESSAGE_FAILURE = "Directory does not exist.";
    public static final String MESSAGE_FILE_NAME_EXIST = "A file with the name %1$s exists in this directory.";
    public static final String MESSAGE_SUCCESS = "AddressBook is exported to %1$s with the name %2$s.";

    String directory;
    String fileName;
    private String fullDirectory;

    public ExportCommand() {}

    public ExportCommand(String directory, String fileName, String fullDirectory) {
        this.directory = directory;
        this.fileName = fileName;
        this.fullDirectory = fullDirectory;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        UserPrefs userPref = new UserPrefs();
        FileEncryptor fe = new FileEncryptor(userPref.getAddressBookFilePath().toString());

        if (fe.isLocked()) {
            throw new CommandException(FileEncryptor.MESSAGE_ADDRESS_BOOK_LOCKED);
        }

        try {
            ObservableList<Person> personList = model.getPersonList();
            CsvWriter csvWriter = new CsvWriter(personList);
            csvWriter.convertToCsv(fullDirectory);
            return new CommandResult(String.format(MESSAGE_SUCCESS, directory, fileName));
        } catch (IOException io) {
            throw new CommandException("ERROR1");
        }
    }
}
