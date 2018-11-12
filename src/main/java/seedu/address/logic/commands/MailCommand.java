//@@author lekoook
package seedu.address.logic.commands;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Objects;

import seedu.address.commons.util.FileEncryptor;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.CliSyntax;
import seedu.address.logic.util.MailInputUtil;
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

    public static final String MESSAGE_SUCCESS = "Mailing to: ";
    public static final String MESSAGE_UNSUPPORTED = "System desktop is unsupported.";
    public static final String MESSAGE_EMPTY_SELECTION = "No contacts selected! Select one or more and try again.";

    /**
     * Instance variables
     */
    private MailType mailType;
    private String mailArgs;
    private Desktop desktop;

    /**
     * Creates a default Mail command
     */
    public MailCommand(MailType mailType) {
        this.mailType = mailType;
        desktop = Desktop.getDesktop();
    }

    public MailCommand(MailType mailType, String mailArgs) {
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
        String recipients = MailInputUtil.buildRecipients(mailingList);

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
        ArrayList<String> emailList = MailInputUtil.retrieveEmails(list);
        URI uriToMail = MailInputUtil.createUri(emailList);
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
        ArrayList<String> emailList = MailInputUtil.retrieveEmails(list);
        URI uriToMail = MailInputUtil.createUri(emailList);
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
        ArrayList<String> emailList = MailInputUtil.retrieveEmails(model.getFilteredPersonList());
        URI uriToMail = MailInputUtil.createUri(emailList);
        sendWithUri(uriToMail);
        return list;
    }

    /**
     * Opens the system's default email application given the specified URI.
     * @param uriToMail URI specifying the recipients.
     */
    private void sendWithUri(URI uriToMail) throws CommandException {
        String os = System.getProperty("os.name");
        if (os.toLowerCase().contains("windows") || os.toLowerCase().contains("mac")) {
            try {
                desktop.mail(uriToMail);
            } catch (UnsupportedOperationException | IOException | SecurityException e) {
                throw new CommandException(e.getMessage());
            }
        } else {
            // Unfortunately due to a bug in Desktop class, a new thread has to be used to prevent app freezing when
            // this app is running in  some Linux distros like Ubuntu.
            // This however, means an exception cannot be easily thrown from the thread.
            // Solution adapted from :
            // https://stackoverflow.com/questions/23176624/javafx-freeze-on-desktop-openfile-desktop-browseuri
            new Thread (() -> {
                try {
                    desktop.mail(uriToMail);
                } catch (UnsupportedOperationException | IOException | SecurityException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }

    @Override
    public boolean equals(Object other) {
        return other == this || (other instanceof MailCommand
                && this.mailType == ((MailCommand) other).mailType
                && Objects.equals(this.mailArgs, ((MailCommand) other).mailArgs)
                && Objects.equals(this.desktop, ((MailCommand) other).desktop));
    }

    /**
     * Enumerations to help determine the mailType.
     */
    public enum MailType {
        TYPE_SELECTION,
        TYPE_GROUPS,
        TYPE_ALL
    }
}
