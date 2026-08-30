package food;

import food.exception.FoodInputException;

/**
 * Makes sense of what the user typed.
 *
 * <p>Turns a raw line into a {@link Command}: which of the things Food can do was asked for, and
 * the task number it applies to. Everything that can go wrong with the <em>shape</em> of a command
 * is rejected here, so by the time Foodbot receives a Command it knows the line was well formed
 * and only has to decide what to do about it.
 *
 * <p>Whether the task number points at a task that actually exists is deliberately not checked
 * here: only TaskList knows how many tasks there are.
 */
public class Parser {
    /** The kinds of thing Food can be asked to do. */
    public enum CommandType {
        /** Show every task in the list. */
        LIST,
        /** Mark the task at the given index as done. */
        MARK,
        /** Mark the task at the given index as not done. */
        UNMARK,
        /** Remove the task at the given index. */
        DELETE,
        /** Add a new todo, deadline or event. */
        ADD,
        /** Leave the chatbot. */
        EXIT
    }

    /** The index carried by commands that do not refer to a particular task. */
    public static final int NO_INDEX = -1;

    private static final String EXIT_PHRASE = "LET ME OUT!";

    /**
     * One understood command.
     *
     * <p>A record rather than a class: it is nothing but the three answers the parser worked out,
     * and records give the constructor, accessors and equals for free.
     *
     * @param type     what was asked for.
     * @param index    0-based task index for MARK, UNMARK and DELETE; {@link #NO_INDEX} otherwise.
     * @param rawInput the untouched line, which ADD needs because Todo, Deadline and Event read
     *                 the details out of it themselves.
     */
    public record Command(CommandType type, int index, String rawInput) {}

    /**
     * Reads one line of user input.
     *
     * @param input the raw line the user typed.
     * @return the command it stands for.
     * @throws FoodInputException if the command is unknown, or is not followed by exactly one
     *                            task number when it needs one.
     */
    public static Command parse(String input) throws FoodInputException {
        // Checked before splitting, since the exit phrase is several words long.
        if (input.equals(EXIT_PHRASE)) {
            return new Command(CommandType.EXIT, NO_INDEX, input);
        }

        String[] parts = input.trim().split(" ");
        String command = parts[0];

        // Arrow labels: each case produces its own value, so no break/fall-through.
        return switch (command) {
            case "list" -> new Command(CommandType.LIST, NO_INDEX, input);
            case "mark" -> new Command(CommandType.MARK, parseTaskIndex(parts), input);
            case "unmark" -> new Command(CommandType.UNMARK, parseTaskIndex(parts), input);
            case "delete" -> new Command(CommandType.DELETE, parseTaskIndex(parts), input);
            case "todo", "deadline", "event" -> new Command(CommandType.ADD, NO_INDEX, input);
            default -> throw new FoodInputException(
                    "OOPS!!! I'm sorry, but I don't know what that means :-(");
        };
    }

    /**
     * Converts the task number following a command word into a 0-based index, e.g. the "2" in
     * "mark 2" becomes 1.
     *
     * @param parts the input split on spaces, with the command word first.
     * @return the 0-based index the user meant.
     * @throws FoodInputException if there is not exactly one argument, or it is not a number.
     */
    private static int parseTaskIndex(String[] parts) throws FoodInputException {
        String command = parts[0];
        if (parts.length != 2) {
            throw new FoodInputException(String.format("%s has to be followed by exactly one task number",
                    command));
        }
        try {
            return Integer.parseInt(parts[1]) - 1;
        } catch (NumberFormatException e) {
            throw new FoodInputException(
                    String.format("%s has to be followed by a number", command), e);
        }
    }

    /** Utility class: everything here is static, so there is nothing to construct. */
    private Parser() {
    }
}
