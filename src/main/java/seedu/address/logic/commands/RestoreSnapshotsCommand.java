//@@author Limminghong
package seedu.address.logic.commands;

import java.util.List;

import seedu.address.commons.util.FileEncryptor;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.UserPrefs;
import seedu.address.model.backup.BackupList;

/**
 * Restores the address book to a snapshot of choice.
 */
public class RestoreSnapshotsCommand extends RestoreCommand {
    private static final String NUMBER_OF_SNAPSHOTS_PLURAL = " snapshots listed!";
    private static final String NUMBER_OF_SNAPSHOTS = " snapshot listed!";

    private static String backupNames;

    public RestoreSnapshotsCommand(BackupList backupList) {
        List<String> fileNames = backupList.getFileNames();
        if (fileNames.size() == 1) {
            backupNames = Integer.toString(fileNames.size()) + NUMBER_OF_SNAPSHOTS + "\n";
        } else {
            backupNames = Integer.toString(fileNames.size()) + NUMBER_OF_SNAPSHOTS_PLURAL + "\n";
        }
        for (int i = 1; i <= fileNames.size(); i++) {
            backupNames += Integer.toString(i) + ". " + fileNames.get(i - 1) + "\n";
        }
    }

    public static String getBackupNames() {
        return backupNames;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        UserPrefs userPref = new UserPrefs();
        FileEncryptor fe = new FileEncryptor(userPref.getAddressBookFilePath().toString());

        if (fe.isLocked()) {
            throw new CommandException(FileEncryptor.MESSAGE_ADDRESS_BOOK_LOCKED);
        }

        return new CommandResult(backupNames);
    }
}
