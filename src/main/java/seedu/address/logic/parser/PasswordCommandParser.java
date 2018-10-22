//@@author lws803
package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.logic.commands.PasswordCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.UserPrefs;

/**
 * Password command parser class
 * Description: Parses the arguments to PasswordCommand class
 */
public class PasswordCommandParser implements Parser<PasswordCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the FindCommand
     * and returns an FindCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public PasswordCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        UserPrefs userPref = new UserPrefs();


        if (trimmedArgs.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, PasswordCommand.MESSAGE_USAGE));
        }

        String[] credentials = trimmedArgs.split("\\s+");

        if (credentials.length < 1) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, PasswordCommand.MESSAGE_USAGE));
        }

        return new PasswordCommand(credentials[0], userPref.getAddressBookFilePath().toString());
    }


}
