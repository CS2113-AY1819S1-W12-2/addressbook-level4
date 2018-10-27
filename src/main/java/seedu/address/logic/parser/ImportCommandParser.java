//@@author Limminghong
package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DIRECTORY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_FILENAME;

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
                        PREFIX_DIRECTORY,
                        PREFIX_FILENAME);

        /**
         * Checks if prefixes are present
         */
        if (!arePrefixesPresent(argMultimap, PREFIX_DIRECTORY, PREFIX_FILENAME)) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, ImportCommand.MESSAGE_USAGE));
        }

        /**
         * Checks if file specified exists
         */
        String fullDirectory = argMultimap.getValue(PREFIX_DIRECTORY).get().trim() + "\\"
                + argMultimap.getValue(PREFIX_FILENAME).get().trim() + ".csv";
        File file = new File(fullDirectory);
        if (!file.exists() || file.isDirectory()) {
            throw new ParseException(ImportCommand.MESSAGE_FAILURE);
        }

        return new ImportCommand(fullDirectory, file);
    }

    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
