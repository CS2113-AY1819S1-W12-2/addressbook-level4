//@@author LowGinWee
package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.COMMAND_SCHEDULE_DELETE;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.schedule.Activity;

/**
 * Deletes an {@code Activity} to the schedule in the address book.
 */
public class ScheduleDeleteCommand extends ScheduleCommand {
    public static final String COMMAND_WORD = COMMAND_SCHEDULE_DELETE;
    public static final String MESSAGE_SUCCESS = "Task \"%s\" on %s has been deleted.";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes task, by index, from schedule.\n"
            + "parameters: INDEX";
    private final Index index;

    /**
     * Creates an ScheduleDeleteCommand to delete the specified {@code Activity}
     */
    public ScheduleDeleteCommand(Index index) {
        requireNonNull(index);
        this.index = index;
    }

    @Override
    public CommandResult updateSchedule(Model model) throws CommandException {
        Activity toDelete = getActivityFromIndex(model, index);
        model.deleteActivity(toDelete);
        return new CommandResult(String.format(MESSAGE_SUCCESS,
                toDelete.getActivityName(),
                Activity.getDateString(toDelete.getDate())));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof ScheduleDeleteCommand// instanceof handles nulls
                && index.equals(((ScheduleDeleteCommand) other).index));
    }
}
