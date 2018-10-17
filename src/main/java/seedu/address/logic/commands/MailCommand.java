//@@author lekoook
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
