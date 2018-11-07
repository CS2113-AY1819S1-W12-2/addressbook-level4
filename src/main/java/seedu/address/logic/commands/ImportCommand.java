//@@author Limminghong
package seedu.address.logic.commands;

import static seedu.address.logic.parser.CliSyntax.PREFIX_DIRECTORY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_FILENAME;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import seedu.address.commons.util.FileEncryptor;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.CliSyntax;
import seedu.address.model.Model;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.storage.CsvReader;

/**
 * Imports a CSV file of the address book from a directory.
 */
public class ImportCommand extends Command {
    public static final String DUPLICATE_PERSON = "This is a duplicated person";
    public static final String COMMAND_WORD = CliSyntax.COMMAND_IMPORT;
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Imports the address book from a directory\n"
            + "Parameters: "
            + PREFIX_DIRECTORY + "Directory "
            + PREFIX_FILENAME + "File Name\n"
            + "Example (For Windows): " + COMMAND_WORD + " "
            + PREFIX_DIRECTORY + "C:\\Users\\USER "
            + PREFIX_FILENAME + "exportFile\n"
            + "Example (For Mac/Unix): " + COMMAND_WORD + " "
            + PREFIX_DIRECTORY + "/home/cs/class/ "
            + PREFIX_FILENAME + "exportFile";
    public static final String MESSAGE_FAILURE = "Directory does not exists or the file is of the wrong format.";
    public static final String MESSAGE_SUCCESS = "AddressBook is imported from %1$s.";

    private static final Logger logger = Logger.getLogger(ImportCommand.class.getName());
    private File file;
    private String directory;

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
            CsvReader csvReader = new CsvReader(file);
            List<Person> personList = csvReader.convertToList();
            for (Person toAdd : personList) {
                try {
                    model.addPerson(toAdd);
                    model.insertPersonIntoPrediction(toAdd);
                } catch (DuplicatePersonException dup) {
                    logger.info(DUPLICATE_PERSON);
                }
            }
            model.commitAddressBook();
            return new CommandResult(String.format(MESSAGE_SUCCESS, directory));
        } catch (IOException io) {
            throw new CommandException(CsvReader.WRONG_FORMAT);
        }
    }
}
