package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import seedu.address.commons.util.FileEncryptor;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.CliSyntax;
import seedu.address.model.Model;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.model.tag.Tag;

/**
 * Lists persons in the address book using specified arguments as filter.
 */
public class ListCommand extends Command {

    public static final String COMMAND_WORD = CliSyntax.COMMAND_LIST;

    /**
     * Type definitions for attributes to list.
     */
    public static final int TYPE_ALL = 0;
    public static final int TYPE_TAG = 1;
    public static final int TYPE_KPI = 2;

    /**
     * Messages to be displayed
     */
    public static final String MESSAGE_SUCCESS_SINGULAR = "%d person listed!";
    public static final String MESSAGE_SUCCESS = "%d persons listed!";

    /**
     * Instance variables
     */
    private int listType;
    private List<String> predicatesList;
    private Set<Tag> inputTags;

    /**
     * Constructor for list command unrelated to Tags.
     * @param listType the type of attribute to filter.
     * @param predicatesList the list of attributes to filter.
     */
    public ListCommand(int listType, List<String> predicatesList) {
        this.listType = listType;
        this.predicatesList = predicatesList;
    }

    /**
     * Constructor for list command related to Tags.
     * @param listType the type of attribute to filter.
     * @param inputTags the Set of Tags to filter.
     */
    public ListCommand(int listType, Set<Tag> inputTags) {
        this.listType = listType;
        this.inputTags = inputTags;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);
        UserPrefs userPref = new UserPrefs();
        FileEncryptor fe = new FileEncryptor(userPref.getAddressBookFilePath().toString());
        Predicate<Person> predicateToUse;

        if (fe.isLocked()) {
            throw new CommandException(FileEncryptor.MESSAGE_ADDRESS_BOOK_LOCKED);
        }

        switch(listType) {
        case TYPE_TAG:
            predicateToUse = showTagsPredicate(inputTags);
            break;
        case TYPE_KPI:
            predicateToUse = showKpiPredicate(predicatesList);
            break;
        default:
            predicateToUse = PREDICATE_SHOW_ALL_PERSONS;
        }

        model.updateFilteredPersonList(predicateToUse);
        int listCount = model.getFilteredPersonList().size();
        if (listCount <= 1) {
            return new CommandResult(String.format(MESSAGE_SUCCESS_SINGULAR, listCount));
        }
        return new CommandResult(String.format(MESSAGE_SUCCESS, listCount));
    }

    /**
     * Returns the Predicate that filters specified Tags.
     * @param inputTags the Tags to filter.
     * @return the appropriate predicate.
     */
    private Predicate<Person> showTagsPredicate(Set<Tag> inputTags) {
        return p -> !Collections.disjoint(p.getTags(), inputTags);
    }

    /**
     * Returns the Predicate that filters specified KPI.
     * @param predicatesList the list of KPI to filter.
     * @return the appropriate predicate.
     */
    private Predicate<Person> showKpiPredicate(List<String> predicatesList) {
        return p -> predicatesList.contains(p.getKpi().value);
    }
}
