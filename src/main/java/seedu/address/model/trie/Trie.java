//@@author lekoook
package seedu.address.model.trie;

import java.util.ArrayList;

/**
 * A bi-directional Tree structure that stems from the root node
 * Each node is a TrieNode object that contains the node information
 */

public class Trie {

    /**
     * Class constants
     */
    private static final char CHAR_ROOT = '.';
    private static final char CHAR_SPACE = ' ';

    /**
     * Class variables
     */
    private TrieNode root;
    private ArrayList<String> baseList;
    private ArrayList<String> predictionsList;

    /**
     * Default constructor
     */
    public Trie(ArrayList<String> inputList) {
        root = new TrieNode(CHAR_ROOT);
        baseList = new ArrayList<>(inputList);
        init();
    }

    /**
     * Initialises the Trie instance with the items in baseList
     */
    private void init() {

        for (int i = 0; i < baseList.size(); i++) {
            this.insertToGraph(baseList.get(i));
        }
    }

    /**
     * Inserts the input string value to the class instance
     * @param value
     */
    public void insert(String value) {
        // Check if this value exists
        if (!baseList.contains(value)) {
            insertToGraph(value);
        }
        baseList.add(value);
    }

    /**
     * Inserts the given string value to the Trie graph.
     * @param keyString the string value to be inserted
     */
    private void insertToGraph(String keyString) {
        // A TrieNode as pointer to traverse through the tree
        TrieNode pointer = root;

        // Run through all characters in the given key string
        for (int i = 0; i < keyString.length(); i++) {
            char ch = keyString.charAt(i);
            ArrayList<TrieNode> children = pointer.getChildren();

            if (children.contains(new TrieNode(ch))) {
                // Set the pointer to that node
                pointer = children.get(children.indexOf(new TrieNode(ch)));
            } else {
                // Create a new node
                TrieNode parent = pointer;
                pointer = pointer.appendChild(new TrieNode(ch));
                pointer.setParent(parent);
            }
        }
        // Mark end of word node
        pointer.setEndNode(true);
    }

    /**
     * Removes all string entries in the instance
     */
    public void clear() {
        root = new TrieNode(CHAR_ROOT);
        baseList = new ArrayList<>();
    }

    /**
     * Removes the input string value from the class instance
     * @param value the value to remove
     */
    public void remove(String value) {
        baseList.remove(value);
        // Check if this value exists
        if (!baseList.contains(value)) {
            removeFromGraph(value);
        }
    }
    /**
     * Removes the input string value from the actual graph implementation
     * Traverse from the root to the last character (End node) of the string
     * Prerequisites: the value must exist in the Trie
     *
     * If the remove of this node does not affect any chidren node
     * (ie the node has no children), remove the whole word
     *
     * @param value the value to remove
     */
    private void removeFromGraph(String value) {
        TrieNode pointer;

        pointer = traverseToEndNode(value);

        // Check for the conditions for removal or setting of end node
        if (pointer.getChildrenSize() == 0) {
            removeWordFromGraph(pointer);
        } else {
            pointer.setEndNode(false);
        }
    }

    /**
     * Traverses the Trie from the start character to the end character of the given string value.
     * @param value the given value
     * @return the TrieNode referencing the end node
     */
    private TrieNode traverseToEndNode(String value) {
        TrieNode pointer = root;

        // Run through all characters in the string and reaches the end node
        for (int i = 0; i < value.length(); i++) {
            char ch = value.charAt(i);
            ArrayList<TrieNode> children = pointer.getChildren();
            pointer = children.get(children.indexOf(new TrieNode(ch)));
        }

        return pointer;
    }

    /**
     * Removes a word from the graph given the end node of that word.
     * Removes every parent level node until a node that has more than one child
     * or it is an end node.
     * @param pointer the first node in the graph to be removed
     */
    private void removeWordFromGraph(TrieNode pointer) {
        // Set this node as non-end node first.
        pointer.setEndNode(false);

        // Traverse upwards the trie.
        while (!pointer.isEndNode() && pointer.getChildrenSize() == 0 && !pointer.equals(root)) {
            TrieNode parent = pointer.getParent();
            parent.removeChild(pointer);
            pointer = parent;
        }
    }

    /**
     * Returns a list of strings that are predicted to complete the {@code prefix}.
     * @param prefix string prefix to be predicted
     * @return a list of string predictions
     */
    public ArrayList<String> getPredictList(String prefix) {
        predictionsList = new ArrayList<>();
        StringBuilder charStack = new StringBuilder();

        // skipToStartNode returns root node if the prefix does not exist in the Trie.
        TrieNode startNode = skipToStartNode(root, prefix);

        if (startNode.equals(root)) {
            return predictionsList;
        }

        // If startNode has ONE child OR LESS, the only possible text prediction is one that has characters
        // up till the character that has more than one child or this is the end of the branch.
        if (startNode.getChildrenSize() <= 1) {
            charStack = buildSingleStack(startNode);
            predictionsList.add(charStack.toString());
            return predictionsList;
        }

        // If startNode has more than one child, explore all possible strings.
        for (int i = 0; i < startNode.getChildren().size(); i++) {
            explore(charStack, startNode.getChildren().get(i));
        }

        return predictionsList;
    }

    /**
     * Returns a string that represents the only possible substring from a starting node.
     *
     * Given a starting node, traverse to the next neighbour node until there is more than one neighbouring
     * node or there is none. If each node only has one neighbour, this means there are no other possible
     * string combinations.
     * @param startNode the starting node
     * @return the only possible substring
     */
    private StringBuilder buildSingleStack(TrieNode startNode) {
        StringBuilder charStack = new StringBuilder();
        while (startNode.getChildrenSize() == 1 && !startNode.isEndNode()) {
            charStack.append(startNode.getFirstChild().getValue());
            startNode = startNode.getFirstChild();
        }

        // End of a branch
        if (startNode.getChildrenSize() == 0) {
            charStack.append(CHAR_SPACE);
        }

        return charStack;
    }

    /**
     * Returns a {@code TrieNode} object that corresponds to the last character of a given string.
     *
     * From a starting node, the method traverse through all nodes that corresponds to every character
     * in the given string up until the last character.
     * @param start the starting node to traverse from
     * @param prefix the given string to traverse with
     * @return a TrieNode instance that holds a value equal to the last character of the {@code prefix}
     */
    private TrieNode skipToStartNode(TrieNode start, String prefix) {
        TrieNode current = start;

        for (int i = 0; i < prefix.length(); i++) {
            char ch = prefix.charAt(i);
            ArrayList<TrieNode> currentNodeChildren = current.getChildren();

            if (currentNodeChildren.contains(new TrieNode(ch))) {
                // If the node representing current character exists, move to that node
                current = currentNodeChildren.get(currentNodeChildren.indexOf(new TrieNode(ch)));
            } else {
                // If a character does not exist, just return the root node
                return root;
            }
        }
        return current;
    }

    /**
     * Traverses through the whole Trie structure to find all possible strings
     * @param charStack StringBuilder to build a possible strings
     * @param pointer the starting node to traverse from
     */
    private void explore(StringBuilder charStack, TrieNode pointer) {
        if (!pointer.equals(root)) {
            charStack.append(pointer.getValue());
        }

        // End character of the word
        if (pointer.isEndNode()) {
            // Branch end, so it is the last character of a word
            if (pointer.getChildrenSize() == 0) {
                charStack.append(CHAR_SPACE);
            }
            predictionsList.add(charStack.toString());
        }

        // Explore all neighbour nodes
        for (int i = 0; i < pointer.getChildren().size(); i++) {
            TrieNode neighbour = pointer.getChildren().get(i);
            explore(charStack, neighbour);
        }

        // Delete the whitespace that is appended after the end node of a branch
        if (pointer.isEndNode() && pointer.getChildrenSize() == 0) {
            charStack.deleteCharAt(charStack.length() - 1);
        }

        if (charStack.length() > 0) {
            charStack.deleteCharAt(charStack.length() - 1);
        }
    }

}
