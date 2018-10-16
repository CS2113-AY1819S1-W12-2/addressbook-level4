//@@author Limminghong
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
