package seedu.address.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.AMY;
import static seedu.address.testutil.TypicalPersons.BENSON;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.model.person.NameContainsKeywordsPredicate;
import seedu.address.testutil.AddressBookBuilder;

public class ModelManagerTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private ModelManager modelManager = new ModelManager();

    @Test
    public void hasPerson_nullPerson_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        modelManager.hasPerson(null);
    }

    @Test
    public void hasPerson_personNotInAddressBook_returnsFalse() {
        assertFalse(modelManager.hasPerson(ALICE));
    }

    @Test
    public void hasPerson_personInAddressBook_returnsTrue() {
        modelManager.addPerson(ALICE);
        assertTrue(modelManager.hasPerson(ALICE));
    }

    @Test
    public void getFilteredPersonList_modifyList_throwsUnsupportedOperationException() {
        thrown.expect(UnsupportedOperationException.class);
        modelManager.getFilteredPersonList().remove(0);
    }

    @Test
    public void equals() {
        AddressBook addressBook = new AddressBookBuilder().withPerson(ALICE).withPerson(BENSON).build();
        AddressBook differentAddressBook = new AddressBook();
        UserPrefs userPrefs = new UserPrefs();

        // same values -> returns true
        modelManager = new ModelManager(addressBook, userPrefs);
        ModelManager modelManagerCopy = new ModelManager(addressBook, userPrefs);
        assertTrue(modelManager.equals(modelManagerCopy));

        // same object -> returns true
        assertTrue(modelManager.equals(modelManager));

        // null -> returns false
        assertFalse(modelManager.equals(null));

        // different types -> returns false
        assertFalse(modelManager.equals(5));

        // different addressBook -> returns false
        assertFalse(modelManager.equals(new ModelManager(differentAddressBook, userPrefs)));

        // different filteredList -> returns false
        String[] keywords = ALICE.getName().fullName.split("\\s+");
        modelManager.updateFilteredPersonList(new NameContainsKeywordsPredicate(Arrays.asList(keywords)));
        assertFalse(modelManager.equals(new ModelManager(addressBook, userPrefs)));

        // resets modelManager to initial state for upcoming tests
        modelManager.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);

        // different userPrefs -> returns true
        UserPrefs differentUserPrefs = new UserPrefs();
        differentUserPrefs.setAddressBookFilePath(Paths.get("differentFilePath"));
        assertTrue(modelManager.equals(new ModelManager(addressBook, differentUserPrefs)));
    }

    @Test
    public void predictText_validArgs_success() {
        ArrayList<String> actualOutput = modelManager.predictText("f");
        ArrayList<String> expectedOutput = new ArrayList<>(Arrays.asList("ind "));

        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void insertPersonIntoPrediction_returnCorrectPrediction() {
        modelManager.insertPersonIntoPrediction(ALICE);

        ArrayList<String> actualOutput = modelManager.predictText("find n/A");
        ArrayList<String> expectedOutput = new ArrayList<>(Arrays.asList("lice Pauline "));

        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void removePersonFromPrediction_returnCorrectPrediction() {
        modelManager.insertPersonIntoPrediction(ALICE);
        modelManager.insertPersonIntoPrediction(AMY);

        ArrayList<String> actualOutputBeforeRemoval = modelManager.predictText("find n/A");
        ArrayList<String> expectedOutputBeforeRemoval = new ArrayList<>(Arrays.asList("lice Pauline ", "my Bee "));
        assertEquals(expectedOutputBeforeRemoval, actualOutputBeforeRemoval);

        modelManager.removePersonFromPrediction(ALICE);
        ArrayList<String> actualOutputAfterRemoval = modelManager.predictText("find n/A");
        ArrayList<String> expectedOutputAfterRemoval = new ArrayList<>(Arrays.asList("my Bee "));
        assertEquals(expectedOutputAfterRemoval, actualOutputAfterRemoval);
    }

    @Test
    public void editPersonInPrediction_returnCorrectPrediction() {
        modelManager.insertPersonIntoPrediction(ALICE);

        ArrayList<String> actualOutputBeforeEdit = modelManager.predictText("find n/A");
        ArrayList<String> expectedOutputBeforeEdit = new ArrayList<>(Arrays.asList("lice Pauline "));
        assertEquals(expectedOutputBeforeEdit, actualOutputBeforeEdit);

        modelManager.editPersonInPrediction(ALICE, AMY);
        ArrayList<String> actualOutputAfterEdit = modelManager.predictText("find n/A");
        ArrayList<String> expectedOutputAfterEdit = new ArrayList<>(Arrays.asList("my Bee "));
        assertEquals(expectedOutputAfterEdit, actualOutputAfterEdit);
    }

    @Test
    public void clearInPrediction_returnEmptyPrediction() {
        modelManager.insertPersonIntoPrediction(ALICE);

        ArrayList<String> actualOutputBeforeClear = modelManager.predictText("find n/A");
        ArrayList<String> expectedOutputBeforeClear = new ArrayList<>(Arrays.asList("lice Pauline "));
        assertEquals(expectedOutputBeforeClear, actualOutputBeforeClear);

        modelManager.clearInPrediction();
        ArrayList<String> actualOutputAfterClear = modelManager.predictText("find n/A");
        ArrayList<String> expectedOutputAfterClear = new ArrayList<>();
        assertEquals(expectedOutputAfterClear, actualOutputAfterClear);
    }

    @Test
    public void reinitialisePrediction_returnCorrectPrediction() {
        modelManager.addPerson(ALICE);

        ArrayList<String> actualOutputBeforeReinit = modelManager.predictText("find n/A");
        ArrayList<String> expectedOutputBeforeReinit = new ArrayList<>();
        assertEquals(expectedOutputBeforeReinit, actualOutputBeforeReinit);

        modelManager.reinitialisePrediction();
        ArrayList<String> actualOutputAfterReinit = modelManager.predictText("find n/A");
        ArrayList<String> expectedOutputAfterReinit = new ArrayList<>(Arrays.asList("lice Pauline "));
        assertEquals(expectedOutputAfterReinit, actualOutputAfterReinit);
    }
}
