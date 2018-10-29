package seedu.address.logic.commands;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.ACTIVITY_ONE_NAME;
import static seedu.address.logic.commands.CommandTestUtil.ACTIVITY_TWO_NAME;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalActivity.ACTIVITY_TASK_1;
import static seedu.address.testutil.TypicalActivity.ACTIVITY_TASK_2;
import static seedu.address.testutil.TypicalActivity.getTypicalAddressBook;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_ACTIVITY;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_ACTIVITY;

import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.CommandHistory;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.schedule.Activity;

public class ScheduleCommandTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private CommandHistory commandHistory = new CommandHistory();
    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    ////ScheduleAddCommand tests
    @Test
    public void constructorScheduleAddCommand_nullActivity_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        new ScheduleAddCommand(null);
    }

    @Test
    public void execute_addActivityAcceptedByModel_successful() {
        Activity activity = ACTIVITY_TASK_1;
        ScheduleAddCommand scheduleAddCommand = new ScheduleAddCommand(activity);

        String expectedMessage = String.format(ScheduleAddCommand.MESSAGE_SUCCESS,
                activity.getActivityName(),
                Activity.getDateString(activity.getDate()));
        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.addActivity(activity);
        expectedModel.commitAddressBook();
        assertCommandSuccess(scheduleAddCommand, model, commandHistory, expectedMessage, expectedModel);
    }

    @Test
    public void equalsScheduleAddCommand() {
        Activity activityOne = ACTIVITY_TASK_1;
        Activity activityTwo = ACTIVITY_TASK_2;
        ScheduleAddCommand addActivityOneCommand = new ScheduleAddCommand(activityOne);
        ScheduleAddCommand addActivityTwoCommand = new ScheduleAddCommand(activityTwo);

        // same object -> returns true
        assertTrue(addActivityOneCommand.equals(addActivityOneCommand));

        // same values -> returns true
        ScheduleAddCommand addActivityOneCommandCopy = new ScheduleAddCommand(activityOne);
        assertTrue(addActivityOneCommand.equals(addActivityOneCommandCopy));

        // different types -> returns false
        assertFalse(addActivityOneCommand.equals(1));

        // null -> returns false
        assertFalse(addActivityOneCommand.equals(null));

        // different person -> returns false
        assertFalse(addActivityOneCommand.equals(addActivityTwoCommand));
    }

    ////ScheduleEditCommand tests
    @Test
    public void constructorScheduleEditCommand_nullActivity_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        new ScheduleEditCommand(null, null);
    }

    @Test
    public void execute_editActivityAcceptedByModel_successful() {
        List<Activity> activityList = model.getActivityList();
        Activity activityAtIndexOne = activityList.get(INDEX_FIRST_ACTIVITY.getZeroBased());
        Activity editedActivity = new Activity(activityAtIndexOne.getDate(), ACTIVITY_ONE_NAME);
        ScheduleEditCommand scheduleEditCommand = new ScheduleEditCommand(INDEX_FIRST_ACTIVITY, ACTIVITY_ONE_NAME);

        String expectedMessage = String.format(ScheduleEditCommand.MESSAGE_SUCCESS,
                activityAtIndexOne.getActivityName(),
                Activity.getDateString(activityAtIndexOne.getDate()),
                ACTIVITY_ONE_NAME);
        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.updateActivity(activityAtIndexOne, editedActivity);
        expectedModel.commitAddressBook();
        assertCommandSuccess(scheduleEditCommand, model, commandHistory, expectedMessage, expectedModel);
    }

    @Test
    public void execute_editInvalidActivityIndex_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getActivityList().size() + 1);
        ScheduleEditCommand scheduleEditCommand = new ScheduleEditCommand(outOfBoundIndex, ACTIVITY_TWO_NAME);
        assertCommandFailure(scheduleEditCommand, model, commandHistory, ScheduleCommand.MESSAGE_INVALID_INDEX);
    }

    @Test
    public void equalsScheduleEditCommand() {
        ScheduleEditCommand scheduleEditCommandCommand = new ScheduleEditCommand(INDEX_FIRST_ACTIVITY,
                ACTIVITY_ONE_NAME);

        // same values -> returns true
        ScheduleEditCommand scheduleEditCommandCommandCopy = new ScheduleEditCommand(INDEX_FIRST_ACTIVITY,
                ACTIVITY_ONE_NAME);
        assertTrue(scheduleEditCommandCommand.equals(scheduleEditCommandCommandCopy));

        // same object -> returns true
        assertTrue(scheduleEditCommandCommand.equals(scheduleEditCommandCommand));

        // null -> returns false
        assertFalse(scheduleEditCommandCommand.equals(null));

        // different types -> returns false
        assertFalse(scheduleEditCommandCommand.equals(new ClearCommand()));

        // different index -> returns false
        assertFalse(scheduleEditCommandCommand.equals(new ScheduleEditCommand(INDEX_SECOND_ACTIVITY,
                ACTIVITY_ONE_NAME)));

        // different descriptor -> returns false
        assertFalse(scheduleEditCommandCommand.equals(new ScheduleEditCommand(INDEX_FIRST_ACTIVITY,
                ACTIVITY_TWO_NAME)));
    }

    ////ScheduleDeleteCommand tests
    @Test
    public void execute_deleteValidIndex_success() {
        Activity activityToDelete = model.getActivityList().get(INDEX_FIRST_ACTIVITY.getZeroBased());
        ScheduleDeleteCommand scheduleDeleteCommand = new ScheduleDeleteCommand(INDEX_FIRST_ACTIVITY);

        String expectedMessage = String.format(ScheduleDeleteCommand.MESSAGE_SUCCESS,
                activityToDelete.getActivityName(),
                Activity.getDateString(activityToDelete.getDate()));

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.deleteActivity(activityToDelete);
        expectedModel.commitAddressBook();

        assertCommandSuccess(scheduleDeleteCommand, model, commandHistory, expectedMessage, expectedModel);
    }

    @Test
    public void execute_deleteInvalidIndex_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getActivityList().size() + 1);
        ScheduleDeleteCommand scheduleDeleteCommand = new ScheduleDeleteCommand(outOfBoundIndex);

        assertCommandFailure(scheduleDeleteCommand, model, commandHistory, ScheduleCommand.MESSAGE_INVALID_INDEX);
    }

    @Test
    public void equals() {
        ScheduleDeleteCommand deleteFirstCommand = new ScheduleDeleteCommand(INDEX_FIRST_ACTIVITY);
        ScheduleDeleteCommand deleteSecondCommand = new ScheduleDeleteCommand(INDEX_SECOND_ACTIVITY);

        // same object -> returns true
        assertTrue(deleteFirstCommand.equals(deleteFirstCommand));

        // same values -> returns true
        ScheduleDeleteCommand deleteFirstCommandCopy = new ScheduleDeleteCommand(INDEX_FIRST_ACTIVITY);
        assertTrue(deleteFirstCommand.equals(deleteFirstCommandCopy));

        // different types -> returns false
        assertFalse(deleteFirstCommand.equals(1));

        // null -> returns false
        assertFalse(deleteFirstCommand.equals(null));

        // different person -> returns false
        assertFalse(deleteFirstCommand.equals(deleteSecondCommand));
    }
}
