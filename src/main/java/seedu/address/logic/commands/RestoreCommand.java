//@@author Limminghong
package seedu.address.logic.commands;

import java.io.File;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.FileEncryptor;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.CliSyntax;
import seedu.address.model.Model;
import seedu.address.model.UserPrefs;
import seedu.address.model.backup.BackupList;

/**
 * Restores the address book to a snapshot of choice.
 */
public class RestoreCommand extends Command {
    public static final String COMMAND_WORD = CliSyntax.COMMAND_RESTORE;
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Restores the address book to a snapshot of choice.\n"
            + "Parameters:" + " snapshots"
            + " or" + " DD/MM/YYYY" + " time";
    public static final String MESSAGE_RESTORED_SUCCESS = "AddressBook has been restored to that of %1$s";

    /**
     * Variables for BackupList
     */
    private Index index;
    private Map<Integer, File> fileMap;
    private List<String> fileName;

    public RestoreCommand() {}

    public RestoreCommand(BackupList backupList, Index index) {
        this.fileMap = backupList.getFileMap();
        this.fileName = backupList.getFileNames();
        this.index = index;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        UserPrefs userPrefs = new UserPrefs();
        FileEncryptor fe = new FileEncryptor(userPrefs.getAddressBookFilePath().toString());

        if (fe.isLocked()) {
            throw new CommandException(FileEncryptor.MESSAGE_ADDRESS_BOOK_LOCKED);
        }

        if (index.getZeroBased() >= fileMap.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_SNAPSHOT_DISPLAYED_INDEX);
        }

        restoreFileFromIndex(model, fileMap, index);
        model.reinitAddressbook();
        return new CommandResult(String.format(MESSAGE_RESTORED_SUCCESS, fileName.get(index.getZeroBased())));
    }

    /**
     * Restores the model to the chosen snapshot
     * @param model to be restored
     * @param fileMap a map of the snapshots with indexes as keys
     * @param index the index of the file that is extracted
     */
    private void restoreFileFromIndex(Model model, Map<Integer, File> fileMap, Index index) {
        File newFile = fileMap.get(index.getZeroBased());
        model.replaceData(Paths.get(newFile.toString()));
    }
}
