//@@author Limminghong
package seedu.address.logic.commands;

import static junit.framework.TestCase.assertEquals;

import java.io.IOException;
import java.util.logging.Logger;

import org.junit.jupiter.api.Test;

import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.ParserUtil;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.backup.BackupList;

class RestoreSnapshotsCommandTest {
    private static final Logger logger = Logger.getLogger(RestoreCommand.class.getName());

    private Model model = new ModelManager();
    private CommandHistory commandHistory = new CommandHistory();

    @Test
    public void execute_snapshots_success() throws Exception {
        try {
            BackupList backupList = ParserUtil.parseBackup("snapshots");
            CommandResult result = new RestoreSnapshotsCommand(backupList).execute(model, commandHistory);
            assertEquals(RestoreSnapshotsCommand.getBackupNames(), result.feedbackToUser);
        } catch (CommandException ce) {
            logger.severe(ce.getMessage());
        } catch (IOException io) {
            logger.severe(io.getMessage());
        }
    }
}
