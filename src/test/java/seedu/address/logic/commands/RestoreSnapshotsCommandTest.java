//@@author Limminghong
package seedu.address.logic.commands;

import static org.junit.Assert.assertEquals;

import java.util.logging.Logger;

import org.junit.Ignore;
import org.junit.Test;

import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;

@Ignore
class RestoreSnapshotsCommandTest {
    private static final Logger logger = Logger.getLogger(RestoreCommand.class.getName());

    private Model model = new ModelManager();
    private CommandHistory commandHistory = new CommandHistory();

    @Test
    public void execute_snapshots_success() throws Exception {
        try {
            CommandResult result = new RestoreSnapshotsCommand().execute(model, commandHistory);
            assertEquals(RestoreSnapshotsCommand.getBackupNames(), result.feedbackToUser);
        } catch (CommandException ce) {
            logger.severe(ce.getMessage());
        }
    }
}
