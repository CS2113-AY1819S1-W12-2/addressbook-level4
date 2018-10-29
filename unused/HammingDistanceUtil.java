//@@author lws803-unused

// Deprecated because of new distance technique - Levenshtein distance
/**
 * Hamming distance to find word similarity
 */
public class HammingDistanceUtil {
    private String left, right;

    public HammingDistanceUtil (String left, String right) {
        this.left = left;
        this.right = right;
    }

    /**
     * Find hamming distance of two words
     */
    public int getDistance () {
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
