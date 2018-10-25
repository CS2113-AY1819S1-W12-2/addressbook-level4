package seedu.address.model;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;
import java.util.function.Predicate;
import java.util.logging.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import seedu.address.commons.core.ComponentManager;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.model.AddressBookChangedEvent;
import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.model.autocomplete.CommandCompleter;
import seedu.address.model.autocomplete.TextPrediction;
import seedu.address.model.person.Person;
import seedu.address.model.schedule.Activity;
import seedu.address.model.tag.Tag;
import seedu.address.model.util.SampleDataUtil;
import seedu.address.storage.AddressBookStorage;
import seedu.address.storage.XmlAddressBookStorage;

/**
 * Represents the in-memory model of the address book data.
 */
public class ModelManager extends ComponentManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final VersionedAddressBook versionedAddressBook;
    private final FilteredList<Person> filteredPersons;
    private List<Person> selectedPersons;
    private TextPrediction textPrediction;

    /**
     * Initializes a ModelManager with the given addressBook and userPrefs.
     */
    public ModelManager(ReadOnlyAddressBook addressBook, UserPrefs userPrefs) {
        super();
        requireAllNonNull(addressBook, userPrefs);
        logger.fine("Initializing with address book: " + addressBook + " and user prefs " + userPrefs);

        versionedAddressBook = new VersionedAddressBook(addressBook);
        filteredPersons = new FilteredList<>(versionedAddressBook.getPersonList());
        textPrediction = new CommandCompleter(this);
    }

    public ModelManager() {
        this(new AddressBook(), new UserPrefs());
    }

    @Override
    public void resetData(ReadOnlyAddressBook newData) {
        versionedAddressBook.resetData(newData);
        indicateAddressBookChanged();
    }

    @Override
    public ReadOnlyAddressBook getAddressBook() {
        return versionedAddressBook;
    }

    /** Raises an event to indicate the model has changed */
    private void indicateAddressBookChanged() {
        raise(new AddressBookChangedEvent(versionedAddressBook));
    }

    @Override
    public boolean hasPerson(Person person) {
        requireNonNull(person);
        return versionedAddressBook.hasPerson(person);
    }

    @Override
    public void deletePerson(Person target) {
        versionedAddressBook.removePerson(target);
        indicateAddressBookChanged();
    }

    @Override
    public void addPerson(Person person) {
        versionedAddressBook.addPerson(person);
        updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        indicateAddressBookChanged();
    }

    @Override
    public void updatePerson(Person target, Person editedPerson) {
        requireAllNonNull(target, editedPerson);

        versionedAddressBook.updatePerson(target, editedPerson);
        indicateAddressBookChanged();
    }

    //=========== Filtered Person List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Person} backed by the internal list of
     * {@code versionedAddressBook}
     */
    @Override
    public ObservableList<Person> getFilteredPersonList() {
        return FXCollections.unmodifiableObservableList(filteredPersons);
    }

    @Override
    public void updateFilteredPersonList(Predicate<Person> predicate) {
        requireNonNull(predicate);
        filteredPersons.setPredicate(predicate);
    }

    //=========== Undo/Redo =================================================================================

    @Override
    public boolean canUndoAddressBook() {
        return versionedAddressBook.canUndo();
    }

    @Override
    public boolean canRedoAddressBook() {
        return versionedAddressBook.canRedo();
    }

    @Override
    public void undoAddressBook() {
        versionedAddressBook.undo();
        indicateAddressBookChanged();
        //@@author lekoook
        textPrediction.reinitialise();
        //@@author
    }

    @Override
    public void redoAddressBook() {
        versionedAddressBook.redo();
        indicateAddressBookChanged();
        //@@author lekoook
        textPrediction.reinitialise();
        //@@author
    }

    @Override
    public void commitAddressBook() {
        versionedAddressBook.commit();
    }

    @Override
    public boolean equals(Object obj) {
        // short circuit if same object
        if (obj == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(obj instanceof ModelManager)) {
            return false;
        }

        // state check
        ModelManager other = (ModelManager) obj;
        return versionedAddressBook.equals(other.versionedAddressBook)
                && filteredPersons.equals(other.filteredPersons);
    }

    //@@author lekoook

    /**
     * Get the TextPrediction instance.
     * @return the TextPrediction instance used for text prediction.
     */
    public TextPrediction getTextPrediction() {
        return textPrediction;
    }

    /**
     * Initialises the list of selected Persons in address book.
     * @param selectedPersons the list to initialise with.
     */
    public void setSelectedPersons(List<Person> selectedPersons) {
        this.selectedPersons = selectedPersons;
    }

    /**
     * Returns the list of selected Persons in address book.
     * @return the list of selected Persons.
     */
    public List<Person> getSelectedPersons() {
        return this.selectedPersons;
    }

    //@@author lws803
    /**
     * Reinitialises the address book
     */
    @Override
    public void reinitAddressbook() {
        UserPrefs userPref = new UserPrefs();
        Path path = Paths.get(userPref.getAddressBookFilePath().toString());
        replaceData(path);
    }

    //@@author lws803
    /**
     * Method to replace data for reinitAddressbook and restoreAddressbook
     * @param path path of .xml file
     */
    @Override
    public void replaceData(Path path) {
        XmlAddressBookStorage storage = new XmlAddressBookStorage(path);
        ReadOnlyAddressBook initialData;
        try {
            initialData = storage.readAddressBook().orElseGet(SampleDataUtil::getSampleAddressBook);
            resetData(initialData);
        } catch (IOException ioe) {
            logger.warning(ioe.getMessage());
        } catch (DataConversionException dataE) {
            logger.warning(dataE.getMessage());
        }
    }

    //@@author Limminghong
    /**
     * Create a backup snapshot in the ".backup" folder
     * @param path to the snapshot
     */
    @Override
    public void backUpAddressbook(Path path) {
        try {
            ReadOnlyAddressBook initialData = versionedAddressBook;
            AddressBookStorage backupStorage = new XmlAddressBookStorage(path);
            backupStorage.saveAddressBook(initialData);
        } catch (IOException io) {
            logger.severe(io.getMessage());
        }
    //@@author LowGinWee

    /**
     * Get a list of unique tags of all persons in the addressbook
     * @return a list of unique tags.
     */
    @Override
    public List<Tag> getUniqueTagList() {
        return versionedAddressBook.getUniqueTagList();
    }
    /**
     * Adds an activity to the schedule in the address book.
     */
    @Override
    public void addActivity(Activity activity) {
        versionedAddressBook.addActivity(activity);
        indicateAddressBookChanged();
    }
    /**
     * Deletes an activity from the schedule in the address book.
     */
    @Override
    public void deleteActivity(Activity activity) {
        versionedAddressBook.deleteActivity(activity);
        indicateAddressBookChanged();
    }
    /**
     * Get the sorted list of activities in the schedule.
     * @return the list of activities.
     */
    @Override
    public ObservableList<Activity> getActivityList() {
        return versionedAddressBook.getActivityList();
    }
    /**
     * Get a TreeMap with the Date of activities as its key and a list of the corresponding activities as its value.
     * @return TreeMap of dates and activity lists.
     */
    @Override
    public TreeMap<Date, ArrayList<Activity>> getSchedule() {
        return versionedAddressBook.getSchedule();
    }
}
