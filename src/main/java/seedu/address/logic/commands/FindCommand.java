//@@author lws803
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
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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
import seedu.address.model.person.KpiContainsKeywordPredicate;
import seedu.address.model.person.NameContainsKeywordsPredicate;
import seedu.address.model.person.NoteContainsKeywordsPredicate;
import seedu.address.model.person.Person;
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

    private final Predicate<Person> predicate = PREDICATE_SHOW_ALL_PERSONS;
    private Map<Prefix, String[]> prefixKeywordMap;
    private Prefix[] types;
    private Set<String> actualMatchesStrings = new HashSet<>();
    private Set<String> guessedMatchesStrings = new HashSet<>();


    public FindCommand(Map<Prefix, String[]> prefixKeywordMap,
                       Prefix[] types) {
        this.prefixKeywordMap = prefixKeywordMap;
        this.types = types;
    }

    @Override
    public CommandResult execute(final Model model, final CommandHistory history) throws CommandException {

        UserPrefs userPref = new UserPrefs();
        FileEncryptor fe = new FileEncryptor(userPref.getAddressBookFilePath().toString());

        if (fe.isLocked()) {
            throw new CommandException(FileEncryptor.MESSAGE_ADDRESS_BOOK_LOCKED);
        }
        requireNonNull(model);

        Predicate<Person> combinedPredicate = PREDICATE_SHOW_ALL_PERSONS;

        combinedPredicate = getPersonPredicate(model, combinedPredicate);

        model.updateFilteredPersonList(combinedPredicate);

        return new CommandResult(
                String.format(Messages.MESSAGE_PERSONS_LISTED_OVERVIEW, model.getFilteredPersonList().size())
                + "\n"
                + "Keywords matched: "
                + combinedMatchesString(actualMatchesStrings)
                + "\n"
                + "Keywords guessed: "
                + combinedMatchesString(guessedMatchesStrings)
        );
    }

    /**
     * Gets the person's predicate based on attributes
     * @param model current model
     * @param combinedPredicate total combined predicate of all the conditions
     * @return returns the total combinedPredicate
     */
    private Predicate<Person> getPersonPredicate(Model model, Predicate<Person> combinedPredicate) {
        for (Prefix type : types) {
            ClosestMatchList closestMatch = new ClosestMatchList(model, type, prefixKeywordMap.get(type));
            List<String> approvedList = Arrays.asList(closestMatch.getApprovedList());
            List<String> keywordsForType = Arrays.asList(prefixKeywordMap.get(type));

            if (type == PREFIX_PHONE) {
                combinedPredicate = combinedPredicate.and(
                        new PhoneContainsKeywordPredicate(approvedList)
                );
            } else if (type == PREFIX_NAME) {
                combinedPredicate = combinedPredicate.and(
                        new NameContainsKeywordsPredicate(approvedList)
                );
            } else if (type == PREFIX_ADDRESS) {
                combinedPredicate = combinedPredicate.and(
                        new AddressContainsKeywordsPredicate(approvedList)
                );
            } else if (type == PREFIX_EMAIL) {
                combinedPredicate = combinedPredicate.and(
                        new EmailContainsKeywordsPredicate(approvedList)
                );
            } else if (type == PREFIX_NOTE) {
                combinedPredicate = combinedPredicate.and(
                        new NoteContainsKeywordsPredicate(approvedList)
                );
            } else if (type == PREFIX_POSITION) {
                combinedPredicate = combinedPredicate.and(
                        new PositionContainsKeywordsPredicate(approvedList)
                );
            } else if (type == PREFIX_TAG) {
                combinedPredicate = combinedPredicate.and(
                        new TagContainsKeywordsPredicate(approvedList)
                );
            } else if (type == PREFIX_KPI) {
                combinedPredicate = combinedPredicate.and(
                        new KpiContainsKeywordPredicate(approvedList)
                );
            }

            Set<String> approvedSet = new HashSet<>(
                    approvedList.stream().distinct().collect(Collectors.toList()));

            Set<String> keywordsForTypeSet = new HashSet<>(
                    keywordsForType.stream().distinct().collect(Collectors.toList()));

            findActualMatches(approvedSet, keywordsForTypeSet);

        }
        return combinedPredicate;
    }


    /**
     * Determine the number of actual keyword matches
     * @param closestMatchesSet closestMatcSet determined by Levensthein distance
     * @param keywordsSet keywords input from command
     */
    private void findActualMatches (final Set<String> closestMatchesSet, final Set<String> keywordsSet) {
        for (String match: closestMatchesSet) {
            if (keywordsSet.contains(match) || keywordsSet.contains(match.replaceAll(",", ""))) {
                actualMatchesStrings.add(match.replaceAll(",", ""));
            } else {
                guessedMatchesStrings.add(match.replaceAll(",", ""));
            }
        }
    }

    /**
     * Combines the matches to form a string for output
     * @return the combined string using StringBuilder
     */
    private String combinedMatchesString (Set<String> stringMatches) {
        StringBuilder output = new StringBuilder("{");
        int count = 0;
        for (String match: stringMatches) {
            if (count == stringMatches.size() - 1) {
                output.append(match);
            } else {
                output.append(match);
                output.append(", ");
            }
            count++;
        }
        output.append("}");
        return output.toString();
    }


    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof FindCommand // instanceof handles nulls
                && predicate.equals(((FindCommand) other).predicate)); // state check
    }
}
