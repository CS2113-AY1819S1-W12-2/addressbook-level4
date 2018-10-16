package seedu.address.model.person;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import seedu.address.model.tag.Tag;

/**
 * Represents a Person in the address book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Person {

    // Identity fields
    private final Name name;
    private final Phone phone;
    private final Email email;

    // Data fields
    private final Address address;
    private final Position position;
    private final Kpi kpi;
    private final Note note;
    private final Set<Tag> tags = new HashSet<>();

    /**
     * Every field must be present and not null.
     */
    public Person(Name name, Phone phone, Email email, Address address, Position position, Kpi kpi,
                  Note note, Set<Tag> tags) {
        //TODO check if position and Kpi is non null in their respective object class
        requireAllNonNull(name, phone, email, address, tags);
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.position = position;
        this.kpi = kpi;
        this.note = note;
        this.tags.addAll(tags);
    }

    public Name getName() {
        return name;
    }

    public Phone getPhone() {
        return phone;
    }

    public Email getEmail() {
        return email;
    }

    public Address getAddress() {
        return address;
    }

    //@@author LowGinWee
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
        return note;
    }
    //@@author

    /**
     * Returns an immutable tag set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Tag> getTags() {
        return Collections.unmodifiableSet(tags);
    }

    /**
     * Returns true if both persons of the same name have at least one other identity field that is the same.
     * This defines a weaker notion of equality between two persons.
     */
    //TODO check for dups
    public boolean isSamePerson(Person otherPerson) {
        if (otherPerson == this) {
            return true;
        }

        return otherPerson != null
                && otherPerson.getName().equals(getName())
                && (otherPerson.getPhone().equals(getPhone()) || otherPerson.getEmail().equals(getEmail()));
    }

    /**
     * Returns true if both persons have the same identity and data fields.
     * This defines a stronger notion of equality between two persons.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Person)) {
            return false;
        }

        Person otherPerson = (Person) other;
        return otherPerson.getName().equals(getName())
                && otherPerson.getPhone().equals(getPhone())
                && otherPerson.getEmail().equals(getEmail())
                && otherPerson.getAddress().equals(getAddress())
                && otherPerson.getPosition().equals(getPosition())
                && otherPerson.getKpi().equals(getKpi())
                && otherPerson.getNote().equals(getNote())
                && otherPerson.getTags().equals(getTags());
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(name, phone, email, address, position, kpi, note, tags);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append(getName())
                .append(" Phone: ")
                .append(getPhone())
                .append(" Email: ")
                .append(getEmail())
                .append(" Address: ")
                .append(getAddress());
        if (positionDoesExist()) {
            builder.append(" Position: ").append(getPosition());
        }

        if (kpiDoesExist()) {
            builder.append(" KPI: ").append(getKpi());
        }
        if (noteDoesExist()) {
            builder.append(" Note: ").append(getNote());
        }
        builder.append(" Tags: ");
        getTags().forEach(builder::append);
        return builder.toString();
    }

    /**
     * Returns true if the person has a position
     */
    public boolean positionDoesExist() {
        if (position == null || !position.doesExist()) {
            return false;
        }
        return true;
    }

    /**
     * Returns true if the person has a KPI score
     */
    public boolean kpiDoesExist() {
        if (kpi == null || !kpi.doesExist()) {
            return false;
        }
        return true;
    }

    /**
     * Returns true if the person has a KPI score
     */
    public boolean noteDoesExist() {
        if (note == null || !note.doesExist()) {
            return false;
        }
        return true;
    }

}
