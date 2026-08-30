package food.task;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import food.exception.FoodInputException;

public class Event extends Task {
    private final LocalDate to;
    private final LocalDate from;
    private static final String SYMBOL = "[E]";

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

    @Override
    public String toString() {
        return String.format("%s %s (from: %d/%d/%d to: %d/%d/%d)",
                                SYMBOL,
                                super.toString(),
                                this.from.getDayOfMonth(),
                                this.from.getMonthValue(),
                                this.from.getYear(),
                                this.to.getDayOfMonth(),
                                this.to.getMonthValue(),
                                this.to.getYear());
    }
}
