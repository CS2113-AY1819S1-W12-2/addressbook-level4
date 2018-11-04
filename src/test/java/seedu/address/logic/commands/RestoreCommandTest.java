//@@author Limminghong
package seedu.address.logic.commands;

import static org.junit.Assert.assertEquals;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.io.IOException;
import java.util.logging.Logger;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

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

public class RestoreCommandTest {
    private static final Logger logger = Logger.getLogger(RestoreCommand.class.getName());
    private static final String BACKUP_DIRECTORY = "src\\test\\data\\RestoreTestXml";

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private CommandHistory commandHistory = new CommandHistory();
    private BackupList backupList;
    private Index trueIndex;
    private Index falseIndex;

    /**
     * Create backup files for the restore command to execute
     */
    public void setUp() {
        try {
            trueIndex = ParserUtil.parseIndex("1");
            falseIndex = ParserUtil.parseIndex("2");
            backupList = new BackupList(BACKUP_DIRECTORY);
        } catch (IOException io) {
            logger.severe(io.getMessage());
        } catch (ParseException pe) {
            logger.severe(pe.getMessage());
        }
    }

    /**
     * Test when the index is valid
     */
    @Ignore
    @Test
    public void execute_index_success() {
        try {
            String fileDate = backupList.getFileNames().get(trueIndex.getZeroBased());
            CommandResult result = new RestoreCommand(backupList, trueIndex).execute(model, commandHistory);
            assertEquals(String.format(RestoreCommand.MESSAGE_RESTORED_SUCCESS, fileDate), result.feedbackToUser);
        } catch (CommandException ce) {
            logger.severe(ce.getMessage());
        }
    }

    /**
     * Test when the index is invalid
     */
    @Ignore
    @Test
    public void execute_index_invalid() throws Exception {
        thrown.expect(CommandException.class);
        thrown.expectMessage(Messages.MESSAGE_INVALID_SNAPSHOT_DISPLAYED_INDEX);
        new RestoreCommand(backupList, falseIndex).execute(model, commandHistory);
    }
}
