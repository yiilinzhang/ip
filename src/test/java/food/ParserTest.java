package food;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import food.Parser.Command;
import food.Parser.CommandType;
import food.exception.FoodInputException;
import org.junit.jupiter.api.Test;

/**
 * Tests {@link Parser#parse(String)}.
 *
 * <p>Parser is a good unit-test target because it is pure: it only maps a String to a Command,
 * so every case can be checked without a file, a console, or any other object being set up.
 *
 * <p>The tests are grouped by the parser's branches: the exit phrase, the argument-less commands,
 * the commands carrying a task number, the add commands, and the inputs that must be rejected.
 */
public class ParserTest {

    // --- exit ---------------------------------------------------------------

    @Test
    public void parse_exitPhrase_exitCommandReturned() throws FoodInputException {
        assertEquals(new Command(CommandType.EXIT, Parser.NO_INDEX, "LET ME OUT!"),
                Parser.parse("LET ME OUT!"));
    }

    @Test
    public void parse_exitPhraseWrongCase_exceptionThrown() {
        // The phrase is matched exactly, so a lower-case version is just an unknown command.
        assertThrows(FoodInputException.class, () -> Parser.parse("let me out!"));
    }

    @Test
    public void parse_exitPhraseWithTrailingSpace_exceptionThrown() {
        // The exit check happens before trimming, so the padded phrase falls through to the
        // command switch, where "LET" is not a known command word.
        assertThrows(FoodInputException.class, () -> Parser.parse("LET ME OUT! "));
    }

    // --- list ---------------------------------------------------------------

    @Test
    public void parse_listCommand_listCommandReturned() throws FoodInputException {
        assertEquals(new Command(CommandType.LIST, Parser.NO_INDEX, "list"),
                Parser.parse("list"));
    }

    @Test
    public void parse_listCommandWithSurroundingSpaces_listCommandReturned() throws FoodInputException {
        // The input is trimmed before splitting, but rawInput keeps the untouched line.
        assertEquals(new Command(CommandType.LIST, Parser.NO_INDEX, "  list  "),
                Parser.parse("  list  "));
    }

    // --- mark / unmark / delete ---------------------------------------------

    @Test
    public void parse_markWithTaskNumber_indexIsZeroBased() throws FoodInputException {
        // "mark 2" refers to the second task, i.e. index 1.
        assertEquals(new Command(CommandType.MARK, 1, "mark 2"), Parser.parse("mark 2"));
    }

    @Test
    public void parse_unmarkWithTaskNumber_indexIsZeroBased() throws FoodInputException {
        assertEquals(new Command(CommandType.UNMARK, 0, "unmark 1"), Parser.parse("unmark 1"));
    }

    @Test
    public void parse_deleteWithTaskNumber_indexIsZeroBased() throws FoodInputException {
        assertEquals(new Command(CommandType.DELETE, 9, "delete 10"), Parser.parse("delete 10"));
    }

    @Test
    public void parse_markOutOfRangeTaskNumber_indexReturnedUnchecked() throws FoodInputException {
        // Whether the task exists is TaskList's job, not the parser's, so "mark 0" parses fine
        // and yields the (invalid) index -1 for TaskList to reject later.
        assertEquals(new Command(CommandType.MARK, -1, "mark 0"), Parser.parse("mark 0"));
    }

    @Test
    public void parse_markWithoutTaskNumber_exceptionThrown() {
        FoodInputException e =
                assertThrows(FoodInputException.class, () -> Parser.parse("mark"));
        assertEquals("mark has to be followed by exactly one task number", e.getMessage());
    }

    @Test
    public void parse_markWithTwoTaskNumbers_exceptionThrown() {
        assertThrows(FoodInputException.class, () -> Parser.parse("mark 1 2"));
    }

    @Test
    public void parse_markWithDoubleSpace_exceptionThrown() {
        // Splitting on a single space turns "mark  2" into three parts, one of them empty,
        // so it is rejected as having the wrong number of arguments.
        assertThrows(FoodInputException.class, () -> Parser.parse("mark  2"));
    }

    @Test
    public void parse_markWithNonNumericArgument_exceptionThrown() {
        FoodInputException e =
                assertThrows(FoodInputException.class, () -> Parser.parse("mark two"));
        assertEquals("mark has to be followed by a number", e.getMessage());
    }

    @Test
    public void parse_deleteWithNonNumericArgument_exceptionThrown() {
        assertThrows(FoodInputException.class, () -> Parser.parse("delete last"));
    }

    // --- todo / deadline / event --------------------------------------------

    @Test
    public void parse_todoCommand_addCommandWithRawInputReturned() throws FoodInputException {
        // ADD carries no index; the whole line is passed on because Todo parses it itself.
        assertEquals(new Command(CommandType.ADD, Parser.NO_INDEX, "todo read book"),
                Parser.parse("todo read book"));
    }

    @Test
    public void parse_deadlineCommand_addCommandWithRawInputReturned() throws FoodInputException {
        String input = "deadline return book /by 2026-09-07";
        assertEquals(new Command(CommandType.ADD, Parser.NO_INDEX, input), Parser.parse(input));
    }

    @Test
    public void parse_eventCommand_addCommandWithRawInputReturned() throws FoodInputException {
        String input = "event camp /from 2026-09-07 /to 2026-09-09";
        assertEquals(new Command(CommandType.ADD, Parser.NO_INDEX, input), Parser.parse(input));
    }

    @Test
    public void parse_todoWithoutDescription_addCommandReturned() throws FoodInputException {
        // The parser only checks the command word; a missing description is Todo's complaint
        // to make, not the parser's.
        assertEquals(new Command(CommandType.ADD, Parser.NO_INDEX, "todo"), Parser.parse("todo"));
    }

    // --- find ---------------------------------------------------------------

    @Test
    public void parse_findWithKeyword_findCommandWithRawInputReturned() throws FoodInputException {
        // FIND carries no index; the keyword is read back out of the raw line, as ADD does.
        assertEquals(new Command(CommandType.FIND, Parser.NO_INDEX, "find book"),
                Parser.parse("find book"));
    }

    @Test
    public void parse_findWithMultiWordKeyword_findCommandReturned() throws FoodInputException {
        assertEquals(new Command(CommandType.FIND, Parser.NO_INDEX, "find read book"),
                Parser.parse("find read book"));
    }

    @Test
    public void parse_findWithoutKeyword_exceptionThrown() {
        FoodInputException e =
                assertThrows(FoodInputException.class, () -> Parser.parse("find"));
        assertEquals("find has to be followed by a keyword", e.getMessage());
    }

    // --- unknown input ------------------------------------------------------

    @Test
    public void parse_unknownCommand_exceptionThrown() {
        FoodInputException e =
                assertThrows(FoodInputException.class, () -> Parser.parse("blah"));
        assertEquals("OOPS!!! I'm sorry, but I don't know what that means :-(", e.getMessage());
    }

    @Test
    public void parse_emptyInput_exceptionThrown() {
        assertThrows(FoodInputException.class, () -> Parser.parse(""));
    }

    @Test
    public void parse_blankInput_exceptionThrown() {
        assertThrows(FoodInputException.class, () -> Parser.parse("   "));
    }

    @Test
    public void parse_commandWordWrongCase_exceptionThrown() {
        // Command words are matched exactly, so "List" is not the same as "list".
        assertThrows(FoodInputException.class, () -> Parser.parse("List"));
    }
}
