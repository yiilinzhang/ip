package food.task;

import food.exception.FoodInputException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A task with nothing but a description, e.g. "todo read book".
 *
 * <p>The simplest of the three task kinds: unlike {@link Deadline} and {@link Event} it carries
 * no dates, so it only has to pull the description out of the line the user typed.
 */
public class Todo extends Task{
    /** Printed in front of every Todo so the user can tell the task kinds apart at a glance. */
    private final String symbol = "[T]";

    /**
     * Creates a Todo from the line the user typed.
     *
     * <p>The named group in the pattern ("name") is what the description is read from; naming the
     * group rather than counting brackets keeps the code readable if the pattern grows.
     *
     * @param task the full line, which must look like "todo &lt;description&gt;"
     * @throws FoodInputException if the line does not match that shape, or the description is blank
     */
    public Todo(String task) throws FoodInputException {
        Pattern p = Pattern.compile("^todo (?<name>.+?)$");
        Matcher m = p.matcher(task);
        if (!m.matches()) {
            throw new FoodInputException("hey that's not a right format...");
        }
        super(m.group("name"), task);
    }

    /**
     * Returns the task as the user sees it, e.g. "[T] [X] read book".
     *
     * @return the symbol for this kind of task, followed by the status and description from
     *         {@link Task#toString}
     */
    @Override
    public String toString() {
        return String.format("%s %s",
                this.symbol,
                super.toString());
    }
}
