//@@author Limminghong
package seedu.address.storage;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javafx.collections.ObservableList;

import seedu.address.model.person.Person;
import seedu.address.model.tag.Tag;

/**
 * Converts the file from {@code Model} to a .csv file
 */
public class CsvParser {
    public static final String CSV_HEADERS = "Name, Phone, Email, Address, Note, Tagged";

    private ObservableList<Person> personList;
    private List<String> stringList = new ArrayList<>();

    /**
     * Parses the {@code ObservableList<Person>} into an array of strings
     * @param personList list of persons from the AddressBook
     */
    public CsvParser(ObservableList<Person> personList) {
        this.personList = personList;
        stringList.add(CSV_HEADERS);
        for (Person p : personList) {
            String personInformation = "\"" + p.getName().toString() + "\""
                    + "," + "\"" + p.getPhone().toString() + "\""
                    + "," + "\"" + p.getEmail().toString() + "\""
                    + "," + "\"" + p.getAddress().toString() + "\""
                    + "," + "\"" + p.getNote().toString() + "\"";
            if (!p.getTags().isEmpty()) {
                personInformation += "," + "\"";
                int sizeOfTags = 0;
                for (Tag t : p.getTags()) {
                    sizeOfTags += 1;
                    personInformation += t.toString();
                    if (sizeOfTags != p.getTags().size()) {
                        personInformation += ", ";
                    }
                }
                personInformation += "\"";
            }
            stringList.add(personInformation);
        }
    }

    /**
     * @return The AddressBook in .csv format in "data" folder
     * @throws IOException if {@code convertedFile} does not exist
     */
    public File convertToCsv() throws IOException {
        File convertedFile = new File("data\\addressbook.csv");
        if (!convertedFile.exists()) {
            convertedFile.createNewFile();
        }
        PrintWriter pw = new PrintWriter(convertedFile);
        for (String s : stringList) {
            pw.println(s);
        }
        pw.close();
        return convertedFile;
    }

}
