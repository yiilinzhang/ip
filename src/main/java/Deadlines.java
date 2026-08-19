import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Deadlines extends Task{
    private final String by;
    private final String symbol = "[D]";

    public Deadlines(String task) throws FoodException{
        Pattern p = Pattern.compile("^deadline (?<name>.+?) /by (?<by>.+?)$");
        Matcher m = p.matcher(task);
        if (!m.matches()) {
            throw new FoodException("hey that's not a right format...");
        }
        super(m.group("name"));
        this.by = m.group("by");
    }

    @Override
    public String toString() {
        return String.format("%s %s (by: %s)",
                this.symbol,
                super.toString(),
                this.by);
    }
}
