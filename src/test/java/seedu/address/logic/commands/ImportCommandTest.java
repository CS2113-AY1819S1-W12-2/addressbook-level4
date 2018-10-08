//@@author Limminghong
package seedu.address.logic.commands;

import static junit.framework.TestCase.assertEquals;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;

import org.junit.jupiter.api.Test;

import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;

class ImportCommandTest {
    private static final Logger logger = Logger.getLogger(ImportCommand.class.getName());

    private Model model = new ModelManager();
    private CommandHistory commandHistory = new CommandHistory();


    @Test
    public void execute_success() {
        Path path = Paths.get("C:\\Users\\USER\\Desktop\\main\\build");
        try {
            CommandResult result = new ImportCommand(path).execute(model, commandHistory);
            assertEquals(ImportCommand.MESSAGE_SUCCESS, result.feedbackToUser);
        } catch (CommandException ce) {
            logger.severe(ce.getMessage());
        }
    }

    @Test
    public void execute_empty() {

        try {
            CommandResult result = new ImportCommand().execute(model, commandHistory);
            assertEquals(ImportCommand.MESSAGE_USAGE, result.feedbackToUser);
        } catch (CommandException ce) {
            logger.severe(ce.getMessage());
        }

    }
}
