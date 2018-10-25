//@@author lws803
package seedu.address.commons.exceptions;

/**
 * Exception thrown when FileEncryption has issues
 */
public class FileEncryptorException extends Exception {

    /**
     * @param message should contain relevant information on the failed constraint(s)
     */
    public FileEncryptorException(String message) {
        super(message);
    }

}
