# lekoook
###### /java/seedu/address/ui/CommandBox.java
``` java
    /**
     * Invokes the methods to auto complete the command.
     * @param textInput the text input from command box.
     */
    private void predictCmd(String textInput) {
        ArrayList<String> output = logic.getCmdPrediction(textInput);
        handlePredictions(output, textInput);
    }

    /**
     * Processes the prediction output from {@code CommandCompleter}.
     * @param input the list of predictions to display.
     * @param textInput the prefix to the predictions to concatenate with.
     */
    private void handlePredictions(ArrayList<String> input, String textInput) {
        if (input.size() == 0) {
            logger.info(Messages.MESSAGE_INVALID_AUTOCOMPLETE_FORMAT);
            raise(new NewResultAvailableEvent(Messages.MESSAGE_EMPTY_STRING));
        } else if (input.size() == 1) {
            commandTextField.appendText(input.get(0));
        } else {
            StringBuilder output = new StringBuilder();
            for (String item : input) {
                output.append(textInput).append(item).append("\n");
            }
            raise(new NewResultAvailableEvent(output.toString()));
        }
    }
}
```
###### /java/seedu/address/commons/util/StringUtil.java
``` java

    /**
     * Tokenizes a string input with white spaces into a list of {@code Index}.
     *
     * @param input the user string input.
     * @return the list of tokenized {@code Index}.
     */
    public static ArrayList<Index> tokenizeIndexWithSpace(String input) {
        ArrayList<Index> output = new ArrayList<>();
        StringTokenizer tokenizer = new StringTokenizer(input);
        while (tokenizer.hasMoreTokens()) {
            Index zeroBasedIndex = Index.fromOneBased(Integer.valueOf(tokenizer.nextToken()));
            output.add(zeroBasedIndex);
        }
        return output;
    }

    /**
     * Tokenizes a string input with the range delimiter into a list of {@code Index}.
     *
     * @param input the user string input.
     * @return the list of tokenized {@code Index}.
     */
    public static ArrayList<Index> tokenizeIndexWithRange(String input) {
        ArrayList<Index> output = new ArrayList<>();
        StringTokenizer tokenizer = new StringTokenizer(input, "- \t\n\r\f");

        // Do a token count check here

        int start = Integer.valueOf(tokenizer.nextToken());
        int end = Integer.valueOf(tokenizer.nextToken());

        for (int i = start; i <= end; i++) {
            output.add(Index.fromOneBased(i));
        }

        return output;
    }

    /**
     * Determines if a string contains non zero unsigned {@code Index} only.
     *
     * @param input the user input string.
     * @return true if contains, false otherwise.
     */
    public static boolean areNonZeroUnsignedInteger(String input) {
        requireNonNull(input);

        StringTokenizer tokenizer = new StringTokenizer(input);
        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();
            if (!token.matches(REGEX_INTEGERS) || Integer.valueOf(token) <= 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * Determines if a user input conforms to the {@code Index} range selection format with the help of regex.
     *
     * @param input the user input string.
     * @return true if conforms, false otherwise.
     */
    public static boolean isRangeIndexFormat(String input) {
        return input.trim().matches(REGEX_INDEX_RANGE_FORMAT);
    }

    /**
     * Determines if a user input has a valid select command format.
     *
     * @param input the user input string.
     * @return true if confirms, false otherwise.
     */
    public static boolean isValidSelectSyntax(String input) {
        if (isRangeIndexFormat(input) || areNonZeroUnsignedInteger(input)) {
            return true;
        }
        return false;
    }
}
```
###### /java/seedu/address/commons/util/FileEncryptor.java
``` java
    public static String getExtension() {
        return extension;
    }
}
```
###### /java/seedu/address/logic/Logic.java
``` java
    /**
     * Retrieves a list of possible predictions for a command box input
     * @param textInput text input from command box
     * @return a list of predictions
     */
    ArrayList<String> getCmdPrediction(String textInput);
}
```
###### /java/seedu/address/logic/parser/MailCommandParser.java
``` java
package seedu.address.logic.parser;

import seedu.address.logic.commands.MailCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses the arguments to be use by MailCommand.
 */
public class MailCommandParser implements Parser<MailCommand> {
    /**
     * Parses the arguments of a mail command.
     * @param args the arguments to be parsed.
     * @return the resulting MailCommand instance.
     * @throws ParseException if the format of the argument is incorrect.
     */
    public MailCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, CliSyntax.PREFIX_ALL, CliSyntax.PREFIX_TAG);

        if (argMultimap.getValue(CliSyntax.PREFIX_ALL).isPresent()) {
            return new MailCommand(MailCommand.TYPE_ALL);
        } else if (argMultimap.getValue(CliSyntax.PREFIX_TAG).isPresent()) {
            return new MailCommand(MailCommand.TYPE_GROUPS, argMultimap.getValue(CliSyntax.PREFIX_TAG).get());
        } else {
            return new MailCommand(MailCommand.TYPE_SELECTION);
        }
    }
}
```
###### /java/seedu/address/logic/parser/ParserUtil.java
``` java
    /**
     * Parses one or more {@code Index} into an {@code Index} list and returns it.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @param oneBasedIndex the user input string.
     * @return the list of {@code Index} to return.
     */
    private static ArrayList<Index> parseMultipleIndex(String oneBasedIndex) {
        String trimmedIndex = oneBasedIndex.trim();
        return StringUtil.tokenizeIndexWithSpace(trimmedIndex);
    }

    /**
     * Parses a range or multiple ranges of {@code Index} into an {@code Index} list and returns it.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @param oneBasedIndex the user input string.
     * @return the list of {@code Index} to return.
     */
    private static ArrayList<Index> parseMultipleRangeIndex(String oneBasedIndex) {
        ArrayList<Index> output = new ArrayList<>();
        StringTokenizer tokenizer = new StringTokenizer(oneBasedIndex, ",");
        while (tokenizer.hasMoreTokens()) {
            ArrayList<Index> indices = StringUtil.tokenizeIndexWithRange(tokenizer.nextToken());
            output.addAll(indices);
        }
        return output;
    }

    /**
     * Parses a single, multiple, or range(s) of {@code Index} into an {@code Index} list and returns it.
     * Leading and trailing whitespaces will be trimmed.
     *
     * A range is defined by using a '-' between two indices, inclusive. Multiple ranges are separated with
     * a comma.
     * Whitespaces are ignored.
     *
     * For example, a valid input specifying ranges could be "1 - 3, 5-7".
     *
     * @param oneBasedIndex the user input string.
     * @return the list of {@code Index} to return.
     * @throws ParseException if the specified index is invalid (not non-zero unsigned integer).
     */
    public static ArrayList<Index> parseSelectIndex(String oneBasedIndex) throws ParseException {

        // Perform a syntax check here
        if (!StringUtil.isValidSelectSyntax(oneBasedIndex)) {
            throw new ParseException(MESSAGE_INVALID_COMMAND_FORMAT);
        }

        if (StringUtil.isRangeIndexFormat(oneBasedIndex)) {
            return parseMultipleRangeIndex(oneBasedIndex);
        } else {
            return parseMultipleIndex(oneBasedIndex);
        }
    }
}
```
###### /java/seedu/address/logic/commands/MailCommand.java
``` java
package seedu.address.logic.commands;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import seedu.address.commons.util.FileEncryptor;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.CliSyntax;
import seedu.address.model.Model;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.model.tag.Tag;

/**
 * Opens the system's default email application.
 * If arguments are present, the email attributes are filled accordingly.
 */
public class MailCommand extends Command {
    public static final String COMMAND_WORD = CliSyntax.COMMAND_MAIL;

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Emails the person using default application.";

    /**
     * Determine which contacts to mail to.
     */
    public static final int TYPE_SELECTION = 1;
    public static final int TYPE_GROUPS = 2;
    public static final int TYPE_ALL = 3;

    private static final String MESSAGE_SUCCESS = "Mailing to: ";
    private static final String MESSAGE_UNSUPPORTED = "System mail application is unsupported.";
    private static final String MESSAGE_EMPTY_SELECTION = "No contacts selected! Select one or more and try again.";

    /**
     * Instance variables
     */
    private int mailType;
    private String mailArgs;
    private Desktop desktop;

    /**
     * Creates a default Mail command
     */
    public MailCommand(int mailType) {
        this.mailType = mailType;
        desktop = Desktop.getDesktop();
    }

    public MailCommand(int mailType, String mailArgs) {
        this.mailType = mailType;
        this.mailArgs = mailArgs;
        desktop = Desktop.getDesktop();
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        UserPrefs userPref = new UserPrefs();
        FileEncryptor fe = new FileEncryptor(userPref.getAddressBookFilePath().toString());

        if (fe.isLocked()) {
            throw new CommandException(FileEncryptor.MESSAGE_ADDRESS_BOOK_LOCKED);
        }

        // Unsupported desktops will lead to error and crash
        if (!Desktop.isDesktopSupported()) {
            throw new CommandException(MESSAGE_UNSUPPORTED);
        }

        ArrayList<Person> mailingList;
        switch(mailType) {
        case TYPE_SELECTION:
            mailingList = mailToSelection(model);
            break;
        case TYPE_GROUPS:
            mailingList = mailToGroups(model, new Tag(mailArgs.trim()));
            break;
        default:
            mailingList = mailToAll(model);
        }
        String recipients = buildRecipients(mailingList);

        return new CommandResult(MESSAGE_SUCCESS + recipients);
    }

    /**
     * Opens system's default email application with selected contacts as recipients.
     * @param model containing the contacts.
     * @return the list of Persons mailed to.
     * @throws CommandException if there is error in creating URI.
     */
    private ArrayList<Person> mailToSelection(Model model) throws CommandException {
        ArrayList<Person> list = new ArrayList<>(model.getSelectedPersons());
        ArrayList<String> emailList = retrieveEmails(list);
        URI uriToMail = createUri(emailList);
        sendWithUri(uriToMail);
        return list;
    }

    /**
     * Opens system's default email application with contacts belonging to specified Tag as recipients.
     * @param model containing the contacts.
     * @return the list of Persons mailed to.
     * @throws CommandException if there is error in creating URI.
     */
    private ArrayList<Person> mailToGroups(Model model, Tag tag) throws CommandException {
        ArrayList<Person> list = new ArrayList<>(model.getFilteredPersonList());
        list.removeIf(person -> !person.getTags().contains(tag));
        ArrayList<String> emailList = retrieveEmails(list);
        URI uriToMail = createUri(emailList);
        sendWithUri(uriToMail);
        return list;
    }

    /**
     * Opens system's default email application with all contacts as recipients.
     * @param model containing the contacts.
     * @return the list of Persons mailed to.
     * @throws CommandException if there is error in creating URI.
     */
    private ArrayList<Person> mailToAll(Model model) throws CommandException {
        ArrayList<Person> list = new ArrayList<>(model.getFilteredPersonList());
        ArrayList<String> emailList = retrieveEmails(model.getFilteredPersonList());
        URI uriToMail = createUri(emailList);
        sendWithUri(uriToMail);
        return list;
    }

    /**
     * Extracts all emails given a list of Person.
     * @param personList the list of Person.
     * @return the list of extracted emails.
     */
    private ArrayList<String> retrieveEmails(List<Person> personList) {
        ArrayList<String> emailList = new ArrayList<>();
        for (Person person : personList) {
            emailList.add(person.getEmail().value);
        }
        return emailList;
    }

    /**
     * Builds the URI to be used in opening the mail application.
     * @param emailList the list of extracted emails to send to.
     * @return the URI to be used by the mail application.
     * @throws CommandException if there is syntax error in the URI.
     */
    private URI createUri(ArrayList<String> emailList) throws CommandException {
        StringBuilder uriToMail = new StringBuilder("mailto:");
        URI uri;

        if (emailList.size() == 0) {
            throw new CommandException(MESSAGE_EMPTY_SELECTION);
        } else {
            for (String email : emailList) {
                uriToMail.append(email).append(",");
            }
        }

        try {
            uri = new URI(uriToMail.toString());
        } catch (URISyntaxException e) {
            throw new CommandException(e.getMessage());
        }
        return uri;
    }

    /**
     * Opens the system's default email application given the specified URI.
     * @param uriToMail URI specifying the recipients.
     * @throws CommandException if unable to open the email application.
     */
    private void sendWithUri(URI uriToMail) throws CommandException {
        try {
            desktop.mail(uriToMail);
        } catch (UnsupportedOperationException | IOException | SecurityException e) {
            throw new CommandException(e.getMessage());
        }
    }

    /**
     * Builds the string of names of recipients mailed to.
     * @param mailingList the list of recipients.
     * @return the string including all recipients.
     */
    private String buildRecipients(ArrayList<Person> mailingList) {
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < mailingList.size(); i++) {
            output.append(mailingList.get(i).getName().fullName);
            if (i < mailingList.size() - 1) {
                output.append(", ");
            }
        }
        return output.toString();
    }
}
```
###### /java/seedu/address/logic/LogicManager.java
``` java
    /**
     * Retrieves a list of possible predictions for a command box input
     * @param textInput text input from command box
     * @return a list of predictions
     */
    @Override
    public ArrayList<String> getCmdPrediction(String textInput) {
        return model.getTextPrediction().predictText(textInput);
    }

    /**
     * Check to see if commandText is a password command
     * @param commandText
     * @return true if commandText is password sensitive
     */
    private boolean isSensitive (String commandText) {
        String[] splited = commandText.split("\\s+");
        if (splited.length > 1) {
            if (splited[0].compareTo("password") == 0) {
                return true;
            }
        }
        return false;
    }

}
```
###### /java/seedu/address/model/autocomplete/AutoCompleteParserPair.java
``` java
package seedu.address.model.autocomplete;

import seedu.address.logic.parser.Prefix;

/**
 * Pair object used in parsing of commands for auto complete functionality
 */
public class AutoCompleteParserPair {
    public final Prefix predictionType;
    public final String prefixValue;

    public AutoCompleteParserPair(Prefix predictionType, String prefixValue) {
        this.predictionType = predictionType;
        this.prefixValue = removeLeadingWhitespace(prefixValue);
    }

    /**
     * Removes leading whitespace for correct text prediction.
     * @param input the user input to trim.
     * @return the trimmed input.
     */
    private String removeLeadingWhitespace(String input) {
        for (int index = 0; index < input.length(); index++) {
            if (!Character.isWhitespace(input.charAt(index))) {
                return input.substring(index);
            }
        }
        return input;
    }
}
```
###### /java/seedu/address/model/autocomplete/AutoCompleteArgumentsParser.java
``` java
package seedu.address.model.autocomplete;

import static seedu.address.logic.parser.CliSyntax.COMMAND_DELETE;
import static seedu.address.logic.parser.CliSyntax.COMMAND_FIND;
import static seedu.address.logic.parser.CliSyntax.COMMAND_IMPORT;
import static seedu.address.logic.parser.CliSyntax.COMMAND_LIST;
import static seedu.address.logic.parser.CliSyntax.COMMAND_MAIL;
import static seedu.address.logic.parser.CliSyntax.COMMAND_SELECT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_COMMAND;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_INVALID;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import seedu.address.logic.parser.ArgumentMultimap;
import seedu.address.logic.parser.ArgumentTokenizer;
import seedu.address.logic.parser.Prefix;

/**
 * Parses the arguments of a command text input for auto completing the command
 */
public class AutoCompleteArgumentsParser {

    /**
     * Parses the arguments to be used for auto completing of commands
     * @param arguments the full string of arguments to parse
     * @return the prefix that will be used for the prediction
     */
    public static AutoCompleteParserPair parse(String command, String arguments, ArgumentMultimap argMultimap) {
        if (arguments.isEmpty()) {
            return new AutoCompleteParserPair(PREFIX_COMMAND, command);
        }

        // TODO: Add or remove command support as necessary
        switch(command) {
        case COMMAND_FIND:
            return getFindParserPair(arguments, argMultimap);
        case COMMAND_LIST:
            return getListParserPair(arguments, argMultimap);
        case COMMAND_SELECT:
        case COMMAND_DELETE:
        case COMMAND_IMPORT:
        case COMMAND_MAIL:
            return getMailParserPair(arguments, argMultimap);
        default:
            return new AutoCompleteParserPair(PREFIX_INVALID, arguments.trim());
        }
    }

    /**
     * Creates a AutoCompleteParserPair instance based on the last Prefix found in find command.
     * @param arguments the user input to search.
     * @param argMultimap used to retrieve the values of the prefix keys.
     * @return the appropriate AutoCompleteParserPair instance.
     */
    private static AutoCompleteParserPair getFindParserPair(String arguments, ArgumentMultimap argMultimap) {
        Prefix lastPrefix = ArgumentTokenizer.findLastPrefix(
                arguments,
                PREFIX_NAME,
                PREFIX_EMAIL,
                PREFIX_PHONE,
                PREFIX_ADDRESS,
                PREFIX_TAG);

        if (argMultimap.getValue(lastPrefix).isPresent()) {
            return new AutoCompleteParserPair(lastPrefix, argMultimap.getValue(lastPrefix).get());
        }
        // Default text prediction is names
        return new AutoCompleteParserPair(PREFIX_NAME, arguments);
    }

    /**
     * Creates a AutoCompleteParserPair instance based on the last Prefix found in mail command.
     * @param arguments the user input to search.
     * @param argMultimap used to retrieve the values of the prefix keys.
     * @return the appropriate AutoCompleteParserPair instance.
     */
    private static AutoCompleteParserPair getMailParserPair(String arguments, ArgumentMultimap argMultimap) {
        Prefix lastPrefix = ArgumentTokenizer.findLastPrefix(arguments, PREFIX_TAG, PREFIX_NAME);
        if (argMultimap.getValue(lastPrefix).isPresent()) {
            return new AutoCompleteParserPair(lastPrefix, argMultimap.getValue(lastPrefix).get());
        }
        // Default text prediction is names
        return new AutoCompleteParserPair(PREFIX_NAME, arguments);
    }

    /**
     * Creates a AutoCompleteParserPair instance based on the last Prefix found in list command.
     * @param arguments the user input to search.
     * @param argMultimap used to retrieve the values of the prefix keys.
     * @return the appropriate AutoCompleteParserPair instance.
     */
    private static AutoCompleteParserPair getListParserPair(String arguments, ArgumentMultimap argMultimap) {
        Prefix lastPrefix = ArgumentTokenizer.findLastPrefix(
                arguments,
                PREFIX_TAG);
        if (argMultimap.getValue(lastPrefix).isPresent()) {
            return new AutoCompleteParserPair(lastPrefix, argMultimap.getValue(lastPrefix).get());
        }
        // Default text prediction is nothing
        return new AutoCompleteParserPair(PREFIX_INVALID, arguments);
    }
}
```
###### /java/seedu/address/model/autocomplete/CommandCompleter.java
``` java
package seedu.address.model.autocomplete;

import java.util.ArrayList;
import java.util.List;

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
        commandList.add(CliSyntax.COMMAND_SCHEDULE_ADD);
        commandList.add(CliSyntax.COMMAND_SCHEDULE_EDIT);
        commandList.add(CliSyntax.COMMAND_SCHEDULE_DELETE);
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
    public void removePerson(Person person, List<Tag> uniqueTagList) {
        nameTrie.remove(person.getName().fullName);
        phoneTrie.remove(person.getPhone().value);
        emailTrie.remove(person.getEmail().value);
        addressTrie.remove(person.getAddress().value);
        for (Tag tag : person.getTags()) {
            if (!uniqueTagList.contains(tag)) {
                tagTrie.remove(tag.toString());
            }
        }
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
    public void editPerson(Person personToEdit, Person editedPerson, List<Tag> uniqueTagList) {
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
            for (Tag tag : personToEdit.getTags()) {
                if (!uniqueTagList.contains(tag)) {
                    tagTrie.remove(tag.toString());
                }
            }
            for (Tag tag : editedPerson.getTags()) {
                tagTrie.insert(tag.toString());
            }
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
```
###### /java/seedu/address/model/autocomplete/AutoCompleteParser.java
``` java
package seedu.address.model.autocomplete;

import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_INVALID;
import static seedu.address.logic.parser.CliSyntax.PREFIX_KPI;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NOTE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_POSITION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import seedu.address.logic.parser.ArgumentMultimap;
import seedu.address.logic.parser.ArgumentTokenizer;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses the text input for auto completing the command
 */
public class AutoCompleteParser {
    /**
     * Pattern instance used to separate command and it's arguments
     */
    private static final Pattern COMMAND_INPUT_FORMAT = Pattern.compile("(?<commandWord>\\S+)(?<arguments>.*)");

    private AutoCompleteArgumentsParser argumentsParser;

    /**
     * Default constructor
     */
    public AutoCompleteParser() {
        argumentsParser = new AutoCompleteArgumentsParser();
    }

    /**
     * TODO: Extend to other attributes
     * Parses the command to be used for auto completing of commands
     * @param textInput text to be parsed
     * @return a pair of values to be used to determine the Trie to use for auto complete
     * @throws ParseException if the user input does not conform the expected format
     */
    public AutoCompleteParserPair parseCommand(String textInput) {
        final Matcher matcher = COMMAND_INPUT_FORMAT.matcher(textInput);

        if (!matcher.matches()) {
            return new AutoCompleteParserPair(PREFIX_INVALID, PREFIX_INVALID.getPrefix());
        }

        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(
                textInput,
                PREFIX_NAME,
                PREFIX_PHONE,
                PREFIX_EMAIL,
                PREFIX_ADDRESS,
                PREFIX_NOTE,
                PREFIX_TAG,
                PREFIX_KPI,
                PREFIX_POSITION);

        final String commandWord = matcher.group("commandWord");
        final String arguments = matcher.group("arguments");
        return AutoCompleteArgumentsParser.parse(commandWord, arguments, argMultimap);
    }
}
```
###### /java/seedu/address/model/trie/TrieNode.java
``` java
package seedu.address.model.trie;

import java.util.ArrayList;

/**
 * A TrieNode instance represents a node in the Trie structure
 */
public class TrieNode {

    private boolean isEndNode;
    private char value;
    private TrieNode parent;
    private ArrayList<TrieNode> children;

    /**
     * Default constructor with no parameters
     * @param value
     */
    public TrieNode(char value) {
        // Initialise all instance variables
        isEndNode = false;
        parent = null;
        children = new ArrayList<>();
        this.value = value;
    }

    public TrieNode getParent() {
        return this.parent;
    }

    public void setParent(TrieNode parent) {
        this.parent = parent;
    }

    public char getValue() {
        return this.value;
    }

    public void setValue(char value) {
        this.value = value;
    }

    public ArrayList<TrieNode> getChildren() {
        return children;
    }

    public int getChildrenSize() {
        return children.size();
    }

    /**
     * Adds a child to the {@code TrieNode} instance.
     * @param child child node to be added
     * @return the newly added child
     */
    public TrieNode appendChild(TrieNode child) {
        children.add(child);
        children.get(children.size() - 1).setParent(this);
        return children.get(children.size() - 1);
    }

    public void removeChild(TrieNode child) {
        children.remove(child);
    }

    public TrieNode getFirstChild() {
        return children.get(0);
    }

    public boolean isEndNode() {
        return isEndNode;
    }

    public void setEndNode(boolean value) {
        isEndNode = value;
    }

    @Override
    public boolean equals(Object obj) {
        TrieNode other = (TrieNode) obj;
        return this.value == other.value;
    }
}
```
###### /java/seedu/address/model/trie/Trie.java
``` java
package seedu.address.model.trie;

import java.util.ArrayList;

/**
 * A bi-directional Tree structure that stems from the root node
 * Each node is a TrieNode object that contains the node information
 */

public class Trie {

    /**
     * Class constants
     */
    private static final char CHAR_ROOT = '.';
    private static final char CHAR_SPACE = ' ';

    /**
     * Class variables
     */
    private TrieNode root;
    private ArrayList<String> baseList;
    private ArrayList<String> predictionsList;

    /**
     * Default constructor
     */
    public Trie(ArrayList<String> inputList) {
        root = new TrieNode(CHAR_ROOT);
        baseList = new ArrayList<>(inputList);
        init();
    }

    /**
     * Initialises the Trie instance with the items in baseList
     */
    private void init() {

        for (int i = 0; i < baseList.size(); i++) {
            this.insertToGraph(baseList.get(i));
        }
    }

    /**
     * Inserts the input string value to the class instance
     * @param value
     */
    public void insert(String value) {
        // Check if this value exists
        if (!baseList.contains(value)) {
            insertToGraph(value);
            baseList.add(value);
        }
    }

    /**
     * Inserts the given string value to the Trie graph.
     * @param keyString the string value to be inserted
     */
    private void insertToGraph(String keyString) {
        // A TrieNode as pointer to traverse through the tree
        TrieNode pointer = root;

        // Run through all characters in the given key string
        for (int i = 0; i < keyString.length(); i++) {
            char ch = keyString.charAt(i);
            ArrayList<TrieNode> children = pointer.getChildren();

            if (children.contains(new TrieNode(ch))) {
                // Set the pointer to that node
                pointer = children.get(children.indexOf(new TrieNode(ch)));
            } else {
                // Create a new node
                TrieNode parent = pointer;
                pointer = pointer.appendChild(new TrieNode(ch));
                pointer.setParent(parent);
            }
        }
        // Mark end of word node
        pointer.setEndNode(true);
    }

    /**
     * Removes all string entries in the instance
     */
    public void clear() {
        root = new TrieNode(CHAR_ROOT);
        baseList = new ArrayList<>();
    }

    /**
     * Removes the input string value from the class instance
     * @param value the value to remove
     */
    public void remove(String value) {
        // Check if this value exists
        if (baseList.contains(value)) {
            removeFromGraph(value);
            baseList.remove(value);
        }
    }
    /**
     * Removes the input string value from the actual graph implementation
     * Traverse from the root to the last character (End node) of the string
     * Prerequisites: the value must exist in the Trie
     *
     * If the remove of this node does not affect any chidren node
     * (ie the node has no children), remove the whole word
     *
     * @param value the value to remove
     */
    private void removeFromGraph(String value) {
        TrieNode pointer;

        pointer = traverseToEndNode(value);

        // Check for the conditions for removal or setting of end node
        if (pointer.getChildrenSize() == 0) {
            removeWordFromGraph(pointer);
        } else {
            pointer.setEndNode(false);
        }
    }

    /**
     * Traverses the Trie from the start character to the end character of the given string value.
     * @param value the given value
     * @return the TrieNode referencing the end node
     */
    private TrieNode traverseToEndNode(String value) {
        TrieNode pointer = root;

        // Run through all characters in the string and reaches the end node
        for (int i = 0; i < value.length(); i++) {
            char ch = value.charAt(i);
            ArrayList<TrieNode> children = pointer.getChildren();
            pointer = children.get(children.indexOf(new TrieNode(ch)));
        }

        return pointer;
    }

    /**
     * Removes a word from the graph given the end node of that word.
     * Removes every parent level node until a node that has more than one child
     * or it is an end node.
     * @param pointer the first node in the graph to be removed
     */
    private void removeWordFromGraph(TrieNode pointer) {
        // Set this node as non-end node first.
        pointer.setEndNode(false);

        // Traverse upwards the trie.
        while (!pointer.isEndNode() && pointer.getChildrenSize() == 0) {
            TrieNode parent = pointer.getParent();
            parent.removeChild(pointer);
            pointer = parent;
        }
    }

    /**
     * Returns a list of strings that are predicted to complete the {@code prefix}.
     * @param prefix string prefix to be predicted
     * @return a list of string predictions
     */
    public ArrayList<String> getPredictList(String prefix) {
        predictionsList = new ArrayList<>();
        StringBuilder charStack = new StringBuilder();

        // skipToStartNode returns root node if the prefix does not exist in the Trie.
        TrieNode startNode = skipToStartNode(root, prefix);

        if (startNode.equals(root)) {
            return predictionsList;
        }

        // If startNode has ONE child OR LESS, the only possible text prediction is one that has characters
        // up till the character that has more than one child or this is the end of the branch.
        if (startNode.getChildrenSize() <= 1) {
            charStack = buildSingleStack(startNode);
            predictionsList.add(charStack.toString());
            return predictionsList;
        }

        // If startNode has more than one child, explore all possible strings.
        for (int i = 0; i < startNode.getChildren().size(); i++) {
            explore(charStack, startNode.getChildren().get(i));
        }

        return predictionsList;
    }

    /**
     * Returns a string that represents the only possible substring from a starting node.
     *
     * Given a starting node, traverse to the next neighbour node until there is more than one neighbouring
     * node or there is none. If each node only has one neighbour, this means there are no other possible
     * string combinations.
     * @param startNode the starting node
     * @return the only possible substring
     */
    private StringBuilder buildSingleStack(TrieNode startNode) {
        StringBuilder charStack = new StringBuilder();
        while (startNode.getChildrenSize() == 1) {
            charStack.append(startNode.getFirstChild().getValue());
            startNode = startNode.getFirstChild();
        }

        // End of a branch
        if (startNode.getChildrenSize() == 0) {
            charStack.append(CHAR_SPACE);
        }

        return charStack;
    }

    /**
     * Returns a {@code TrieNode} object that corresponds to the last character of a given string.
     *
     * From a starting node, the method traverse through all nodes that corresponds to every character
     * in the given string up until the last character.
     * @param start the starting node to traverse from
     * @param prefix the given string to traverse with
     * @return a TrieNode instance that holds a value equal to the last character of the {@code prefix}
     */
    private TrieNode skipToStartNode(TrieNode start, String prefix) {
        TrieNode current = start;

        for (int i = 0; i < prefix.length(); i++) {
            char ch = prefix.charAt(i);
            ArrayList<TrieNode> currentNodeChildren = current.getChildren();

            if (currentNodeChildren.contains(new TrieNode(ch))) {
                // If the node representing current character exists, move to that node
                current = currentNodeChildren.get(currentNodeChildren.indexOf(new TrieNode(ch)));
            } else {
                // If a character does not exist, just return the root node
                return root;
            }
        }
        return current;
    }

    /**
     * Traverses through the whole Trie structure to find all possible strings
     * @param charStack StringBuilder to build a possible strings
     * @param pointer the starting node to traverse from
     */
    private void explore(StringBuilder charStack, TrieNode pointer) {
        if (!pointer.equals(root)) {
            charStack.append(pointer.getValue());
        }

        // End character of the word
        if (pointer.isEndNode()) {
            // Branch end, so it is the last character of a word
            if (pointer.getChildrenSize() == 0) {
                charStack.append(CHAR_SPACE);
            }
            predictionsList.add(charStack.toString());
        }

        // Explore all neighbour nodes
        for (int i = 0; i < pointer.getChildren().size(); i++) {
            TrieNode neighbour = pointer.getChildren().get(i);
            explore(charStack, neighbour);
        }

        // Delete the whitespace that is appended after the end node of a branch
        if (pointer.isEndNode() && pointer.getChildrenSize() == 0) {
            charStack.deleteCharAt(charStack.length() - 1);
        }

        if (charStack.length() > 0) {
            charStack.deleteCharAt(charStack.length() - 1);
        }
    }

}
```
###### /java/seedu/address/model/ModelManager.java
``` java
        textPrediction.reinitialise();
```
###### /java/seedu/address/model/ModelManager.java
``` java
        textPrediction.reinitialise();
```
###### /java/seedu/address/model/ModelManager.java
``` java

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


```
###### /java/seedu/address/model/Model.java
``` java
    /**
     * Get the TextPrediction instance.
     * @return the TextPrediction instance used for text prediction.
     */
    TextPrediction getTextPrediction();

    /**
     * Initialises the list of selected Persons in address book.
     * @param selectedPersons the list to initialise with.
     */
    void setSelectedPersons(List<Person> selectedPersons);

    /**
     * Returns the list of selected Persons in address book.
     * @return the list of selected Persons.
     */
    List<Person> getSelectedPersons();

```
