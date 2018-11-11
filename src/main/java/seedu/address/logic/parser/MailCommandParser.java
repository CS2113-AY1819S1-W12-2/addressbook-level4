//@@author lekoook
package seedu.address.logic.parser;

import seedu.address.logic.commands.MailCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses the arguments to be use by MailCommand.
 */
public class MailCommandParser implements Parser<MailCommand> {

    public static final String MESSAGE_MULTIPLE_ARGS = "Please specify only ONE argument type!\n"
            + "%s\n"
            + "%s";
    public static final String MESSAGE_CORRECT_EG = "CORRECT Eg: mail t/subordinates";
    public static final String MESSAGE_WRONG_EG = "WRONG Eg: mail t/subordinates all/";

    /**
     * Parses the arguments of a mail command.
     * @param args the arguments to be parsed.
     * @return the resulting MailCommand instance.
     * @throws ParseException if the format of the argument is incorrect.
     */
    public MailCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, CliSyntax.PREFIX_ALL, CliSyntax.PREFIX_TAG);

        // Throw exception when more than one argument prefix specified.
        // Preamble count as one type, hence the int '2' is used here.
        if (argMultimap.getSize() > 2) {
            throw new ParseException(String.format(MESSAGE_MULTIPLE_ARGS, MESSAGE_CORRECT_EG, MESSAGE_WRONG_EG));
        }

        if (argMultimap.getValue(CliSyntax.PREFIX_ALL).isPresent()) {
            return new MailCommand(MailCommand.MailType.TYPE_ALL);
        } else if (argMultimap.getValue(CliSyntax.PREFIX_TAG).isPresent()) {
            return new MailCommand(MailCommand.MailType.TYPE_GROUPS, argMultimap.getValue(CliSyntax.PREFIX_TAG).get());
        } else {
            return new MailCommand(MailCommand.MailType.TYPE_SELECTION);
        }
    }
}
