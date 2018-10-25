package seedu.address.logic.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_LIST_FIRST;
import static seedu.address.testutil.TypicalIndexes.INDEX_LIST_SECOND;
import static seedu.address.testutil.TypicalIndexes.INDEX_LIST_SIX;
import static seedu.address.testutil.TypicalIndexes.INDEX_LIST_THIRD;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.ArrayList;

import org.junit.Rule;
import org.junit.Test;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.commons.events.ui.JumpToListRequestEvent;
import seedu.address.logic.CommandHistory;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.ui.testutil.EventsCollectorRule;

/**
 * Contains integration tests (interaction with the Model) for {@code SelectCommand}.
 */
public class SelectCommandTest {
    @Rule
    public final EventsCollectorRule eventsCollectorRule = new EventsCollectorRule();

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private CommandHistory commandHistory = new CommandHistory();

    @Test
    public void execute_validIndexUnfilteredList_success() {
        Index lastPersonIndex = Index.fromOneBased(model.getFilteredPersonList().size());
        ArrayList<Index> lastPersonIndexList = new ArrayList<>();
        lastPersonIndexList.add(lastPersonIndex);

        assertExecutionSuccessSingle(INDEX_LIST_FIRST);
        assertExecutionSuccessSingle(INDEX_LIST_THIRD);
        assertExecutionSuccessSingle(lastPersonIndexList);
        assertExecutionSuccessMultiple(INDEX_LIST_SIX);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_failure() {
        Index outOfBoundsIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        ArrayList<Index> outOfBoundsIndexList = new ArrayList<>();
        outOfBoundsIndexList.add(outOfBoundsIndex);

        assertExecutionFailure(outOfBoundsIndexList, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_validIndexFilteredList_success() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);
        showPersonAtIndex(expectedModel, INDEX_FIRST_PERSON);

        assertExecutionSuccessSingle(INDEX_LIST_FIRST);
    }

    @Test
    public void execute_invalidIndexFilteredList_failure() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);
        showPersonAtIndex(expectedModel, INDEX_FIRST_PERSON);

        ArrayList<Index> outOfBoundsIndex = INDEX_LIST_SECOND;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundsIndex.get(0).getZeroBased() < model.getAddressBook().getPersonList().size());

        assertExecutionFailure(outOfBoundsIndex, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        SelectCommand selectFirstCommand = new SelectCommand(INDEX_LIST_FIRST);
        SelectCommand selectSecondCommand = new SelectCommand(INDEX_LIST_SECOND);

        // same object -> returns true
        assertTrue(selectFirstCommand.equals(selectFirstCommand));

        // same values -> returns true
        SelectCommand selectFirstCommandCopy = new SelectCommand(INDEX_LIST_FIRST);
        assertTrue(selectFirstCommand.equals(selectFirstCommandCopy));

        // different types -> returns false
        assertFalse(selectFirstCommand.equals(1));

        // null -> returns false
        assertFalse(selectFirstCommand.equals(null));

        // different person -> returns false
        assertFalse(selectFirstCommand.equals(selectSecondCommand));
    }

    /**
     * Executes a {@code SelectCommand} with the given {@code index}, and checks that a {@code CommandException}
     * is thrown with the {@code expectedMessage}.
     */
    private void assertExecutionFailure(ArrayList<Index> indexArrayList, String expectedMessage) {
        SelectCommand selectCommand = new SelectCommand(indexArrayList);
        assertCommandFailure(selectCommand, model, commandHistory, expectedMessage);
        assertTrue(eventsCollectorRule.eventsCollector.isEmpty());
    }

    //@@author lekoook
    /**
     * Executes a {@code SelectCommand} with the given {@code index}, and checks that {@code JumpToListRequestEvent}
     * is raised with the correct index.
     */
    private void assertExecutionSuccessSingle(ArrayList<Index> indexArrayList) {
        SelectCommand selectCommand = new SelectCommand(indexArrayList);
        String expectedMessage =
                String.format(SelectCommand.MESSAGE_SELECT_PERSON_SUCCESS_SINGLE, indexArrayList.size());

        assertCommandSuccess(selectCommand, model, commandHistory, expectedMessage, expectedModel);

        JumpToListRequestEvent lastEvent = (JumpToListRequestEvent) eventsCollectorRule.eventsCollector.getMostRecent();
        assertEquals(extractIndexAsIntegers(indexArrayList), lastEvent.targetIndex);
    }

    /**
     * Executes a {@code SelectCommand} with the given {@code index}, and checks that {@code JumpToListRequestEvent}
     * is raised with the correct index.
     */
    private void assertExecutionSuccessMultiple(ArrayList<Index> indexArrayList) {
        SelectCommand selectCommand = new SelectCommand(indexArrayList);
        String expectedMessage =
                String.format(SelectCommand.MESSAGE_SELECT_PERSON_SUCCESS_MULTIPLE, indexArrayList.size());

        assertCommandSuccess(selectCommand, model, commandHistory, expectedMessage, expectedModel);

        JumpToListRequestEvent lastEvent = (JumpToListRequestEvent) eventsCollectorRule.eventsCollector.getMostRecent();
        assertEquals(extractIndexAsIntegers(indexArrayList), lastEvent.targetIndex);
    }

    /**
     * Extracts an array list of {@code Index} to an array list of {@code Integer}.
     * @param indexArrayList the list of {@code Index} to extract from.
     * @return the list of {@code Integer} extracted.
     */
    private ArrayList<Integer> extractIndexAsIntegers(ArrayList<Index> indexArrayList) {
        ArrayList<Integer> output = new ArrayList<>();
        for (Index index : indexArrayList) {
            output.add(index.getZeroBased());
        }
        return output;
    }
}
