# Limminghong
###### /java/seedu/address/logic/parser/ImportCommandParser.java
``` java
package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DIRECTORY;

import java.io.File;
import java.util.stream.Stream;

import seedu.address.logic.commands.ImportCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new ImportCommand object
 */
public class ImportCommandParser implements Parser<ImportCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the ImportCommand
     * and returns a ImportCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public ImportCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(
                        args,
                        PREFIX_DIRECTORY);

        /**
         * Checks if prefixes are present
         */
        if (!arePrefixesPresent(argMultimap, PREFIX_DIRECTORY)) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, ImportCommand.MESSAGE_USAGE));
        }

        /**
         * Checks if file specified exists
         */
        File file = new File(argMultimap.getValue(PREFIX_DIRECTORY).get().trim());
        if (!file.exists() || file.isDirectory()) {
            throw new ParseException(ImportCommand.MESSAGE_FAILURE);
        }

        return new ImportCommand(argMultimap.getValue(PREFIX_DIRECTORY).get().trim(), file);
    }

    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
```
###### /java/seedu/address/logic/parser/ExportCommandParser.java
``` java
package seedu.address.logic.parser;

import static seedu.address.logic.parser.CliSyntax.PREFIX_DIRECTORY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_FILENAME;

import java.io.File;
import java.util.stream.Stream;

import seedu.address.logic.commands.ExportCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new ExportCommand object
 */
public class ExportCommandParser implements Parser<ExportCommand> {
    public static final String DEFAULT_DIRECTORY = "exports";
    public static final String DEFAULT_FILE_NAME = "export";

    /**
     * Parses the given {@code String} of arguments in the context of the ExportCommand
     * and returns a ExportCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public ExportCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(
                        args,
                        PREFIX_FILENAME,
                        PREFIX_DIRECTORY);

        /**
         * Checks if directory is specified.
         */
        File dest;
        String directory;
        if (arePrefixesPresent(argMultimap, PREFIX_DIRECTORY)) {
            directory = argMultimap.getValue(PREFIX_DIRECTORY).get();
            dest = new File(directory);
            if (!dest.exists()) {
                throw new ParseException(ExportCommand.MESSAGE_FAILURE);
            }
        } else {
            directory = DEFAULT_DIRECTORY;
            dest = new File(directory);
            if (!dest.exists()) {
                new File(directory).mkdir();
            }
        }
        directory = directory.trim();

        /**
         * Checks if name of file has been specified.
         */
        String fileName = DEFAULT_FILE_NAME;
        if (arePrefixesPresent(argMultimap, PREFIX_FILENAME)) {
            fileName = argMultimap.getValue(PREFIX_FILENAME).get();
        }
        fileName += ".csv";
        fileName = fileName.trim();

        /**
         * Checks if a file with the same name in the same directory exist.
         */
        File fullFile = new File(directory + "/" + fileName);
        if (fullFile.exists()) {
            throw new ParseException(String.format(ExportCommand.MESSAGE_FILE_NAME_EXIST, fileName));
        }

        return new ExportCommand(directory, fileName, fullFile);
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
```
###### /java/seedu/address/logic/parser/ParserUtil.java
``` java
    /**
     * Parses {@code String Snapshots} into a {@code Snapshots}.
     * @throws IOException if the ".backup" directory does not exist.
     */
    public static BackupList parseBackup(String backupList) throws IOException {
        requireNonNull(backupList);
        File backupDir = new File(BackUpCommand.DEST_PATH);
        if (!backupDir.exists()) {
            throw new IOException(BackupList.MESSAGE_BACKUP_CONSTRAINTS);
        }
        return new BackupList(backupDir);
    }

```
###### /java/seedu/address/logic/parser/RestoreCommandParser.java
``` java
package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.COMMAND_SNAPSHOTS;

import java.io.IOException;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.RestoreCommand;
import seedu.address.logic.commands.RestoreSnapshotsCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.backup.BackupList;

/**
 * Parses input arguments and creates a new RestoreCommand object
 */
public class RestoreCommandParser implements Parser<RestoreCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the RestoreCommand
     * and returns a RestoreCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public RestoreCommand parse(String args) throws ParseException {
        args = args.trim();
        try {
            BackupList backupList = ParserUtil.parseBackup(args);
            if (args.equals(COMMAND_SNAPSHOTS)) {
                return new RestoreSnapshotsCommand(backupList);
            } else {
                Index index = ParserUtil.parseIndex(args);
                return new RestoreCommand(backupList, index);
            }
        } catch (ParseException pe) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, RestoreCommand.MESSAGE_USAGE), pe);
        } catch (IOException io) {
            throw new ParseException(
                    String.format(BackupList.MESSAGE_BACKUP_CONSTRAINTS));
        }
    }

}
```
###### /java/seedu/address/logic/commands/BackUpCommand.java
``` java
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
```
###### /java/seedu/address/logic/commands/RestoreCommand.java
``` java
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

    public RestoreCommand() {}

    public RestoreCommand(BackupList backupList, Index index) {
        this.fileMap = backupList.getFileMap();
        this.fileName = backupList.getFileNames();
        this.index = index;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        UserPrefs userPref = new UserPrefs();
        FileEncryptor fe = new FileEncryptor(userPref.getAddressBookFilePath().toString());
        Path path = Paths.get(userPref.getAddressBookFilePath().toString());
        XmlAddressBookStorage storage = new XmlAddressBookStorage(path);
        ReadOnlyAddressBook initialData;

        if (fe.isLocked()) {
            throw new CommandException(FileEncryptor.MESSAGE_ADDRESS_BOOK_LOCKED);
        }

        if (index.getZeroBased() >= fileMap.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_SNAPSHOT_DISPLAYED_INDEX);
        }

        try {
            restoreFileFromIndex(userPref, fileMap, index);
            initialData = storage.readAddressBook().orElseGet(SampleDataUtil::getSampleAddressBook);
            model.resetData(initialData);
            return new CommandResult(String.format(MESSAGE_RESTORED_SUCCESS, fileName.get(index.getZeroBased())));
        } catch (IOException io) {
            throw new CommandException(Messages.MESSAGE_INVALID_SNAPSHOT_DISPLAYED_INDEX);
        } catch (DataConversionException dataE) {
            throw new CommandException(Messages.MESSAGE_INVALID_SNAPSHOT_DISPLAYED_INDEX);
        }
    }

    /**
     * @param userPrefs instance of the UserPref object to extract the AddressBook path
     * @param fileMap a map of the snapshots with indexes as keys
     * @param index the index of the file that is extracted
     * @throws IOException if either of the path does not exist
     */
    private void restoreFileFromIndex(UserPrefs userPrefs, Map<Integer, File> fileMap, Index index) throws IOException {
        File newFile = fileMap.get(index.getZeroBased());
        File dest = new File(userPrefs.getAddressBookFilePath().toString());
        Files.copy(newFile.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }
}
```
###### /java/seedu/address/logic/commands/ExportCommand.java
``` java
package seedu.address.logic.commands;

import static seedu.address.logic.parser.CliSyntax.PREFIX_DIRECTORY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_FILENAME;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import javafx.collections.ObservableList;
import seedu.address.commons.util.FileEncryptor;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.CliSyntax;
import seedu.address.model.Model;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.storage.CsvWriter;

/**
 * Exports CSV file into a directory from the address book.
 */
public class ExportCommand extends Command {
    public static final String COMMAND_WORD = CliSyntax.COMMAND_EXPORT;
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Exports the address book into a directory\n"
            + "Parameters: "
            + PREFIX_DIRECTORY + "Directory "
            + PREFIX_FILENAME + "Name of File\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_DIRECTORY + "C:\\Users\\USER\\ "
            + PREFIX_FILENAME + "export1";
    public static final String MESSAGE_FAILURE = "Directory does not exist.";
    public static final String MESSAGE_FILE_NAME_EXIST = "A file with the name %1$s exists in this directory.";
    public static final String MESSAGE_SUCCESS = "AddressBook is exported to %1$s with the name %2$s.";

    private String directory;
    private String fileName;
    private File file;

    public ExportCommand() {}

    public ExportCommand(String directory, String fileName, File file) {
        this.directory = directory;
        this.fileName = fileName;
        this.file = file;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        UserPrefs userPref = new UserPrefs();
        FileEncryptor fe = new FileEncryptor(userPref.getAddressBookFilePath().toString());

        if (fe.isLocked()) {
            throw new CommandException(FileEncryptor.MESSAGE_ADDRESS_BOOK_LOCKED);
        }

        try {
            ObservableList<Person> personList = model.getAddressBook().getPersonList();
            CsvWriter csvWriter = new CsvWriter(personList);
            File srcCsv = csvWriter.convertToCsv();
            Files.copy(srcCsv.toPath(), file.toPath());
            srcCsv.delete();
            return new CommandResult(String.format(MESSAGE_SUCCESS, directory, fileName));
        } catch (IOException io) {
            throw new CommandException("ERROR1");
        } catch (Exception e) {
            throw new CommandException("ERROR2");
        }
    }
}
```
###### /java/seedu/address/logic/commands/ImportCommand.java
``` java
package seedu.address.logic.commands;

import static seedu.address.logic.parser.CliSyntax.PREFIX_DIRECTORY;

import java.io.File;
import java.io.IOException;
import java.util.List;

import seedu.address.commons.util.FileEncryptor;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.CliSyntax;
import seedu.address.model.Model;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.storage.CsvWriter;

/**
 * Imports a CSV file of the address book from a directory.
 */
public class ImportCommand extends Command {
    public static final String COMMAND_WORD = CliSyntax.COMMAND_IMPORT;
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Imports the address book into a directory\n"
            + "Parameters: "
            + PREFIX_DIRECTORY + "Directory\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_DIRECTORY + "C:\\Users\\USER\\export.xml";
    public static final String MESSAGE_FAILURE = "Directory does not exists.";
    public static final String MESSAGE_SUCCESS = "AddressBook is imported from %1$s.";

    private File file;
    private String directory;

    public ImportCommand() {}

    public ImportCommand(String directory, File file) {
        this.directory = directory;
        this.file = file;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        UserPrefs userPref = new UserPrefs();
        FileEncryptor fe = new FileEncryptor(userPref.getAddressBookFilePath().toString());

        if (fe.isLocked()) {
            throw new CommandException(FileEncryptor.MESSAGE_ADDRESS_BOOK_LOCKED);
        }

        try {
            CsvWriter csvWriter = new CsvWriter(file);
            List<Person> personList = csvWriter.convertToList();
            for (Person toAdd : personList) {
                try {
                    model.addPerson(toAdd);
                    model.getTextPrediction().insertPerson(toAdd);
                } catch (DuplicatePersonException dup) {
                    // TODO: ask if continue is needed since cannot leave exception blank
                    continue;
                }
            }
            model.commitAddressBook();
            return new CommandResult(String.format(MESSAGE_SUCCESS, directory));
        } catch (IOException io) {
            throw new CommandException("ERROR");
        }
    }
}
```
###### /java/seedu/address/logic/commands/RestoreSnapshotsCommand.java
``` java
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
```
###### /java/seedu/address/storage/CsvWriter.java
``` java
package seedu.address.storage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javafx.collections.ObservableList;

import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Kpi;
import seedu.address.model.person.Name;
import seedu.address.model.person.Note;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Position;
import seedu.address.model.tag.Tag;

/**
 * Converts the file from {@code Model} to a .csv file
 */
public class CsvWriter {
    public static final String CSV_HEADERS = "Name, Phone, Email, Address, Position, Kpi, Note, Tagged";

    private List<String> stringList = new ArrayList<>();

    /**
     * Parses the {@code ObservableList<Person>} into an array of strings
     * @param personList list of persons from the AddressBook
     */
    public CsvWriter(ObservableList<Person> personList) {
        stringList.add(CSV_HEADERS);
        for (Person p : personList) {
            String personInformation = "\"" + p.getName().toString() + "\""
                    + "," + "\"" + p.getPhone().toString() + "\""
                    + "," + "\"" + p.getEmail().toString() + "\""
                    + "," + "\"" + p.getAddress().toString() + "\""
                    + "," + "\"" + p.getPosition().toString() + "\""
                    + "," + "\"" + p.getKpi().toString() + "\""
                    + "," + "\"" + p.getNote().toString() + "\"";
            if (!p.getTags().isEmpty()) {
                personInformation += "," + "\"";
                int sizeOfTags = 0;
                for (Tag t : p.getTags()) {
                    sizeOfTags += 1;
                    personInformation += t.toString();
                    if (sizeOfTags != p.getTags().size()) {
                        personInformation += ", ";
                    }
                }
                personInformation += "\"";
            }
            stringList.add(personInformation);
        }
    }

    /**
     * @param file .csv File that is being converted to an array of strings
     * @throws IOException if file is not found
     */
    public CsvWriter(File file) throws IOException {
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);

        String line;
        while ((line = br.readLine()) != null) {
            stringList.add(line);
        }
        br.close();
    }

    /**
     * @return The AddressBook in .csv format in "data" folder
     * @throws IOException if {@code convertedFile} does not exist
     */
    public File convertToCsv() throws IOException {
        File convertedFile = new File("data\\addressbook.csv");
        if (!convertedFile.exists()) {
            convertedFile.createNewFile();
        }
        PrintWriter pw = new PrintWriter(convertedFile);
        for (String s : stringList) {
            pw.println(s);
        }
        pw.close();
        return convertedFile;
    }

    /**
     * @return a {@code List} of {@code Person}
     */
    public List<Person> convertToList() {
        List<Person> personList = new ArrayList<>();
        int counter = 0;
        for (String line : stringList) {
            if (counter != 0) {
                line = line.substring(0, line.length() - 1);
                String[] sections = line.split("\",");

                Name name = new Name(sections[0].substring(1).trim());
                Phone phone = new Phone(sections[1].substring(1).trim());
                Email email = new Email(sections[2].substring(1).trim());
                Address address = new Address(sections[3].substring(1).trim());

                Position position;
                if (sections[4].substring(1).equals("null")) {
                    position = new Position();
                } else {
                    position = new Position(sections[4].substring(1).trim());
                }

                Kpi kpi;
                if (sections[5].substring(1).equals("null")) {
                    kpi = new Kpi();
                } else {
                    kpi = new Kpi(sections[5].substring(1).trim());
                }

                Note note;
                if (sections[6].substring(1).equals("null")) {
                    note = new Note();
                } else {
                    note = new Note(sections[6].substring(1).trim());
                }

                Set<Tag> tagList = new HashSet<>();
                if (sections.length == 8) {
                    String[] tags = sections[7].substring(1).split(", ");
                    for (String tagName : tags) {
                        Tag tag = new Tag(tagName.trim());
                        tagList.add(tag);
                    }
                }
                Person person = new Person(name, phone, email, address, position, kpi, note, tagList);
                personList.add(person);
            }
            counter++;
        }
        return personList;
    }
}
```
###### /java/seedu/address/model/backup/BackupList.java
``` java
package seedu.address.model.backup;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a list of all the snapshots of backups.
 */
public class BackupList {
    public static final String MESSAGE_BACKUP_CONSTRAINTS = "There are no previous backups.";

    private List<String> fileNames = new ArrayList<>();
    private Map<Integer, File> fileMap = new HashMap<>();

    public BackupList(File backupDir) throws IOException {
        List<File> backupFiles = Arrays.asList(backupDir.listFiles());
        Collections.reverse(backupFiles);
        for (File snapshots : backupFiles) {
            String millis = snapshots.getName();
            millis = millis.substring(0, millis.length() - 4);
            String fileName = millisToDateAndTime(millis);
            fileNames.add(fileName);
            fileMap.put(fileNames.indexOf(fileName), snapshots);
        }
        if (fileNames.size() == 0) {
            throw new IOException(MESSAGE_BACKUP_CONSTRAINTS);
        }
    }

    public List<String> getFileNames() {
        return this.fileNames;
    }

    public Map<Integer, File> getFileMap() {
        return this.fileMap;
    }

    /**
     * Parses a (@code String millis) into an (@code String)
     * @return A converted and formatted form of date and time.
     */
    public String millisToDateAndTime(String millis) {
        long timestamp = Long.parseLong(millis);
        LocalDateTime dateTime = LocalDateTime.ofInstant(
                Instant.ofEpochMilli(timestamp),
                ZoneId.systemDefault()
        );
        return dateTime.format(DateTimeFormatter.ofPattern("d MMM uuuu HH:mm:ss"));
    }
}
```
