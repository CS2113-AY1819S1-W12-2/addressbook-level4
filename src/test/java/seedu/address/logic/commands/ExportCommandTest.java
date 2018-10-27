//@@author Limminghong
package seedu.address.logic.commands;

import static junit.framework.TestCase.assertEquals;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.io.File;
import java.util.logging.Logger;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.ExportCommandParser;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;

class ExportCommandTest {
    private static final Logger logger = Logger.getLogger(ExportCommand.class.getName());

    private Model model;
    private CommandHistory commandHistory = new CommandHistory();

    private String directory;
    private String fileName;
    private String fullDirectory;
    private File tmpFile;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        directory = ExportCommandParser.DEFAULT_DIRECTORY.trim();
        fileName = ExportCommandParser.DEFAULT_FILE_NAME + ".csv";
        fileName = fileName.trim();
        fullDirectory = directory + "\\" + fileName;
        tmpFile = new File(directory);
        if (!tmpFile.exists()) {
            tmpFile.mkdir();
        }
    }

    @Test
    public void execute_success() {
        try {
            CommandResult result = new ExportCommand(directory, fileName, fullDirectory).execute(model, commandHistory);
            assertEquals(String.format(ExportCommand.MESSAGE_SUCCESS, directory, fileName), result.feedbackToUser);
        } catch (CommandException ce) {
            logger.severe(ce.getMessage());
        }
    }

    @AfterEach
    public void tearDown() {
        File exportedFile = new File(fullDirectory);
        File exportedDirectory = new File(directory);
        exportedFile.delete();
        exportedDirectory.delete();
    }

}
