//@@author lws803
package seedu.address.commons.util;


/**
 * Finds similarity of texts based on hamming distance
 */
public interface HammingDistanceUtil {

    /**
     * Find Hammign distance between two strings
     * @param left left string
     * @param right right string
     * @return returns the distance
     */
    static int getDistance (final String left, final String right) {
        int distance = 0;
        if (left.length() < right.length()) {
            for (int i = 0; i < left.length(); i++) {
                if (left.charAt(i) != right.charAt(i)) {
                    distance++;
                }
            }
        } else {
            for (int i = 0; i < right.length(); i++) {
                if (left.charAt(i) != right.charAt(i)) {
                    distance++;
                }
            }
        }
        return distance;
    }
}
