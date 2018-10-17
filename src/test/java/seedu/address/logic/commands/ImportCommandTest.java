//@@author Limminghong
package seedu.address.logic.commands;

import static junit.framework.TestCase.assertEquals;

import java.util.logging.Logger;

import org.junit.jupiter.api.Test;

import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.ImportCommandParser;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;

class ImportCommandTest {
    private static final Logger logger = Logger.getLogger(ImportCommand.class.getName());

    private Model model = new ModelManager();
    private CommandHistory commandHistory = new CommandHistory();


    @Test
    public void execute_empty() {
        try {
            String arg = "import";
            ImportCommand importResult = new ImportCommandParser().parse(arg);
            CommandResult result = importResult.execute(model, commandHistory);
            assertEquals(ImportCommand.MESSAGE_USAGE, result.feedbackToUser);
        } catch (ParseException pe) {
            logger.severe(pe.getMessage());
        } catch (CommandException ce) {
            logger.severe(ce.getMessage());
        }

    }
}
