//@@author lekoook
package seedu.address.logic.parser;

import seedu.address.logic.commands.MailCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses the arguments to be use by MailCommand.
 */
public class MailCommandParser implements Parser<MailCommand> {
    /**
     * Parses the arguments of a mail command.
     * @param args the arguments to be parsed.
     * @return the resulting MailCommand instance.
     * @throws ParseException if the format of the argument is incorrect.
     */
    public MailCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, CliSyntax.PREFIX_ALL, CliSyntax.PREFIX_TAG);

        if (argMultimap.getValue(CliSyntax.PREFIX_ALL).isPresent()) {
            return new MailCommand(MailCommand.TYPE_ALL);
        } else if (argMultimap.getValue(CliSyntax.PREFIX_TAG).isPresent()) {
            return new MailCommand(MailCommand.TYPE_GROUPS, argMultimap.getValue(CliSyntax.PREFIX_TAG).get());
        } else {
            return new MailCommand(MailCommand.TYPE_SELECTION);
        }
    }
}
