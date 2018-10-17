package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NOTE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_POSITION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.Arrays;

import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.NameContainsKeywordsPredicate;
import seedu.address.model.person.Note;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Position;
import seedu.address.model.tag.Tag;

/**
 * Parses input arguments and creates a new FindCommand object
 */
public class FindCommandParser implements Parser<FindCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the FindCommand
     * and returns an FindCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public FindCommand parse(String args) throws ParseException {

        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(
                        args,
                        PREFIX_NAME,
                        PREFIX_PHONE,
                        PREFIX_EMAIL,
                        PREFIX_ADDRESS,
                        PREFIX_NOTE,
                        PREFIX_POSITION,
                        PREFIX_TAG
                        );

        if (!arePrefixesPresent(argMultimap,
                PREFIX_NAME,
                PREFIX_ADDRESS,
                PREFIX_PHONE,
                PREFIX_EMAIL,
                PREFIX_POSITION,
                PREFIX_NOTE,
                PREFIX_TAG)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }

        if (argMultimap.getSize() > 2) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }

        String trimmedArgs = "";
        Prefix type = PREFIX_NAME;
        Name name;
        Phone phone;
        Address address;
        Email email;
        Position position;
        Note note;
        Tag tag;

        if (arePrefixesPresent(argMultimap, PREFIX_NAME)) {
            name = ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME).get());
            trimmedArgs = name.fullName.trim();
            type = PREFIX_NAME;
        } else if (arePrefixesPresent(argMultimap, PREFIX_ADDRESS)) {
            address = ParserUtil.parseAddress(argMultimap.getValue(PREFIX_ADDRESS).get());
            trimmedArgs = address.value.trim();
            type = PREFIX_ADDRESS;
        } else if (arePrefixesPresent(argMultimap, PREFIX_PHONE)) {
            phone = ParserUtil.parsePhone(argMultimap.getValue(PREFIX_PHONE).get());
            trimmedArgs = phone.value.trim();
            type = PREFIX_PHONE;
        } else if (arePrefixesPresent(argMultimap, PREFIX_EMAIL)) {
            email = ParserUtil.parseEmail(argMultimap.getValue(PREFIX_EMAIL).get());
            trimmedArgs = email.value.trim();
            type = PREFIX_EMAIL;
        } else if (arePrefixesPresent(argMultimap, PREFIX_POSITION)) {
            position = ParserUtil.parsePosition(argMultimap.getValue(PREFIX_POSITION).get());
            trimmedArgs = position.value.trim();
            type = PREFIX_POSITION;
        } else if (arePrefixesPresent(argMultimap, PREFIX_NOTE)) {
            note = ParserUtil.parseNote(argMultimap.getValue(PREFIX_NOTE).get());
            trimmedArgs = note.value.trim();
            type = PREFIX_NOTE;
        } else if (arePrefixesPresent(argMultimap, PREFIX_TAG)) {
            tag = ParserUtil.parseTag(argMultimap.getValue(PREFIX_TAG).get());
            trimmedArgs = tag.tagName.trim();
            type = PREFIX_TAG;
        }

        if (trimmedArgs.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }

        String[] keywords = trimmedArgs.split("\\s+");

        return new FindCommand(new NameContainsKeywordsPredicate(Arrays.asList(keywords)), keywords, type);
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        for (Prefix currPrefix: prefixes) {
            if (argumentMultimap.getValue(currPrefix).isPresent()) {
                return true;
            }
        }
        return false;
    }


}
