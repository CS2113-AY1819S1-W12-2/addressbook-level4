//@@author peiying98
package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.CliSyntax;
import seedu.address.model.Model;

/**
 * Commands for sorting by respective attributes
 */

public class SortCommand extends Command {

    public static final String COMMAND_WORD = CliSyntax.COMMAND_SORT;
    public static final String REVERSE_ORDER = CliSyntax.REVERSE_SEQUENCE;

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Sorts address book in ascending or descending order"
            + "according to the attribute specified\n"
            + "attributes includes n/, p/, e/, a/, t/, k/, r/"
            + "Parameters: "
            + "[PREFIX]\n"
            + "Example: " + COMMAND_WORD + " " + PREFIX_NAME + " " + REVERSE_ORDER;

    public static final String MESSAGE_SORT_SUCCESS = "Sorted address book by %1$s in %2$s order.";
    public static final String MESSAGE_SORT_EMPTY = "No person to sort in this attribute.";

    private final String attribute;
    private final String isReverseOrder;

    // Default values for sorting
    private String sortBy = "name";
    private String sequence = "ascending";

    /**
     * @param attribute     specify which attribute to sort by
     * @param isReverseOrder specify if sorting is to be in ascending or descending order
     */

    public SortCommand(String attribute, boolean isReverseOrder) {
        requireNonNull(attribute);
        requireNonNull(isReverseOrder);

        this.attribute = attribute;
        this.isReverseOrder = isReverseOrder;

    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);

        Comparator<ReadOnlyPerson> comparator = sortComparatorByPrefix(this.attribute);
        try {
            model.sortPersonList(comparator, isReverseOrder);
        } catch (EmptyPersonListException eple) {
            throw new CommandException(MESSAGE_PERSONS_LIST_EMPTY);
        }

        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        if (isReverseOrder) {
            this.sequence = "descending";
        }
        return new CommandResult(String.format(MESSAGE_SUCCESS, sortBy, sequence));
    }

    /**
     * Comparator depending on the attribute specified
     */
    private Comparator<ReadOnlyPerson> sortComparatorByPrefix(String attribute) {
        switch (attribute) {
            case PREFIX_NAME:
                this.sortBy = "name";
                return (o1, o2) -> o1.getName().toString().compareToIgnoreCase(o2.getName().toString());
            case PREFIX_PHONE:
                this.sortBy = "phone";
                return (o1, o2) -> o1.getPhone().toString().compareToIgnoreCase(o2.getPhone().toString());
            case PREFIX_EMAIL:
                this.sortBy = "email";
                return (o1, o2) -> o1.getEmail().toString().compareToIgnoreCase(o2.getEmail().toString());
            case PREFIX_ADDRESS:
                this.sortBy = "address";
                return (o1, o2) -> o1.getAddress().toString().compareToIgnoreCase(o2.getAddress().toString());
            case PREFIX_TAG:
                this.sortBy = "tag";
                return (o1, o2) -> o1.getTag().toString().compareToIgnoreCase(o2.getTag().toString());
            default:
                return (o1, o2) -> o1.getName().toString().compareToIgnoreCase(o2.getName().toString());
        }
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof SortCommand // instanceof handles nulls
                && attribute.equals(((SortCommand) other).attribute)
                && REVERSE_ORDER.equals(((SortCommand) other).REVERSE_ORDER));

    }
}
