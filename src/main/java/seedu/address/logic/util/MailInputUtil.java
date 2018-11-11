package seedu.address.logic.util;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import seedu.address.logic.commands.MailCommand;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.person.Person;

/**
 * Utility class that helps to build variables to be used by MailCommand.
 */
public class MailInputUtil {
    /**
     * Extracts all emails given a list of Person.
     * @param personList the list of Person.
     * @return the list of extracted emails.
     */
    public static ArrayList<String> retrieveEmails(List<Person> personList) {
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
    public static URI createUri(ArrayList<String> emailList) throws CommandException {
        StringBuilder uriToMail = new StringBuilder("mailto:");
        URI uri;

        if (emailList.size() == 0) {
            throw new CommandException(MailCommand.MESSAGE_EMPTY_SELECTION);
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
     * Builds the string of names of recipients mailed to.
     * @param mailingList the list of recipients.
     * @return the string including all recipients.
     */
    public static String buildRecipients(ArrayList<Person> mailingList) {
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
