//@@author LowGinWee
package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.FileEncryptor;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.UserPrefs;
import seedu.address.model.schedule.Activity;

/**
 * Updates the schedule in the address book.
 */
public abstract class ScheduleCommand extends Command {
    //TODO to check for duplicates
    public static final String MESSAGE_DUPLICATE_TASK = "This task already exists in the schedule";
    public static final String MESSAGE_INVALID_INDEX = "Index is not valid";




    /**
     * Updates the schedule, add, edit or delete.
     */
    public abstract CommandResult updateSchedule(Model model) throws CommandException;

    /**
     * @param index specified index of task to edit or delete.
     * @return {@code Activity} of specified index.
     */
    public Activity getActivityFromIndex (Model model, Index index) throws CommandException {
        List<Activity> activities = model.getActivityList();
        if (index.getZeroBased() >= activities.size()) {
            throw new CommandException(MESSAGE_INVALID_INDEX);
        }
        return activities.get(index.getZeroBased());
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);
        UserPrefs userPref = new UserPrefs();
        FileEncryptor fe = new FileEncryptor(userPref.getAddressBookFilePath().toString());

        if (fe.isLocked()) {
            throw new CommandException(FileEncryptor.MESSAGE_ADDRESS_BOOK_LOCKED);
        }

        CommandResult result = updateSchedule(model);
        model.commitAddressBook();
        return result;
    }
}
