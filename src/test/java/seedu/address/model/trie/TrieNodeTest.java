//@@author lekoook
package seedu.address.model.trie;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Test;

public class TrieNodeTest {
    @Test
    public void constructor_validValues() {
        TrieNode node = new TrieNode('a');

        assertEquals(node.getValue(), 'a');
        assertFalse(node.isEndNode());
        assertNull(node.getParent());
        assertEquals(new ArrayList<>(), node.getChildren());
    }

    @Test
    public void setParent_validArgs_success() {
        TrieNode node = new TrieNode('b');
        TrieNode parent = new TrieNode('a');
        node.setParent(parent);
        assertEquals(parent, node.getParent());
    }

    @Test
    public void setValue_validValue_success() {
        TrieNode node = new TrieNode('a');
        char newValue = 'b';
        node.setValue(newValue);
        assertEquals(newValue, node.getValue());
    }

    @Test
    public void getChildrenSize_empty_success() {
        TrieNode node = new TrieNode('a');
        assertEquals(0, node.getChildrenSize());
    }

    @Test
    public void testChildren_validArgs_success() {
        TrieNode node = new TrieNode('a');
        TrieNode toAppend = new TrieNode('b');

        node.appendChild(toAppend);
        assertEquals(1, node.getChildrenSize());
        assertEquals(toAppend, node.getChildren().get(0));
        assertEquals(toAppend, node.getFirstChild());

        node.removeChild(toAppend);
        assertEquals(0, node.getChildrenSize());
    }

    @Test
    public void setEndNode_success() {
        TrieNode node = new TrieNode('a');
        node.setEndNode(true);
        assertTrue(node.isEndNode());
    }
}
