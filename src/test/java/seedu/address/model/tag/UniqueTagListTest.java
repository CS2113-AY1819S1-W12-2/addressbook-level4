package seedu.address.model.tag;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BOB;
import static seedu.address.testutil.TypicalPersons.FRIENDS;

import java.util.ArrayList;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.model.person.Person;
import seedu.address.model.person.UniquePersonList;
import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.testutil.PersonBuilder;



public class UniqueTagListTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private final UniqueTagList uniqueTagList = new UniqueTagList();

    @Test
    public void contains_nullTag_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        uniqueTagList.contains(null);
    }

    @Test
    public void contains_tagNotInList_returnsFalse() {
        assertFalse(uniqueTagList.contains(FRIENDS));
    }

    @Test
    public void contains_tagInList_returnsTrue() {
        uniqueTagList.add(ALICE);
        assertTrue(uniqueTagList.contains(FRIENDS));
    }


    @Test
    public void add_nullTag_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        uniqueTagList.add((Tag) null);
    }

    @Test
    public void add_nullPerson_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        uniqueTagList.add((Person) null);
    }

    @Test
    public void add_duplicatePerson_throwsDuplicatePersonException() {
        uniqueTagList.add(ALICE);
        thrown.expect(DuplicatePersonException.class);
        uniqueTagList.add(ALICE);
    }

    @Test
    public void setTag_nullTargetPerson_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        uniqueTagList.setTag(null, ALICE);
    }

    @Test
    public void setTag_nullEditedPerson_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        uniqueTagList.setTag(ALICE, null);
    }

    @Test
    public void setTag_targetPersonNotInList_throwsTagNotFoundException() {
        thrown.expect(TagNotFoundException.class);
        uniqueTagList.setTag(ALICE, ALICE);
    }

    @Test
    public void setTag_editedPersonIsSamePerson_success() {
        uniqueTagList.add(ALICE);
        uniqueTagList.setTag(ALICE, ALICE);
        UniqueTagList expectedUniqueTagList = new UniqueTagList();
        expectedUniqueTagList.add(ALICE);
        assertEquals(expectedUniqueTagList, uniqueTagList);
    }

    @Test
    public void setTag_editedPersonHasSameIdentity_success() {
        uniqueTagList.add(ALICE);
        Person editedAlice = new PersonBuilder(ALICE).withTags(VALID_TAG_HUSBAND)
                .build();
        uniqueTagList.setTag(ALICE, editedAlice);
        UniqueTagList expectedUniqueTagList = new UniqueTagList();
        expectedUniqueTagList.add(editedAlice);
        assertEquals(expectedUniqueTagList, uniqueTagList);
    }

    @Test
    public void setTag_editedPersonHasDifferentIdentity_success() {
        uniqueTagList.add(ALICE);
        uniqueTagList.setTag(ALICE, BOB);
        UniqueTagList expectedUniqueTagList = new UniqueTagList();
        expectedUniqueTagList.add(BOB);
        assertEquals(expectedUniqueTagList, uniqueTagList);
    }


    @Test
    public void remove_nullTag_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        uniqueTagList.remove(null);
    }

    @Test
    public void remove_personDoesNotExist_throwsTagNotFoundException() {
        thrown.expect(TagNotFoundException.class);
        uniqueTagList.remove(ALICE);
    }

    @Test
    public void remove_existingPerson_removesPerson() {
        uniqueTagList.add(ALICE);
        uniqueTagList.remove(ALICE);
        UniqueTagList expectedUniqueTagList = new UniqueTagList();
        assertEquals(expectedUniqueTagList, uniqueTagList);
    }

    @Test
    public void setTags_nullMap_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        uniqueTagList.setTags(null);
    }

    @Test
    public void setTags_uniqueTagList_replacesOwnListWithProvidedMap() {
        uniqueTagList.add(ALICE);
        UniqueTagList expectedUniqueTagList = new UniqueTagList();
        expectedUniqueTagList.add(BOB);
        uniqueTagList.setTags(expectedUniqueTagList.asUnmodifiableObservableMap());
        assertEquals(expectedUniqueTagList, uniqueTagList);
    }


    @Test
    public void getPersons_tagNotInList_throwsTagNotFoundException() {
        thrown.expect(TagNotFoundException.class);
        uniqueTagList.getPersons(FRIENDS);
    }

    @Test
    public void getPersons_validTags_returnsUniquePersonList() {
        uniqueTagList.add(ALICE);
        UniquePersonList expectedPersonList = new UniquePersonList();
        expectedPersonList.add(ALICE);
        assertEquals(expectedPersonList.getPersons(), uniqueTagList.getPersons(FRIENDS));
    }

    @Test
    public void getUnqiueTagList_noTagInList_throwsTagNotFoundException() {
        thrown.expect(TagNotFoundException.class);
        uniqueTagList.getUniqueTagList();
    }

    @Test
    public void getUnqiueTagList_validTags_returnsTagList() {
        uniqueTagList.add(FRIENDS);
        List<Tag> expectedTagList = new ArrayList<>();
        expectedTagList.add(FRIENDS);
        assertEquals(expectedTagList, uniqueTagList.getUniqueTagList());
    }

    @Test
    public void asUnmodifiableObservableMap_modifyMap_throwsUnsupportedOperationException() {
        thrown.expect(UnsupportedOperationException.class);
        uniqueTagList.asUnmodifiableObservableMap().clear();
    }
}
