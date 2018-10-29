//@@author lekoook
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
