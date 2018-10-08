//@@author Limminghong
package seedu.address.logic.commands;

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
    public static final String MESSAGE_BACKUP_LIST_SUCCESS = "Parser for list works :)";
    public static final String MESSAGE_INDEX_SUCCESS = "Parser for index works :)";
    private int flag;

    public RestoreCommand() {
        this.flag = 0;
    }

    public RestoreCommand(BackupList backupList) {
        this.flag = 1;
    }

    public RestoreCommand(Index index) {
        this.flag = 2;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        UserPrefs userPref = new UserPrefs();
        FileEncryptor fe = new FileEncryptor(userPref.getAddressBookFilePath().toString());

        if (fe.isLocked()) {
            throw new CommandException(FileEncryptor.MESSAGE_ADDRESS_BOOK_LOCKED);
        }

        if (flag == 1) {
            return new CommandResult(MESSAGE_BACKUP_LIST_SUCCESS);
        } else if (flag == 2) {
            return new CommandResult(MESSAGE_INDEX_SUCCESS);
        } else {
            return new CommandResult(MESSAGE_USAGE);
        }
    }
}
