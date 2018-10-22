package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NOTE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_POSITION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
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


        Map<Prefix, String[]> prefixKeywordMap = new HashMap<>();

        if (arePrefixesPresent(argMultimap, PREFIX_NAME)) {
            Name name = ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME).get());
            String[] keywords = name.fullName.trim().split("\\s+");
            prefixKeywordMap.put(PREFIX_NAME, keywords);
        }
        if (arePrefixesPresent(argMultimap, PREFIX_ADDRESS)) {
            Address address = ParserUtil.parseAddress(argMultimap.getValue(PREFIX_ADDRESS).get());
            String[] keywords = address.value.trim().split("\\s+");
            prefixKeywordMap.put(PREFIX_ADDRESS, keywords);
        }
        if (arePrefixesPresent(argMultimap, PREFIX_PHONE)) {

            Phone phone = ParserUtil.parsePhone(argMultimap.getValue(PREFIX_PHONE).get());
            String[] keywords = phone.value.trim().split("\\s+");
            prefixKeywordMap.put(PREFIX_PHONE, keywords);
        }
        if (arePrefixesPresent(argMultimap, PREFIX_EMAIL)) {
            Email email = ParserUtil.parseEmail(argMultimap.getValue(PREFIX_EMAIL).get());
            String[] keywords = email.value.trim().split("\\s+");
            prefixKeywordMap.put(PREFIX_EMAIL, keywords);
        }
        if (arePrefixesPresent(argMultimap, PREFIX_POSITION)) {
            Position position = ParserUtil.parsePosition(argMultimap.getValue(PREFIX_POSITION).get());
            String[] keywords = position.value.trim().split("\\s+");
            prefixKeywordMap.put(PREFIX_POSITION, keywords);
        }
        if (arePrefixesPresent(argMultimap, PREFIX_NOTE)) {
            Note note = ParserUtil.parseNote(argMultimap.getValue(PREFIX_NOTE).get());
            String[] keywords = note.value.trim().split("\\s+");
            prefixKeywordMap.put(PREFIX_NOTE, keywords);
        }
        if (arePrefixesPresent(argMultimap, PREFIX_TAG)) {
            Tag tag = ParserUtil.parseTag(argMultimap.getValue(PREFIX_TAG).get());
            String[] keywords = tag.tagName.trim().split("\\s+");
            prefixKeywordMap.put(PREFIX_TAG, keywords);
        }

        if (prefixKeywordMap.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }
        Set<Prefix> keys = prefixKeywordMap.keySet();
        return new FindCommand(prefixKeywordMap, keys.toArray(new Prefix[0]));
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
