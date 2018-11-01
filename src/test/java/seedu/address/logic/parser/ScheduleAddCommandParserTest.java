package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.ACTIVITY_ONE_DATE;
import static seedu.address.logic.commands.CommandTestUtil.ACTIVITY_ONE_DATE_DESC;
import static seedu.address.logic.commands.CommandTestUtil.ACTIVITY_ONE_NAME;
import static seedu.address.logic.commands.CommandTestUtil.ACTIVITY_ONE_NAME_DESC;
import static seedu.address.logic.commands.CommandTestUtil.ACTIVITY_TWO_NAME_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_ACTIVITY_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_DATE_DESC;
import static seedu.address.logic.commands.CommandTestUtil.PREAMBLE_WHITESPACE;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.Test;

import seedu.address.logic.commands.ScheduleAddCommand;
import seedu.address.model.schedule.Activity;
import seedu.address.testutil.ActivityBuilder;

public class ScheduleAddCommandParserTest {
    private final ScheduleAddCommandParser parser = new ScheduleAddCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        Activity expectedActivity = new ActivityBuilder()
                .withActivityName(ACTIVITY_ONE_NAME)
                .withDateString(ACTIVITY_ONE_DATE)
                .build();

        //leading whitespace
        assertParseSuccess(parser,
                PREAMBLE_WHITESPACE
                        + ACTIVITY_ONE_DATE_DESC
                        + ACTIVITY_ONE_NAME_DESC,
                new ScheduleAddCommand(expectedActivity));

        //multiple activity names. Last name accepted
        assertParseSuccess(parser,
                ACTIVITY_ONE_DATE_DESC
                        + ACTIVITY_TWO_NAME_DESC
                        + ACTIVITY_ONE_NAME_DESC,
                new ScheduleAddCommand(expectedActivity));

        //multiple dates. Last date accepted
        assertParseSuccess(parser,
                ACTIVITY_ONE_DATE_DESC
                        + ACTIVITY_ONE_DATE_DESC
                        + ACTIVITY_ONE_NAME_DESC,
                new ScheduleAddCommand(expectedActivity));

    }

    @Test
    public void parse_compulsoryFieldMissing_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, ScheduleAddCommand.MESSAGE_USAGE);

        // missing Activity prefix
        assertParseFailure(parser, ACTIVITY_ONE_DATE_DESC, expectedMessage);

        //missing Date prefix
        assertParseFailure(parser, ACTIVITY_ONE_NAME_DESC, expectedMessage);
    }

    @Test
    public void parse_invalidValue_failure() {
        //invalid Activity name
        assertParseFailure(parser,
                ACTIVITY_ONE_DATE_DESC
                        + INVALID_ACTIVITY_DESC,
                Activity.MESSAGE_ACTIVITY_CONSTRAINTS);

        //invalid date
        assertParseFailure(parser,
                INVALID_DATE_DESC
                        + ACTIVITY_ONE_NAME_DESC,
                Activity.MESSAGE_DATE_CONSTRAINTS);
    }
}
