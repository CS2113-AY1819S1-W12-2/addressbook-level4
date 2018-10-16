//@@author Limminghong
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
