import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Deadline extends Task{
    private final LocalDate by;
    private final String symbol = "[D]";

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

    @Override
    public String toString() {
        // Returns date in DD/MM/YYY
        return String.format("%s %s (by: %d/%d/%d)",
                this.symbol,
                super.toString(),
                this.by.getDayOfMonth(),
                this.by.getMonthValue(),
                this.by.getYear());
    }
}
