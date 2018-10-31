//@@author Limminghong
package seedu.address.logic.commands;

import static junit.framework.TestCase.assertEquals;

import java.io.IOException;
import java.util.logging.Logger;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.ParserUtil;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.backup.BackupList;


class RestoreCommandTest {
    private static final Logger logger = Logger.getLogger(RestoreCommand.class.getName());

    private Model model = new ModelManager();
    private CommandHistory commandHistory = new CommandHistory();

    @Test
    public void execute_index_success() throws Exception {
        try {
            BackupList backupList = ParserUtil.parseBackup("snapshots");
            Index index = ParserUtil.parseIndex("1");
            String fileDate = backupList.getFileNames().get(index.getZeroBased());
            CommandResult result = new RestoreCommand(backupList, index).execute(model, commandHistory);
            assertEquals(String.format(RestoreCommand.MESSAGE_RESTORED_SUCCESS, fileDate), result.feedbackToUser);
        } catch (CommandException ce) {
            logger.severe(ce.getMessage());
        } catch (IOException io) {
            logger.severe(io.getMessage());
        }
    }
}
