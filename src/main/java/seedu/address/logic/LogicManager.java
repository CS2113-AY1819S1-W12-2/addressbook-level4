package seedu.address.logic;

import java.util.ArrayList;
import java.util.Date;
import java.util.TreeMap;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import seedu.address.commons.core.ComponentManager;
import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.AddressBookParser;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.schedule.Activity;

/**
 * The main LogicManager of the app.
 */
public class LogicManager extends ComponentManager implements Logic {
    private final Logger logger = LogsCenter.getLogger(LogicManager.class);

    private final Model model;
    private final CommandHistory history;
    private final AddressBookParser addressBookParser;

    public LogicManager(Model model) {
        this.model = model;
        history = new CommandHistory();
        addressBookParser = new AddressBookParser();
    }

    @Override
    public CommandResult execute(String commandText) throws CommandException, ParseException {
        boolean isPasswordSenstive = isSensitive(commandText);

        if (!isPasswordSenstive) {
            logger.info("----------------[USER COMMAND][" + commandText + "]");
        } else {
            logger.info("----------------[USER COMMAND][" + "password ********" + "]");
        }

        try {
            Command command = addressBookParser.parseCommand(commandText);
            return command.execute(model, history);
        } finally {
            if (!isPasswordSenstive) {
                history.add(commandText);
            } else {
                history.add("password *******");
            }
        }
    }

    @Override
    public ObservableList<Person> getFilteredPersonList() {
        return model.getFilteredPersonList();
    }

    @Override
    public ListElementPointer getHistorySnapshot() {
        return new ListElementPointer(history.getHistory());
    }

    //@@author LowGinWee
    @Override
    public TreeMap<Date, ArrayList<Activity>> getSchedule() {
        return model.getSchedule();
    }

    //@@author lekoook
    /**
     * Retrieves a list of possible predictions for a command box input
     * @param textInput text input from command box
     * @return a list of predictions
     */
    @Override
    public ArrayList<String> getCmdPrediction(String textInput) {
        return model.getTextPrediction().predictText(textInput);
    }

    /**
     * Check to see if commandText is a password command
     * @param commandText
     * @return true if commandText is password sensitive
     */
    private boolean isSensitive (String commandText) {
        String[] splited = commandText.split("\\s+");
        if (splited.length > 1) {
            if (splited[0].compareTo("password") == 0) {
                return true;
            }
        }
        return false;
    }

}
