//@@author lws803
package seedu.address.logic.commands;

import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.Arrays;
import java.util.function.Predicate;

import seedu.address.commons.exceptions.FileEncryptorException;
import seedu.address.commons.util.FileEncryptor;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.NameContainsKeywordsPredicate;
import seedu.address.model.person.Person;

/**
 * Encrypts the XML data using a password and returns a message
 * Message will be displayed on CommandResult
 */
public class PasswordCommand extends Command {
    public static final String COMMAND_WORD = "password";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Hashes file using password.\n"
            + "Parameter: PASSWORD\n"
            + "Example: " + COMMAND_WORD + " myPassword";

    public static final String MESSAGE_ENCRYPT_SUCCESS = "File encrypted!";
    public static final String MESSAGE_DECRYPT_SUCCESS = "File decrypted!";

    private String password;
    private FileEncryptor fe;

    /**
     * Executes the FileEncryptor and obtains a message
     * @param credentials will be obtained from parser
     */
    public PasswordCommand (String credentials, String path) {
        fe = new FileEncryptor(path);
        this.password = credentials;
    }

    @Override
    public CommandResult execute (Model model, CommandHistory history) throws CommandException {

        String message;
        try {
            message = fe.process(this.password);
        } catch (FileEncryptorException fex) {
            throw new CommandException(fex.getLocalizedMessage());
        }


        if (message.compareTo(FileEncryptor.MESSAGE_DECRYPTED) == 0) {
            model.reinitAddressbook();
            model.reinitialisePrediction();
            model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
            return new CommandResult(MESSAGE_DECRYPT_SUCCESS);
        } else {
            String[] approvedList = {};
            Predicate<Person> emptyPredicate = new NameContainsKeywordsPredicate(Arrays.asList(approvedList));
            model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS.and(emptyPredicate));
            return new CommandResult(MESSAGE_ENCRYPT_SUCCESS);
        }
    }

}
