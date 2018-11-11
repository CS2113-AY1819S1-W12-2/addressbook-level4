//@@author Limminghong
package seedu.address.logic.commands;

import static org.junit.Assert.assertEquals;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;

import org.junit.After;
import org.junit.Before;
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
import seedu.address.model.person.Person;

public class RestoreCommandTest {
    private static final Logger logger = Logger.getLogger(RestoreCommand.class.getName());
    private static final String BACKUP_DIRECTORY = "src" + File.separator
        + "test" + File.separator
        + "data" + File.separator
        + "RestoreTestXml";
    private static final String FILE_NAME = "1540743391606.xml";

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
    @Before
    public void setUp() {
        try {
            trueIndex = ParserUtil.parseIndex("1");
            falseIndex = ParserUtil.parseIndex("2");
            String fileString = BACKUP_DIRECTORY + File.separator + FILE_NAME;
            Path filePath = Paths.get(fileString);
            model.backUpAddressbook(filePath);
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
    @Test
    public void execute_index_success() throws CommandException {
        Person personToDelete = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        model.deletePerson(personToDelete);

        String fileDate = backupList.getFileNames().get(trueIndex.getZeroBased());
        String expectedMessage = String.format(RestoreCommand.MESSAGE_RESTORED_SUCCESS, fileDate);

        CommandResult result = new RestoreCommand(backupList, trueIndex).execute(model, commandHistory);
        assertEquals(expectedMessage, result.feedbackToUser);
    }

    /**
     * Test when the index is invalid
     */
    @Test
    public void execute_index_invalid() throws Exception {
        thrown.expect(CommandException.class);
        thrown.expectMessage(Messages.MESSAGE_INVALID_SNAPSHOT_DISPLAYED_INDEX);
        new RestoreCommand(backupList, falseIndex).execute(model, commandHistory);
    }

    @After
    public void tearDown() {
        String fileString = BACKUP_DIRECTORY + File.separator + FILE_NAME;
        File deleteFile = new File(fileString);
        deleteFile.delete();
    }
}
