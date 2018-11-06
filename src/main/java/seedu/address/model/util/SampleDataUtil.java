package seedu.address.model.util;

import java.util.HashSet;
import java.util.Set;

import seedu.address.logic.parser.ParserUtil;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.AddressBook;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Kpi;
import seedu.address.model.person.Name;
import seedu.address.model.person.Note;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Position;
import seedu.address.model.schedule.Activity;
import seedu.address.model.tag.Tag;

/**
 * Contains utility methods for populating {@code AddressBook} with sample data.
 */
public class SampleDataUtil {
    public static Person[] getSamplePersons() {
        return new Person[] {
            new Person(new Name("Alex Yeoh"), new Phone("87438807"), new Email("alexyeoh@example.com"),
                new Address("Blk 30 Geylang Street 29, #06-40"), new Position("CEO"), new Kpi("0"),
                new Note("Alex is a friend"), getTagSet("friends")),
            new Person(new Name("Bernice Yu"), new Phone("99272758"), new Email("berniceyu@example.com"),
                new Address("Blk 30 Lorong 3 Serangoon Gardens, #07-18"), new Position("CEO"), new Kpi("0"),
                new Note("Bernice is a friend"), getTagSet("colleagues", "friends")),
            new Person(new Name("Charlotte Oliveiro"), new Phone("93210283"), new Email("charlotte@example.com"),
                new Address("Blk 11 Ang Mo Kio Street 74, #11-04"), new Position("CEO"), new Kpi("0"),
                new Note("Charlotte is a friend"), getTagSet("neighbours")),
            new Person(new Name("David Li"), new Phone("91031282"), new Email("lidavid@example.com"),
                new Address("Blk 436 Serangoon Gardens Street 26, #16-43"), new Position("CEO"), new Kpi("0"),
                new Note("David is a friend"), getTagSet("family")),
            new Person(new Name("Irfan Ibrahim"), new Phone("92492021"), new Email("irfan@example.com"),
                new Address("Blk 47 Tampines Street 20, #17-35"), new Position("CEO"), new Kpi("0"),
                new Note("Irfan is a friend"), getTagSet("classmates")),
            new Person(new Name("Roy Balakrishnan"), new Phone("92624417"), new Email("royb@example.com"),
                new Address("Blk 45 Aljunied Street 85, #11-31"), new Position("CEO"), new Kpi("0"),
                new Note("Roy is a friend"), getTagSet("colleagues 1"))
        };
    }
    public static Activity[] getSampleSchedule() {
        return new Activity[]{
            new Activity(Activity.toDate(30, 10, 2018), "Write report."),
            new Activity(Activity.toDate(30, 10, 2018), "Attend meeting with Alexia."),
            new Activity(Activity.toDate(31, 10, 2018), "Check inventory."),
            new Activity(Activity.toDate(31, 10, 2018), "Walk the ground."),
            new Activity(Activity.toDate(01, 11, 2018), "Prepare speech."),
            new Activity(Activity.toDate(11, 11, 2018), "Project due."),
        };
    }

    public static ReadOnlyAddressBook getSampleAddressBook() {
        AddressBook sampleAb = new AddressBook();
        for (Person samplePerson : getSamplePersons()) {
            sampleAb.addPerson(samplePerson);
        }
        for (Activity sampleActivity : getSampleSchedule()) {
            sampleAb.addActivity(sampleActivity);
        }
        return sampleAb;
    }

    /**
     * Returns a tag set containing the list of strings given.
     */
    public static Set<Tag> getTagSet(String... strings) {
        Set<Tag> tagSet = new HashSet<>();
        try {
            for (String tagString : strings) {
                Tag tag = ParserUtil.parseTag(tagString);
                tagSet.add(tag);
            }
        } catch (ParseException e) {
            return new HashSet<>();
        }
        return tagSet;
    }

}
