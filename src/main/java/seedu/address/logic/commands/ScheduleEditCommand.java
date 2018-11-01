//@@author LowGinWee
package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.COMMAND_SCHEDULE_EDIT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ACTIVITY;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.schedule.Activity;

/**
 * Edits an {@code Activity} to the schedule in the address book.
 */
public class ScheduleEditCommand extends ScheduleCommand {
    public static final String COMMAND_WORD = COMMAND_SCHEDULE_EDIT;
    public static final String MESSAGE_SUCCESS = "Task \"%s\" on %s has been edited to \"%s\".";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Edit task, by index, from schedule.\n"
            + "parameters: INDEX " + PREFIX_ACTIVITY + "Task";
    private final String task;
    private final Index index;

    /**
     * Creates an ScheduleEditCommand to edit the specified {@code Activity}
     */
    public ScheduleEditCommand(Index index, String task) {
        requireNonNull(task);
        requireNonNull(index);
        this.task = task;
        this.index = index;
    }

    @Override
    public CommandResult updateSchedule(Model model) throws CommandException {
        Activity toDelete = getActivityFromIndex(model, index);
        Activity toAdd = new Activity(toDelete.getDate(), task);
        model.updateActivity(toDelete, toAdd);
        return new CommandResult(String.format(MESSAGE_SUCCESS,
                toDelete.getActivityName(),
                Activity.getDateString(toAdd.getDate()),
                toAdd.getActivityName()));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof ScheduleEditCommand// instanceof handles nulls
                && task.equals(((ScheduleEditCommand) other).task)
                && index.equals(((ScheduleEditCommand) other).index));
    }
}
