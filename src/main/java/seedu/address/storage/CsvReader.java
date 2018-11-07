//@@author Limminghong
package seedu.address.storage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.parser.ParserUtil;
import seedu.address.logic.parser.exceptions.ParseException;
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
 * Converts the file from a CSV file to {@code Model}
 */
public class CsvReader {
    public static final String WRONG_FORMAT = "The information in this file is of the wrong format";

    private List<String> stringList = new ArrayList<>();

    /**
     * File to stringList
     * @param file .csv File that is being converted to an array of strings
     * @throws IOException if file is not found
     */
    public CsvReader(File file) throws IOException {
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);

        String line;
        while ((line = br.readLine()) != null) {
            stringList.add(line);
        }
        br.close();
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
     * Unwrap {@code String} from quotation marks
     * @param information to be unwrapped
     * @return Unwrapped {@code String}
     */
    private String unwrapQuotation(String information) {
        String unwrappedString = information.substring(1, information.length() - 1);
        return unwrappedString;
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
                    information += ", " + initialSections[i].trim();
                    if (initialSections[i].charAt(initialSections[i].length() - 1) == '\"') {
                        break;
                    }
                    i++;
                }
                information = information.substring(2);
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
                String[] tags = sections.get(7).split(", ");
                for (String tagName : tags) {
                    Tag tag;
                    if (havePriority(tagName)) {
                        String[] tagSections = tagName.trim().split(" ");
                        Index priority = Index.fromZeroBased(Integer.parseInt(tagSections[1]));
                        tag = new Tag(tagSections[0], priority);
                    } else {
                        tag = new Tag(tagName.trim());
                    }

                    tagList.add(tag);
                }
            }
            return new Person(name, phone, email, address, position, kpi, note, tagList);
        } catch (IllegalArgumentException iae) {
            throw new IOException(WRONG_FORMAT);
        }
    }

    /**
     * Check if the tag have priority
     * @param tagName string of tag
     * @return true if have priority, false if does not have priority
     */
    private boolean havePriority(String tagName) {
        String[] split = tagName.split(" ");
        if (split.length == 2) {
            return true;
        } else {
            return false;
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
