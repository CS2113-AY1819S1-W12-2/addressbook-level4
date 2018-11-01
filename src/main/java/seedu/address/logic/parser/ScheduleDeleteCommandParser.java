//@@author LowGinWee
package seedu.address.logic.parser;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.ScheduleDeleteCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new ScheduleDeleteCommand object
 */
public class ScheduleDeleteCommandParser implements Parser<ScheduleDeleteCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the ScheduleDeleteCommand
     * and returns a ScheduleDeleteCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public ScheduleDeleteCommand parse(String args) throws ParseException {
        Index index;
        index = ParserUtil.parseIndex(args);
        return new ScheduleDeleteCommand(index);
    }
}
