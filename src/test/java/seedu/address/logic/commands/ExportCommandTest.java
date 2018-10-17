//@@author Limminghong
package seedu.address.logic.commands;

import static junit.framework.TestCase.assertEquals;

import java.io.File;
import java.util.logging.Logger;

import org.junit.jupiter.api.Test;

import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.ExportCommandParser;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;

class ExportCommandTest {
    private static final Logger logger = Logger.getLogger(ExportCommand.class.getName());

    private Model model = new ModelManager();
    private CommandHistory commandHistory = new CommandHistory();

    @Test
    public void execute_success() {
        String directory = ExportCommandParser.DEFAULT_DIRECTORY.trim();
        String fileName = ExportCommandParser.DEFAULT_FILE_NAME + ".xml";
        fileName = fileName.trim();
        File file = new File(directory + "/" + fileName);
        try {
            CommandResult result = new ExportCommand(directory, fileName, file).execute(model, commandHistory);
            assertEquals(ExportCommand.MESSAGE_SUCCESS, result.feedbackToUser);
        } catch (CommandException ce) {
            logger.severe(ce.getMessage());
        }
    }
}
