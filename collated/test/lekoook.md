# lekoook
###### /java/seedu/address/commons/util/StringUtilTest.java
``` java
    @Test
    public void tokenizeIndexWithSpace_validArgs_correctIndex() {
        ArrayList<Index> actualOutput = StringUtil.tokenizeIndexWithSpace("  1 3   4 5      7");
        ArrayList<Index> expectedOutput = new ArrayList<>(Arrays.asList(
                Index.fromOneBased(1),
                Index.fromOneBased(3),
                Index.fromOneBased(4),
                Index.fromOneBased(5),
                Index.fromOneBased(7)));

        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void tokenizeIndexWithRange_validArgs_correctIndex() {
        ArrayList<Index> actualOutput = StringUtil.tokenizeIndexWithRange("     1 -    5");
        ArrayList<Index> expectedOutput = new ArrayList<>(Arrays.asList(
                Index.fromOneBased(1),
                Index.fromOneBased(2),
                Index.fromOneBased(3),
                Index.fromOneBased(4),
                Index.fromOneBased(5)));

        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void areNonZeroUnsignedInteger_validArgs_returnCorrectBoolean() {
        assertTrue(StringUtil.areNonZeroUnsignedInteger("1   2 3"));
        assertFalse(StringUtil.areNonZeroUnsignedInteger("-1 2 3"));
        assertFalse(StringUtil.areNonZeroUnsignedInteger("1 2 0 3"));
    }

    @Test
    public void isRangeIndexFormat_validFormat_returnCorrectBoolean() {
        assertTrue(StringUtil.isRangeIndexFormat(" 1  -    3"));
        assertTrue(StringUtil.isRangeIndexFormat("1 -  3,   6 - 9"));
        assertFalse(StringUtil.isRangeIndexFormat(" -  3, 6 - 10"));
    }

    @Test
    public void isValidSelectSyntax_validFormat_returnCorrectBoolean() {
        assertTrue(StringUtil.isValidSelectSyntax("1   2 3"));
        assertFalse(StringUtil.isValidSelectSyntax("-1 2 3"));
        assertFalse(StringUtil.isValidSelectSyntax("1 2 0 3"));
        assertTrue(StringUtil.isValidSelectSyntax(" 1  -    3"));
        assertTrue(StringUtil.isValidSelectSyntax("1 -  3,   6 - 9"));
        assertFalse(StringUtil.isValidSelectSyntax(" -  3, 6 - 10"));
    }
}
```
###### /java/seedu/address/logic/parser/MailCommandParserTest.java
``` java
package seedu.address.logic.parser;

import static seedu.address.logic.commands.MailCommand.TYPE_ALL;
import static seedu.address.logic.commands.MailCommand.TYPE_GROUPS;
import static seedu.address.logic.commands.MailCommand.TYPE_SELECTION;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.Test;

import seedu.address.logic.commands.MailCommand;

public class MailCommandParserTest {

    private MailCommandParser parser = new MailCommandParser();

    @Test
    public void parse_allPersons_success() {
        MailCommand expectedCommand = new MailCommand(TYPE_ALL);
        assertParseSuccess(parser, "mail " + CliSyntax.PREFIX_ALL.getPrefix(), expectedCommand);
    }

    @Test
    public void parse_selectedPersons_success() {
        MailCommand expectedCommand = new MailCommand(TYPE_SELECTION);
        assertParseSuccess(parser, "mail", expectedCommand);
    }

    @Test
    public void parse_selectedTags_success() {
        MailCommand expectedCommand = new MailCommand(TYPE_GROUPS, "");
        assertParseSuccess(parser, "mail " + CliSyntax.PREFIX_TAG, expectedCommand);
    }
}
```
###### /java/seedu/address/logic/parser/SelectCommandParserTest.java
``` java
    @Test
    public void parse_validArgs_returnsSelectCommandSingle() {
        assertParseSuccess(parser, "1", new SelectCommand(INDEX_LIST_FIRST));
    }

    @Test
    public void parse_validArgs_returnSelectCommandMultiple() {
        assertParseSuccess(parser, "1 2 3", new SelectCommand(INDEX_LIST_THREE));
    }

    @Test
    public void parse_validArgs_returnSelectCommandRange() {
        assertParseSuccess(parser, "1 - 3", new SelectCommand(INDEX_LIST_THREE));
    }

    @Test
    public void parse_validArgs_returnSelectCommandMultipleRange() {
        assertParseSuccess(parser, "1  -  3 , 5 -7", new SelectCommand(INDEX_LIST_SIX));
    }

    @Test
    public void parse_invalidSingleNegative_throwsParseException() {
        assertParseFailure(parser, "-1", invalidMessage);
    }

    @Test
    public void parse_invalidMultipleNegative_throwsParseException() {
        assertParseFailure(parser, "-1 -2 3", invalidMessage);
    }

    @Test
    public void parse_invalidRangeFormat_throwsParseException() {
        assertParseFailure(parser, "1 -- 3", invalidMessage);
    }

    @Test
    public void parse_invalidRangeIndex_throwsParseException() {
        assertParseFailure(parser, "-3 - 3", invalidMessage);
    }
}
```
###### /java/seedu/address/logic/parser/ParserUtilTest.java
``` java
    @Test
    public void parseSelectIndex_validInput_success() throws Exception {
        // No white spaces, single Index
        assertEquals(INDEX_LIST_FIRST, ParserUtil.parseSelectIndex("1"));

        // No white spaces, multiple Index
        assertEquals(INDEX_LIST_THREE, ParserUtil.parseSelectIndex("1 2 3"));

        // No white spaces, single range Index
        assertEquals(INDEX_LIST_THREE, ParserUtil.parseSelectIndex("1-3"));

        // No white spaces, multiple range Index
        assertEquals(INDEX_LIST_SIX, ParserUtil.parseSelectIndex("1-3,5-7"));

        // With white spaces, single Index
        assertEquals(INDEX_LIST_FIRST, ParserUtil.parseSelectIndex("  1     "));

        // With white spaces, multiple Index
        assertEquals(INDEX_LIST_THREE, ParserUtil.parseSelectIndex(" 1      2   3    "));

        // With white spaces, single range Index
        assertEquals(INDEX_LIST_THREE, ParserUtil.parseSelectIndex("        1  - 3  "));

        // With white spaces, multiple range Index
        assertEquals(INDEX_LIST_SIX, ParserUtil.parseSelectIndex("1 -    3 , 5  - 7  "));
    }

    @Test
    public void parseSelectIndex_invalidInput_throwsParseException() throws Exception {
        thrown.expect(ParseException.class);
        // Non numeric
        ParserUtil.parseSelectIndex("a");

        // Non positive values
        ParserUtil.parseSelectIndex("-1");
        ParserUtil.parseSelectIndex("0");

        // Invalid range format/values
        ParserUtil.parseSelectIndex("1 -- 3");
        ParserUtil.parseSelectIndex("-4 - 2");

        // Invalid multiple range format/values
        ParserUtil.parseSelectIndex("1 - 3 5 - 9");
        ParserUtil.parseSelectIndex("1-4 ,, 6-8");
    }
}
```
###### /java/seedu/address/logic/commands/SelectCommandTest.java
``` java
    /**
     * Executes a {@code SelectCommand} with the given {@code index}, and checks that {@code JumpToListRequestEvent}
     * is raised with the correct index.
     */
    private void assertExecutionSuccessSingle(ArrayList<Index> indexArrayList) {
        SelectCommand selectCommand = new SelectCommand(indexArrayList);
        String expectedMessage =
                String.format(SelectCommand.MESSAGE_SELECT_PERSON_SUCCESS_SINGLE, indexArrayList.size());

        assertCommandSuccess(selectCommand, model, commandHistory, expectedMessage, expectedModel);

        JumpToListRequestEvent lastEvent = (JumpToListRequestEvent) eventsCollectorRule.eventsCollector.getMostRecent();
        assertEquals(extractIndexAsIntegers(indexArrayList), lastEvent.targetIndex);
    }

    /**
     * Executes a {@code SelectCommand} with the given {@code index}, and checks that {@code JumpToListRequestEvent}
     * is raised with the correct index.
     */
    private void assertExecutionSuccessMultiple(ArrayList<Index> indexArrayList) {
        SelectCommand selectCommand = new SelectCommand(indexArrayList);
        String expectedMessage =
                String.format(SelectCommand.MESSAGE_SELECT_PERSON_SUCCESS_MULTIPLE, indexArrayList.size());

        assertCommandSuccess(selectCommand, model, commandHistory, expectedMessage, expectedModel);

        JumpToListRequestEvent lastEvent = (JumpToListRequestEvent) eventsCollectorRule.eventsCollector.getMostRecent();
        assertEquals(extractIndexAsIntegers(indexArrayList), lastEvent.targetIndex);
    }

    /**
     * Extracts an array list of {@code Index} to an array list of {@code Integer}.
     * @param indexArrayList the list of {@code Index} to extract from.
     * @return the list of {@code Integer} extracted.
     */
    private ArrayList<Integer> extractIndexAsIntegers(ArrayList<Index> indexArrayList) {
        ArrayList<Integer> output = new ArrayList<>();
        for (Index index : indexArrayList) {
            output.add(index.getZeroBased());
        }
        return output;
    }
}
```
###### /java/seedu/address/logic/commands/AddCommandTest.java
``` java
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
```
###### /java/seedu/address/logic/commands/MailCommandTest.java
``` java
package seedu.address.logic.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.logic.commands.MailCommand.MESSAGE_UNSUPPORTED;
import static seedu.address.logic.commands.MailCommand.TYPE_ALL;
import static seedu.address.logic.commands.MailCommand.TYPE_GROUPS;
import static seedu.address.logic.commands.MailCommand.TYPE_SELECTION;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.ArrayList;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.model.tag.Tag;

public class MailCommandTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private CommandHistory commandHistory = new CommandHistory();

    @Ignore
    @Test
    public void execute_selectedPersons_success() {
        MailCommand mailCommand = new MailCommand(TYPE_SELECTION);

        // Selects the first 3 persons in the address book.
        model.setSelectedPersons(new ArrayList<>(model.getFilteredPersonList().subList(0, 3)));

        String expectedMessage = MailCommand.MESSAGE_SUCCESS
                + buildRecipients(new ArrayList<>(model.getSelectedPersons()));

        CommandTestUtil.assertCommandSuccess(mailCommand, model, commandHistory, expectedMessage, model);
    }

    @Ignore
    @Test
    public void execute_allPersons_success() {
        MailCommand mailCommand = new MailCommand(TYPE_ALL);

        String expectedMessage = MailCommand.MESSAGE_SUCCESS
                + buildRecipients(new ArrayList<>(model.getFilteredPersonList()));

        CommandTestUtil.assertCommandSuccess(mailCommand, model, commandHistory, expectedMessage, model);
    }

    @Ignore
    @Test
    public void execute_selectedGroups_success() {
        Tag tagToUse = (Tag) model.getFilteredPersonList().get(0).getTags().toArray()[0];
        String tagToUseInString = tagToUse.toString();

        MailCommand mailCommand = new MailCommand(TYPE_GROUPS, tagToUseInString);

        // Retrieve the list of persons with that tag and build the expected message with it.
        ArrayList<Person> mailingList = new ArrayList<>(model.getFilteredPersonList());
        mailingList.removeIf(person -> !person.getTags().contains(tagToUse));
        String expectedMessage = MailCommand.MESSAGE_SUCCESS + buildRecipients(mailingList);

        CommandTestUtil.assertCommandSuccess(mailCommand, model, commandHistory, expectedMessage, model);
    }

    @Test
    public void execute_zeroRecipients_throwsCommandException() {
        MailCommand mailCommand = new MailCommand(TYPE_SELECTION);

        String expectedMessage = MailCommand.MESSAGE_EMPTY_SELECTION;
        model.setSelectedPersons(new ArrayList<>());

        CommandTestUtil.assertCommandFailure(mailCommand, model, commandHistory, expectedMessage);
    }

    @Test
    public void execute_unsupportedDesktops_throwsCommandException() throws CommandException {
        thrown.expect(CommandException.class);
        thrown.expectMessage(MESSAGE_UNSUPPORTED);

        MailCommand mailCommand = new MailCommandStubThrowsException(TYPE_ALL);

        String expectedMessage = MailCommand.MESSAGE_SUCCESS
                + buildRecipients(new ArrayList<>(model.getFilteredPersonList()));

        try {
            CommandResult result = mailCommand.execute(model, commandHistory);
            assertEquals(expectedMessage, result.feedbackToUser);
        } catch (CommandException e) {
            throw new CommandException(e.getMessage());
        }
    }

    @Test
    public void equals() {
        MailCommand mailAllCommand = new MailCommand(TYPE_ALL, "");
        MailCommand mailAllCommandCopy = new MailCommand(TYPE_ALL, "");
        MailCommand mailSelectionCommand = new MailCommand(TYPE_SELECTION, "");
        MailCommand mailFirstSpecifiedTagCommand = new MailCommand(TYPE_GROUPS, "accountants");
        MailCommand mailSecondSpecifiedTagCommand = new MailCommand(TYPE_GROUPS, "hr");

        // Equal commands -> return true
        assertTrue(mailAllCommand.equals(mailAllCommandCopy));

        // Unequal commands -> return false
        assertFalse(mailAllCommand.equals(mailSelectionCommand));
        assertFalse(mailFirstSpecifiedTagCommand.equals(mailSecondSpecifiedTagCommand));
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

    /**
     * A MailCommand stub that throws CommandException
     */
    private class MailCommandStubThrowsException extends MailCommand {
        public MailCommandStubThrowsException(int mailType) {
            super(mailType);
        }

        public MailCommandStubThrowsException(int mailType, String mailArgs) {
            super(mailType, mailArgs);
        }

        @Override
        public CommandResult execute(Model model, CommandHistory history) throws CommandException {
            throw new CommandException(MESSAGE_UNSUPPORTED);
        }
    }
    /*
    /**
     * A MailCommand stub that runs successful without throwing CommandException
     */
    /*
    private class MailCommandStubSuccess extends MailCommand {
        public MailCommandStubSuccess(int mailType) {
            super(mailType);
        }

        public MailCommandStubSuccess(int mailType, String mailArgs) {
            super(mailType, mailArgs);
        }

        @Override
        public CommandResult execute(Model model, CommandHistory history) throws CommandException {

            ArrayList<Person> mailingList = mailToAll(model);
            String recipients = buildRecipients(mailingList);

            return new CommandResult(MESSAGE_SUCCESS + recipients);
        }
    }
    */
}
```
###### /java/seedu/address/model/autocomplete/CommandCompleterTest.java
``` java
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
```
###### /java/seedu/address/model/autocomplete/AutoCompleteParserPairTest.java
``` java
package seedu.address.model.autocomplete;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import seedu.address.logic.parser.CliSyntax;

public class AutoCompleteParserPairTest {

    @Test
    public void constructor_validArgs_success() {
        AutoCompleteParserPair pair = new AutoCompleteParserPair(CliSyntax.PREFIX_NAME, "  john");
        assertEquals(CliSyntax.PREFIX_NAME, pair.predictionType);
        assertEquals("john", pair.prefixValue);

        // argument contains only white space
        AutoCompleteParserPair emptyPair = new AutoCompleteParserPair(CliSyntax.PREFIX_NAME, "    ");
        assertEquals(CliSyntax.PREFIX_NAME, emptyPair.predictionType);
        assertEquals("    ", emptyPair.prefixValue);
    }

    @Test
    public void equals() {
        AutoCompleteParserPair namePair = new AutoCompleteParserPair(CliSyntax.PREFIX_NAME, "tom");
        AutoCompleteParserPair namePairCopy = new AutoCompleteParserPair(CliSyntax.PREFIX_NAME, "tom");
        AutoCompleteParserPair phonePair = new AutoCompleteParserPair(CliSyntax.PREFIX_PHONE, "999");
        AutoCompleteParserPair phonePairCopy = new AutoCompleteParserPair(CliSyntax.PREFIX_PHONE, "999");
        AutoCompleteParserPair addrPair = new AutoCompleteParserPair(CliSyntax.PREFIX_ADDRESS, "Mars");
        AutoCompleteParserPair addrPairCopy = new AutoCompleteParserPair(CliSyntax.PREFIX_ADDRESS, "Mars");

        // Matching pairs
        assertTrue(namePair.equals(namePairCopy));
        assertTrue(phonePair.equals(phonePairCopy));
        assertTrue(addrPair.equals(addrPairCopy));

        // Non matching pairs
        assertFalse(namePair.equals(phonePair));
        assertFalse(namePair.equals(addrPair));
        assertFalse(phonePair.equals(addrPair));
    }
}
```
###### /java/seedu/address/model/autocomplete/AutoCompleteParserTest.java
``` java
package seedu.address.model.autocomplete;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import seedu.address.logic.parser.CliSyntax;

public class AutoCompleteParserTest {

    @Test
    public void parseCommand_validArgs_returnValidParserPair() {
        AutoCompleteParser parserUnderTest = new AutoCompleteParser();
        AutoCompleteParserPair actualPair = parserUnderTest.parseCommand("list t/friends");
        AutoCompleteParserPair expectedPair = new AutoCompleteParserPair(CliSyntax.PREFIX_TAG, "friends");

        assertEquals(expectedPair, actualPair);
    }

    @Test
    public void parseCommand_invalidArgs_returnInvalidParserPair() {
        AutoCompleteParser parserUnderTest = new AutoCompleteParser();
        AutoCompleteParserPair actualPair = parserUnderTest.parseCommand("");
        AutoCompleteParserPair expectedPair =
                new AutoCompleteParserPair(CliSyntax.PREFIX_INVALID, CliSyntax.PREFIX_INVALID.getPrefix());

        assertEquals(expectedPair, actualPair);
    }
}
```
###### /java/seedu/address/model/autocomplete/AutoCompleteArgumentsParserTest.java
``` java
package seedu.address.model.autocomplete;

import static org.junit.Assert.assertEquals;
import static seedu.address.logic.parser.CliSyntax.COMMAND_FIND;
import static seedu.address.logic.parser.CliSyntax.COMMAND_LIST;
import static seedu.address.logic.parser.CliSyntax.COMMAND_MAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_COMMAND;
import static seedu.address.logic.parser.CliSyntax.PREFIX_INVALID;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import org.junit.Test;

import seedu.address.logic.parser.ArgumentMultimap;

public class AutoCompleteArgumentsParserTest {

    private ArgumentMultimap argumentMultimap = new ArgumentMultimap();

    @Test
    public void parse_nonEmptyValidArgs_returnValidPrefix() {
        argumentMultimap.put(PREFIX_NAME, "john");
        AutoCompleteParserPair actualFindPair =
                AutoCompleteArgumentsParser.parse(COMMAND_FIND, " n/john", argumentMultimap);
        AutoCompleteParserPair expectedFindPair = new AutoCompleteParserPair(PREFIX_NAME, "john");

        argumentMultimap.put(PREFIX_TAG, "friends");
        AutoCompleteParserPair actualListPair =
                AutoCompleteArgumentsParser.parse(COMMAND_LIST, " t/friends", argumentMultimap);
        AutoCompleteParserPair expectedListPair = new AutoCompleteParserPair(PREFIX_TAG, "friends");

        AutoCompleteParserPair actualMailPair =
                AutoCompleteArgumentsParser.parse(COMMAND_MAIL, " t/friends", argumentMultimap);
        AutoCompleteParserPair expectedMailPair = new AutoCompleteParserPair(PREFIX_TAG, "friends");

        assertEquals(expectedFindPair, actualFindPair);
        assertEquals(expectedListPair, actualListPair);
        assertEquals(expectedMailPair, actualMailPair);
    }

    @Test
    public void parse_emptyArgs_returnCommandPrefix() {
        AutoCompleteParserPair actualPair =
                AutoCompleteArgumentsParser.parse(COMMAND_FIND, "", argumentMultimap);
        AutoCompleteParserPair expectedPair = new AutoCompleteParserPair(PREFIX_COMMAND, COMMAND_FIND);

        assertEquals(expectedPair, actualPair);
    }

    @Test
    public void parse_unspecifiedArgsPrefix_returnDefaultPrefix() {
        AutoCompleteParserPair actualFindPair =
                AutoCompleteArgumentsParser.parse(COMMAND_FIND, "john", argumentMultimap);
        AutoCompleteParserPair expectedFindPair = new AutoCompleteParserPair(PREFIX_NAME, "john");

        AutoCompleteParserPair actualListPair =
                AutoCompleteArgumentsParser.parse(COMMAND_LIST, "john", argumentMultimap);
        AutoCompleteParserPair expectedListPair = new AutoCompleteParserPair(PREFIX_INVALID, "john");

        AutoCompleteParserPair actualMailPair =
                AutoCompleteArgumentsParser.parse(COMMAND_MAIL, "john", argumentMultimap);
        AutoCompleteParserPair expectedMailPair = new AutoCompleteParserPair(PREFIX_NAME, "john");

        assertEquals(expectedFindPair, actualFindPair);
        assertEquals(expectedListPair, actualListPair);
        assertEquals(expectedMailPair, actualMailPair);
    }

    @Test
    public void parse_invalidArgs_returnInvalidPrefix() {
        AutoCompleteParserPair actualPair =
                AutoCompleteArgumentsParser.parse("invalidCommand", "arg", argumentMultimap);
        AutoCompleteParserPair expectedPair = new AutoCompleteParserPair(PREFIX_INVALID, "arg");

        assertEquals(expectedPair, actualPair);
    }
}
```
###### /java/seedu/address/model/trie/TrieNodeTest.java
``` java
package seedu.address.model.trie;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Test;

public class TrieNodeTest {
    @Test
    public void constructor_validValues() {
        TrieNode node = new TrieNode('a');

        assertEquals(node.getValue(), 'a');
        assertFalse(node.isEndNode());
        assertNull(node.getParent());
        assertEquals(new ArrayList<>(), node.getChildren());
    }

    @Test
    public void setParent_validArgs_success() {
        TrieNode node = new TrieNode('b');
        TrieNode parent = new TrieNode('a');
        node.setParent(parent);
        assertEquals(parent, node.getParent());
    }

    @Test
    public void setValue_validValue_success() {
        TrieNode node = new TrieNode('a');
        char newValue = 'b';
        node.setValue(newValue);
        assertEquals(newValue, node.getValue());
    }

    @Test
    public void getChildrenSize_empty_success() {
        TrieNode node = new TrieNode('a');
        assertEquals(0, node.getChildrenSize());
    }

    @Test
    public void testChildren_validArgs_success() {
        TrieNode node = new TrieNode('a');
        TrieNode toAppend = new TrieNode('b');

        node.appendChild(toAppend);
        assertEquals(1, node.getChildrenSize());
        assertEquals(toAppend, node.getChildren().get(0));
        assertEquals(toAppend, node.getFirstChild());

        node.removeChild(toAppend);
        assertEquals(0, node.getChildrenSize());
    }

    @Test
    public void setEndNode_success() {
        TrieNode node = new TrieNode('a');
        node.setEndNode(true);
        assertTrue(node.isEndNode());
    }
}
```
###### /java/seedu/address/model/trie/TrieTest.java
``` java
package seedu.address.model.trie;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Test;

public class TrieTest {

    private ArrayList<String> testStrings = new ArrayList<>(Arrays.asList("hello", "hell", "helipad", "world", "help"));

    @Test
    public void constructor_validArgs_success() {
        Trie trie = new Trie(testStrings);
    }

    @Test
    public void getPredictList_validArgs_success() {
        Trie trieUnderTest = new Trie(testStrings);

        ArrayList<String> actualOutput = trieUnderTest.getPredictList("hel");
        ArrayList<String> expectedOutput = new ArrayList<>(Arrays.asList("l", "lo ", "ipad ", "p "));
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void getPredictList_singleOutput_success() {
        Trie trieUnderTest = new Trie(testStrings);

        ArrayList<String> actualOutput = trieUnderTest.getPredictList("wor");
        ArrayList<String> expectedOutput = new ArrayList<>(Arrays.asList("ld "));
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void insert_newValue_success() {
        Trie trieUnderTest = new Trie(testStrings);
        trieUnderTest.insert("helium");

        ArrayList<String> expectedOutput = new ArrayList<>(Arrays.asList("l", "lo ", "ipad ", "ium ", "p "));
        assertPredictSuccess("hel", trieUnderTest, expectedOutput);
    }

    @Test
    public void remove_existingValues_success() {
        Trie trieUnderTest = new Trie(testStrings);
        trieUnderTest.remove("help");
        trieUnderTest.remove("hell");

        ArrayList<String> expectedOutput = new ArrayList<>(Arrays.asList("lo ", "ipad "));
        assertPredictSuccess("hel", trieUnderTest, expectedOutput);
    }

    @Test
    public void clear_nonEmptyTrie_success() {
        Trie trieUnderTest = new Trie(testStrings);
        trieUnderTest.clear();

        ArrayList<String> expectedOutput = new ArrayList<>();
        assertPredictSuccess("hel", trieUnderTest, expectedOutput);
    }

    /**
     * Asserts that the return values of prediction from a Trie under test is same as expected values.
     *
     * @param prefix the prefix string used for prediction.
     * @param trieUnderTest the Trie getting tested.
     * @param expectedOutput the expected values from the prediction using the prefix string.
     */
    public static void assertPredictSuccess(String prefix, Trie trieUnderTest, ArrayList<String> expectedOutput) {
        ArrayList<String> actualOutput = trieUnderTest.getPredictList(prefix);
        assertEquals(expectedOutput, actualOutput);
    }
}
```
