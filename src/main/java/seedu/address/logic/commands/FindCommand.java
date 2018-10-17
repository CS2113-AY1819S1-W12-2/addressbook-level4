package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NOTE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_POSITION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.Arrays;

import seedu.address.commons.core.Messages;
import seedu.address.commons.util.FileEncryptor;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.Prefix;
import seedu.address.model.Model;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.AddressContainsKeywordsPredicate;
import seedu.address.model.person.ClosestMatchList;
import seedu.address.model.person.EmailContainsKeywordsPredicate;
import seedu.address.model.person.NameContainsKeywordsPredicate;
import seedu.address.model.person.NoteContainsKeywordsPredicate;
import seedu.address.model.person.PhoneContainsKeywordPredicate;
import seedu.address.model.person.PositionContainsKeywordsPredicate;
import seedu.address.model.person.TagContainsKeywordsPredicate;


/**
 * Finds and lists all persons in address book whose name contains any of the argument keywords.
 * Keyword matching is case insensitive.
 */
public class FindCommand extends Command {

    public static final String COMMAND_WORD = "find";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds all persons whose names contain any of "
            + "the specified keywords and displays them as a list with index numbers.\n"
            + "Parameters: PREFIX/KEYWORD [MORE_KEYWORDS]...\n"
            + "Example: " + COMMAND_WORD + " " + PREFIX_NAME + "alice bob charlie";

    private final NameContainsKeywordsPredicate predicate;
    private String[] nameKeywords;
    private Prefix type;

    public FindCommand(NameContainsKeywordsPredicate predicate, String[] names, Prefix type) {
        this.predicate = predicate;
        this.nameKeywords = names;
        this.type = type;
    }

    @Override
    public CommandResult execute(final Model model, final CommandHistory history) throws CommandException {

        UserPrefs userPref = new UserPrefs();
        FileEncryptor fe = new FileEncryptor(userPref.getAddressBookFilePath().toString());

        if (fe.isLocked()) {
            throw new CommandException(FileEncryptor.MESSAGE_ADDRESS_BOOK_LOCKED);
        }
        requireNonNull(model);

        ClosestMatchList closestMatch = new ClosestMatchList(model, type, nameKeywords);
        String[] approvedList = closestMatch.getApprovedList();

        if (type == PREFIX_PHONE) {
            model.updateFilteredPersonList(new PhoneContainsKeywordPredicate(Arrays.asList(approvedList)));
        } else if (type == PREFIX_NAME) {
            model.updateFilteredPersonList(new NameContainsKeywordsPredicate(Arrays.asList(approvedList)));
        } else if (type == PREFIX_ADDRESS) {
            model.updateFilteredPersonList(new AddressContainsKeywordsPredicate(Arrays.asList(approvedList)));
        } else if (type == PREFIX_EMAIL) {
            model.updateFilteredPersonList(new EmailContainsKeywordsPredicate(Arrays.asList(approvedList)));
        } else if (type == PREFIX_NOTE) {
            model.updateFilteredPersonList(new NoteContainsKeywordsPredicate(Arrays.asList(approvedList)));
        } else if (type == PREFIX_POSITION) {
            model.updateFilteredPersonList(new PositionContainsKeywordsPredicate(Arrays.asList(approvedList)));
        } else if (type == PREFIX_TAG) {
            model.updateFilteredPersonList(new TagContainsKeywordsPredicate(Arrays.asList(approvedList)));
        }

        return new CommandResult(
                String.format(Messages.MESSAGE_PERSONS_LISTED_OVERVIEW, model.getFilteredPersonList().size()));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof FindCommand // instanceof handles nulls
                && predicate.equals(((FindCommand) other).predicate)); // state check
    }
}
