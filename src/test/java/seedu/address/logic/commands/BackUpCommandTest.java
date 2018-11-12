//@@author Limminghong
package seedu.address.logic.commands;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.logging.Logger;

import org.junit.After;
import org.junit.Test;

import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;

public class BackUpCommandTest {
    private static final Logger logger = Logger.getLogger(BackUpCommand.class.getName());

    private Model model = new ModelManager();
    private CommandHistory commandHistory = new CommandHistory();
    private String fileName;
    private File tmpDest;

    /**
     * Test for execute in BackupCommand
     */
    @Test
    public void execute_success() {
        BackUpCommand backUpCommand = new BackUpCommand();
        try {
            CommandResult result = backUpCommand.execute(model, commandHistory);
            fileName = backUpCommand.getFileName();
            assertEquals(BackUpCommand.MESSAGE_SUCCESS, result.feedbackToUser);
        } catch (CommandException ce) {
            logger.severe(ce.getMessage());
        }

    }

    /**
     * Cleanup stubs created in setup
     */
    @After
    public void tearDown() {
        tmpDest = new File(fileName);
        tmpDest.delete();
    }
}
