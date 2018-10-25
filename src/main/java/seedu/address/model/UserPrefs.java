package seedu.address.model;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.commons.util.JsonUtil;

/**
 * Represents User's preferences.
 */
public class UserPrefs {

    private GuiSettings guiSettings;
    private Path addressBookFilePath = Paths.get("data" , "addressbook.xml");

    //@@author lws803
    public UserPrefs() {
        setGuiSettings(500, 500, 0, 0);
        try {
            Optional<Map> configMap = JsonUtil.readJsonFile(Paths.get("preferences.json"), Map.class);
            if (configMap.isPresent()) {
                Map<String, String> castedConfigMap = configMap.get();
                addressBookFilePath = Paths.get(castedConfigMap.get("addressBookFilePath"));
            }
        } catch (DataConversionException dce) {
            // TODO: Add logger here
        }
    }
    //@@author

    public GuiSettings getGuiSettings() {
        return guiSettings == null ? new GuiSettings() : guiSettings;
    }

    public void updateLastUsedGuiSetting(GuiSettings guiSettings) {
        this.guiSettings = guiSettings;
    }

    public void setGuiSettings(double width, double height, int x, int y) {
        guiSettings = new GuiSettings(width, height, x, y);
    }

    public Path getAddressBookFilePath() {
        return addressBookFilePath;
    }

    public void setAddressBookFilePath(Path addressBookFilePath) {
        this.addressBookFilePath = addressBookFilePath;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof UserPrefs)) { //this handles null as well.
            return false;
        }

        UserPrefs o = (UserPrefs) other;

        return Objects.equals(guiSettings, o.guiSettings)
                && Objects.equals(addressBookFilePath, o.addressBookFilePath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(guiSettings, addressBookFilePath);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Gui Settings : " + guiSettings.toString());
        sb.append("\nLocal data file location : " + addressBookFilePath);
        return sb.toString();
    }

}
