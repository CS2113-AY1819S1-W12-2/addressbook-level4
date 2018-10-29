package seedu.address.model;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;

import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import seedu.address.model.person.Person;
import seedu.address.model.person.UniquePersonList;
import seedu.address.model.schedule.Activity;
import seedu.address.model.schedule.Schedule;
import seedu.address.model.tag.Tag;
import seedu.address.model.tag.UniqueTagList;

/**
 * Wraps all data at the address-book level
 * Duplicates are not allowed (by .isSamePerson comparison)
 */
public class AddressBook implements ReadOnlyAddressBook {

    private final UniquePersonList persons;
    private final UniqueTagList tags;
    private final Schedule schedule;

    /*
     * The 'unusual' code block below is an non-static initialization block, sometimes used to avoid duplication
     * between constructors. See https://docs.oracle.com/javase/tutorial/java/javaOO/initial.html
     *
     * Note that non-static init blocks are not recommended to use. There are other ways to avoid duplication
     *   among constructors.
     */
    {
        persons = new UniquePersonList();
        tags = new UniqueTagList();
        schedule = new Schedule();
    }

    public AddressBook() {}

    /**
     * Creates an AddressBook using the Persons in the {@code toBeCopied}
     */
    public AddressBook(ReadOnlyAddressBook toBeCopied) {
        this();
        resetData(toBeCopied);
    }

    //// list overwrite operations

    /**
     * Replaces the contents of the person list with {@code persons}.
     * {@code persons} must not contain duplicate persons.
     */
    public void setPersons(List<Person> persons) {
        this.persons.setPersons(persons);
    }

    /**
     * Resets the existing data of this {@code AddressBook} with {@code newData}.
     */
    public void resetData(ReadOnlyAddressBook newData) {
        requireNonNull(newData);

        setPersons(newData.getPersonList());
        setTags(newData.getTagList());
        setSchedule(newData.getActivityList());
    }

    //// person-level operations

    /**
     * Returns true if a person with the same identity as {@code person} exists in the address book.
     */
    public boolean hasPerson(Person person) {
        requireNonNull(person);
        return persons.contains(person);
    }

    /**
     * Adds a person to the address book.
     * The person must not already exist in the address book.
     */
    public void addPerson(Person p) {
        persons.add(p);
        tags.add(p);
    }

    /**
     * Replaces the given person {@code target} in the list with {@code editedPerson}.
     * {@code target} must exist in the address book.
     * The person identity of {@code editedPerson} must not be the same as another existing person in the address book.
     */
    public void updatePerson(Person target, Person editedPerson) {
        requireNonNull(editedPerson);

        persons.setPerson(target, editedPerson);
        tags.setTag(target, editedPerson);
    }

    /**
     * Removes {@code key} from this {@code AddressBook}.
     * {@code key} must exist in the address book.
     */
    public void removePerson(Person key) {
        persons.remove(key);
        tags.remove(key);
    }

    /**
     * @return the List of Unique Tags.
     */
    public List<Tag> getUniqueTagList() {
        return tags.getUniqueTagList();
    }

    /**
     * @return the List of persons associated with specified tag
     */
    public List<Person> getPersonsFromTag(Tag tag) {
        return tags.getPersons(tag);
    }

    //// util methods

    @Override
    public String toString() {
        return persons.asUnmodifiableObservableList().size() + " persons";
    }

    @Override
    public ObservableList<Person> getPersonList() {
        return persons.asUnmodifiableObservableList();
    }

    @Override
    public ObservableMap<Tag, UniquePersonList> getTagList() {
        return tags.asUnmodifiableObservableMap();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof AddressBook // instanceof handles nulls
                && persons.equals(((AddressBook) other).persons));
    }

    @Override
    public int hashCode() {
        return persons.hashCode();
    }

    //@@author LowGinWee
    /**
     * Replaces the contents of the tag HashMap with {@code tagListMap}.
     */
    public void setTags(ObservableMap<Tag, UniquePersonList> tagListMap) {
        this.tags.setTags(tagListMap);
    }

    /**
     * Replaces the contents of the schedule with {@code activities}.
     */
    public void setSchedule(List<Activity> activities) {
        this.schedule.setSchedule(activities);
    }

    /**
     * Adds an Activity to the schedule in the address book.
     */
    public void addActivity(Activity activity) {
        schedule.add(activity);
    }

    /**
     * Deletes an Activity from the schedule in the address book.
     */
    public void deleteActivity(Activity activity) {
        schedule.delete(activity);
    }

    /**
     * Replaces the given activity {@code target} with {@code editedActivity}.
     * {@code target} must exist in the address book.
     */
    public void updateActivity(Activity target, Activity editedActivity) {
        schedule.delete(target);
        schedule.add(editedActivity);
    }

    @Override
    public ObservableList<Activity> getActivityList() {
        return schedule.getActivities();
    }

    @Override
    public TreeMap<Date, ArrayList<Activity>> getSchedule() {
        return schedule.getSchedule();
    }
}
