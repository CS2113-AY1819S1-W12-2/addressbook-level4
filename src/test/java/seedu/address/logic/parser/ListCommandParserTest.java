package seedu.address.logic.parser;

import static org.junit.Assert.assertEquals;
import static seedu.address.logic.commands.ListCommand.TYPE_ALL;
import static seedu.address.logic.commands.ListCommand.TYPE_KPI;
import static seedu.address.logic.commands.ListCommand.TYPE_TAG;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import org.junit.Test;

import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.tag.Tag;

public class ListCommandParserTest {

    @Test
    public void parse_tagListing_success() throws ParseException {
        ListCommandParser parserUnderTest = new ListCommandParser();
        ListCommand actualListCommand = parserUnderTest.parse("list t/accountants");
        ListCommand expectedListCommand =
                new ListCommand(TYPE_TAG, new HashSet<>(Arrays.asList(new Tag("accountants"))));

        assertEquals(expectedListCommand, actualListCommand);
    }

    @Test
    public void parse_kpiListing_success() throws ParseException {
        ListCommandParser parserUnderTest = new ListCommandParser();
        ListCommand actualListCommand = parserUnderTest.parse("list k/1.0");
        ListCommand expectedListCommand =
                new ListCommand(TYPE_KPI, new ArrayList<>(Arrays.asList("1.0")));

        assertEquals(expectedListCommand, actualListCommand);
    }

    @Test
    public void parse_allListing_success() throws ParseException {
        ListCommandParser parserUnderTest = new ListCommandParser();
        ListCommand actualListCommand = parserUnderTest.parse("list all/");
        ListCommand expectedListCommand =
                new ListCommand(TYPE_ALL, new ArrayList<>());

        assertEquals(expectedListCommand, actualListCommand);
    }
}
