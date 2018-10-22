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

    /**
     * @param message should contain relevant information on the failed constraint(s)
     * @param cause of the main exception
     */
    public FileEncryptorException(String message, Throwable cause) {
        super(message, cause);
    }

}
