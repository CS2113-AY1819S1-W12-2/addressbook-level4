//@@author lws803
package seedu.address.logic.commands;

import static org.junit.Assert.assertEquals;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import seedu.address.commons.exceptions.FileEncryptorException;
import seedu.address.commons.util.FileEncryptor;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;

/**
 * Testing for password encrypytion and decryption
 */
public class PasswordCommandTest {

    private static final Logger LOGGER = Logger.getLogger(PasswordCommandTest.class.getName());

    private String password = "test1234";
    private String tempFileName = "test.tmp";
    private String toWrite = "Hello";
    private Model model;
    private CommandHistory commandHistory = new CommandHistory();
    private File tmpFile;

    /**
     * Sets up the temporary file
     * @throws IOException
     */
    @Before
    public void setup () throws IOException {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        tmpFile = new File(tempFileName);
        FileWriter writer = new FileWriter(tmpFile);
        writer.write(toWrite);
        writer.close();
    }

    /**
     * FileEncrypter Encryption and decryption test command
     */
    @Test
    public void encryptDecryptTest () throws IOException {

        FileEncryptor feEncrypt = new FileEncryptor(tmpFile.getPath());

        try {
            String message = feEncrypt.process(password);
            assertEquals(FileEncryptor.MESSAGE_ENCRYPTED, message);
        } catch (FileEncryptorException fex) {
            LOGGER.log(Level.WARNING, fex.getLocalizedMessage());
        }

        try {
            String message = feEncrypt.process(password);
            assertEquals(FileEncryptor.MESSAGE_DECRYPTED, message);
        } catch (FileEncryptorException fex) {
            LOGGER.log(Level.WARNING, fex.getLocalizedMessage());
        }

        BufferedReader reader = new BufferedReader(new FileReader(tempFileName));
        assertEquals(toWrite, reader.readLine());
        reader.close();
    }

    /**
     * PasswordCommand unit test
     */
    @Test
    public void passwordTest () {
        assertPasswordCommandSuccess(
                new PasswordCommand(password, tmpFile.getPath()),
                model,
                commandHistory,
                PasswordCommand.MESSAGE_ENCRYPT_SUCCESS
        );

        assertPasswordCommandSuccess(
                new PasswordCommand(password, tmpFile.getPath()),
                model,
                commandHistory,
                PasswordCommand.MESSAGE_DECRYPT_SUCCESS
        );
    }

    /**
     * Cleans up the temp file
     */
    @After
    public void cleanup () {
        File tmpFile = new File(tempFileName);
        tmpFile.delete();
    }


    /**
     * AssertCommand method specifically to test password command success
     * @param command
     * @param actualModel
     * @param actualCommandHistory
     * @param expectedMessage
     */
    public static void assertPasswordCommandSuccess(Command command, Model actualModel,
                                                    CommandHistory actualCommandHistory,
                                                    String expectedMessage) {
        try {
            CommandResult result = command.execute(actualModel, actualCommandHistory);
            assertEquals(expectedMessage, result.feedbackToUser);
        } catch (CommandException ce) {
            throw new AssertionError("Execution of command should not fail.", ce);
        }
    }

}
