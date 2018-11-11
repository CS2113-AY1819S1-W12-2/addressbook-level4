//@@author peiying98
package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SORT_ATTRIBUTE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SORT_ORDER;

import java.util.stream.Stream;

import seedu.address.logic.commands.SortCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new SortCommand object
 */

public class SortCommandParser implements Parser<SortCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the SortCommand
     * and returns an SortCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public SortCommand parse(String args) throws ParseException {
        requireNonNull(args);

        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_SORT_ATTRIBUTE, PREFIX_SORT_ORDER);



        /** To check
         if (argMultimap.size() > 2) {
         throw new ParseException(
         String.format(SortCommand.MESSAGE_MULTIPLE_ATTRIBUTE_ERROR, SortCommand.MESSAGE_USAGE)
         );
         }
         **/

        if (!arePrefixesPresent(argMultimap, PREFIX_SORT_ATTRIBUTE)) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, SortCommand.MESSAGE_USAGE));
        }
        String attribute = ParserUtil.parseAttribute(argMultimap.getValue(PREFIX_SORT_ATTRIBUTE).get());;
        if (arePrefixesPresent(argMultimap, PREFIX_SORT_ORDER)) {
            String order = ParserUtil.parseOrder(argMultimap.getValue(PREFIX_SORT_ORDER).get());
            return new SortCommand(attribute, order);
        }
        /**
         * Invalid command arguments would result in a loaded preamble
         */
        if (!argMultimap.getPreamble().equals("")) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, SortCommand.MESSAGE_USAGE));
        }
        return new SortCommand(attribute);

    }


    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }

}

