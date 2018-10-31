//@@author lekoook
package seedu.address.model.autocomplete;

import static org.junit.Assert.assertEquals;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;
import java.util.function.Predicate;

import org.junit.Test;

import javafx.collections.ObservableList;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.person.Person;
import seedu.address.model.schedule.Activity;
import seedu.address.model.tag.Tag;
import seedu.address.testutil.TypicalPersons;

public class CommandCompleterTest {

    @Test
    public void predictText_validArgs_returnCorrectPredictions() {
        ModelStubWithDefaultPersons modelStubWithDefaultPersons = new ModelStubWithDefaultPersons();
        CommandCompleter commandCompleter = new CommandCompleter(modelStubWithDefaultPersons);

        ArrayList<String> actualCommandPredictions = commandCompleter.predictText("e");
        ArrayList<String> expectedCommandPredictions = new ArrayList<>(Arrays.asList("dit ", "xit ", "xport "));

        ArrayList<String> actualNamePredictions = commandCompleter.predictText("find n/Alice");
        ArrayList<String> expectedNamePredictions = new ArrayList<>(Arrays.asList(" Pauline "));

        ArrayList<String> actualAddressPredictions = commandCompleter.predictText("find a/wall");
        ArrayList<String> expectedAddressPredictions = new ArrayList<>(Arrays.asList(" street "));

        ArrayList<String> actualPhonePredictions = commandCompleter.predictText("find p/9");
        ArrayList<String> expectedPhonePredictions =
                new ArrayList<>(Arrays.asList("4351253 ", "482224 ", "482427 ", "8765432 ", "5352563 ", "667777 "));

        ArrayList<String> actualEmailPredictions = commandCompleter.predictText("find e/a");
        ArrayList<String> expectedEmailPredictions =
                new ArrayList<>(Arrays.asList("lice@example.com ", "nna@example.com "));

        ArrayList<String> actualTagPredictions = commandCompleter.predictText("list t/f");
        ArrayList<String> expectedTagPredictions = new ArrayList<>(Arrays.asList("riends "));

        // Empty predictions
        ArrayList<String> actualEmptyPredictions = commandCompleter.predictText("exit ");
        ArrayList<String> expectedEmptyPredictions = new ArrayList<>();

        assertEquals(expectedCommandPredictions, actualCommandPredictions);
        assertEquals(expectedNamePredictions, actualNamePredictions);
        assertEquals(expectedAddressPredictions, actualAddressPredictions);
        assertEquals(expectedPhonePredictions, actualPhonePredictions);
        //assertEquals(expectedEmailPredictions, actualEmailPredictions);
        assertEquals(expectedTagPredictions, actualTagPredictions);
        assertEquals(expectedEmptyPredictions, actualEmptyPredictions);
    }

    @Test
    public void insertPerson_returnCorrectPredictions() {
        ModelStubWithDefaultPersons modelStubWithDefaultPersons = new ModelStubWithDefaultPersons();
        CommandCompleter commandCompleter = new CommandCompleter(modelStubWithDefaultPersons);

        commandCompleter.insertPerson(TypicalPersons.ANNABELLE);

        ArrayList<String> actualNamePredictions = commandCompleter.predictText("find n/A");
        ArrayList<String> expectedNamePredictions = new ArrayList<>(Arrays.asList("lice Pauline ", "nnabelle "));

        ArrayList<String> actualPhonePredictions = commandCompleter.predictText("find p/1");
        ArrayList<String> expectedPhonePredictions = new ArrayList<>(Arrays.asList("968 "));

        ArrayList<String> actualAddressPredictions = commandCompleter.predictText("find a/M");
        ArrayList<String> expectedAddressPredictions = new ArrayList<>(Arrays.asList("onroe, Connecticut "));

        ArrayList<String> actualEmailPredictions = commandCompleter.predictText("find e/a");
        ArrayList<String> expectedEmailPredictions =
                new ArrayList<>(Arrays.asList("lice@example.com", "nna@example.com", "nnabelle@warren.com"));

        ArrayList<String> actualTagPredictions = commandCompleter.predictText("list t/s");
        ArrayList<String> expectedTagPredictions = new ArrayList<>(Arrays.asList("carer "));

        assertEquals(expectedNamePredictions, actualNamePredictions);
        assertEquals(expectedPhonePredictions, actualPhonePredictions);
        assertEquals(expectedAddressPredictions, actualAddressPredictions);
        //assertEquals(expectedEmailPredictions, actualEmailPredictions);
        assertEquals(expectedTagPredictions, actualTagPredictions);
    }

    @Test
    public void clearData_initialModelWithPerson_emptyEntries() {
        ModelStubWithDefaultPersons modelStubWithDefaultPersons = new ModelStubWithDefaultPersons();
        CommandCompleter commandCompleter = new CommandCompleter(modelStubWithDefaultPersons);

        commandCompleter.clearData();

        ArrayList<String> actualEmptyPredictions = commandCompleter.predictText("find n/A");
        ArrayList<String> expectedEmptyPredictions = new ArrayList<>();

        assertEquals(expectedEmptyPredictions, actualEmptyPredictions);
    }

    @Test
    public void reinitialise_initialModelWithPerson_updatedEntries() {
        ModelStub modelStubWithAddPerson = new ModelStubWithAddPerson();
        CommandCompleter commandCompleter = new CommandCompleter(modelStubWithAddPerson);

        // Adding a new person into the Model directly.
        modelStubWithAddPerson.addPerson(TypicalPersons.ANNABELLE);
        commandCompleter.reinitialise();

        ArrayList<String> actualNewPredictions = commandCompleter.predictText("find n/A");
        ArrayList<String> expectedNewPredictions = new ArrayList<>(Arrays.asList("lice Pauline ", "nnabelle "));

        assertEquals(expectedNewPredictions, actualNewPredictions);
    }

    private class ModelStub implements Model {
        @Override
        public void addPerson(Person person) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void resetData(ReadOnlyAddressBook newData) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasPerson(Person person) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void deletePerson(Person target) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updatePerson(Person target, Person editedPerson) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<Person> getFilteredPersonList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updateFilteredPersonList(Predicate<Person> predicate) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean canUndoAddressBook() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean canRedoAddressBook() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void undoAddressBook() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void redoAddressBook() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void commitAddressBook() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public TextPrediction getTextPrediction() {
            return new CommandCompleter(this);
        }

        @Override
        public void setSelectedPersons(List<Person> personListView) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public List<Person> getSelectedPersons() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void reinitAddressbook() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public TreeMap<Date, ArrayList<Activity>> getSchedule() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addActivity(Activity activity) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void deleteActivity(Activity activity) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updateActivity(Activity target, Activity editedActivity) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<Activity> getActivityList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public List<Tag> getUniqueTagList() {
            throw new AssertionError("This method should not be called.");
        }
    }

    private class ModelStubWithDefaultPersons extends ModelStub {
        @Override
        public ReadOnlyAddressBook getAddressBook() {
            return getTypicalAddressBook();
        }
    }


    private class ModelStubWithAddPerson extends ModelStub {
        private AddressBook addressBook = getTypicalAddressBook();

        @Override
        public void addPerson(Person person) {
            addressBook.addPerson(person);
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            return addressBook;
        }
    }
}
