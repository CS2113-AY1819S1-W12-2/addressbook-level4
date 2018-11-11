//@@author peiying98
package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SORT_ATTRIBUTE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SORT_ORDER;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import seedu.address.commons.util.FileEncryptor;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.CliSyntax;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Kpi;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Position;


/**
 * Commands for sorting by respective attributes
 */

public class SortCommand extends Command {

    public static final String COMMAND_WORD = CliSyntax.COMMAND_SORT;
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Sorts the address book in"
            + " ascending or descending order by the respective attributes.\n"
            + "Parameters: "
            + PREFIX_SORT_ATTRIBUTE + "SORT_ATTRIBUTE "
            + PREFIX_SORT_ORDER + "SORT_ORDER\n"
            + "Possible SORT_ATTRIBUTE inputs: name, phone, email, address, position, kpi.\n"
            + "Possible SORT_ORDER inputs: asc, des.\n"
            + "Sort order input is optional and is set to ascending by default.\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_SORT_ATTRIBUTE + "name "
            + PREFIX_SORT_ORDER + "asc";

    public static final String MESSAGE_SORT_SUCCESS = "Sorted address book in order.";
    public static final String MESSAGE_SORT_EMPTY = "No person to sort in this attribute.";
    public static final String MESSAGE_INVALID_SORT_ATTRIBUTE = "Invalid sort attribute input";
    public static final String MESSAGE_INVALID_SORT_ORDER = "Invalid sort order input";

    private final String attribute;
    private final String order;

    /**
     * @param attribute     specify which attribute to sort by
     * @param order specify if sorting is to be in ascending or descending order
     */

    public SortCommand(String attribute, String order) {
        requireNonNull(attribute);
        requireNonNull(order);

        this.attribute = attribute;
        this.order = order;
    }
    public SortCommand(String attribute) {
        requireNonNull(attribute);
        this.attribute = attribute;
        this.order = "asc";
    }

    /**
     * Name Sorter
     */
    public class NameSorter implements Comparator<Person> {
        public int compare(Person one, Person another) {
            return one.getName().toString().compareTo(another.getName().toString());
        }
    }

    /**
     * Phone Sorter
     */
    public class PhoneSorter implements Comparator<Person> {
        public int compare(Person one, Person another) {
            return one.getPhone().toString().compareTo(another.getPhone().toString());
        }
    }

    /**
     * Email Sorter
     */
    public class EmailSorter implements Comparator<Person> {
        public int compare(Person one, Person another) {
            return one.getEmail().toString().compareTo(another.getEmail().toString());
        }
    }

    /**
     * Address Sorter
     */
    public class AddressSorter implements Comparator<Person> {
        public int compare(Person one, Person another) {
            return one.getAddress().toString().compareTo(another.getAddress().toString());
        }
    }

    /**
     * Position Sorter
     */
    public class PositionSorter implements Comparator<Person> {
        public int compare(Person one, Person another) {
            return one.getPosition().toString().compareTo(another.getPosition().toString());
        }
    }

    /**
     * KPI Sorter
     */
    public class KpiSorter implements Comparator<Person> {
        public int compare(Person one, Person another) {
            return one.getKpi().toString().compareTo(another.getKpi().toString());
        }
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);

        UserPrefs userPref = new UserPrefs();
        FileEncryptor fe = new FileEncryptor(userPref.getAddressBookFilePath().toString());

        if (fe.isLocked()) {
            throw new CommandException(FileEncryptor.MESSAGE_ADDRESS_BOOK_LOCKED);
        }
        List<Person> sortedList = getList(model);
        if (sortedList.size() == 0) {
            throw new CommandException(MESSAGE_SORT_EMPTY);
        }
        sortList(sortedList);

        AddressBook sortedAddressBook = new AddressBook();
        sortedAddressBook.setPersons(sortedList);
        model.resetData(sortedAddressBook);

        return new CommandResult(MESSAGE_SORT_SUCCESS);

    }

    List<Person> getList(Model model) {
        List<Person> lastShownList = model.getFilteredPersonList();
        List<Person> sortedList = new ArrayList<>();
        for (Person person : lastShownList) {
            sortedList.add(person);
        }
        return sortedList;
    }

    /**
     * Sorts the list according to attribute specified
     */
    void sortList(List<Person> sortedList) throws CommandException {
        switch (attribute) {
        case Phone.SORT_ATTRIBUTE:
            Collections.sort(sortedList, new PhoneSorter());
            break;
        case Name.SORT_ATTRIBUTE:
            Collections.sort(sortedList, new NameSorter());
            break;
        case Email.SORT_ATTRIBUTE:
            Collections.sort(sortedList, new EmailSorter());
            break;
        case Address.SORT_ATTRIBUTE:
            Collections.sort(sortedList, new AddressSorter());
            break;
        case Position.SORT_ATTRIBUTE:
            Collections.sort(sortedList, new PositionSorter());
            break;
        case Kpi.SORT_ATTRIBUTE:
            Collections.sort(sortedList, new KpiSorter());
            break;
        default:
            throw new CommandException(MESSAGE_INVALID_SORT_ATTRIBUTE);
        }

        if (order.equals("asc")) {
        } else if (order.equals("des")) {
            Collections.reverse(sortedList);
        } else {
            throw new CommandException(MESSAGE_INVALID_SORT_ORDER);
        }
    }


}
