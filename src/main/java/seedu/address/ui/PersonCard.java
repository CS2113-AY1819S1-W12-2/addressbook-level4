package seedu.address.ui;

import java.util.ArrayList;
import java.util.List;

import javafx.fxml.FXML;
import javafx.geometry.Orientation;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import seedu.address.model.person.Person;



/**
 * An UI component that displays information of a {@code Person}.
 */
public class PersonCard extends UiPart<Region> {

    private static final String FXML = "PersonListCard.fxml";
    private static final int LABEL_HEIGHT = 18;

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/addressbook-level4/issues/336">The issue on AddressBook level 4</a>
     */

    public final Person person;

    @FXML
    private HBox cardPane;
    @FXML
    private Label name;
    @FXML
    private Label id;
    @FXML
    private Label phone;
    @FXML
    private Label address;
    @FXML
    private Label email;
    @FXML
    private FlowPane information;
    @FXML
    private FlowPane tags;

    public PersonCard(Person person, int displayedIndex) {
        super(FXML);
        this.person = person;
        id.setText(displayedIndex + ". ");
        name.setText(person.getName().fullName);
        phone.setText(person.getPhone().value);
        address.setText(person.getAddress().value);
        email.setText(person.getEmail().value);
        //@@author LowGinWee
        List<Label> highPriorityTags = new ArrayList<>();
        List<Label> mediumPriorityTags = new ArrayList<>();;
        List<Label> lowPriorityTags = new ArrayList<>();;
        person.getTags().forEach(tag -> {
                Label newLabel = new Label(tag.tagName);
                if (tag.priority == tag.PRIORITY_HIGH) {
                    newLabel.setStyle("-fx-border-color:red; -fx-background-color: red;");
                    highPriorityTags.add(newLabel);
                }
                if (tag.priority == tag.PRIORITY_MEDIUM) {
                    newLabel.setStyle("-fx-text-fill:Black; -fx-border-color:yellow; -fx-background-color: yellow;");
                    mediumPriorityTags.add(newLabel);
                }
                if (tag.priority == tag.PRIORITY_LOW) {
                    newLabel.setStyle("-fx-border-color:green; -fx-background-color: green;");
                    lowPriorityTags.add(newLabel);
                }
            }
        );
        for (Label label : highPriorityTags) {
            tags.getChildren().add(label);
        }
        for (Label label : mediumPriorityTags) {
            tags.getChildren().add(label);
        }
        for (Label label : lowPriorityTags) {
            tags.getChildren().add(label);
        }
        //TODO find a btr way to find height
        information.setOrientation(Orientation.VERTICAL);
        int height = 0;
        if (person.noteDoesExist()) {
            information.getChildren().add(new Label("Note: " + person.getNote().value));
            height += LABEL_HEIGHT;
        }
        if (person.positionDoesExist()) {
            information.getChildren().add(new Label("Position: " + person.getPosition().value));
            height += LABEL_HEIGHT;
        }
        if (person.kpiDoesExist()) {
            information.getChildren().add(new Label("KPI: " + person.getKpi().value));
            height += LABEL_HEIGHT;
        }
        information.setMaxHeight(height);
        //@@author
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof PersonCard)) {
            return false;
        }

        // state check
        PersonCard card = (PersonCard) other;
        return id.getText().equals(card.id.getText())
                && person.equals(card.person);
    }
}
