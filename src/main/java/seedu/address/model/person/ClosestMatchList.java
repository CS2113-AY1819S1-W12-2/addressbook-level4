//@@author lws803
package seedu.address.model.person;

import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NOTE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_POSITION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javafx.collections.ObservableList;

import seedu.address.commons.util.LevenshteinDistanceUtil;
import seedu.address.logic.parser.Prefix;
import seedu.address.model.Model;

/**
 * To generate a list of closest matches
 * Description: We run thru all arguments given together will all the names
 * (First and last)
 * Then attach a Levensthein distance to each of them to form a pair
 * The pairs are then stored in a treemap which we will generate another list
 * from the first few
 */
public class ClosestMatchList {
    private int lowestDist = Integer.MAX_VALUE;
    private ObservableList<Person> listToFilter;
    private List<String> approvedNames = new ArrayList<String>();
    private Map<String, Integer> discoveredNames = new TreeMap<String, Integer>();

    /**
     * Pair of integer and string
     */
    private static class Pair {
        private int dist;
        private String nameSegment;

        private Pair(int a, String b) {
            this.dist = a;
            this.nameSegment = b;
        }

        private int getDist () {
            return this.dist;
        }

        private String getNameSegment () {
            return nameSegment;
        }
    }

    private Set <Pair> nameMap = new TreeSet<Pair>(new Comparator<Pair>() {
        @Override
        public int compare(Pair o1, Pair o2) {
            if (o1.getDist() - o2.getDist() == 0) {
                if (o1.getDist() == o2.getDist()) {
                    return 1;
                } else {
                    return o1.getNameSegment().compareTo(o2.getNameSegment());
                }
            }
            return o1.getDist() - o2.getDist();
        }
    });


    /**
     * Filters and generates maps from names from model
     * and arguments
     */
    public ClosestMatchList (Model model, Prefix type, String[] searchKeys) {
        this.listToFilter = model.getAddressBook().getPersonList();

        for (Person person: listToFilter) {
            generateNameMapFromAttrib(searchKeys, person, type);
        }

        addToApprovedNamesList();
    }


    /**
     * Bulk of the computation
     * Runs thru model and stores the pairs in a tree out of
     * similarity indexes using levensthein distances together with nameSegment
     */
    private void generateNameMapFromAttrib (String[] searchKey, Person person, Prefix myPrefix) {
        String compareString = person.getName().fullName;

        if (myPrefix == PREFIX_NAME) {
            compareString = person.getName().fullName;
        } else if (myPrefix == PREFIX_PHONE) {
            compareString = person.getPhone().value;
        } else if (myPrefix == PREFIX_EMAIL) {
            compareString = person.getEmail().value;
        } else if (myPrefix == PREFIX_ADDRESS) {
            compareString = person.getAddress().value;
        } else if (myPrefix == PREFIX_POSITION) {
            compareString = person.getPosition().value;
        } else if (myPrefix == PREFIX_NOTE) {
            compareString = person.getNote().value;
        } else if (myPrefix == PREFIX_TAG) {
            compareString = person.getStringTags();
        }

        generateNameMap(searchKey, compareString);
    }


    /**
     * Generate the namemap from the compareString provided
     * @param searchKey obtained from arguments in FindCommand
     * @param compareString obtained from personList as per attribute
     */
    // TODO: Add it such that when accurate names are typed out, the results wont show the rest.
    private void generateNameMap(String[] searchKey, String compareString) {
        String[] stringSplitted = compareString.split("\\s+");
        for (String nameSegment: stringSplitted) {

            for (String nameArg: searchKey) {
                int dist = LevenshteinDistanceUtil.levenshteinDistance(nameArg.toLowerCase(),
                        nameSegment.toLowerCase());

                if (dist < lowestDist) {
                    lowestDist = dist;
                }

                Pair distNamePair = new Pair(dist, nameSegment);

                if (!discoveredNames.containsKey(nameSegment)) {
                    nameMap.add(distNamePair);
                    discoveredNames.put(nameSegment, dist);
                } else if (discoveredNames.get(nameSegment) > dist) {
                    discoveredNames.replace(nameSegment, dist); // Replace with the new dist
                    nameMap.add(distNamePair); // Check to see if this will replace
                }
            }

        }
    }

    /**
     * Add the contents in the tree to a name list
     */
    private void addToApprovedNamesList() {
        for (Pair pair: nameMap) {
            if (pair.getDist() - lowestDist > 1) {
                // Break the loop when distances get too far
                return;
            }
            approvedNames.add(pair.getNameSegment());
        }
    }

    /**
     * Gets the approved list
     */
    public String[] getApprovedList () {
        return approvedNames.toArray(new String[0]);
    }
}
