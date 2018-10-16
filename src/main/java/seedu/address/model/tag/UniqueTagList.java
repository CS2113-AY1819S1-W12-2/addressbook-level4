package seedu.address.model.tag;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import seedu.address.model.person.Person;
import seedu.address.model.person.UniquePersonList;

/**
 *
 * //TODO write description
 *
 *
 */
public class UniqueTagList {
    private final ObservableMap<Tag, UniquePersonList> internalList = FXCollections.observableHashMap();

    /**
     * Returns true if the list contains an equivalent tag as the given argument.
     */
    public boolean contains(Tag toCheck) {
        requireNonNull(toCheck);
        return internalList.containsKey(toCheck);
    }

    /**
     * Returns the List of all unique tags.
     */
    public List<Tag> getUniqueTagList() {
        if (internalList.isEmpty()) {
            throw new TagNotFoundException();
        }
        return new ArrayList<>(internalList.keySet());
    }

    /**
     * Returns the List of person associated with given tag.
     */
    public List<Person> getPersons(Tag tag) {
        if (!contains(tag)) {
            throw new TagNotFoundException();
        }
        return new ArrayList<>(internalList.get(tag).getPersons());
    }

    /**
     * Adds a tag to the list.
     * The tag must not already exist in the list.
     */
    public void add(Tag toAdd) {
        requireNonNull(toAdd);
        internalList.put(toAdd, new UniquePersonList());
    }

    /**
     * Adds all tags that belong to the person to the Map as a Key and adds the person to its (@Code UniquePersonList).
     */
    //TODO refactor this
    public void add(Person person) {
        requireNonNull(person);
        for (Tag tag : person.getTags()) {
            requireNonNull(tag);
            if (!contains(tag)) {
                internalList.put(tag, new UniquePersonList());
            }
            internalList.get(tag).add(person);
        }

    }

    /**
     * Replaces the tags of person {@code target} in the list with the tags of the edited person{@code editedPerson}.
     * {@code target} must exist in the list.
     * The person identity of {@code editedPerson} must not be the same as another existing person in the Map.
     */
    public void setTag(Person target, Person editedPerson) {
        requireAllNonNull(target, editedPerson);

        remove(target);
        add(editedPerson);
    }

    /**
     * Removes the tags of the equivalent person from the Map.
     * The person must exist in the list.
     */
    public void remove(Person toRemove) {
        requireNonNull(toRemove);
        for (Tag t : toRemove.getTags()) {
            internalList.get(t).remove(toRemove);
            if (internalList.get(t).isEmpty()) {
                internalList.remove(t);
            }
        }
    }

    //TODO to edit description
    /**
     * Replaces the contents of this Map with {@code tagListMap}.
     */
    public void setTags(ObservableMap <Tag, UniquePersonList> tagListMap) {
        requireAllNonNull(tagListMap);

        internalList.clear();
        for (Tag tag : tagListMap.keySet()) {
            if (!contains(tag)) {
                internalList.put(tag, new UniquePersonList());
            }
            internalList.get(tag).setPersons(tagListMap.get(tag));
        }
    }

    /**
     * Returns the backing list as an unmodifiable {@code ObservableMap}.
     */
    public ObservableMap<Tag, UniquePersonList> asUnmodifiableObservableMap() {
        return FXCollections.unmodifiableObservableMap(internalList);
    }

    /*TODO replace iterate
    @Override
    public Iterator<Tag> iterator() {
        return internalList.iterator();
    }*/

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof seedu.address.model.tag.UniqueTagList // instanceof handles nulls
                && internalList.equals(((seedu.address.model.tag.UniqueTagList) other).internalList));
    }

    @Override
    public int hashCode() {
        return internalList.hashCode();
    }
}
