package seedu.address.ui;

import java.util.ArrayList;
import java.util.Date;
import java.util.TreeMap;
import java.util.logging.Logger;

import com.google.common.eventbus.Subscribe;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Region;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.model.AddressBookChangedEvent;
import seedu.address.model.schedule.Activity;



/**
 * The Schedule Panel of the App.
 */
public class SchedulePanel extends UiPart<Region> {

    private static final String FXML = "SchedulePanel.fxml";

    private final Logger logger = LogsCenter.getLogger(getClass());

    @FXML
    private TextArea textField;

    public SchedulePanel(TreeMap<Date, ArrayList<Activity>> schedule) {
        super(FXML);
        loadSchedule(schedule);
        registerAsAnEventHandler(this);
    }

    /**
     * Loads the schedule of the address book.
     */
    public void loadSchedule(TreeMap<Date, ArrayList<Activity>> schedule) {
        int count = 1;
        String scheduleText = new String();
        for (Date date : schedule.keySet()) {
            scheduleText += Activity.getDateString(date) + ":\n";
            for (Activity activity : schedule.get(date)) {
                scheduleText += count + ". " + activity.getActivityName() + "\n";
                count++;
            }
        }
        textField.setText(scheduleText);
    }

    @Subscribe
    public void handleAddressBookChangedEvent(AddressBookChangedEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event, "updating schedule"));

        loadSchedule(event.data.getSchedule());

    }

}
