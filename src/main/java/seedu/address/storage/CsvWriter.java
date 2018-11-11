//@@author Limminghong
package seedu.address.storage;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javafx.collections.ObservableList;

import seedu.address.model.person.Person;
import seedu.address.model.tag.Tag;

/**
 * Converts the file from {@code Model} to a .csv file
 */
public class CsvWriter {
    public static final String CSV_HEADERS = "Name, Phone, Email, Address, Position, Kpi, Note, Tagged";

    private List<String> stringList = new ArrayList<>();

    /**
     * Parses the {@code ObservableList<Person>} into an array of strings
     * @param personList list of persons from the AddressBook
     */
    public CsvWriter(ObservableList<Person> personList) {
        stringList.add(CSV_HEADERS);
        for (Person p : personList) {
            List<String> specificInformation;
            String personInformation;
            specificInformation = segmentInformation(p);
            personInformation = concatInformation(specificInformation);
            stringList.add(personInformation);
        }
    }

    /**
     * Creates a .csv file in the path
     * @param pathName directory of the file
     * @throws IOException file does not exist
     */
    public void convertToCsv(String pathName) throws IOException {
        File convertedFile = new File(pathName);
        if (!convertedFile.exists()) {
            convertedFile.createNewFile();
        }
        PrintWriter pw = new PrintWriter(convertedFile);
        for (String s : stringList) {
            pw.println(s);
        }
        pw.close();
    }

    /**
     * Segment the information of a person to a {@code List<person>}
     * @param person information to be segmented
     * @return a {@code List<>} of {@code String}
     */
    private List<String> segmentInformation(Person person) {
        List<String> specificInformation = new ArrayList<>();

        specificInformation.add(person.getName().toString());
        specificInformation.add(person.getPhone().toString());
        specificInformation.add(person.getEmail().toString());
        specificInformation.add(person.getAddress().toString());

        if (person.positionDoesExist()) {
            specificInformation.add(person.getPosition().toString());
        } else {
            specificInformation.add("");
        }

        if (person.kpiDoesExist()) {
            specificInformation.add(person.getKpi().toString());
        } else {
            specificInformation.add("");
        }

        if (person.noteDoesExist()) {
            specificInformation.add(person.getNote().toString());
        } else {
            specificInformation.add("");
        }

        if (!person.getTags().isEmpty()) {
            specificInformation.add(tagsToString(person.getTags()));
        } else {
            specificInformation.add("");
        }

        for (int i = 0; i < specificInformation.size(); i++) {
            if (specificInformation.get(i).indexOf(',') > -1) {
                String wrappedInformation = wrapQuotation(specificInformation.get(i));
                specificInformation.set(i, wrappedInformation);
            }
        }

        return specificInformation;
    }

    /**
     * Concatenate the {@code List<String>} into one full {@code String} with commas
     * @param specificInformation {@code List<String>} to be concatenated
     * @return {@code String} with commas
     */
    private String concatInformation(List<String> specificInformation) {
        String personInformation = "";
        for (String information : specificInformation) {
            personInformation += information + ",";
        }
        personInformation = personInformation.substring(0, personInformation.length() - 1);
        return personInformation;
    }

    /**
     * Wrap {@code String} with quotation marks
     * @param s to be wrapped
     * @return Wrapped {@code String}
     */
    private String wrapQuotation(String s) {
        String wrappedString = "\"" + s + "\"";
        return wrappedString;
    }

    /**
     * Convert the {@code Tag} into {@code String} separated with commas
     * @param tags Tags to be converted
     * @return converted {@code String}
     */
    private String tagsToString(Set<Tag> tags) {
        String tagsString = "";
        for (Tag tag : tags) {
            tagsString += tag.toString() + ", ";
        }
        tagsString = tagsString.substring(0, tagsString.length() - 2);
        return tagsString;
    }
}
