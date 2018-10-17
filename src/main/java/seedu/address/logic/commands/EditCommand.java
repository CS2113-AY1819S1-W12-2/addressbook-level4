package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ALL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_KPI;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NOTE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_POSITION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.CollectionUtil;
import seedu.address.commons.util.FileEncryptor;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.CliSyntax;
import seedu.address.model.Model;
import seedu.address.model.UserPrefs;
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
 * Edits the details of an existing person in the address book.
 */
public class EditCommand extends Command {

    public static final String COMMAND_WORD = CliSyntax.COMMAND_EDIT;

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the details of the person identified "
            + "by the index number used in the displayed person list. "
            + "Existing values will be overwritten by the input values.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + "[" + PREFIX_NAME + "NAME] "
            + "[" + PREFIX_PHONE + "PHONE] "
            + "[" + PREFIX_EMAIL + "EMAIL] "
            + "[" + PREFIX_ADDRESS + "ADDRESS] "
            + "[" + PREFIX_POSITION + "POSITION] "
            + "[" + PREFIX_KPI + "KPI] "
            + "[" + PREFIX_NOTE + "NOTE] "
            + "[" + PREFIX_TAG + "TAG]...\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_PHONE + "91234567 "
            + PREFIX_EMAIL + "johndoe@example.com\n"
            + "Use " + PREFIX_ALL + " instead of \"INDEX\" to edit all listed persons.";

    public static final String MESSAGE_EDIT_PERSON_SUCCESS = "Edited Person: %1$s";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";
    public static final String MESSAGE_DUPLICATE_PERSON = "This person already exists in the address book.";
    public static final String MESSAGE_EDIT_ALL_SUCCESS = "Edited %d Person(s)";

    private final Index index;
    private final Boolean editAll;
    private final EditPersonDescriptor editPersonDescriptor;

    /**
     * @param index of the person in the filtered person list to edit
     * @param editPersonDescriptor details to edit the person with
     */
    public EditCommand(Index index, EditPersonDescriptor editPersonDescriptor) {
        requireNonNull(index);
        requireNonNull(editPersonDescriptor);

        this.index = index;
        this.editAll = false;
        this.editPersonDescriptor = new EditPersonDescriptor(editPersonDescriptor);
    }
    public EditCommand(EditPersonDescriptor editPersonDescriptor) {
        requireNonNull(editPersonDescriptor);

        this.index = null;
        this.editAll = true;
        this.editPersonDescriptor = new EditPersonDescriptor(editPersonDescriptor);
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);

        UserPrefs userPref = new UserPrefs();
        FileEncryptor fe = new FileEncryptor(userPref.getAddressBookFilePath().toString());

        if (fe.isLocked()) {
            throw new CommandException(FileEncryptor.MESSAGE_ADDRESS_BOOK_LOCKED);
        }

        if (editAll) {
            return editAllEntries(model);
        }

        return editSingleEntry(model);
    }

    /**
     * Edits all persons listed and returns a {@code CommandResult} with the total number of persons edited.
     */
    private CommandResult editAllEntries(Model model) throws CommandException {
        List<Person> lastShownList = model.getFilteredPersonList();
        List<Person> personsToCopy = new ArrayList<>();

        for (Person person : lastShownList) {
            personsToCopy.add(person);
        }
        for (Person personToEdit : personsToCopy) {
            Person editedPerson = createEditedPerson(personToEdit, editPersonDescriptor);

            if (!personToEdit.isSamePerson(editedPerson) && model.hasPerson(editedPerson)) {
                throw new CommandException(MESSAGE_DUPLICATE_PERSON);
            }

            model.updatePerson(personToEdit, editedPerson);
            //TODO to enquire and merge with lekoook
            //model.getTextPrediction().editPerson(personToEdit, editedPerson);
        }

        model.commitAddressBook();

        //TODO change this to show only edited stuff and format
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        return new CommandResult(String.format(MESSAGE_EDIT_ALL_SUCCESS, personsToCopy.size()));
    }

    /**
     * Edits specified person by index and returns a {@code CommandResult} details of {@code editedPerson}
     */
    private CommandResult editSingleEntry(Model model) throws CommandException {
        List<Person> lastShownList = model.getFilteredPersonList();
        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToEdit = lastShownList.get(index.getZeroBased());
        Person editedPerson = createEditedPerson(personToEdit, editPersonDescriptor);

        if (!personToEdit.isSamePerson(editedPerson) && model.hasPerson(editedPerson)) {
            throw new CommandException(MESSAGE_DUPLICATE_PERSON);
        }

        model.updatePerson(personToEdit, editedPerson);

        model.commitAddressBook();
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        model.getTextPrediction().editPerson(personToEdit, editedPerson);
        return new CommandResult(String.format(MESSAGE_EDIT_PERSON_SUCCESS, editedPerson));
    }


    /**
     * Creates and returns a {@code Person} with the details of {@code personToEdit}
     * edited with {@code editPersonDescriptor}.
     */
    private static Person createEditedPerson(Person personToEdit, EditPersonDescriptor editPersonDescriptor) {
        assert personToEdit != null;

        Name updatedName = editPersonDescriptor.getName().orElse(personToEdit.getName());
        Phone updatedPhone = editPersonDescriptor.getPhone().orElse(personToEdit.getPhone());
        Email updatedEmail = editPersonDescriptor.getEmail().orElse(personToEdit.getEmail());
        Address updatedAddress = editPersonDescriptor.getAddress().orElse(personToEdit.getAddress());
        Position updatedPosition = editPersonDescriptor.getPosition().orElse(personToEdit.getPosition());
        Kpi updatedKpi = editPersonDescriptor.getKpi().orElse(personToEdit.getKpi());
        Note updatedNote = editPersonDescriptor.getNote().orElse(personToEdit.getNote());
        Set<Tag> updatedTags = editPersonDescriptor.getTags().orElse(personToEdit.getTags());

        if (editPersonDescriptor.removePosition) {
            updatedPosition = null;
        }
        if (editPersonDescriptor.removeKpi) {
            updatedKpi = null;
        }
        if (editPersonDescriptor.removeNote) {
            updatedNote = null;
        }
        return new Person(updatedName, updatedPhone, updatedEmail, updatedAddress,
                updatedPosition, updatedKpi, updatedNote, updatedTags);
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof EditCommand)) {
            return false;
        }

        // state check
        EditCommand e = (EditCommand) other;
        return index.equals(e.index)
                && editPersonDescriptor.equals(e.editPersonDescriptor);
    }

    /**
     * Stores the details to edit the person with. Each non-empty field value will replace the
     * corresponding field value of the person.
     */
    public static class EditPersonDescriptor {
        private Name name;
        private Phone phone;
        private Email email;
        private Address address;
        private Position position;
        private Kpi kpi;
        private Note note;
        private Set<Tag> tags;
        private boolean removePosition;
        private boolean removeKpi;
        private boolean removeNote;

        //@@author LowGinWee
        public EditPersonDescriptor() {
            removePosition = false;
            removeKpi = false;
            removeNote = false;
        }
        //@@author

        /**
         * Copy constructor.
         * A defensive copy of {@code tags} is used internally.
         */
        public EditPersonDescriptor(EditPersonDescriptor toCopy) {
            setName(toCopy.name);
            setPhone(toCopy.phone);
            setEmail(toCopy.email);
            setAddress(toCopy.address);
            setPosition(toCopy.position);
            setKpi(toCopy.kpi);
            setNote(toCopy.note);
            setTags(toCopy.tags);
            removeKpi = toCopy.removeKpi;
            removePosition = toCopy.removePosition;
            removeNote = toCopy.removeNote;
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            if (removeKpi || removePosition || removeNote) {
                return true;
            }
            return CollectionUtil.isAnyNonNull(name, phone, email, address,
                    position, kpi, note, tags);
        }

        public void setName(Name name) {
            this.name = name;
        }

        public Optional<Name> getName() {
            return Optional.ofNullable(name);
        }

        public void setPhone(Phone phone) {
            this.phone = phone;

        }

        public Optional<Phone> getPhone() {
            return Optional.ofNullable(phone);
        }

        public void setEmail(Email email) {
            this.email = email;
        }

        public Optional<Email> getEmail() {
            return Optional.ofNullable(email);
        }

        public void setAddress(Address address) {
            this.address = address;
        }

        public Optional<Address> getAddress() {
            return Optional.ofNullable(address);
        }

        //@@author LowGinWee
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

        public void setRemoveKpi() {
            removeKpi = true;
        }

        public void setRemoveNote() {
            removeNote = true;
        }
        //@@author

        /**
         * Sets {@code tags} to this object's {@code tags}.
         * A defensive copy of {@code tags} is used internally.
         */
        public void setTags(Set<Tag> tags) {
            this.tags = (tags != null) ? new HashSet<>(tags) : null;
        }

        /**
         * Returns an unmodifiable tag set, which throws {@code UnsupportedOperationException}
         * if modification is attempted.
         * Returns {@code Optional#empty()} if {@code tags} is null.
         */
        public Optional<Set<Tag>> getTags() {
            return (tags != null) ? Optional.of(Collections.unmodifiableSet(tags)) : Optional.empty();
        }

        @Override
        public boolean equals(Object other) {
            // short circuit if same object
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof EditPersonDescriptor)) {
                return false;
            }

            // state check
            EditPersonDescriptor e = (EditPersonDescriptor) other;

            return getName().equals(e.getName())
                    && getPhone().equals(e.getPhone())
                    && getEmail().equals(e.getEmail())
                    && getAddress().equals(e.getAddress())
                    && getPosition().equals(e.getPosition())
                    && getKpi().equals(e.getKpi())
                    && getNote().equals(e.getNote())
                    && getTags().equals(e.getTags());
        }
    }
}
