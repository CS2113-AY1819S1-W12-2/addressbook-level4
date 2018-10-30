//@author: peiying98

package seedu.address.model.person;

import java.util.Set;

import javafx.beans.property.ObjectProperty;
import seedu.address.model.tag.Tag;
import seedu.address.model.tag.UniqueTagList;

/**
 * A read-only immutable interface for a Person in the addressbook.
 * Implementations should guarantee: details are present and not null, field values are validated.
 */
public interface ReadPersonOnly {

    ObjectProperty<Name> nameProperty();
    Name getName();
    ObjectProperty<Phone> phoneProperty();
    Phone getPhone();
    ObjectProperty<Email> emailProperty();
    Email getEmail();
    ObjectProperty<Address> addressProperty();
    Address getAddress();
    ObjectProperty<Position> positionProperty();
    Kpi getPosition();
    ObjectProperty<Kpi> kpiProperty();
    Kpi getKpi();
    ObjectProperty<Note> noteProperty();
    Kpi getNote();
    ObjectProperty<UniqueTagList> tagProperty();
    Set<Tag> getTags();

    /**
     * Returns true if both have the same state. (interfaces cannot override .equals)
     */
    default boolean isSameStateAs(ReadPersonOnly other) {
        return other == this // short circuit if same object
                || (other != null // this is first to avoid NPE below
                && other.getName().equals(this.getName()) // state checks here onwards
                && other.getPhone().equals(this.getPhone())
                && other.getEmail().equals(this.getEmail())
                && other.getAddress().equals(this.getAddress())
                && other.getPosition().equals(this.getPosition())
                && other.getKpi().equals(this.getKpi())
                && other.getNote().equals(this.getNote())
                && other.getTags().equals(this.getTags())
        );
    }

    /**
     * Formats the person as text, showing all contact details.
     */
    default String getAsText() {
        final StringBuilder builder = new StringBuilder();
        builder.append(getName())
                .append(" Phone: ")
                .append(getPhone())
                .append(" Email: ")
                .append(getEmail())
                .append(" Address: ")
                .append(getAddress())
                .append(" Position: ")
                .append(getPosition())
                .append(" KPI: ")
                .append(getKpi())
                .append(" Notes: ")
                .append(getNote())
                .append(getTags())
                .append(" Tags: ");
        getTags().forEach(builder::append);
        return builder.toString();
    }

}
