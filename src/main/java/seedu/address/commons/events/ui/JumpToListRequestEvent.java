package seedu.address.commons.events.ui;

import java.util.ArrayList;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.events.BaseEvent;

/**
 * Indicates a request to jump to the list of persons
 */
public class JumpToListRequestEvent extends BaseEvent {

    public final ArrayList<Integer> targetIndex;

    public JumpToListRequestEvent(ArrayList<Index> targetIndex) {
        this.targetIndex = new ArrayList<>(extractIndexAsIntegers(targetIndex));
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }

    /**
     * Extracts out an array list of {@code Integer} from an array list of {@code Index}.
     * @param indexArrayList array list of {@code Index} to extract from.
     * @return extracted array list of {@code Integer}.
     */
    private ArrayList<Integer> extractIndexAsIntegers(ArrayList<Index> indexArrayList) {
        ArrayList<Integer> output = new ArrayList<>();
        for (Index index : indexArrayList) {
            output.add(index.getZeroBased());
        }
        return output;
    }
}
