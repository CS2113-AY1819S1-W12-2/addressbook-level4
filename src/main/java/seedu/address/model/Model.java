package seedu.address.model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

import javafx.collections.ObservableList;
import seedu.address.model.person.Person;
import seedu.address.model.person.ReadPersonOnly;

/**
 * The API of the Model component.
 */
public interface Model {
    /** {@code Predicate} that always evaluate to true */
    Predicate<Person> PREDICATE_SHOW_ALL_PERSONS = unused -> true;

    /** Clears existing backing model and replaces with the provided new data. */
    void resetData(ReadOnlyAddressBook newData);

    /** Returns the AddressBook */
    ReadOnlyAddressBook getAddressBook();

    /**
     * Returns true if a person with the same identity as {@code person} exists in the address book.
     */
    boolean hasPerson(Person person);

    /**
     * Deletes the given person.
     * The person must exist in the address book.
     */
    void deletePerson(Person target);

    /**
     * Adds the given person.
     * {@code person} must not already exist in the address book.
     */
    void addPerson(Person person);

    /**
     * Replaces the given person {@code target} with {@code editedPerson}.
     * {@code target} must exist in the address book.
     * The person identity of {@code editedPerson} must not be the same as another existing person in the address book.
     */
    void updatePerson(Person target, Person editedPerson);

    /** Returns an unmodifiable view of the filtered person list */
    ObservableList<Person> getFilteredPersonList();

    /**
     * Updates the filter of the filtered person list to filter by the given {@code predicate}.
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredPersonList(Predicate<Person> predicate);

    /**
     * Returns true if the model has previous address book states to restore.
     */
    boolean canUndoAddressBook();

    /**
     * Returns true if the model has undone address book states to restore.
     */
    boolean canRedoAddressBook();

    /**
     * Restores the model's address book to its previous state.
     */
    void undoAddressBook();

    /**
     * Restores the model's address book to its previously undone state.
     */
    void redoAddressBook();

    /**
     * Saves the current address book state for undo/redo.
     */
    void commitAddressBook();

    //@@author lekoook
    /**
     * Retrieves a list of possible predictions for a command box input
     * @param textInput text input from command box
     * @return a list of predictions
     */
    ArrayList<String> getCmdPrediction(String textInput);

    /**
     * Adds a Person's attributes to the respective Trie instances for auto complete
     * @param person the person to add
     */
    void addPersonToTrie(Person person);

    /**
     * Deletes a Person's attributes from the respective Trie instances for auto complete
     * @param person the person to delete
     */
    void deletePersonFromTrie(Person person);

    /**
     * Removes all entries in all Trie instances
     */
    void clearAllTries();

    /**
     * Edits a Person's attributes in each respective Trie instances for auto complete.
     * @param personToEdit the original person.
     * @param editedPerson the new person.
     */
    void editPersonInTrie(Person personToEdit, Person editedPerson);

    void setSelectedPersons(List<Person> personListView);

    List<Person> getSelectedPersons();

    //@author peiying98
    /** Sorts address book list */
    void sortPerson(Comparator<ReadPersonOnly> sortComparator, boolean isReverseOrder) throws NoPersonsException;
}
