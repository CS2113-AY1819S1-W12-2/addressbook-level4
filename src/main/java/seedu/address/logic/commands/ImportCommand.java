//@@author Limminghong
package seedu.address.logic.commands;

import static seedu.address.logic.parser.CliSyntax.PREFIX_DIRECTORY;

import java.io.File;
import java.io.IOException;
import java.util.List;

import seedu.address.commons.util.FileEncryptor;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.CliSyntax;
import seedu.address.model.Model;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.storage.CsvWriter;

/**
 * Imports a CSV file of the address book from a directory.
 */
public class ImportCommand extends Command {
    public static final String COMMAND_WORD = CliSyntax.COMMAND_IMPORT;
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Imports the address book into a directory\n"
            + "Parameters: "
            + PREFIX_DIRECTORY + "Directory\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_DIRECTORY + "C:\\Users\\USER\\export.xml";
    public static final String MESSAGE_FAILURE = "Directory does not exists.";
    public static final String MESSAGE_SUCCESS = "AddressBook is imported from %1$s.";

    private File file;
    private String directory;

    public ImportCommand() {}

    public ImportCommand(String directory, File file) {
        this.directory = directory;
        this.file = file;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        UserPrefs userPref = new UserPrefs();
        FileEncryptor fe = new FileEncryptor(userPref.getAddressBookFilePath().toString());

        if (fe.isLocked()) {
            throw new CommandException(FileEncryptor.MESSAGE_ADDRESS_BOOK_LOCKED);
        }

        try {
            CsvWriter csvWriter = new CsvWriter(file);
            List<Person> personList = csvWriter.convertToList();
            for (Person toAdd : personList) {
                try {
                    model.addPerson(toAdd);
                    model.getTextPrediction().insertPerson(toAdd);
                } catch (DuplicatePersonException dup) {
                    // TODO: ask if continue is needed since cannot leave exception blank
                    continue;
                }
            }
            model.commitAddressBook();
            return new CommandResult(String.format(MESSAGE_SUCCESS, directory));
        } catch (IOException io) {
            throw new CommandException("ERROR");
        }
    }
}
