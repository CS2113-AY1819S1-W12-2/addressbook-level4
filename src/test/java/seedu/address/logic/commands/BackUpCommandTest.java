//@@author Limminghong
package seedu.address.logic.commands;

import static junit.framework.TestCase.assertEquals;

import java.io.File;
import java.util.logging.Logger;

import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.Test;

import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;

class BackUpCommandTest {
    private static final Logger logger = Logger.getLogger(BackUpCommand.class.getName());

    private Model model = new ModelManager();
    private CommandHistory commandHistory = new CommandHistory();
    private BackUpCommand backUpCommand;
    private String fileName;

    @Before
    public void setUp() {
        backUpCommand = new BackUpCommand();
        fileName = backUpCommand.getFileName();
    }

    @Test
    public void execute_success() {
        try {
            CommandResult result = backUpCommand.execute(model, commandHistory);
            assertEquals(BackUpCommand.MESSAGE_SUCCESS, result.feedbackToUser);
        } catch (CommandException ce) {
            logger.severe(ce.getMessage());
        }
    }

    @After
    public void cleanUp() {
        File tmpDest = new File(fileName);
        tmpDest.delete();
    }
}
