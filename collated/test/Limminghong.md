# Limminghong
###### /java/seedu/address/logic/commands/ImportCommandTest.java
``` java
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
```
###### /java/seedu/address/logic/commands/RestoreCommandTest.java
``` java
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
```
###### /java/seedu/address/logic/commands/RestoreSnapshotsCommandTest.java
``` java
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
```
###### /java/seedu/address/logic/commands/BackUpCommandTest.java
``` java
package seedu.address.logic.commands;

import static junit.framework.TestCase.assertEquals;

import java.util.logging.Logger;

import org.junit.jupiter.api.Test;

import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;

class BackUpCommandTest {
    private static final Logger logger = Logger.getLogger(ImportCommand.class.getName());

    private Model model = new ModelManager();
    private CommandHistory commandHistory = new CommandHistory();


    @Test
    public void execute_success() {
        try {
            CommandResult result = new BackUpCommand().execute(model, commandHistory);
            assertEquals(BackUpCommand.MESSAGE_SUCCESS, result.feedbackToUser);
        } catch (CommandException ce) {
            logger.severe(ce.getMessage());
        }
    }
}
```
###### /java/seedu/address/logic/commands/ExportCommandTest.java
``` java
package seedu.address.logic.commands;

import static junit.framework.TestCase.assertEquals;

import java.io.File;
import java.util.logging.Logger;

import org.junit.jupiter.api.Test;

import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.ExportCommandParser;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;

class ExportCommandTest {
    private static final Logger logger = Logger.getLogger(ExportCommand.class.getName());

    private Model model = new ModelManager();
    private CommandHistory commandHistory = new CommandHistory();

    @Test
    public void execute_success() {
        String directory = ExportCommandParser.DEFAULT_DIRECTORY.trim();
        String fileName = ExportCommandParser.DEFAULT_FILE_NAME + ".csv";
        fileName = fileName.trim();
        File file = new File(directory + "/" + fileName);
        try {
            CommandResult result = new ExportCommand(directory, fileName, file).execute(model, commandHistory);
            assertEquals(ExportCommand.MESSAGE_SUCCESS, result.feedbackToUser);
        } catch (CommandException ce) {
            logger.severe(ce.getMessage());
        }
    }
}
```
