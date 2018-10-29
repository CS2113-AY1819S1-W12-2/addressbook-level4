//@@author lekoook
package seedu.address.logic.parser;

import static seedu.address.logic.commands.MailCommand.TYPE_ALL;
import static seedu.address.logic.commands.MailCommand.TYPE_GROUPS;
import static seedu.address.logic.commands.MailCommand.TYPE_SELECTION;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.Test;

import seedu.address.logic.commands.MailCommand;

public class MailCommandParserTest {

    private MailCommandParser parser = new MailCommandParser();

    @Test
    public void parse_allPersons_success() {
        MailCommand expectedCommand = new MailCommand(TYPE_ALL);
        assertParseSuccess(parser, "mail " + CliSyntax.PREFIX_ALL.getPrefix(), expectedCommand);
    }

    @Test
    public void parse_selectedPersons_success() {
        MailCommand expectedCommand = new MailCommand(TYPE_SELECTION);
        assertParseSuccess(parser, "mail", expectedCommand);
    }

    @Test
    public void parse_selectedTags_success() {
        MailCommand expectedCommand = new MailCommand(TYPE_GROUPS, "");
        assertParseSuccess(parser, "mail " + CliSyntax.PREFIX_TAG, expectedCommand);
    }
}
