//@@author LowGinWee
package seedu.address.model.schedule;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * A TreeMap with Unique Dates of activities as keys. Each Date has a list of Activities, with the same date,
 * as its value.
 */
public class Schedule {
    private final TreeMap<Date, ArrayList<Activity>> schedule = new TreeMap<>();

    /**
     * Constructs the Schedule.
     */
    public Schedule() {
    }

    /**
     * Sets the schedule based on a list of activities
     */
    public void setSchedule(List<Activity> activities) {
        requireNonNull(activities);
        schedule.clear();
        for (Activity activity : activities) {
            add(activity);
        }
    }

    /**
     * @return a list of all activities, sorted based on their dates
     */
    public ObservableList<Activity> getActivities() {
        ObservableList<Activity> activities = FXCollections.observableArrayList();
        for (Date date : schedule.keySet()) {
            activities.addAll(schedule.get(date));
        }
        return FXCollections.unmodifiableObservableList(activities);
    }

    /**
     * @return schedule.
     */
    public TreeMap<Date, ArrayList<Activity>> getSchedule() {
        return schedule;
    }

    /**
     * Adds an activity to the schedule.
     * @param activity A valid activity.
     */
    public void add(Activity activity) {
        requireNonNull(activity);
        Date date = activity.getDate();
        if (!contains(date)) {
            schedule.put(date, new ArrayList<>());
        }
        schedule.get(date).add(activity);
    }

    /**
     * Deletes an activity from the schedule.
     * @param activity A valid activity.
     */
    public void delete(Activity activity) {
        requireNonNull(activity);
        schedule.get(activity.getDate()).remove(activity);
        if (schedule.get(activity.getDate()).isEmpty()) {
            schedule.remove(activity.getDate());
        }
    }

    /**
     * Checks if a date is unique in the schedule.
     * @param date A valid date.
     * @return A boolean if the date exists.
     */
    private boolean contains(Date date) {
        if (schedule.containsKey(date)) {
            return true;
        }
        return false;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Schedule // instanceof handles nulls
                && schedule.equals(((Schedule) other).schedule));
    }


}
