//@@author lekoook
package seedu.address.model.autocomplete;

import static seedu.address.logic.parser.CliSyntax.COMMAND_FIND;
import static seedu.address.logic.parser.CliSyntax.COMMAND_LIST;
import static seedu.address.logic.parser.CliSyntax.COMMAND_MAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_COMMAND;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_INVALID;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import seedu.address.logic.parser.ArgumentMultimap;
import seedu.address.logic.parser.ArgumentTokenizer;
import seedu.address.logic.parser.Prefix;

/**
 * Parses the arguments of a command text input for auto completing the command
 */
public class AutoCompleteArgumentsParser {

    /**
     * Parses the arguments to be used for auto completing of commands
     * @param arguments the full string of arguments to parse
     * @return the prefix that will be used for the prediction
     */
    public static AutoCompleteParserPair parse(String command, String arguments, ArgumentMultimap argMultimap) {
        if (arguments.isEmpty()) {
            return new AutoCompleteParserPair(PREFIX_COMMAND, command);
        }

        // TODO: Add or remove command support as necessary
        switch(command) {
        case COMMAND_FIND:
            return getFindParserPair(arguments, argMultimap);
        case COMMAND_LIST:
            return getListParserPair(arguments, argMultimap);
        case COMMAND_MAIL:
            return getMailParserPair(arguments, argMultimap);
        default:
            return new AutoCompleteParserPair(PREFIX_INVALID, arguments.trim());
        }
    }

    /**
     * Creates a AutoCompleteParserPair instance based on the last Prefix found in find command.
     * @param arguments the user input to search.
     * @param argMultimap used to retrieve the values of the prefix keys.
     * @return the appropriate AutoCompleteParserPair instance.
     */
    private static AutoCompleteParserPair getFindParserPair(String arguments, ArgumentMultimap argMultimap) {
        Prefix lastPrefix = ArgumentTokenizer.findLastPrefix(
                arguments,
                PREFIX_NAME,
                PREFIX_EMAIL,
                PREFIX_PHONE,
                PREFIX_ADDRESS,
                PREFIX_TAG);

        if (argMultimap.getValue(lastPrefix).isPresent()) {
            return new AutoCompleteParserPair(lastPrefix, argMultimap.getValue(lastPrefix).get());
        }
        // Default text prediction is names
        return new AutoCompleteParserPair(PREFIX_NAME, arguments);
    }

    /**
     * Creates a AutoCompleteParserPair instance based on the last Prefix found in mail command.
     * @param arguments the user input to search.
     * @param argMultimap used to retrieve the values of the prefix keys.
     * @return the appropriate AutoCompleteParserPair instance.
     */
    private static AutoCompleteParserPair getMailParserPair(String arguments, ArgumentMultimap argMultimap) {
        Prefix lastPrefix = ArgumentTokenizer.findLastPrefix(arguments, PREFIX_TAG, PREFIX_NAME);
        if (argMultimap.getValue(lastPrefix).isPresent()) {
            return new AutoCompleteParserPair(lastPrefix, argMultimap.getValue(lastPrefix).get());
        }
        // Default text prediction is names
        return new AutoCompleteParserPair(PREFIX_NAME, arguments);
    }

    /**
     * Creates a AutoCompleteParserPair instance based on the last Prefix found in list command.
     * @param arguments the user input to search.
     * @param argMultimap used to retrieve the values of the prefix keys.
     * @return the appropriate AutoCompleteParserPair instance.
     */
    private static AutoCompleteParserPair getListParserPair(String arguments, ArgumentMultimap argMultimap) {
        Prefix lastPrefix = ArgumentTokenizer.findLastPrefix(
                arguments,
                PREFIX_TAG);
        if (argMultimap.getValue(lastPrefix).isPresent()) {
            return new AutoCompleteParserPair(lastPrefix, argMultimap.getValue(lastPrefix).get());
        }
        // Default text prediction is nothing
        return new AutoCompleteParserPair(PREFIX_INVALID, arguments);
    }
}
