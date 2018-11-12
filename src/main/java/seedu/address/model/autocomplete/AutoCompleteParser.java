//@@author lekoook
package seedu.address.model.autocomplete;

import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_INVALID;
import static seedu.address.logic.parser.CliSyntax.PREFIX_KPI;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NOTE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_POSITION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import seedu.address.logic.parser.ArgumentMultimap;
import seedu.address.logic.parser.ArgumentTokenizer;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses the text input for auto completing the command
 */
public class AutoCompleteParser {
    /**
     * Pattern instance used to separate command and it's arguments
     */
    private static final Pattern COMMAND_INPUT_FORMAT = Pattern.compile("(?<commandWord>\\S+)(?<arguments>.*)");

    /**
     * TODO: Extend to other attributes
     * Parses the command to be used for auto completing of commands
     * @param textInput text to be parsed
     * @return a pair of values to be used to determine the Trie to use for auto complete
     * @throws ParseException if the user input does not conform the expected format
     */
    public static AutoCompleteParserPair parseCommand(String textInput) {
        final Matcher matcher = COMMAND_INPUT_FORMAT.matcher(textInput);

        if (!matcher.matches()) {
            return new AutoCompleteParserPair(PREFIX_INVALID, PREFIX_INVALID.getPrefix());
        }

        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(
                textInput,
                PREFIX_NAME,
                PREFIX_PHONE,
                PREFIX_EMAIL,
                PREFIX_ADDRESS,
                PREFIX_NOTE,
                PREFIX_TAG,
                PREFIX_KPI,
                PREFIX_POSITION);

        final String commandWord = matcher.group("commandWord");
        final String arguments = matcher.group("arguments");
        return AutoCompleteArgumentsParser.parse(commandWord, arguments, argMultimap);
    }
}
