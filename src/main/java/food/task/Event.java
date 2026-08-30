package food.task;

import food.exception.FoodInputException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A task that spans a range of dates, e.g. "event camp /from 2026-09-07 /to 2026-09-09".
 *
 * <p>The only task kind that holds two dates. Both are parsed into {@link LocalDate} so that a bad
 * date is rejected when the task is created rather than when it is later displayed.
 */
public class Event extends Task{
    /** The day the event ends. */
    private final LocalDate to;
    /** The day the event starts. */
    private final LocalDate from;
    /** Printed in front of every Event so the user can tell the task kinds apart at a glance. */
    private final String symbol = "[E]";

    /**
     * Creates an Event from the line the user typed.
     *
     * <p>The two dates are not checked against each other, so a "from" later than a "to" is
     * accepted; adding that check would be a reasonable extension.
     *
     * @param task the full line, which must look like
     *             "event &lt;description&gt; /from &lt;date&gt; /to &lt;date&gt;"
     * @throws FoodInputException if the line does not match that shape, the description is blank,
     *                            or either date is not a real date in yyyy-mm-dd form
     */
    public Event(String task) throws FoodInputException {
        Pattern p = Pattern.compile("^event (?<name>.+?) /from (?<from>.+?) /to (?<to>.+)$");
        Matcher m = p.matcher(task);
        if (!m.matches()) {
            throw new FoodInputException("hey that's not a right format...");
        }
        super(m.group("name"), task);
        try {
            this.to = LocalDate.parse(m.group("to"));
            this.from = LocalDate.parse(m.group("from"));
        } catch (DateTimeParseException e) {
            throw new FoodInputException("I need the date as yyyy-mm-dd, e.g. /by 2026-09-07", e);
        }
    }

    /**
     * Returns the task as the user sees it, e.g.
     * "[E] [X] camp (from: 7/9/2026 to: 9/9/2026)".
     *
     * @return the symbol, the status and description from {@link Task#toString}, and both dates
     */
    @Override
    public String toString() {
        return String.format("%s %s (from: %d/%d/%d to: %d/%d/%d)",
                                this.symbol,
                                super.toString(),
                                this.from.getDayOfMonth(),
                                this.from.getMonthValue(),
                                this.from.getYear(),
                                this.to.getDayOfMonth(),
                                this.to.getMonthValue(),
                                this.to.getYear());
    }
}
