//@@author Limminghong
package seedu.address.logic.commands;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Map;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.commons.util.FileEncryptor;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.CliSyntax;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.UserPrefs;
import seedu.address.model.backup.BackupList;
import seedu.address.model.util.SampleDataUtil;
import seedu.address.storage.XmlAddressBookStorage;

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
    UserPrefs userPrefs;


    public RestoreCommand() {}

    public RestoreCommand(BackupList backupList, Index index) {
        userPrefs = new UserPrefs();
        this.fileMap = backupList.getFileMap();
        this.fileName = backupList.getFileNames();
        this.index = index;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        FileEncryptor fe = new FileEncryptor(userPrefs.getAddressBookFilePath().toString());

        if (fe.isLocked()) {
            throw new CommandException(FileEncryptor.MESSAGE_ADDRESS_BOOK_LOCKED);
        }

        if (index.getZeroBased() >= fileMap.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_SNAPSHOT_DISPLAYED_INDEX);
        }

        try {
            restoreFileFromIndex(model, fileMap, index);
            model.reinitAddressbook();
            return new CommandResult(String.format(MESSAGE_RESTORED_SUCCESS, fileName.get(index.getZeroBased())));
        } catch (IOException io) {
            throw new CommandException(Messages.MESSAGE_INVALID_SNAPSHOT_DISPLAYED_INDEX);
        }
    }

    /**
     * Copy and pastes a backup snapshot from the chosen index to the destination
     * @param fileMap a map of the snapshots with indexes as keys
     * @param index the index of the file that is extracted
     * @throws IOException if either of the path does not exist
     */
    private void restoreFileFromIndex(Model model, Map<Integer, File> fileMap, Index index) throws IOException {
        File newFile = fileMap.get(index.getZeroBased());
        model.replaceData(Paths.get(newFile.toString()));
    }
}
