package seedu.address.model;

import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import seedu.address.model.person.Person;
import seedu.address.model.tag.Tag;
import seedu.address.model.person.UniquePersonList;

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

}
