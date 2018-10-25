//@@author LowGinWee
package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.model.Model;
import seedu.address.model.schedule.Activity;

/**
 * Adds an {@code Activity} to the schedule in the address book.
 */
public class ScheduleAddCommand extends ScheduleCommand {
    private static final String MESSAGE_SUCCESS = "Task \"%s\" on %s has been added to your schedule.";
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
