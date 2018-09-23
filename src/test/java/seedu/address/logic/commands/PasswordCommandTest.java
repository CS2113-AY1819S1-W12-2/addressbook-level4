package seedu.address.logic.commands;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import seedu.address.commons.util.FileEncryptor;

/**
 * Testing for password encrypytion and decryption
 */
public class PasswordCommandTest {

    private String password = "test1234";
    private String tempFileName = "test.tmp";
    private String toWrite = "Hello";

    /**
     * Encryption and decryption test command
     */
    @Test
    public void encryptDecryptTest () throws IOException{
        File tmpFile = new File(tempFileName);
        FileWriter writer = new FileWriter(tmpFile);
        writer.write(toWrite);
        writer.close();

        FileEncryptor feEncrypt = new FileEncryptor(password, tempFileName);
        assertEquals("File encrypted!", feEncrypt.getMessage());


        FileEncryptor feDecrypt = new FileEncryptor(password, tempFileName);
        assertEquals("File decrypted!", feDecrypt.getMessage());


        BufferedReader reader = new BufferedReader(new FileReader(tempFileName));
        assertEquals(toWrite, reader.readLine());
        reader.close();

        tmpFile.delete(); // Delete and end off
    }
}
