//@@author lekoook
package seedu.address.model.autocomplete;

import java.util.ArrayList;

import javafx.collections.ObservableList;
import seedu.address.logic.parser.CliSyntax;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.tag.Tag;
import seedu.address.model.trie.Trie;

/**
 * Completes the command for the user by predicting the possible substrings.
 */
public class CommandCompleter implements TextPrediction {

    /**
     * Model instance to access data.
     */
    private Model model;

    /**
     * Text prediction parser to parse user input.
     */
    private AutoCompleteParser parser;

    /**
     * Trie instances for various commands and arguments.
     */
    private Trie commandTrie;
    private Trie nameTrie;
    private Trie phoneTrie;
    private Trie emailTrie;
    private Trie addressTrie;
    private Trie tagTrie;

    /**
     * Word lists of strings used to instantiate Trie objects.
     */
    private ArrayList<String> commandList;
    private ArrayList<String> nameList;
    private ArrayList<String> phoneList;
    private ArrayList<String> emailList;
    private ArrayList<String> addressList;
    private ArrayList<String> tagList;

    /**
     * Creates a command completer with the {@code model} data.
     * @param model the data represented in the address book.
     */
    public CommandCompleter(Model model) {
        this.model = model;
        this.parser = new AutoCompleteParser();
        this.commandList = new ArrayList<>();
        this.nameList = new ArrayList<>();
        this.phoneList = new ArrayList<>();
        this.emailList = new ArrayList<>();
        this.addressList = new ArrayList<>();
        this.tagList = new ArrayList<>();
        initLists();
        initTries();
    }

    /**
     * Initialises all words lists.
     */
    private void initLists() {
        initCommandsList();
        initAttributesLists();
    }

    /**
     * Initialises command words list with keywords in {@code CliSyntax}.
     */
    private void initCommandsList() {
        commandList.add(CliSyntax.COMMAND_ADD);
        commandList.add(CliSyntax.COMMAND_CLEAR);
        commandList.add(CliSyntax.COMMAND_DELETE);
        commandList.add(CliSyntax.COMMAND_EDIT);
        commandList.add(CliSyntax.COMMAND_EXIT);
        commandList.add(CliSyntax.COMMAND_FIND);
        commandList.add(CliSyntax.COMMAND_HELP);
        commandList.add(CliSyntax.COMMAND_HISTORY);
        commandList.add(CliSyntax.COMMAND_LIST);
        commandList.add(CliSyntax.COMMAND_REDO);
        commandList.add(CliSyntax.COMMAND_SELECT);
        commandList.add(CliSyntax.COMMAND_UNDO);
        commandList.add(CliSyntax.COMMAND_PASSWORD);
        commandList.add(CliSyntax.COMMAND_MAIL);
        commandList.add(CliSyntax.COMMAND_BACKUP);
        commandList.add(CliSyntax.COMMAND_RESTORE);
        commandList.add(CliSyntax.COMMAND_IMPORT);
        commandList.add(CliSyntax.COMMAND_EXPORT);
    }

    /**
     * Initialises attributes words lists with attribute value in each {@code Person}.
     */
    private void initAttributesLists() {
        // TODO: prevent duplicates from being added to lists
        ObservableList<Person> list = model.getAddressBook().getPersonList();
        for (Person item : list) {
            nameList.add(item.getName().fullName);
            phoneList.add(item.getPhone().value);
            emailList.add(item.getEmail().value);
            addressList.add(item.getAddress().value);
            // TODO: find a better way to do this
            for (Tag tag : item.getTags()) {
                tagList.add(tag.toString());
            }
        }
    }

    /**
     * Initialises all Trie instances using the words lists.
     */
    private void initTries() {
        commandTrie = new Trie(commandList);
        nameTrie = new Trie(nameList);
        phoneTrie = new Trie(phoneList);
        emailTrie = new Trie(emailList);
        addressTrie = new Trie(addressList);
        tagTrie = new Trie(tagList);
    }

    /**
     * Predict the next possible list of text
     * @param textInput the string to be parsed
     * @return predicted list of text
     */
    public ArrayList<String> predictText(String textInput) {
        AutoCompleteParserPair pair = parser.parseCommand(textInput);
        PredictionType predictionType = getPredictionType(pair);
        switch (predictionType) {
        case PREDICT_COMMAND:
            return commandTrie.getPredictList(pair.prefixValue);
        case PREDICT_NAME:
            return nameTrie.getPredictList(pair.prefixValue);
        case PREDICT_ADDRESS:
            return addressTrie.getPredictList(pair.prefixValue);
        case PREDICT_PHONE:
            return phoneTrie.getPredictList(pair.prefixValue);
        case PREDICT_EMAIL:
            return emailTrie.getPredictList(pair.prefixValue);
        case PREDICT_TAG:
            return tagTrie.getPredictList(pair.prefixValue);
        default:
            return new ArrayList<>();
        }
    }

    /**
     * Adds a Person's attributes to the respective Trie instances for auto complete.
     * @param person the person to add
     */
    @Override
    public void insertPerson(Person person) {
        nameTrie.insert(person.getName().fullName);
        phoneTrie.insert(person.getPhone().value);
        emailTrie.insert(person.getEmail().value);
        addressTrie.insert(person.getAddress().value);
        // TODO: find a better way to do this
        for (Tag tag : person.getTags()) {
            tagTrie.insert(tag.toString());
        }
    }

    /**
     * Deletes a Person's attributes from the respective Trie instances for auto complete.
     * @param person the person to delete
     */
    @Override
    public void removePerson(Person person) {
        nameTrie.remove(person.getName().fullName);
        phoneTrie.remove(person.getPhone().value);
        emailTrie.remove(person.getEmail().value);
        addressTrie.remove(person.getAddress().value);
        // TODO: find a way to delete single occurrence tags
    }

    /**
     * Removes all entries in all Trie instances
     */
    @Override
    public void clearData() {
        nameTrie.clear();
        phoneTrie.clear();
        emailTrie.clear();
        addressTrie.clear();
        tagTrie.clear();
    }

    /**
     * Edits a Person's attributes in each respective Trie instances for auto complete.
     * Compares the differences of attributes and only update the Trie instances for attributes that were changed.
     * @param personToEdit the original person.
     * @param editedPerson the new person.
     */
    @Override
    public void editPerson(Person personToEdit, Person editedPerson) {
        if (!personToEdit.getName().equals(editedPerson.getName())) {
            nameTrie.remove(personToEdit.getName().fullName);
            nameTrie.insert(editedPerson.getName().fullName);
        }
        if (!personToEdit.getEmail().equals(editedPerson.getEmail())) {
            emailTrie.remove(personToEdit.getEmail().value);
            emailTrie.insert(editedPerson.getEmail().value);
        }
        if (!personToEdit.getPhone().equals(editedPerson.getPhone())) {
            phoneTrie.remove(personToEdit.getPhone().value);
            phoneTrie.insert(editedPerson.getPhone().value);
        }
        if (!personToEdit.getAddress().equals(editedPerson.getAddress())) {
            addressTrie.remove(personToEdit.getAddress().value);
            addressTrie.insert(editedPerson.getAddress().value);
        }
        if (!personToEdit.getTags().equals(editedPerson.getTags())) {
            // TODO: find a way to edit the tags data structure
        }
    }

    /**
     * Determines the type of prefix to predict it's arguments.
     * @param pair containing the prefix.
     * @return the type of prefix.
     */
    private PredictionType getPredictionType(AutoCompleteParserPair pair) {
        if (pair.predictionType.equals(CliSyntax.PREFIX_TAG)) {
            return PredictionType.PREDICT_TAG;
        } else if (pair.predictionType.equals(CliSyntax.PREFIX_EMAIL)) {
            return PredictionType.PREDICT_EMAIL;
        } else if (pair.predictionType.equals(CliSyntax.PREFIX_ADDRESS)) {
            return PredictionType.PREDICT_ADDRESS;
        } else if (pair.predictionType.equals(CliSyntax.PREFIX_PHONE)) {
            return PredictionType.PREDICT_PHONE;
        } else if (pair.predictionType.equals(CliSyntax.PREFIX_NAME)) {
            return PredictionType.PREDICT_NAME;
        } else if (pair.predictionType.equals(CliSyntax.PREFIX_COMMAND)) {
            return PredictionType.PREDICT_COMMAND;
        } else {
            return PredictionType.PREDICT_INVALID;
        }
    }

    /**
     * Used to determine which Trie data structure to text predict from.
     */
    private enum PredictionType {
        PREDICT_ADDRESS,
        PREDICT_EMAIL,
        PREDICT_NAME ,
        PREDICT_PHONE,
        PREDICT_TAG,
        PREDICT_COMMAND,
        PREDICT_INVALID
    }

    /**
     * Reinitialise all data structures with given Model.
     */
    @Override
    public void reinitialise() {
        initLists();
        initTries();
    }
}
