package seedu.address.logic.commands;

import static org.junit.Assert.assertEquals;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.logic.commands.ImportCommand.MESSAGE_SUCCESS;
import static seedu.address.logic.commands.ImportCommand.MESSAGE_USAGE;
import static seedu.address.storage.CsvReader.WRONG_FORMAT;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.io.File;
import java.util.logging.Logger;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.ImportCommandParser;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;

public class ImportCommandTest {
    private static final Logger logger = Logger.getLogger(ImportCommand.class.getName());

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private final String directory = "src"
            + File.separator + "test"
            + File.separator + "data"
            + File.separator + "ImportTestCsv"
            + File.separator + "ImportList.csv";
    private final String directoryTwo = "src"
            + File.separator + "test"
            + File.separator + "data"
            + File.separator + "ImportTestCsv"
            + File.separator + "ImportList2.csv";
    private final String directoryWrongFormat = "src"
            + File.separator + "test"
            + File.separator + "data"
            + File.separator + "ImportTestCsv"
            + File.separator + "ImportListWrongFormat.csv";
    private final String arg = "import";
    private final Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private CommandHistory commandHistory = new CommandHistory();


    @Test
    public void execute_success() {
        try {
            File importFile = new File(directory);
            String expectedMessage =
                    String.format(String.format(MESSAGE_SUCCESS, directory));

            CommandResult result = new ImportCommand(directory, importFile).execute(model, commandHistory);

            assertEquals(expectedMessage, result.feedbackToUser);
        } catch (CommandException ce) {
            logger.severe(ce.getMessage());
        }
    }

    @Test
    public void execute_empty() throws Exception {
        thrown.expect(ParseException.class);
        thrown.expectMessage(MESSAGE_USAGE);
        new ImportCommandParser().parse(arg).execute(model, commandHistory);
    }

    @Test
    public void execute_wrong_format() throws Exception {
        thrown.expect(CommandException.class);
        thrown.expectMessage(WRONG_FORMAT);
        File wrongFile = new File(directoryWrongFormat);
        new ImportCommand(directoryWrongFormat, wrongFile).execute(model, commandHistory);
    }

    @Test
    public void equals() {
        File importFileOne = new File(directory);
        File importFileTwo = new File(directoryTwo);
        ImportCommand importCommandFileOne = new ImportCommand(directory, importFileOne);
        ImportCommand importCommandFileTwo = new ImportCommand(directoryTwo, importFileTwo);

        // same object -> returns true
        assertTrue(importCommandFileOne.equals(importCommandFileOne));

        // same values -> returns true
        ImportCommand importCommandFileOneCopy = new ImportCommand(directory, importFileOne);
        assertTrue(importCommandFileOne.equals(importCommandFileOneCopy));

        // different types -> returns false
        assertFalse(importCommandFileOne.equals(1));

        // null -> returns false
        assertFalse(importCommandFileOne.equals(null));

        // different person -> returns false
        assertFalse(importCommandFileOne.equals(importCommandFileTwo));
    }
}
