//@@author Limminghong
package seedu.address.logic.commands;

import static junit.framework.TestCase.assertEquals;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.ParserUtil;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.backup.BackupList;


class RestoreCommandTest {
    private static final Logger logger = Logger.getLogger(RestoreCommand.class.getName());
    private static final String BACKUP_DIRECTORY = ".backupStub";

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private CommandHistory commandHistory = new CommandHistory();
    private BackUpCommand backUpCommand;
    private String backupName;
    private File backupDir;
    private BackupList backupList;

    /**
     * Create backup files for the restore command to execute
     */
    @BeforeEach
    public void setUp() {
        backUpCommand = new BackUpCommand(BACKUP_DIRECTORY);
        backupName = backUpCommand.getFileName();
        try {
            backUpCommand.execute(model, commandHistory);
            backupDir = new File(BACKUP_DIRECTORY);
            backupList = new BackupList(backupDir);
        } catch (CommandException ce) {
            logger.severe(ce.getMessage());
        } catch (IOException io) {
            logger.severe(io.getMessage());
        }
    }

    /**
     * Test when the index is valid
     */
    @Test
    public void execute_index_success() {
        try {
            Index index = ParserUtil.parseIndex("1");
            String fileDate = backupList.getFileNames().get(index.getZeroBased());
            CommandResult result = new RestoreCommand(backupList, index).execute(model, commandHistory);
            assertEquals(String.format(RestoreCommand.MESSAGE_RESTORED_SUCCESS, fileDate), result.feedbackToUser);
        } catch (ParseException pe) {
            logger.severe(pe.getMessage());
        } catch (CommandException ce) {
            logger.severe(ce.getMessage());
        }
    }

    /**
     * Test when the index is invalid
     */
    @Test
    public void execute_index_invalid() {
        try {
            Index index = ParserUtil.parseIndex("2");
            CommandResult result = new RestoreCommand(backupList, index).execute(model, commandHistory);
            assertEquals(Messages.MESSAGE_INVALID_SNAPSHOT_DISPLAYED_INDEX, result.feedbackToUser);
        } catch (ParseException pe) {
            logger.severe(pe.getMessage());
        } catch (CommandException ce) {
            logger.severe(ce.getMessage());
        }
    }

    /**
     * Remove the backup snapshot created
     */
    @AfterEach
    public void tearDown() {
        File deleteBackup = new File(backupName);
        deleteBackup.delete();
        backupDir.delete();
    }
}
