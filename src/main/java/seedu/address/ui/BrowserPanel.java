package seedu.address.ui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.google.common.eventbus.Subscribe;

import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import seedu.address.MainApp;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.ui.PersonPanelSelectionChangedEvent;
import seedu.address.model.person.Person;
import seedu.address.model.tag.Tag;

/**
 * The Browser Panel of the App.
 */
public class BrowserPanel extends UiPart<Region> {
    public static final String DEFAULT_PAGE = "default.html";
    public static final String SEARCH_PAGE_URL =
            "https://se-edu.github.io/addressbook-level4/DummySearchPage.html?name=";

    private static final String FXML = "BrowserPanel.fxml";

    private final Logger logger = LogsCenter.getLogger(getClass());

    @FXML
    private WebView browser;
    @FXML
    private Label nameLabel;
    @FXML
    private Label addressLabel;
    @FXML
    private Label phoneLabel;
    @FXML
    private Label emailLabel;
    @FXML
    private Label kpiLabel;
    @FXML
    private Label noteLabel;
    @FXML
    private FlowPane tagPanel;
    @FXML
    private VBox welcomePane;

    public BrowserPanel() {
        super(FXML);

        // To prevent triggering events for typing inside the loaded Web page.
        getRoot().setOnKeyPressed(Event::consume);

        loadDefaultPage();
        registerAsAnEventHandler(this);
    }

    /**
     * updates panel based on selected person.
     */
    private void loadPersonPage(Person person) {
        loadPage(SEARCH_PAGE_URL + person.getName().fullName);
        welcomePane.setOpacity(0);
        tagPanel.getChildren().clear();
        String nameText = person.getName().toString();
        if (person.positionDoesExist()) {
            nameText += "\n" + person.getPosition().toString();
        }
        nameLabel.setText(nameText);
        addressLabel.setText(person.getAddress().toString());
        phoneLabel.setText(person.getPhone().toString());
        emailLabel.setText(person.getEmail().toString());
        if (person.kpiDoesExist()) {
            kpiLabel.setText("KPI: " + person.getKpi().toString());
        } else {
            kpiLabel.setText("KPI: ");
        }
        if (person.noteDoesExist()) {
            noteLabel.setText(person.getNote().toString());
        } else {
            noteLabel.setText("");
        }
        List<Label> highPriorityTags = new ArrayList<>();
        List<Label> mediumPriorityTags = new ArrayList<>();
        List<Label> lowPriorityTags = new ArrayList<>();
        person.getTags().forEach(tag -> {
            Label newLabel = new Label(tag.tagName);
            if (tag.priority.getZeroBased() == Tag.PRIORITY_HIGH) {
                newLabel.setStyle("-fx-text-fill:Black; -fx-border-color:red; -fx-background-color: red; "
                        + "-fx-border-radius: 5 5 5 5;"
                        + "-fx-background-radius: 5 5 5 5; ");
                newLabel.setPadding(new Insets(0, 5, 0, 5));
                highPriorityTags.add(newLabel);
            }
            if (tag.priority.getZeroBased() == Tag.PRIORITY_MEDIUM) {
                newLabel.setStyle("-fx-text-fill:Black; -fx-border-color:yellow; -fx-background-color: yellow; "
                        + "-fx-border-radius: 5 5 5 5;"
                        + "-fx-background-radius: 5 5 5 5;");
                newLabel.setPadding(new Insets(0, 5, 0, 5));
                mediumPriorityTags.add(newLabel);
            }
            if (tag.priority.getZeroBased() == Tag.PRIORITY_LOW) {
                newLabel.setStyle("-fx-text-fill:Black; -fx-border-color:green; -fx-background-color: green;"
                        + "-fx-border-radius: 5 5 5 5;"
                        + "-fx-background-radius: 5 5 5 5;");
                newLabel.setPadding(new Insets(0, 5, 0, 5));
                lowPriorityTags.add(newLabel);
            }
        }
        );
        for (Label label : highPriorityTags) {
            tagPanel.getChildren().add(label);
        }
        for (Label label : mediumPriorityTags) {
            tagPanel.getChildren().add(label);
        }
        for (Label label : lowPriorityTags) {
            tagPanel.getChildren().add(label);
        }
    }

    public void loadPage(String url) {
        Platform.runLater(() -> browser.getEngine().load(url));
    }
    /**
     * Frees resources allocated to the browser.
     */
    public void freeResources() {
        browser = null;
    }

    /**
     * Loads a default HTML file with a background that matches the general theme.
     */
    private void loadDefaultPage() {
        URL defaultPage = MainApp.class.getResource(FXML_FILE_FOLDER + DEFAULT_PAGE);
        loadPage(defaultPage.toExternalForm());
    }

    @Subscribe
    private void handlePersonPanelSelectionChangedEvent(PersonPanelSelectionChangedEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        loadPersonPage(event.getNewSelection());
    }
}
