//@@author Limminghong
package seedu.address.storage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javafx.collections.ObservableList;

import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Kpi;
import seedu.address.model.person.Name;
import seedu.address.model.person.Note;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Position;
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
            List<String> specificInformation = new ArrayList<>();
            String personInformation;
            specificInformation = segmentInformation(p);
            personInformation = concatInformation(specificInformation);
            stringList.add(personInformation);
        }
    }

    /**
     * @param file .csv File that is being converted to an array of strings
     * @throws IOException if file is not found
     */
    public CsvWriter(File file) throws IOException {
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);

        String line;
        while ((line = br.readLine()) != null) {
            stringList.add(line);
        }
        br.close();
    }

    /**
     * @return The AddressBook in .csv format to the directory provided
     * @throws IOException if {@code convertedFile} does not exist
     */
    public File convertToCsv(String pathName) throws IOException {
        File convertedFile = new File(pathName);
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

    /**
     * @return a {@code List} of {@code Person}
     */
    public List<Person> convertToList() {
        List<Person> personList = new ArrayList<>();
        int counter = 0;
        for (String line : stringList) {
            if (counter != 0) {
                line = line.substring(0, line.length() - 1);
                String[] sections = line.split("\",");

                Name name = new Name(sections[0].substring(1).trim());
                Phone phone = new Phone(sections[1].substring(1).trim());
                Email email = new Email(sections[2].substring(1).trim());
                Address address = new Address(sections[3].substring(1).trim());

                Position position;
                if (sections[4].substring(1).equals("null")) {
                    position = new Position();
                } else {
                    position = new Position(sections[4].substring(1).trim());
                }

                Kpi kpi;
                if (sections[5].substring(1).equals("null")) {
                    kpi = new Kpi();
                } else {
                    kpi = new Kpi(sections[5].substring(1).trim());
                }

                Note note;
                if (sections[6].substring(1).equals("null")) {
                    note = new Note();
                } else {
                    note = new Note(sections[6].substring(1).trim());
                }

                Set<Tag> tagList = new HashSet<>();
                if (sections.length == 8) {
                    String[] tags = sections[7].substring(1).split(", ");
                    for (String tagName : tags) {
                        Tag tag = new Tag(tagName.trim());
                        tagList.add(tag);
                    }
                }
                Person person = new Person(name, phone, email, address, position, kpi, note, tagList);
                personList.add(person);
            }
            counter++;
        }
        return personList;
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
            specificInformation.add("null");
        }

        if (person.kpiDoesExist()) {
            specificInformation.add(person.getKpi().toString());
        } else {
            specificInformation.add("null");
        }

        if (person.noteDoesExist()) {
            specificInformation.add(person.getNote().toString());
        } else {
            specificInformation.add("null");
        }

        if (!person.getTags().isEmpty()) {
            specificInformation.add(tagsToString(person.getTags()));
        } else {
            specificInformation.add("null");
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
