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
    public static final String ERROR = "error";

    public static final String DEST_PATH = ".backup";

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        UserPrefs userPref = new UserPrefs();
        FileEncryptor fe = new FileEncryptor(userPref.getAddressBookFilePath().toString());
        File source = new File(userPref.getAddressBookFilePath().toString());
        File backupDest = new File(DEST_PATH);

        if (fe.isLocked()) {
            throw new CommandException(FileEncryptor.MESSAGE_ADDRESS_BOOK_LOCKED);
        }

        try {
            if (!backupDest.exists()) {
                new File(DEST_PATH).mkdir();
            }
            String fileName = Long.toString(System.currentTimeMillis());
            File dest = new File(DEST_PATH + "/" + fileName + ".xml");
            Files.copy(source.toPath(), dest.toPath());
            return new CommandResult(MESSAGE_SUCCESS);
        } catch (IOException io) {
            throw new CommandException(ERROR);
        }
    }
}
