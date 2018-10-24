//@@author Limminghong
package seedu.address.logic.commands;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

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
    public static final String MESSAGE_ERROR = "The source or destination does not exist!";
    public static final String DEST_PATH = ".backup";

    private String sourceName;
    private String fileName;
    private UserPrefs userPref;

    public BackUpCommand() {
        userPref = new UserPrefs();
        sourceName = userPref.getAddressBookFilePath().toString();
        fileName = DEST_PATH + "\\" + Long.toString(System.currentTimeMillis()) + ".xml";
    }

    public BackUpCommand(String sourceName) {
        userPref = new UserPrefs();
        this.sourceName = sourceName;
        fileName = DEST_PATH + "\\" + Long.toString(System.currentTimeMillis()) + ".xml";
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        FileEncryptor fe = new FileEncryptor(userPref.getAddressBookFilePath().toString());
        File backupDest = new File(DEST_PATH);

        if (fe.isLocked()) {
            throw new CommandException(FileEncryptor.MESSAGE_ADDRESS_BOOK_LOCKED);
        }

        if (!backupDest.exists()) {
            new File(DEST_PATH).mkdir();
        }

        try {
            makeBackup(sourceName, fileName);
            return new CommandResult(MESSAGE_SUCCESS);
        } catch (IOException io) {
            throw new CommandException(MESSAGE_ERROR);
        }
    }

    /**
     * @param sourceName the directory of the addressbook.xml
     * @param fileName the name of the destination file
     * @throws IOException if either the source file or destination cannot be found
     */
    public void makeBackup (String sourceName, String fileName) throws IOException {
        File source = new File(sourceName);
        File dest = new File(fileName);
        Files.copy(source.toPath(), dest.toPath());
    }

    /**
     * @return filename
     */
    public String getFileName() {
        return this.fileName;
    }
}
