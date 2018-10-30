package seedu.address.model.autocomplete;

import java.util.ArrayList;

import seedu.address.model.person.Person;

/**
 * API of text prediction.
 */
public interface TextPrediction {
    /**
     * Predict the next possible list of text
     * @param textInput the string to be parsed
     * @return predicted list of text
     */
    ArrayList<String> predictText(String textInput);

    /**
     * Adds a Person's attributes to the respective Trie instances for auto complete
     * @param person the person to add
     */
    void insertPerson(Person person);

    /**
     * Deletes a Person's attributes from the respective Trie instances for auto complete
     * @param person the person to delete
     */
    void removePerson(Person person);

    /**
     * Edits a Person's attributes in each respective Trie instances for auto complete.
     * Compares the differences of attributes and only update the Trie instances for attributes that were changed.
     * @param personToEdit the original person.
     * @param editedPerson the new person.
     */
    void editPerson(Person personToEdit, Person editedPerson);

    /**
     * Removes all entries in all Trie instances
     */
    void clearData();

    /**
     * Reinitialise all data structures.
     * <p>
     * This method has a lower performance as compared to methods with specific operation (insertion, removal, etc.).
     * Hence, unless the all the data has changed, use this method sparingly.
     */
    void reinitialise();
}
