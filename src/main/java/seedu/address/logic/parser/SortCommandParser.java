//@@author peiying98
package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.function.Consumer;
import java.util.stream.Stream;

import seedu.address.logic.commands.SortCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new SortCommand object
 */

public class SortCommandParser implements Parser<SortCommand>{

    /**
     * Parses the given {@code String} of arguments in the context of the SortCommand
     * and returns an SortCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */

    private String attribute;
    private Boolean isReverseOrder;

    public SortCommand parse(String args) throws ParseException {
        requireNonNull(args);

        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL, PREFIX_ADDRESS, PREFIX_TAG);

        if (!args.matches("^|( [npea]/(r)?)$")) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, SortCommand.MESSAGE_USAGE));
        }

        /** To check
        if (argMultimap.size() > 2) {
            throw new ParseException(
                    String.format(SortCommand.MESSAGE_MULTIPLE_ATTRIBUTE_ERROR, SortCommand.MESSAGE_USAGE)
            );
        }
         **/

        if (!(arePrefixesPresent(argMultimap, PREFIX_NAME)
                || arePrefixesPresent(argMultimap, PREFIX_PHONE))) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, SortCommand.MESSAGE_USAGE));
        }

        /**
         * Invalid command arguments would result in a loaded preamble
         */
        //@@author sunarjo-denny-reused
        if (!argMultimap.getPreamble().equals("")) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, SortCommand.MESSAGE_USAGE));
        }

        if (argMultimap.size() == 1) {
            attribute = PREFIX_NAME.getPrefix();
        }

        argMultimap.getValue(PREFIX_NAME).ifPresent(setOrder(PREFIX_NAME));
        argMultimap.getValue(PREFIX_PHONE).ifPresent(setOrder(PREFIX_PHONE));

        return new SortCommand(attribute, isReverseOrder);

    }

    private Consumer<String> setOrder(Prefix prefix) {
        return s-> {
            attribute = prefix.toString();

            if (s.equals(SortCommand.REVERSE_ORDER)) {
                isReverseOrder = true;
                return;
            } else {
                isReverseOrder = false;
                return;
            }
        };
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }

}
