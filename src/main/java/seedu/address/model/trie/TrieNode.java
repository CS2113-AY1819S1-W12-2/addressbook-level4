//@@author lekoook
package seedu.address.model.trie;

import java.util.ArrayList;

/**
 * A TrieNode instance represents a node in the Trie structure
 */
public class TrieNode {

    private boolean isEndNode;
    private char value;
    private TrieNode parent;
    private ArrayList<TrieNode> children;

    /**
     * Default constructor with no parameters
     * @param value
     */
    public TrieNode(char value) {
        // Initialise all instance variables
        isEndNode = false;
        parent = null;
        children = new ArrayList<>();
        this.value = value;
    }

    public TrieNode getParent() {
        return this.parent;
    }

    public void setParent(TrieNode parent) {
        this.parent = parent;
    }

    public char getValue() {
        return this.value;
    }

    public void setValue(char value) {
        this.value = value;
    }

    public ArrayList<TrieNode> getChildren() {
        return children;
    }

    public int getChildrenSize() {
        return children.size();
    }

    /**
     * Adds a child to the {@code TrieNode} instance.
     * @param child child node to be added
     * @return the newly added child
     */
    public TrieNode appendChild(TrieNode child) {
        children.add(child);
        children.get(children.size() - 1).setParent(this);
        return children.get(children.size() - 1);
    }

    public void removeChild(TrieNode child) {
        children.remove(child);
    }

    public TrieNode getFirstChild() {
        return children.get(0);
    }

    public boolean isEndNode() {
        return isEndNode;
    }

    public void setEndNode(boolean value) {
        isEndNode = value;
    }

    @Override
    public boolean equals(Object obj) {
        TrieNode other = (TrieNode) obj;
        return this.value == other.value;
    }
}
