package systemtests;

import static org.junit.Assert.assertFalse;
import static seedu.address.commons.core.Messages.MESSAGE_PERSONS_LISTED_OVERVIEW;
import static seedu.address.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.testutil.TypicalPersons.BENSON;
import static seedu.address.testutil.TypicalPersons.CARL;
import static seedu.address.testutil.TypicalPersons.DANIEL;
import static seedu.address.testutil.TypicalPersons.ELLE;
import static seedu.address.testutil.TypicalPersons.KEYWORD_MATCHING_MEIER;

import org.junit.Test;

import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.commands.RedoCommand;
import seedu.address.logic.commands.UndoCommand;
import seedu.address.model.Model;

public class FindCommandSystemTest extends AddressBookSystemTest {

    @Test
    public void find() {
        /* Case: find multiple persons in address book, command with leading spaces and trailing spaces
         * -> 2 persons found
         */
        String command = "   " + FindCommand.COMMAND_WORD + " " + PREFIX_NAME + KEYWORD_MATCHING_MEIER + "   ";
        Model expectedModel = getModel();
        ModelHelper.setFilteredList(expectedModel, BENSON, DANIEL, ELLE);
        // first names of Benson and Daniel are "Meier"
        assertCommandSuccess(command, expectedModel, "{Meier}", "{Meyer}");
        assertSelectedCardUnchanged();

        /* Case: repeat previous find command where person list is displaying the persons we are finding
         * -> 2 persons found
         */
        command = FindCommand.COMMAND_WORD + " " + PREFIX_NAME + KEYWORD_MATCHING_MEIER;
        assertCommandSuccess(command, expectedModel, "{Meier}", "{Meyer}");
        assertSelectedCardUnchanged();

        /**
         * Case: find person where person list is
         * not displaying the person we are finding -> 1 person found
         */
        command = FindCommand.COMMAND_WORD + " " + PREFIX_NAME + " Carl";
        ModelHelper.setFilteredList(expectedModel, CARL);
        assertCommandSuccess(command, expectedModel, "{Carl}", "{}");
        assertSelectedCardUnchanged();

        /* Case: find multiple persons in address book, 2 keywords -> 2 persons found */
        command = FindCommand.COMMAND_WORD + " " + PREFIX_NAME + " Benson Daniel";
        ModelHelper.setFilteredList(expectedModel, BENSON, DANIEL);
        assertCommandSuccess(
                command,
                expectedModel,
                "{Benson, Daniel}",
                "{}"
        );

        assertSelectedCardUnchanged();

        /* Case: find multiple persons in address book, 2 keywords in reversed order -> 2 persons found */
        command = FindCommand.COMMAND_WORD + " " + PREFIX_NAME + " Daniel Benson";
        assertCommandSuccess(command, expectedModel, "{Benson, Daniel}", "{}");

        assertSelectedCardUnchanged();

        /* Case: find multiple persons in address book, 2 keywords with 1 repeat -> 2 persons found */
        command = FindCommand.COMMAND_WORD + " " + PREFIX_NAME + " Daniel Benson Daniel";
        assertCommandSuccess(command, expectedModel, "{Benson, Daniel}", "{}");

        assertSelectedCardUnchanged();

        /* Case: find multiple persons in address book, 2 matching keywords and 1 non-matching keyword
         * -> 2 persons found
         */
        command = FindCommand.COMMAND_WORD + " " + PREFIX_NAME + " Daniel Benson NonMatchingKeyWord";
        assertCommandSuccess(command, expectedModel, "{Benson, Daniel}", "{}");

        assertSelectedCardUnchanged();

        /* Case: undo previous find command -> rejected */
        command = UndoCommand.COMMAND_WORD;
        String expectedResultMessage = UndoCommand.MESSAGE_FAILURE;
        assertCommandFailure(command, expectedResultMessage);

        /* Case: redo previous find command -> rejected */
        command = RedoCommand.COMMAND_WORD;
        expectedResultMessage = RedoCommand.MESSAGE_FAILURE;
        assertCommandFailure(command, expectedResultMessage);

        /* Case: find same persons in address book after deleting 1 of them -> 1 person found */
        executeCommand(DeleteCommand.COMMAND_WORD + " 1");
        assertFalse(getModel().getAddressBook().getPersonList().contains(BENSON));
        command = FindCommand.COMMAND_WORD + " " + PREFIX_NAME + KEYWORD_MATCHING_MEIER;
        expectedModel = getModel();
        ModelHelper.setFilteredList(expectedModel, DANIEL, ELLE);
        assertCommandSuccess(command, expectedModel, "{Meier}", "{Meyer}");

        assertSelectedCardUnchanged();

        /* Case: find person in address book, keyword is same as name but of different case -> 1 person found */
        command = FindCommand.COMMAND_WORD + " " + PREFIX_NAME + " MeIeR";
        assertCommandSuccess(command, expectedModel, "{Meier}", "{Meyer}");
        assertSelectedCardUnchanged();

        /* Case: mixed case command word -> rejected */
        command = "FiNd Meier";
        assertCommandFailure(command, MESSAGE_UNKNOWN_COMMAND);
    }

    /**
     * Executes {@code command} and verifies that the command box displays an empty string, the result display
     * box displays {@code Messages#MESSAGE_PERSONS_LISTED_OVERVIEW} with the number of people in the filtered list,
     * and the model related components equal to {@code expectedModel}.
     * These verifications are done by
     * {@code AddressBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
     * Also verifies that the status bar remains unchanged, and the command box has the default style class, and the
     * selected card updated accordingly, depending on {@code cardStatus}.
     * @see AddressBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     */
    private void assertCommandSuccess(String command,
                                      Model expectedModel,
                                      String keywordsMatchedString,
                                      String guessedMatchesString) {
        StringBuilder expectedResultMessage = new StringBuilder(String.format(
                MESSAGE_PERSONS_LISTED_OVERVIEW, expectedModel.getFilteredPersonList().size()));

        expectedResultMessage.append("\n");
        expectedResultMessage.append("Keywords matched: ");
        expectedResultMessage.append(keywordsMatchedString);
        expectedResultMessage.append("\n");
        expectedResultMessage.append("Keywords guessed: ");
        expectedResultMessage.append(guessedMatchesString);

        executeCommand(command);
        assertApplicationDisplaysExpected("", expectedResultMessage.toString(), expectedModel);
        assertCommandBoxShowsDefaultStyle();
        assertStatusBarUnchanged();
    }

    /**
     * Executes {@code command} and verifies that the command box displays {@code command}, the result display
     * box displays {@code expectedResultMessage} and the model related components equal to the current model.
     * These verifications are done by
     * {@code AddressBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
     * Also verifies that the browser url, selected card and status bar remain unchanged, and the command box has the
     * error style.
     * @see AddressBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     */
    private void assertCommandFailure(String command, String expectedResultMessage) {
        Model expectedModel = getModel();

        executeCommand(command);
        assertApplicationDisplaysExpected(command, expectedResultMessage, expectedModel);
        assertSelectedCardUnchanged();
        assertCommandBoxShowsErrorStyle();
        assertStatusBarUnchanged();
    }
}
