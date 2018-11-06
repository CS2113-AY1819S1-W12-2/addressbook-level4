package seedu.address.logic.parser;

import static seedu.address.logic.parser.CliSyntax.PREFIX_KPI;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.ArrayList;
import java.util.List;

import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses the list command arguments to determine which attributes to filter for listing.
 */
public class ListCommandParser implements Parser<ListCommand> {

    @Override
    public ListCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(
                args,
                PREFIX_NAME,
                PREFIX_TAG,
                PREFIX_KPI);

        if (argMultimap.getValue(PREFIX_TAG).isPresent()) {
            return new ListCommand(ListCommand.TYPE_TAG, ParserUtil.parseTags(argMultimap.getAllValues(PREFIX_TAG)));
        } else if (argMultimap.getValue(PREFIX_KPI).isPresent()) {
            List<String> kpiList = argMultimap.getAllValues(PREFIX_KPI);
            checkKpiFormat(kpiList);
            return new ListCommand(ListCommand.TYPE_KPI, argMultimap.getAllValues(PREFIX_KPI));
        } else {
            return new ListCommand(ListCommand.TYPE_ALL, new ArrayList<>());
        }
    }

    /**
     * Checks if a KPI string input is valid.
     * @param kpiList the list of KPI strings input.
     * @throws ParseException if any one of the KPI is invalid.
     */
    private void checkKpiFormat(List<String> kpiList) throws ParseException {
        for (String kpi : kpiList) {
            ParserUtil.parseKpi(kpi);
        }
    }
}
