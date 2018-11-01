//@@author LowGinWee
package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.COMMAND_SCHEDULE_ADD;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ACTIVITY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;

import seedu.address.model.Model;
import seedu.address.model.schedule.Activity;

/**
 * Adds an {@code Activity} to the schedule in the address book.
 */
public class ScheduleAddCommand extends ScheduleCommand {
    public static final String COMMAND_WORD = COMMAND_SCHEDULE_ADD;
    public static final String MESSAGE_SUCCESS = "Task \"%s\" on %s has been added to your schedule.";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Adds a new task to your schedule.\n"
            + "parameters: "
            + PREFIX_DATE + "DD/MM/YYYY "
            + PREFIX_ACTIVITY + "task";
    private final Activity toAdd;

    /**
     * Creates an ScheduleAddCommand to add the specified {@code Activity}
     */
    public ScheduleAddCommand(Activity activity) {
        requireNonNull(activity);
        toAdd = activity;
    }

    @Override
    public CommandResult updateSchedule(Model model) {
        model.addActivity(toAdd);
        return new CommandResult(String.format(MESSAGE_SUCCESS,
                toAdd.getActivityName(),
                Activity.getDateString(toAdd.getDate())));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof ScheduleAddCommand// instanceof handles nulls
                && toAdd.equals(((ScheduleAddCommand) other).toAdd));
    }
}
