package seedu.address.logic.parser;

import java.util.ArrayList;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.SelectCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new SelectCommand object
 */
public class SelectCommandParser implements Parser<SelectCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the SelectCommand
     * and returns an SelectCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public SelectCommand parse(String args) throws ParseException {
        try {
            ArrayList<Index> indexArrayList = ParserUtil.parseSelectIndex(args);
            return new SelectCommand(indexArrayList);
        } catch (ParseException pe) {
            throw new ParseException(
                    String.format(pe.getMessage(), SelectCommand.MESSAGE_USAGE));
        }
    }
}
