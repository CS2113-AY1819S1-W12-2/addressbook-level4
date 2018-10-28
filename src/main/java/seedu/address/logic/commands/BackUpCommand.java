//@@author Limminghong
package seedu.address.logic.commands;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import seedu.address.commons.util.FileEncryptor;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.CliSyntax;
import seedu.address.model.Model;
import seedu.address.model.UserPrefs;

/**
 * Backs up a snapshot of the address book into a .backup folder.
 */
public class BackUpCommand extends Command {

    public static final String COMMAND_WORD = CliSyntax.COMMAND_BACKUP;
    public static final String MESSAGE_SUCCESS = "Address book has been backed up!";
    public static final String DEST_PATH = ".backup";

    private String fileName;
    private Path destPath;
    private UserPrefs userPref;
    private String destFolder;

    public BackUpCommand() {
        userPref = new UserPrefs();
        destFolder = DEST_PATH;
        fileName = destFolder + "\\" + Long.toString(System.currentTimeMillis()) + ".xml";
        destPath = Paths.get(fileName);
    }

    public BackUpCommand(String destination) {
        userPref = new UserPrefs();
        destFolder = destination;
        fileName = destFolder + "\\" + Long.toString(System.currentTimeMillis()) + ".xml";
        destPath = Paths.get(fileName);
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        FileEncryptor fe = new FileEncryptor(userPref.getAddressBookFilePath().toString());
        File backupDest = new File(destFolder);

        if (fe.isLocked()) {
            throw new CommandException(FileEncryptor.MESSAGE_ADDRESS_BOOK_LOCKED);
        }

        if (!backupDest.exists()) {
            new File(destFolder).mkdir();
        }

        model.backUpAddressbook(destPath);
        return new CommandResult(MESSAGE_SUCCESS);
    }

    /**
     * @return filename
     */
    public String getFileName() {
        return this.fileName;
    }
}
