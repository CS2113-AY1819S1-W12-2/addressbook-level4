//@@author peiying98
package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_KPI;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NOTE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_POSITION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import seedu.address.commons.util.FileEncryptor;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.CliSyntax;
import seedu.address.model.Model;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;

/**
 * Commands for sorting by respective attributes
 */

public class SortCommand extends Command {

    public static final String COMMAND_WORD = CliSyntax.COMMAND_SORT;

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Sorts address book in ascending order"
            + "according to the attribute specified\n"
            + "attributes includes n/, p/, e/, a/, t/, k/, r/"
            + "Parameters: "
            + "[PREFIX]\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_NAME;

    // might add reverse later on

    public static final String MESSAGE_SORT_SUCCESS = "Sorted address book by %1$s.";
    public static final String MESSAGE_SORT_EMPTY = "No person to sort in this attribute.";

    private final String attribute;



    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {

    }
}
