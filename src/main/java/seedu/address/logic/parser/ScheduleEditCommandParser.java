//@@author LowGinWee
package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.ScheduleEditCommand.MESSAGE_USAGE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ACTIVITY;

import java.util.stream.Stream;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.ScheduleEditCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new ScheduleEditCommand object
 */
public class ScheduleEditCommandParser implements Parser<ScheduleEditCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the ScheduleEditCommand
     * and returns a ScheduleEditCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public ScheduleEditCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(
                        " " + args,
                        PREFIX_ACTIVITY);
        if (!arePrefixesPresent(argMultimap, PREFIX_ACTIVITY) || argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, MESSAGE_USAGE));
        }
        Index index;
        index = ParserUtil.parseIndex(argMultimap.getPreamble());
        String task = ParserUtil.parseActivityName(argMultimap.getValue(PREFIX_ACTIVITY).get());
        return new ScheduleEditCommand(index, task);
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
