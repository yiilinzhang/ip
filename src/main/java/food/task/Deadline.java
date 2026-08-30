package food.task;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A task that has to be finished by a particular date, e.g. "deadline return book /by 2026-09-07".
 *
 * <p>The date is kept as a {@link LocalDate} rather than as the text the user typed, so that it is
 * validated once when the task is created and can later be compared or reformatted.
 */
public class Deadline extends Task {
    /** The day the task is due. */
    private final LocalDate by;
    /** Printed in front of every Deadline so the user can tell the task kinds apart at a glance. */
    private static final String SYMBOL = "[D]";

    /**
     * Creates a Deadline from the line the user typed.
     *
     * @param task the full line, which must look like "deadline &lt;description&gt; /by &lt;date&gt;"
     * @throws FoodInputException if the line does not match that shape, the description is blank,
     *                            or the date is not a real date in yyyy-mm-dd form
     */
    public Deadline(String task) throws FoodInputException {
        Pattern p = Pattern.compile("^deadline (?<name>.+?) /by (?<by>.+?)$");
        Matcher m = p.matcher(task);
        if (!m.matches()) {
            throw new FoodInputException("hey that's not a right format...");
        }
        super(m.group("name"), task);
        try {
            // Accepts YYYY-MM-DD
            this.by = LocalDate.parse(m.group("by"));
        } catch (DateTimeParseException e) {
            throw new FoodInputException(
                    "I need the date as yyyy-mm-dd, e.g. /by 2026-09-07", e);
        }
    }

    /**
     * Returns the task as the user sees it, e.g. "[D] [X] return book (by: 7/9/2026)".
     *
     * <p>The date is shown as d/m/yyyy even though it is typed as yyyy-mm-dd: the input form is
     * unambiguous to parse, the output form is the one the user reads more easily.
     *
     * @return the symbol, the status and description from {@link Task#toString}, and the due date
     */
    @Override
    public String toString() {
        // Returns date in DD/MM/YYY
        return String.format("%s %s (by: %d/%d/%d)",
                SYMBOL,
                super.toString(),
                this.by.getDayOfMonth(),
                this.by.getMonthValue(),
                this.by.getYear());
    }
}
