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
    public static final String WRONG_FORMAT = "The information in this file is of the wrong format";

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
     * @return a {@code List} of {@code Person}
     */
    public List<Person> convertToList() throws IOException {
        List<Person> personList = new ArrayList<>();
        int counter = 0;
        for (String line : stringList) {
            if (counter != 0) {
                line = formatLine(line);
                List<String> sections = splitToSections(line);
                if (sections.size() < 4 || sections.size() > 8) {
                    throw new IOException(WRONG_FORMAT);
                }
                Person person = makeSectionsIntoPerson(sections);
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
     * Unwrap {@code String} from quotation marks
     * @param information to be unwrapped
     * @return Unwrapped {@code String}
     */
    private String unwrapQuotation(String information) {
        String unwrappedString = information.substring(1, information.length() - 1);
        return unwrappedString;
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

    /**
     * Split the line of person information into an array of information
     * @param line a single line of person information
     * @return array of information in {@code String}
     */
    private List<String> splitToSections(String line) {
        String[] initialSections = line.split(",");
        List<String> sections = new ArrayList<>();

        for (int i = 0; i < initialSections.length; i++) {
            if (!initialSections[i].equals("") && initialSections[i].charAt(0) == '\"') {
                String information = new String();
                while (true) {
                    information += "," + initialSections[i].trim();
                    if (initialSections[i].charAt(initialSections[i].length() - 1) == '\"') {
                        break;
                    }
                    i++;
                }
                information = information.substring(1);
                information = unwrapQuotation(information);
                sections.add(information);
            } else {
                sections.add(initialSections[i].trim());
            }
        }
        return sections;
    }

    /**
     * Convert an array of {@code ArrayList<String>} into a {@code Person} object
     * @param sections information about a person
     * @return {@code Person} object with values of {@param sections}
     */
    private Person makeSectionsIntoPerson(List<String> sections) throws IOException {
        try {
            Name name = new Name(sections.get(0));
            Phone phone = new Phone(sections.get(1));
            Email email = new Email(sections.get(2));
            Address address = new Address(sections.get(3));

            Position position;
            if (sections.get(4).equals("null")) {
                position = new Position();
            } else {
                position = new Position(sections.get(4).trim());
            }

            Kpi kpi;
            if (sections.get(5).equals("null")) {
                kpi = new Kpi();
            } else {
                kpi = new Kpi(sections.get(5).trim());
            }

            Note note;
            if (sections.get(6).equals("null")) {
                note = new Note();
            } else {
                note = new Note(sections.get(6).trim());
            }

            Set<Tag> tagList = new HashSet<>();
            if (sections.size() == 8) {
                String[] tags = sections.get(7).split(",");
                for (String tagName : tags) {
                    Tag tag = new Tag(tagName.trim());
                    tagList.add(tag);
                }
            }
            return new Person(name, phone, email, address, position, kpi, note, tagList);
        } catch (IllegalArgumentException iae) {
            throw new IOException(WRONG_FORMAT);
        }
    }

    /**
     * Format this line into a friendlier format for the parser
     * @param line to be formatted
     * @return formatted line
     */
    private String formatLine(String line) {
        String formattedLine = new String();
        for (int i = 0; i < line.length(); i++) {
            formattedLine += line.charAt(i);
            if (i != line.length() - 1 && line.charAt(i) == ',' && line.charAt(i + 1) == ',') {
                formattedLine += "null";
            }
        }
        return formattedLine;
    }
}
