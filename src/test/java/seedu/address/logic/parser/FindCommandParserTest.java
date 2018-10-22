package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import seedu.address.logic.commands.FindCommand;

public class FindCommandParserTest {

    private FindCommandParser parser = new FindCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "     ", String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_validArgs_returnsFindCommand() {
        // no leading and trailing whitespaces
        String[] names = {"Alice", "Bob"};

        Map<Prefix, String[]> prefixKeywordsMap = new HashMap<>();
        prefixKeywordsMap.put(PREFIX_NAME, names);
        Set<Prefix> keys = prefixKeywordsMap.keySet();

        // FindCommand expectedFindCommand =
        //        new FindCommand(, names, keys.toArray(new Prefix[0]));
        // assertParseSuccess(parser,  "Alice Bob", expectedFindCommand);

        // multiple whitespaces between keywords
        // assertParseSuccess(parser, " \n Alice \n \t Bob  \t", expectedFindCommand);
    }

}
