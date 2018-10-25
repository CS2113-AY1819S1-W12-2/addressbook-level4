package seedu.address.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.TreeMap;

import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import seedu.address.model.person.Person;
import seedu.address.model.person.UniquePersonList;
import seedu.address.model.schedule.Activity;
import seedu.address.model.tag.Tag;


/**
 * Unmodifiable view of an address book
 */
public interface ReadOnlyAddressBook {

    /**
     * Returns an unmodifiable view of the persons list.
     * This list will not contain any duplicate persons.
     */
    ObservableList<Person> getPersonList();
    ObservableMap<Tag, UniquePersonList> getTagList();
    ObservableList<Activity> getActivityList();

    TreeMap<Date, ArrayList<Activity>> getSchedule();
}
