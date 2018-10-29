# lekoook
###### /java/seedu/address/logic/parser/SelectCommandParserTest.java
``` java
    @Test
    public void parse_validArgs_returnsSelectCommandSingle() {
        assertParseSuccess(parser, "1", new SelectCommand(INDEX_LIST_FIRST));
    }

    @Test
    public void parse_validArgs_returnSelectCommandMultiple() {
        assertParseSuccess(parser, "1 2 3", new SelectCommand(INDEX_LIST_THREE));
    }

    @Test
    public void parse_validArgs_returnSelectCommandRange() {
        assertParseSuccess(parser, "1 - 3", new SelectCommand(INDEX_LIST_THREE));
    }

    @Test
    public void parse_validArgs_returnSelectCommandMultipleRange() {
        assertParseSuccess(parser, "1  -  3 , 5 -7", new SelectCommand(INDEX_LIST_SIX));
    }

    @Test
    public void parse_invalidSingleNegative_throwsParseException() {
        assertParseFailure(parser, "-1", invalidMessage);
    }

    @Test
    public void parse_invalidMultipleNegative_throwsParseException() {
        assertParseFailure(parser, "-1 -2 3", invalidMessage);
    }

    @Test
    public void parse_invalidRangeFormat_throwsParseException() {
        assertParseFailure(parser, "1 -- 3", invalidMessage);
    }

    @Test
    public void parse_invalidRangeIndex_throwsParseException() {
        assertParseFailure(parser, "-3 - 3", invalidMessage);
    }
}
```
###### /java/seedu/address/logic/parser/ParserUtilTest.java
``` java
    @Test
    public void parseSelectIndex_validInput_success() throws Exception {
        // No white spaces, single Index
        assertEquals(INDEX_LIST_FIRST, ParserUtil.parseSelectIndex("1"));

        // No white spaces, multiple Index
        assertEquals(INDEX_LIST_THREE, ParserUtil.parseSelectIndex("1 2 3"));

        // No white spaces, single range Index
        assertEquals(INDEX_LIST_THREE, ParserUtil.parseSelectIndex("1-3"));

        // No white spaces, multiple range Index
        assertEquals(INDEX_LIST_SIX, ParserUtil.parseSelectIndex("1-3,5-7"));

        // With white spaces, single Index
        assertEquals(INDEX_LIST_FIRST, ParserUtil.parseSelectIndex("  1     "));

        // With white spaces, multiple Index
        assertEquals(INDEX_LIST_THREE, ParserUtil.parseSelectIndex(" 1      2   3    "));

        // With white spaces, single range Index
        assertEquals(INDEX_LIST_THREE, ParserUtil.parseSelectIndex("        1  - 3  "));

        // With white spaces, multiple range Index
        assertEquals(INDEX_LIST_SIX, ParserUtil.parseSelectIndex("1 -    3 , 5  - 7  "));
    }

    @Test
    public void parseSelectIndex_invalidInput_throwsParseException() throws Exception {
        thrown.expect(ParseException.class);
        // Non numeric
        ParserUtil.parseSelectIndex("a");

        // Non positive values
        ParserUtil.parseSelectIndex("-1");
        ParserUtil.parseSelectIndex("0");

        // Invalid range format/values
        ParserUtil.parseSelectIndex("1 -- 3");
        ParserUtil.parseSelectIndex("-4 - 2");

        // Invalid multiple range format/values
        ParserUtil.parseSelectIndex("1 - 3 5 - 9");
        ParserUtil.parseSelectIndex("1-4 ,, 6-8");
    }
}
```
###### /java/seedu/address/logic/commands/SelectCommandTest.java
``` java
    /**
     * Executes a {@code SelectCommand} with the given {@code index}, and checks that {@code JumpToListRequestEvent}
     * is raised with the correct index.
     */
    private void assertExecutionSuccessSingle(ArrayList<Index> indexArrayList) {
        SelectCommand selectCommand = new SelectCommand(indexArrayList);
        String expectedMessage =
                String.format(SelectCommand.MESSAGE_SELECT_PERSON_SUCCESS_SINGLE, indexArrayList.size());

        assertCommandSuccess(selectCommand, model, commandHistory, expectedMessage, expectedModel);

        JumpToListRequestEvent lastEvent = (JumpToListRequestEvent) eventsCollectorRule.eventsCollector.getMostRecent();
        assertEquals(extractIndexAsIntegers(indexArrayList), lastEvent.targetIndex);
    }

    /**
     * Executes a {@code SelectCommand} with the given {@code index}, and checks that {@code JumpToListRequestEvent}
     * is raised with the correct index.
     */
    private void assertExecutionSuccessMultiple(ArrayList<Index> indexArrayList) {
        SelectCommand selectCommand = new SelectCommand(indexArrayList);
        String expectedMessage =
                String.format(SelectCommand.MESSAGE_SELECT_PERSON_SUCCESS_MULTIPLE, indexArrayList.size());

        assertCommandSuccess(selectCommand, model, commandHistory, expectedMessage, expectedModel);

        JumpToListRequestEvent lastEvent = (JumpToListRequestEvent) eventsCollectorRule.eventsCollector.getMostRecent();
        assertEquals(extractIndexAsIntegers(indexArrayList), lastEvent.targetIndex);
    }

    /**
     * Extracts an array list of {@code Index} to an array list of {@code Integer}.
     * @param indexArrayList the list of {@code Index} to extract from.
     * @return the list of {@code Integer} extracted.
     */
    private ArrayList<Integer> extractIndexAsIntegers(ArrayList<Index> indexArrayList) {
        ArrayList<Integer> output = new ArrayList<>();
        for (Index index : indexArrayList) {
            output.add(index.getZeroBased());
        }
        return output;
    }
}
```
###### /java/seedu/address/logic/commands/AddCommandTest.java
``` java
        @Override
        public TextPrediction getTextPrediction() {
            return new CommandCompleter(this);
        }

        @Override
        public void setSelectedPersons(List<Person> personListView) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public List<Person> getSelectedPersons() {
            throw new AssertionError("This method should not be called.");
        }
```
