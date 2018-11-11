package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.StringUtil;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Kpi;
import seedu.address.model.person.Name;
import seedu.address.model.person.Note;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Position;
import seedu.address.model.schedule.Activity;
import seedu.address.model.tag.Tag;

/**
 * Contains utility methods used for parsing strings in the various *Parser classes.
 */
public class ParserUtil {

    public static final String MESSAGE_INVALID_INDEX = "Index is not a non-zero unsigned integer.";

    /**
     * Parses {@code oneBasedIndex} into an {@code Index} and returns it. Leading and trailing whitespaces will be
     * trimmed.
     * @throws ParseException if the specified index is invalid (not non-zero unsigned integer).
     */
    public static Index parseIndex(String oneBasedIndex) throws ParseException {
        String trimmedIndex = oneBasedIndex.trim();
        if (!StringUtil.isNonZeroUnsignedInteger(trimmedIndex)) {
            throw new ParseException(MESSAGE_INVALID_INDEX);
        }
        return Index.fromOneBased(Integer.parseInt(trimmedIndex));
    }

    /**
     * Parses a {@code String name} into a {@code Name}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code name} is invalid.
     */
    public static Name parseName(String name) throws ParseException {
        requireNonNull(name);
        String trimmedName = name.trim().toLowerCase();
        if (!Name.isValidName(trimmedName)) {
            throw new ParseException(Name.MESSAGE_NAME_CONSTRAINTS);
        }
        String parsedName = new String();
        StringTokenizer st = new StringTokenizer(trimmedName);
        while (st.hasMoreTokens()) {
            String str = st.nextToken();
            str = Character.toUpperCase(str.charAt(0)) + str.substring(1);
            parsedName += str + " ";
        }
        parsedName = parsedName.trim();
        return new Name(parsedName);
    }

    /**
     * Parses a {@code String phone} into a {@code Phone}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code phone} is invalid.
     */
    public static Phone parsePhone(String phone) throws ParseException {
        requireNonNull(phone);
        String trimmedPhone = phone.trim();
        if (!Phone.isValidPhone(trimmedPhone)) {
            throw new ParseException(Phone.MESSAGE_PHONE_CONSTRAINTS);
        }
        return new Phone(trimmedPhone);
    }

    /**
     * Parses a {@code String address} into an {@code Address}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code address} is invalid.
     */
    public static Address parseAddress(String address) throws ParseException {
        requireNonNull(address);
        String trimmedAddress = address.trim();
        if (!Address.isValidAddress(trimmedAddress)) {
            throw new ParseException(Address.MESSAGE_ADDRESS_CONSTRAINTS);
        }
        return new Address(trimmedAddress);
    }

    /**
     * Parses a {@code String email} into an {@code Email}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code email} is invalid.
     */
    public static Email parseEmail(String email) throws ParseException {
        requireNonNull(email);
        String trimmedEmail = email.trim();
        if (!Email.isValidEmail(trimmedEmail)) {
            throw new ParseException(Email.MESSAGE_EMAIL_CONSTRAINTS);
        }
        return new Email(trimmedEmail);
    }

    /**
     * Parses a {@code String tag} into a {@code Tag}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code tag} is invalid.
     */
    public static Tag parseTag(String tag) throws ParseException {
        requireNonNull(tag);
        String trimmedTag = tag.trim();
        if (!Tag.isValidTagName(trimmedTag)) {
            throw new ParseException(Tag.MESSAGE_TAG_CONSTRAINTS);
        }
        StringTokenizer st = new StringTokenizer(trimmedTag);
        String tagName = st.nextToken();
        Index priority;
        if (st.hasMoreTokens()) {
            priority = Index.fromZeroBased(Integer.parseInt(st.nextToken()));
        } else {
            priority = Index.fromZeroBased(Tag.PRIORITY_LOW);
        }
        return new Tag(tagName, priority);
    }

    /**
     * Parses {@code Collection<String> tags} into a {@code Set<Tag>}.
     */
    public static Set<Tag> parseTags(Collection<String> tags) throws ParseException {
        requireNonNull(tags);
        final Set<Tag> tagSet = new HashSet<>();
        for (String tagName : tags) {
            tagSet.add(parseTag(tagName));
        }
        return tagSet;
    }

    //@@author LowGinWee
    /**
     * Parses a {@code String position} into an {@code Position}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code position} is invalid.
     */
    public static Position parsePosition(String position) throws ParseException {
        String trimmedPosition = position.trim();
        if (!Position.isValidPosition(trimmedPosition)) {
            throw new ParseException(Position.MESSAGE_POSITION_CONSTRAINTS);
        }
        return new Position(trimmedPosition);
    }

    /**
     * Parses a {@code String note} into an {@code Note}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code note} is invalid.
     */
    public static Note parseNote(String note) throws ParseException {
        String trimmedNote = note.trim();
        if (!Note.isValidNote(trimmedNote)) {
            throw new ParseException(Note.MESSAGE_NOTE_CONSTRAINTS);
        }
        return new Note(trimmedNote);
    }
    /**
     * Parses a {@code String kpi} into an {@code Kpi}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code kpi} is invalid.
     */
    public static Kpi parseKpi(String kpi) throws ParseException {
        String trimmedScore = kpi.trim();
        if (!Kpi.isValidKpi(trimmedScore)) {
            throw new ParseException(Kpi.MESSAGE_KPI_CONSTRAINTS);
        }
        return new Kpi(trimmedScore);
    }
    /**
     * Parses a {@code String position} into an {@code Position}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code position} is invalid.
     */
    public static Date parseDate(String dateString) throws ParseException {
        String trimmedDate = dateString.trim();
        if (!Activity.isValidDate(trimmedDate)) {
            throw new ParseException(Activity.MESSAGE_DATE_CONSTRAINTS);
        }
        StringTokenizer tokens = new StringTokenizer(trimmedDate, "/");
        int day = Integer.parseInt(tokens.nextToken());
        int month = Integer.parseInt(tokens.nextToken());
        int year = Integer.parseInt(tokens.nextToken());
        return Activity.toDate(day, month, year);
    }

    /**
     * Parses a {@code String activityName} into an {@code Activity}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code activityName} is invalid.
     */
    public static String parseActivityName(String activityName) throws ParseException {
        String trimmedActivityName = activityName.trim();
        if (!Activity.isValidActivity(trimmedActivityName)) {
            throw new ParseException(Activity.MESSAGE_ACTIVITY_CONSTRAINTS);
        }
        return trimmedActivityName;
    }

    //@@author lekoook
    /**
     * Parses one or more {@code Index} into an {@code Index} list and returns it.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @param oneBasedIndex the user input string.
     * @return the list of {@code Index} to return.
     */
    private static ArrayList<Index> parseMultipleIndex(String oneBasedIndex) {
        String trimmedIndex = oneBasedIndex.trim();
        return StringUtil.tokenizeIndexWithSpace(trimmedIndex);
    }

    /**
     * Parses a range or multiple ranges of {@code Index} into an {@code Index} list and returns it.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @param oneBasedIndex the user input string.
     * @return the list of {@code Index} to return.
     */
    private static ArrayList<Index> parseMultipleRangeIndex(String oneBasedIndex) {
        ArrayList<Index> output = new ArrayList<>();
        StringTokenizer tokenizer = new StringTokenizer(oneBasedIndex, ",");
        while (tokenizer.hasMoreTokens()) {
            ArrayList<Index> indices = StringUtil.tokenizeIndexWithRange(tokenizer.nextToken());
            output.addAll(indices);
        }
        return output;
    }

    /**
     * Parses a single, multiple, or range(s) of {@code Index} into an {@code Index} list and returns it.
     * Leading and trailing whitespaces will be trimmed.
     *
     * A range is defined by using a '-' between two indices, inclusive. Multiple ranges are separated with
     * a comma.
     * Whitespaces are ignored.
     *
     * For example, a valid input specifying ranges could be "1 - 3, 5-7".
     *
     * @param oneBasedIndex the user input string.
     * @return the list of {@code Index} to return.
     * @throws ParseException if the specified index is invalid (not non-zero unsigned integer).
     */
    public static ArrayList<Index> parseIndices(String oneBasedIndex) throws ParseException {

        // Perform a syntax check here
        if (!StringUtil.isValidMultipleIndexFormat(oneBasedIndex)) {
            throw new ParseException(MESSAGE_INVALID_COMMAND_FORMAT);
        }

        if (StringUtil.isRangeIndexFormat(oneBasedIndex)) {
            return parseMultipleRangeIndex(oneBasedIndex);
        } else {
            return parseMultipleIndex(oneBasedIndex);
        }
    }
}
