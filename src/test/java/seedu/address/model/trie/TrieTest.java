//@@author lekoook
package seedu.address.model.trie;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Test;

public class TrieTest {

    private ArrayList<String> testStrings = new ArrayList<>(Arrays.asList("hello", "hell", "helipad", "world", "help"));

    @Test
    public void constructor_validArgs_success() {
        Trie trie = new Trie(testStrings);
    }

    @Test
    public void getPredictList_validArgs_success() {
        Trie trieUnderTest = new Trie(testStrings);

        ArrayList<String> actualOutput = trieUnderTest.getPredictList("hel");
        ArrayList<String> expectedOutput = new ArrayList<>(Arrays.asList("l", "lo ", "ipad ", "p "));
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void getPredictList_singleOutput_success() {
        Trie trieUnderTest = new Trie(testStrings);

        ArrayList<String> actualOutput = trieUnderTest.getPredictList("wor");
        ArrayList<String> expectedOutput = new ArrayList<>(Arrays.asList("ld "));
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void insert_newValue_success() {
        Trie trieUnderTest = new Trie(testStrings);
        trieUnderTest.insert("helium");

        ArrayList<String> expectedOutput = new ArrayList<>(Arrays.asList("l", "lo ", "ipad ", "ium ", "p "));
        assertPredictSuccess("hel", trieUnderTest, expectedOutput);
    }

    @Test
    public void remove_existingValues_success() {
        Trie trieUnderTest = new Trie(testStrings);
        trieUnderTest.remove("help");
        trieUnderTest.remove("hell");

        ArrayList<String> expectedOutput = new ArrayList<>(Arrays.asList("lo ", "ipad "));
        assertPredictSuccess("hel", trieUnderTest, expectedOutput);
    }

    @Test
    public void insertAndRemove_multipleValues_success() {
        Trie trieUnderTest = new Trie(testStrings);

        ArrayList<String> actualBeforeInsertion = trieUnderTest.getPredictList("hel");
        ArrayList<String> expectedBeforeInsertion = new ArrayList<>(Arrays.asList("l", "lo ", "ipad ", "p "));
        assertEquals(expectedBeforeInsertion, actualBeforeInsertion);

        // Insert duplicate
        trieUnderTest.insert("hello");
        ArrayList<String> actualAfterInsertion = trieUnderTest.getPredictList("hel");
        ArrayList<String> expectedAfterInsertion = new ArrayList<>(Arrays.asList("l", "lo ", "ipad ", "p "));
        assertEquals(expectedAfterInsertion, actualAfterInsertion);

        // Remove the duplicate
        trieUnderTest.remove("hello");
        ArrayList<String> actualAfterRemoval = trieUnderTest.getPredictList("hel");
        ArrayList<String> expectedAfterRemoval = new ArrayList<>(Arrays.asList("l", "lo ", "ipad ", "p "));
        assertEquals(expectedAfterRemoval, actualAfterRemoval);
    }

    @Test
    public void clear_nonEmptyTrie_success() {
        Trie trieUnderTest = new Trie(testStrings);
        trieUnderTest.clear();

        ArrayList<String> expectedOutput = new ArrayList<>();
        assertPredictSuccess("hel", trieUnderTest, expectedOutput);
    }

    /**
     * Asserts that the return values of prediction from a Trie under test is same as expected values.
     *
     * @param prefix the prefix string used for prediction.
     * @param trieUnderTest the Trie getting tested.
     * @param expectedOutput the expected values from the prediction using the prefix string.
     */
    public static void assertPredictSuccess(String prefix, Trie trieUnderTest, ArrayList<String> expectedOutput) {
        ArrayList<String> actualOutput = trieUnderTest.getPredictList(prefix);
        assertEquals(expectedOutput, actualOutput);
    }
}
