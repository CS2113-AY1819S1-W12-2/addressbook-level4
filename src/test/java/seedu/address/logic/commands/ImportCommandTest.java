package seedu.address.logic.commands;

import static org.junit.Assert.assertEquals;

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
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

public class ImportCommandTest {
    private static final Logger logger = Logger.getLogger(ImportCommand.class.getName());

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private final String directory = "src"
            + File.separator + "test"
            + File.separator + "data"
            + File.separator + "ImportTestCsv"
            + File.separator + "ImportList.csv";
    private final String directoryWrongFormat = "src"
            + File.separator + "test"
            + File.separator + "data"
            + File.separator + "ImportTestCsv"
            + File.separator + "ImportListWrongFormat.csv";
    private final String arg = "import";
    private final Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private final Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private CommandHistory commandHistory = new CommandHistory();


    @Test
    public void execute_success() throws Exception {
        Person person = new PersonBuilder().build();
        expectedModel.addPerson(person);

        File importFile = new File(directory);
        String expectedMessage =
                String.format(String.format(MESSAGE_SUCCESS, directory));

        CommandResult result = new ImportCommand(directory, importFile).execute(model, commandHistory);

        assertEquals(expectedMessage, result.feedbackToUser);
    }

    @Test
    public void execute_empty() throws Exception {
        thrown.expect(ParseException.class);
        thrown.expectMessage(MESSAGE_USAGE);
        new ImportCommandParser().parse(arg).execute(model, commandHistory);
    }

    @Test
    public void exevute_wrong_format() throws Exception {
        thrown.expect(CommandException.class);
        thrown.expectMessage(WRONG_FORMAT);
        File wrongFile = new File(directoryWrongFormat);
        new ImportCommand(directoryWrongFormat, wrongFile).execute(model, commandHistory);
    }
}
