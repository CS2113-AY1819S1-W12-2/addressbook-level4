//@@author LowGinWee
package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.ScheduleAddCommand.MESSAGE_USAGE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ACTIVITY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;

import java.util.Date;
import java.util.stream.Stream;

import seedu.address.logic.commands.ScheduleAddCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.schedule.Activity;

/**
 * Parses input arguments and creates a new ScheduleAddCommand object
 */
public class ScheduleAddCommandParser implements Parser<ScheduleAddCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the ScheduleAddCommand
     * and returns a ScheduleAddCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public ScheduleAddCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(
                        " " + args,
                        PREFIX_DATE,
                        PREFIX_ACTIVITY);
        if (!arePrefixesPresent(argMultimap, PREFIX_DATE, PREFIX_ACTIVITY) || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, MESSAGE_USAGE));
        }
        Date date = ParserUtil.parseDate(argMultimap.getValue(PREFIX_DATE).get());
        String task = ParserUtil.parseActivityName(argMultimap.getValue(PREFIX_ACTIVITY).get());
        Activity activity = new Activity(date, task);
        return new ScheduleAddCommand(activity);
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
