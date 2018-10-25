package seedu.address.storage;

import java.util.StringTokenizer;
import javax.xml.bind.annotation.XmlValue;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.tag.Tag;

/**
 * JAXB-friendly adapted version of the Tag.
 */
public class XmlAdaptedTag {

    //TODO convert this to Xml element to contain both value and proirity
    @XmlValue
    private String tagName;

    /**
     * Constructs an XmlAdaptedTag.
     * This is the no-arg constructor that is required by JAXB.
     */
    public XmlAdaptedTag() {}

    /**
     * Constructs a {@code XmlAdaptedTag} with the given {@code tagName}.
     */
    public XmlAdaptedTag(String tagName) {
        this.tagName = tagName;
    }

    /**
     * Converts a given Tag into this class for JAXB use.
     *
     * @param source future changes to this will not affect the created
     */
    public XmlAdaptedTag(Tag source) {
        tagName = source.tagName;
        if (source.priority.getZeroBased() != Tag.PRIORITY_LOW) {
            tagName += " " + source.priority.getZeroBased();
        }
    }

    /**
     * Converts this jaxb-friendly adapted tag object into the model's Tag object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted person
     */
    public Tag toModelType() throws IllegalValueException {
        if (!Tag.isValidTagName(tagName)) {
            throw new IllegalValueException(Tag.MESSAGE_TAG_CONSTRAINTS);
        }

        StringTokenizer st = new StringTokenizer(tagName);
        tagName = st.nextToken();
        if (st.hasMoreTokens()) {
            Index priority = Index.fromZeroBased(Integer.parseInt(st.nextToken()));
            return new Tag(tagName, priority);
        }
        return new Tag(tagName);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof XmlAdaptedTag)) {
            return false;
        }

        return tagName.equals(((XmlAdaptedTag) other).tagName);
    }
}
