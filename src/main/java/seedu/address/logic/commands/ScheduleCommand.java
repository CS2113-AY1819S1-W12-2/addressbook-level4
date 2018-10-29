//@@author LowGinWee
package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.COMMAND_SCHEDULE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ACTIVITY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;

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

    public static final String COMMAND_WORD = COMMAND_SCHEDULE;
    public static final String COMMAND_WORD_ADD = "add";
    public static final String COMMAND_WORD_DELETE = "delete";
    public static final String COMMAND_WORD_EDIT = "edit";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": updates contents in your schedule.\n"
            + "parameters: \"add\", \"edit\" or \"delete\"";
    //TODO to check for duplicates
    public static final String MESSAGE_DUPLICATE_TASK = "This task already exists in the schedule";
    public static final String MESSAGE_INVALID_INDEX = "Index is not valid";
    public static final String MESSAGE_ADD = COMMAND_WORD
            + " "
            + COMMAND_WORD_ADD
            + ": Adds a new task to your schedule.\n"
            + "parameters: "
            + PREFIX_DATE + "DD/MM/YYYY "
            + PREFIX_ACTIVITY + "task";
    public static final String MESSAGE_DELETE = COMMAND_WORD
            + " "
            + COMMAND_WORD_DELETE
            + ": Deletes task, by index, from schedule.\n"
            + "parameters: INDEX";
    public static final String MESSAGE_EDIT = COMMAND_WORD
            + " "
            + COMMAND_WORD_EDIT
            + ": Edit task, by index, from schedule.\n"
            + "parameters: INDEX " + PREFIX_ACTIVITY + "Task";

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
