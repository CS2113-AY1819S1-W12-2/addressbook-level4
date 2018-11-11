//@@author Limminghong
package seedu.address.logic.commands;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;

import org.junit.After;
import org.junit.Rule;
import org.junit.Test;

import org.junit.rules.ExpectedException;

import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.backup.BackupList;

public class RestoreSnapshotsCommandTest {
    private static final Logger logger = Logger.getLogger(RestoreCommand.class.getName());
    private static final String BACKUP_DIRECTORY = "src" + File.separator
            + "test" + File.separator
            + "data" + File.separator
            + "RestoreTestXml";
    private static final String FILE_NAME = "1540743391606.xml";

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private Model model = new ModelManager();
    private CommandHistory commandHistory = new CommandHistory();

    @Test
    public void execute_snapshots_failure() throws Exception {
        thrown.expect(ParseException.class);
        thrown.expectMessage(BackupList.MESSAGE_BACKUP_CONSTRAINTS);
        new RestoreSnapshotsCommand(BACKUP_DIRECTORY).execute(model, commandHistory);
    }

    @Test
    public void execute_snapshots_success() throws Exception {
        try {
            String fileString = BACKUP_DIRECTORY + File.separator + FILE_NAME;
            Path filePath = Paths.get(fileString);
            model.backUpAddressbook(filePath);

            CommandResult result = new RestoreSnapshotsCommand(BACKUP_DIRECTORY).execute(model, commandHistory);
            assertEquals(RestoreSnapshotsCommand.getBackupNames(), result.feedbackToUser);
        } catch (CommandException ce) {
            logger.severe(ce.getMessage());
        }
    }

    @After
    public void tearDown() {
        String fileString = BACKUP_DIRECTORY + File.separator + FILE_NAME;
        File deleteFile = new File(fileString);
        deleteFile.delete();
    }
}
