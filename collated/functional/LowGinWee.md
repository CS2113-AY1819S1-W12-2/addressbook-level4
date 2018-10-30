# LowGinWee
###### /java/seedu/address/logic/Logic.java
``` java
    /** @return the TreeMap of the Schedule */
    TreeMap<Date, ArrayList<Activity>> getSchedule();

```
###### /java/seedu/address/logic/parser/AddCommandParser.java
``` java
        /**
         * Checks if note has been specified
         */
        Note note = new Note();
        if (arePrefixesPresent(argMultimap, PREFIX_NOTE)) {
            note = ParserUtil.parseNote(argMultimap.getValue(PREFIX_NOTE).get());
        }

        /**
         * Checks if position has been specified
         */
        Position position = new Position();
        if (arePrefixesPresent(argMultimap, PREFIX_POSITION)) {
            position = ParserUtil.parsePosition(argMultimap.getValue(PREFIX_POSITION).get());
        }

        /**
         * Checks if Kpi has been specified
         */
        Kpi kpi = new Kpi();
        if (arePrefixesPresent(argMultimap, PREFIX_KPI)) {
            kpi = ParserUtil.parseKpi(argMultimap.getValue(PREFIX_KPI).get());
        }
```
###### /java/seedu/address/logic/parser/ParserUtil.java
``` java
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
        return Activity.toDate(day, --month, year);
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

```
###### /java/seedu/address/logic/parser/EditCommandParser.java
``` java
        //TODO Refactor this 2 methods, remove if null/empty
        if (argMultimap.getValue(PREFIX_POSITION).isPresent()) {
            if (argMultimap.getValue(PREFIX_POSITION).get().isEmpty()) {
                editPersonDescriptor.setRemovePosition();
            } else {
                editPersonDescriptor.setPosition(ParserUtil.parsePosition(argMultimap.getValue(PREFIX_POSITION).get()));
            }
        }
        if (argMultimap.getValue(PREFIX_KPI).isPresent()) {
            if (argMultimap.getValue(PREFIX_KPI).get().isEmpty()) {
                editPersonDescriptor.setRemoveKpi();
            } else {
                editPersonDescriptor.setKpi(ParserUtil.parseKpi(argMultimap.getValue(PREFIX_KPI).get()));
            }
        }
        //TODO to reset notes if empty field
        if (argMultimap.getValue(PREFIX_NOTE).isPresent()) {
            if (argMultimap.getValue(PREFIX_NOTE).get().isEmpty()) {
                editPersonDescriptor.setRemoveNote();
            } else {
                editPersonDescriptor.setNote(ParserUtil.parseNote(argMultimap.getValue(PREFIX_NOTE).get()));
            }
        }
```
###### /java/seedu/address/logic/parser/ScheduleCommandParser.java
``` java
package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ACTIVITY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;

import java.util.Date;
import java.util.stream.Stream;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.ScheduleAddCommand;
import seedu.address.logic.commands.ScheduleCommand;
import seedu.address.logic.commands.ScheduleDeleteCommand;
import seedu.address.logic.commands.ScheduleEditCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.schedule.Activity;

/**
 * Parses input arguments and creates a new ScheduleCommand object
 */
public class ScheduleCommandParser implements Parser<ScheduleCommand> {

    private String errorMessage;
    /**
     * Parses the given {@code String} of arguments in the context of the ScheduleCommand
     * and returns a ScheduleCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public ScheduleCommand parse(String args) throws ParseException {
        args = args.trim();
        String[] input = args.split(" ", 2);
        try {
            switch (input[0].trim()) {
            case ScheduleCommand.COMMAND_WORD_ADD:
                errorMessage = ScheduleCommand.MESSAGE_ADD;
                return parseAdd(input[1]);
            case ScheduleCommand.COMMAND_WORD_DELETE:
                errorMessage = ScheduleCommand.MESSAGE_DELETE;
                return parseDelete(input[1]);
            case ScheduleCommand.COMMAND_WORD_EDIT:
                errorMessage = ScheduleCommand.MESSAGE_EDIT;
                return parseEdit(input[1]);
            default:
                throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                        ScheduleCommand.MESSAGE_USAGE));
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, errorMessage));
        }
    }

    /**
     * Parses the given {@code String} of arguments in the context of the ScheduleAddCommand
     * and returns a ScheduleAddCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    private ScheduleCommand parseAdd(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(
                        " " + args,
                        PREFIX_DATE,
                        PREFIX_ACTIVITY);
        if (!arePrefixesPresent(argMultimap, PREFIX_DATE, PREFIX_ACTIVITY) || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, ScheduleCommand.MESSAGE_ADD));
        }
        Date date = ParserUtil.parseDate(argMultimap.getValue(PREFIX_DATE).get());
        String task = ParserUtil.parseActivityName(argMultimap.getValue(PREFIX_ACTIVITY).get());
        Activity activity = new Activity(date, task);
        return new ScheduleAddCommand(activity);
    }

    /**
     * Parses the given {@code String} of arguments in the context of the ScheduleDeleteCommand
     * and returns a ScheduleDeleteCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    private ScheduleCommand parseDelete(String args) throws ParseException {

        Index index;
        try {
            index = Index.fromOneBased(Integer.parseInt(args));
        } catch (NumberFormatException e) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    ScheduleCommand.MESSAGE_INVALID_INDEX + "\n" + ScheduleCommand.MESSAGE_DELETE));
        }
        return new ScheduleDeleteCommand(index);
    }

    /**
     * Parses the given {@code String} of arguments in the context of the ScheduleEditCommand
     * and returns a ScheduleEditCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    private ScheduleCommand parseEdit(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(
                        " " + args,
                        PREFIX_ACTIVITY);
        if (!arePrefixesPresent(argMultimap, PREFIX_ACTIVITY) || argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, ScheduleCommand.MESSAGE_EDIT));
        }

        Index index;
        try {
            index = Index.fromOneBased(Integer.parseInt(argMultimap.getPreamble().trim()));
        } catch (NumberFormatException e) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, ScheduleCommand.MESSAGE_EDIT));
        }
        String task = argMultimap.getValue(PREFIX_ACTIVITY).get().trim();
        return new ScheduleEditCommand(index, task);
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
```
###### /java/seedu/address/logic/commands/ScheduleAddCommand.java
``` java
package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.model.Model;
import seedu.address.model.schedule.Activity;

/**
 * Adds an {@code Activity} to the schedule in the address book.
 */
public class ScheduleAddCommand extends ScheduleCommand {
    public static final String MESSAGE_SUCCESS = "Task \"%s\" on %s has been added to your schedule.";
    private final Activity toAdd;

    /**
     * Creates an ScheduleAddCommand to add the specified {@code Activity}
     */
    public ScheduleAddCommand(Activity activity) {
        requireNonNull(activity);
        toAdd = activity;
    }

    @Override
    public CommandResult updateSchedule(Model model) {
        model.addActivity(toAdd);
        return new CommandResult(String.format(MESSAGE_SUCCESS,
                toAdd.getActivityName(),
                Activity.getDateString(toAdd.getDate())));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof ScheduleAddCommand// instanceof handles nulls
                && toAdd.equals(((ScheduleAddCommand) other).toAdd));
    }
}
```
###### /java/seedu/address/logic/commands/ScheduleCommand.java
``` java
package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.COMMAND_SCHEDULE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ACTIVITY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.FileEncryptor;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.UserPrefs;
import seedu.address.model.schedule.Activity;

/**
 * Updates the schedule in the address book.
 */
public abstract class ScheduleCommand extends Command {

    public static final String COMMAND_WORD = COMMAND_SCHEDULE;
    public static final String COMMAND_WORD_ADD = "add";
    public static final String COMMAND_WORD_DELETE = "delete";
    public static final String COMMAND_WORD_EDIT = "edit";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": updates contents in your schedule.\n"
            + "parameters: \"add\", \"edit\" or \"delete\"";
    //TODO to check for duplicates
    public static final String MESSAGE_DUPLICATE_TASK = "This task already exists in the schedule";
    public static final String MESSAGE_INVALID_INDEX = "Index is not valid";
    public static final String MESSAGE_ADD = COMMAND_WORD
            + " "
            + COMMAND_WORD_ADD
            + ": Adds a new task to your schedule.\n"
            + "parameters: "
            + PREFIX_DATE + "DD/MM/YYYY "
            + PREFIX_ACTIVITY + "task";
    public static final String MESSAGE_DELETE = COMMAND_WORD
            + " "
            + COMMAND_WORD_DELETE
            + ": Deletes task, by index, from schedule.\n"
            + "parameters: INDEX";
    public static final String MESSAGE_EDIT = COMMAND_WORD
            + " "
            + COMMAND_WORD_EDIT
            + ": Edit task, by index, from schedule.\n"
            + "parameters: INDEX " + PREFIX_ACTIVITY + "Task";

    /**
     * Updates the schedule, add, edit or delete.
     */
    public abstract CommandResult updateSchedule(Model model) throws CommandException;

    /**
     * @param index specified index of task to edit or delete.
     * @return {@code Activity} of specified index.
     */
    public Activity getActivityFromIndex (Model model, Index index) throws CommandException {
        List<Activity> activities = model.getActivityList();
        if (index.getZeroBased() >= activities.size()) {
            throw new CommandException(MESSAGE_INVALID_INDEX);
        }
        return activities.get(index.getZeroBased());
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);
        UserPrefs userPref = new UserPrefs();
        FileEncryptor fe = new FileEncryptor(userPref.getAddressBookFilePath().toString());

        if (fe.isLocked()) {
            throw new CommandException(FileEncryptor.MESSAGE_ADDRESS_BOOK_LOCKED);
        }

        CommandResult result = updateSchedule(model);
        model.commitAddressBook();
        return result;
    }
}
```
###### /java/seedu/address/logic/commands/EditCommand.java
``` java
        public EditPersonDescriptor() {
            removePosition = false;
            removeKpi = false;
            removeNote = false;
        }
```
###### /java/seedu/address/logic/commands/EditCommand.java
``` java
        public void setPosition(Position position) {
            this.position = position;
        }

        public Optional<Position> getPosition() {
            return Optional.ofNullable(position);
        }

        public void setKpi(Kpi kpi) {
            this.kpi = kpi;
        }

        public Optional<Kpi> getKpi() {
            return Optional.ofNullable(kpi);
        }

        public void setNote(Note note) {
            this.note = note;
        }

        public Optional<Note> getNote() {
            return Optional.ofNullable(note);
        }

        public void setRemovePosition() {
            removePosition = true;
        }

        public boolean isRemovePosition() {
            return removePosition;
        }

        public void setRemoveKpi() {
            removeKpi = true;
        }

        public boolean isRemoveKpi() {
            return removePosition;
        }

        public void setRemoveNote() {
            removeNote = true;
        }

        public boolean isRemoveNote() {
            return removePosition;
        }
```
###### /java/seedu/address/logic/commands/ScheduleEditCommand.java
``` java
package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.schedule.Activity;

/**
 * Edits an {@code Activity} to the schedule in the address book.
 */
public class ScheduleEditCommand extends ScheduleCommand {
    public static final String MESSAGE_SUCCESS = "Task \"%s\" on %s has been edited to \"%s\".";
    private final String task;
    private final Index index;

    /**
     * Creates an ScheduleEditCommand to edit the specified {@code Activity}
     */
    public ScheduleEditCommand(Index index, String task) {
        requireNonNull(task);
        requireNonNull(index);
        this.task = task;
        this.index = index;
    }

    @Override
    public CommandResult updateSchedule(Model model) throws CommandException {
        Activity toDelete = getActivityFromIndex(model, index);
        Activity toAdd = new Activity(toDelete.getDate(), task);
        model.updateActivity(toDelete, toAdd);
        return new CommandResult(String.format(MESSAGE_SUCCESS,
                toDelete.getActivityName(),
                Activity.getDateString(toAdd.getDate()),
                toAdd.getActivityName()));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof ScheduleEditCommand// instanceof handles nulls
                && task.equals(((ScheduleEditCommand) other).task)
                && index.equals(((ScheduleEditCommand) other).index));
    }
}
```
###### /java/seedu/address/logic/commands/ScheduleDeleteCommand.java
``` java
package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.schedule.Activity;

/**
 * Deletes an {@code Activity} to the schedule in the address book.
 */
public class ScheduleDeleteCommand extends ScheduleCommand {
    public static final String MESSAGE_SUCCESS = "Task \"%s\" on %s has been deleted.";
    private final Index index;

    /**
     * Creates an ScheduleDeleteCommand to delete the specified {@code Activity}
     */
    public ScheduleDeleteCommand(Index index) {
        requireNonNull(index);
        this.index = index;
    }

    @Override
    public CommandResult updateSchedule(Model model) throws CommandException {
        Activity toDelete = getActivityFromIndex(model, index);
        model.deleteActivity(toDelete);
        return new CommandResult(String.format(MESSAGE_SUCCESS,
                toDelete.getActivityName(),
                Activity.getDateString(toDelete.getDate())));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof ScheduleDeleteCommand// instanceof handles nulls
                && index.equals(((ScheduleDeleteCommand) other).index));
    }
}
```
###### /java/seedu/address/logic/LogicManager.java
``` java
    @Override
    public TreeMap<Date, ArrayList<Activity>> getSchedule() {
        return model.getSchedule();
    }

```
###### /java/seedu/address/storage/XmlAdaptedPerson.java
``` java
        final Position modelPosition;
        //TODO refactor this? note to doesExist()?
        if (position == null) {
            modelPosition = new Position();
        } else if (!Position.isValidPosition(position)) {
            throw new IllegalValueException(Position.MESSAGE_POSITION_CONSTRAINTS);
        } else {
            modelPosition = new Position(position);
        }

        final Kpi modelKpi;
        //TODO refactor this? note to doesExist()?
        if (kpi == null) {
            modelKpi = new Kpi();
        } else if (!Kpi.isValidKpi(kpi)) {
            throw new IllegalValueException(Kpi.MESSAGE_KPI_CONSTRAINTS);
        } else {
            modelKpi = new Kpi(kpi);
        }

        final Note modelNote;
        //TODO refactor this? note to doesExist()?
        if (note == null) {
            modelNote = new Note();
        } else if (!Note.isValidNote(note)) {
            throw new IllegalValueException(Note.MESSAGE_NOTE_CONSTRAINTS);
        } else {
            modelNote = new Note(note);
        }
```
###### /java/seedu/address/model/schedule/Activity.java
``` java
package seedu.address.model.schedule;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Represents an Activity in the schedule.
 */
public class Activity {
    public static final String DATE_VALIDATION_REGEX = "\\d{2}/\\d{2}/\\d{4}";
    public static final String ACTIVITY_VALIDATION_REGEX = "[\\p{Alnum}][\\p{Alnum} ]*\\.*";
    public static final String MESSAGE_DATE_CONSTRAINTS = "Date should be in \"DD/MM/YYYY\" and must be a valid date.";
    public static final String MESSAGE_ACTIVITY_CONSTRAINTS = "Task name should only contain alphanumeric characters,"
            + "spaces and fullstops.";

    private final Date date;
    private final String activityName;

    /**
     * Creates an Activity.
     * @param date A valid date.
     * @param activity Activity string.
     */
    public Activity(Date date, String activity) {
        requireNonNull(activity);
        requireNonNull(date);
        checkArgument(isValidActivity(activity), MESSAGE_ACTIVITY_CONSTRAINTS);
        this.date = date;
        this.activityName = activity;
    }

    /**
     * @return Date of activity.
     */
    public Date getDate() {
        return date;
    }

    /**
     * @return Activity name.
     */
    public String getActivityName() {
        return activityName;
    }

    /**
     * Converts day, month and year specified to a {@code Date} object
     * @param day A valid day of the month
     * @param month A valid month of the year
     * @param year A valid year.
     * @return {@code Date} of activity.
     */
    public static Date toDate(int day, int month, int year) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, day);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date date = cal.getTime();
        return date;
    }

    /**
     * @return {@code String} of date in "DAY dd/mm/yyyy" format.
     */
    public static String getDateString (Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        String dayOfWeek = new SimpleDateFormat("EE", Locale.ENGLISH).format(date);
        int month = cal.get(Calendar.MONTH);
        return dayOfWeek + " " + cal.get(Calendar.DATE) + "/" + ++month
                + "/" + cal.get(Calendar.YEAR);
    }


    /**
     * Checks if specified date is valid
     */
    public static boolean isValidDate(String test) {
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        format.setLenient(false);
        try {
            format.parse(test);
        } catch (ParseException e) {
            return false;
        }
        return test.matches(DATE_VALIDATION_REGEX);
    }

    /**
     * Checks if specified Activity name is valid
     */
    public static boolean isValidActivity(String test) {
        return test.matches(ACTIVITY_VALIDATION_REGEX);
    }
    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Activity // instanceof handles nulls
                && activityName.equals(((Activity) other).activityName) // state check
                && date.equals(((Activity) other).date)); // state check
    }

    @Override
    public int hashCode() {
        return (activityName + date).hashCode();
    }
}
```
###### /java/seedu/address/model/schedule/Schedule.java
``` java
package seedu.address.model.schedule;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * A TreeMap with Unique Dates of activities as keys. Each Date has a list of Activities, with the same date,
 * as its value.
 */
public class Schedule {
    private final TreeMap<Date, ArrayList<Activity>> schedule = new TreeMap<>();

    /**
     * Constructs the Schedule.
     */
    public Schedule() {
    }

    /**
     * Sets the schedule based on a list of activities
     */
    public void setSchedule(List<Activity> activities) {
        requireNonNull(activities);
        schedule.clear();
        for (Activity activity : activities) {
            add(activity);
        }
    }

    /**
     * @return a list of all activities, sorted based on their dates
     */
    public ObservableList<Activity> getActivities() {
        ObservableList<Activity> activities = FXCollections.observableArrayList();
        for (Date date : schedule.keySet()) {
            activities.addAll(schedule.get(date));
        }
        return FXCollections.unmodifiableObservableList(activities);
    }

    /**
     * @return schedule.
     */
    public TreeMap<Date, ArrayList<Activity>> getSchedule() {
        return schedule;
    }

    /**
     * Adds an activity to the schedule.
     * @param activity A valid activity.
     */
    public void add(Activity activity) {
        requireNonNull(activity);
        Date date = activity.getDate();
        if (!contains(date)) {
            schedule.put(date, new ArrayList<>());
        }
        schedule.get(date).add(activity);
    }

    /**
     * Deletes an activity from the schedule.
     * @param activity A valid activity.
     */
    public void delete(Activity activity) {
        requireNonNull(activity);
        schedule.get(activity.getDate()).remove(activity);
        if (schedule.get(activity.getDate()).isEmpty()) {
            schedule.remove(activity.getDate());
        }
    }

    /**
     * Checks if a date is unique in the schedule.
     * @param date A valid date.
     * @return A boolean if the date exists.
     */
    private boolean contains(Date date) {
        if (schedule.containsKey(date)) {
            return true;
        }
        return false;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Schedule // instanceof handles nulls
                && schedule.equals(((Schedule) other).schedule));
    }


}
```
###### /java/seedu/address/model/person/Position.java
``` java
package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Person's Position in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidPosition(String)}
 */
public class Position {
    //TODO change constraints
    public static final String MESSAGE_POSITION_CONSTRAINTS = "Positions should only contain "
            + "alphanumeric characters and spaces";

    public static final String POSITION_VALIDATION_REGEX = "[\\p{Alnum}][\\p{Alnum} ]*";

    public final String value;

    /**
     * Constructs a {@code Position}.
     *
     * @param position A valid position name.
     */
    public Position(String position) {
        requireNonNull(position);
        checkArgument(isValidPosition(position), MESSAGE_POSITION_CONSTRAINTS);
        value = position;
    }

    /**
     * No position provided
     */
    //TODO find a better solution to null.
    public Position() {
        value = null;
    }

    /**
     * Returns true if a position has been assigned to the person.
     */
    public boolean doesExist() {
        if (value != null) {
            return true;
        }
        return false;
    }

    /**
     * Returns true if a given string is a valid position.
     */
    public static boolean isValidPosition(String test) {
        return test.matches(POSITION_VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        if (!doesExist() && !((Position) other).doesExist()) {
            return true;
        }
        return other == this // short circuit if same object
                || (other instanceof Position // instanceof handles nulls
                && value.equals(((Position) other).value)); // state check
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
```
###### /java/seedu/address/model/person/Kpi.java
``` java
package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import java.util.Objects;

/**
 * Represents a Person's KPI(Key Performance Index) in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidKpi(String)}
 */
public class Kpi {

    public static final String MESSAGE_KPI_CONSTRAINTS = "KPI score should be a number from 0 - 5";
    public static final String KPI_VALIDATION_REGEX = "([0-4]{1}(\\.[0-9]+)?)|([5]{1}(\\.[0]+)?)";
    public final String value;

    /**
     * Constructs a {@code KPI}.
     *
     * @param kpi A valid KPI score.
     */
    public Kpi(String kpi) {
        requireNonNull(kpi);
        checkArgument(isValidKpi(kpi), MESSAGE_KPI_CONSTRAINTS);
        value = kpi;
    }

    public Kpi() {
        this.value = null;
    }

    /**
     * Returns true if a KPI has been assigned to the person.
     */
    public boolean doesExist() {
        if (value != null) {
            return true;
        }
        return false;
    }

    /**
     * Returns true if a given string is a valid KPI score.
     */
    public static boolean isValidKpi(String test) {
        return test.matches(KPI_VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return value;
    }

    //TODO To resolve issue when one is null and the other is not
    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Kpi // instanceof handles nulls
                && Objects.equals(this.value, ((Kpi) other).value));
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}

```
###### /java/seedu/address/model/person/Note.java
``` java
package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Note in the address book.
 * Guarantees: immutable; name is valid as declared in {@link #isValidNote(String)}
 */
public class Note {

    public static final String MESSAGE_NOTE_CONSTRAINTS = "Notes or Descriptions "
            + "should only contain alphanumeric characters and spaces";

    public static final String NOTE_VALIDATION_REGEX = "[\\p{Alnum}][\\p{Alnum} ]*\\.*";

    public final String value;

    /**
     * Constructs a {@code Note}
     */
    public Note() {
        value = null;
    }

    /**
     * Constructs a {@code Tag}.
     *
     * @param note A valid note provided.
     */
    public Note(String note) {
        requireNonNull(note);
        checkArgument(isValidNote(note), MESSAGE_NOTE_CONSTRAINTS);
        value = note;
    }

    /**
     * Returns true if a Note has been assigned to the person.
     */
    public boolean doesExist() {
        if (value != null) {
            return true;
        }
        return false;
    }

    /**
     * Returns true if a given string is a valid note.
     */
    public static boolean isValidNote(String test) {
        return test.matches(NOTE_VALIDATION_REGEX);
    }

    /**
     * Format state as text for viewing.
     */
    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        if (!doesExist() && !((Note) other).doesExist()) {
            return true;
        }
        return other == this // short circuit if same object
                || (other instanceof Note // instanceof handles nulls
                && value.equals(((Note) other).value)); // state check
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}


```
###### /java/seedu/address/model/person/Person.java
``` java
    public Position getPosition() {
        if (positionDoesExist()) {
            return position;
        }
        return new Position();
    }

    public Kpi getKpi() {
        if (kpiDoesExist()) {
            return kpi;
        }
        return new Kpi();
    }

    public Note getNote() {
        if (noteDoesExist()) {
            return note;
        }
        return new Note();
    }
```
###### /java/seedu/address/model/AddressBook.java
``` java
    /**
     * Replaces the contents of the tag HashMap with {@code tagListMap}.
     */
    public void setTags(ObservableMap<Tag, UniquePersonList> tagListMap) {
        this.tags.setTags(tagListMap);
    }

    /**
     * Replaces the contents of the schedule with {@code activities}.
     */
    public void setSchedule(List<Activity> activities) {
        this.schedule.setSchedule(activities);
    }

    /**
     * Adds an Activity to the schedule in the address book.
     */
    public void addActivity(Activity activity) {
        schedule.add(activity);
    }

    /**
     * Deletes an Activity from the schedule in the address book.
     */
    public void deleteActivity(Activity activity) {
        schedule.delete(activity);
    }

    /**
     * Replaces the given activity {@code target} with {@code editedActivity}.
     * {@code target} must exist in the address book.
     */
    public void updateActivity(Activity target, Activity editedActivity) {
        schedule.delete(target);
        schedule.add(editedActivity);
    }

    @Override
    public ObservableList<Activity> getActivityList() {
        return schedule.getActivities();
    }

    @Override
    public TreeMap<Date, ArrayList<Activity>> getSchedule() {
        return schedule.getSchedule();
    }
}
```
###### /java/seedu/address/model/ModelManager.java
``` java

    /**
     * Get a list of unique tags of all persons in the addressbook
     * @return a list of unique tags.
     */
    @Override
    public List<Tag> getUniqueTagList() {
        return versionedAddressBook.getUniqueTagList();
    }
    /**
     * Adds an activity to the schedule in the address book.
     */
    @Override
    public void addActivity(Activity activity) {
        versionedAddressBook.addActivity(activity);
        indicateAddressBookChanged();
    }
    /**
     * Deletes an activity from the schedule in the address book.
     */
    @Override
    public void deleteActivity(Activity activity) {
        versionedAddressBook.deleteActivity(activity);
        indicateAddressBookChanged();
    }
    /**
     * Replaces the given activity {@code target} with {@code editedActivity}.
     * {@code target} must exist in the address book.
     */
    @Override
    public void updateActivity(Activity target, Activity editedActivity) {
        versionedAddressBook.updateActivity(target, editedActivity);
        indicateAddressBookChanged();
    }
    /**
     * Get the sorted list of activities in the schedule.
     * @return the list of activities.
     */
    @Override
    public ObservableList<Activity> getActivityList() {
        return versionedAddressBook.getActivityList();
    }
    /**
     * Get a TreeMap with the Date of activities as its key and a list of the corresponding activities as its value.
     * @return TreeMap of dates and activity lists.
     */
    @Override
    public TreeMap<Date, ArrayList<Activity>> getSchedule() {
        return versionedAddressBook.getSchedule();
    }
}
```
###### /java/seedu/address/model/tag/Tag.java
``` java
package seedu.address.model.tag;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import seedu.address.commons.core.index.Index;

/**
 * Represents a Tag in the address book.
 * Guarantees: immutable; name is valid as declared in {@link #isValidTagName(String)}
 */
public class Tag {
    public static final String MESSAGE_TAG_CONSTRAINTS = "Tags names should be alphanumeric,"
            + "followed by its priority(optional)(1 - 2)";
    public static final String TAG_VALIDATION_REGEX = "(((\\p{Alnum})+)(\\s([1-2]{1}))?)";
    public static final int PRIORITY_HIGH = 2;
    public static final int PRIORITY_MEDIUM = 1;
    public static final int PRIORITY_LOW = 0;

    public final String tagName;
    public final Index priority;

    /**
     * Constructs a {@code Tag}.
     * @param tagName A valid tag name.
     * @param priority Priority of Tag.
     */
    public Tag(String tagName, Index priority) {
        requireNonNull(tagName);
        checkArgument(isValidTagName(tagName), MESSAGE_TAG_CONSTRAINTS);
        this.tagName = tagName;
        this.priority = priority;
    }

    /**
     * Constructs a {@code Tag}.
     * @param tagName A valid tag name.
     */
    public Tag(String tagName) {
        requireNonNull(tagName);
        checkArgument(isValidTagName(tagName), MESSAGE_TAG_CONSTRAINTS);
        this.tagName = tagName;
        this.priority = Index.fromZeroBased(PRIORITY_LOW);
    }

    /**
     * Returns true if a given string is a valid tag name.
     */
    public static boolean isValidTagName(String test) {
        return test.matches(TAG_VALIDATION_REGEX);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Tag // instanceof handles nulls
                && tagName.equals(((Tag) other).tagName)
                && priority.equals(((Tag) other).priority)); // state check
    }

    @Override
    public int hashCode() {
        return (tagName).hashCode();
    }

    /**
     * Format state as text for viewing.
     */
    @Override
    public String toString() {
        String fullTag = tagName;
        if (!(priority.getZeroBased() == PRIORITY_LOW)) {
            fullTag += " " + priority.getZeroBased();
        }
        return fullTag;
    }
}
```
###### /java/seedu/address/model/Model.java
``` java
    List<Tag> getUniqueTagList();

    /**
     * Adds an activity to the schedule in the address book.
     */
    void addActivity(Activity activity);
    /**
     * Deletes an activity from the schedule in the address book.
     */
    void deleteActivity(Activity activity);
    /**
     * Replaces the given activity {@code target} with {@code editedActivity}.
     * {@code target} must exist in the address book.
     */
    void updateActivity(Activity target, Activity editedActivity);
    /**
     * Get the sorted list of activities in the schedule.
     * @return the list of activities.
     */
    ObservableList<Activity> getActivityList();
    /**
     * Get a TreeMap with the Date of activities as its key and a list of the corresponding activities as its value.
     * @return TreeMap of dates and activity lists.
     */
    TreeMap<Date, ArrayList<Activity>> getSchedule();

}
```
