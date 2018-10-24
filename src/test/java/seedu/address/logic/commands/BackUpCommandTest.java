//@@author Limminghong
package seedu.address.logic.commands;

import static junit.framework.TestCase.assertEquals;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Logger;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;

class BackUpCommandTest {
    private static final Logger logger = Logger.getLogger(BackUpCommand.class.getName());

    private Model model = new ModelManager();
    private CommandHistory commandHistory = new CommandHistory();
    private String fileName;
    private File tmpSource;
    private File tmpDest;
    private String toWrite = "Hello World!";
    private Boolean checkStub = false;

    /**
     * Create stubs
     * @throws IOException if path to file does not exist
     */
    @BeforeEach
    public void setUp() throws IOException {
        UserPrefs userPrefs = new UserPrefs();
        tmpSource = new File(userPrefs.getAddressBookFilePath().toString());
        if (!tmpSource.exists()) {
            tmpSource.createNewFile();
            FileWriter writer = new FileWriter(tmpSource);
            writer.write(toWrite);
            writer.close();
            checkStub = true;
        }
    }

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
    @AfterEach
    public void tearDown() {
        tmpDest = new File(fileName);
        tmpDest.delete();
        if (checkStub) {
            tmpSource.delete();
        }
    }
}
