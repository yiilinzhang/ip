import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Events extends Task{
    private final String to;
    private final String from;
    private final String symbol = "[E]";

    public Events(String task) throws FoodException{
        Pattern p = Pattern.compile("^event (?<name>.+?) /from (?<from>.+?) /(?<to>.+)$");
        Matcher m = p.matcher(task);
        if (!m.matches()) {
            throw new FoodException("hey that's not a right format...");
        }
        super(m.group("name"));
        this.to = m.group("to");
        this.from = m.group("from");
    }

    @Override
    public String toString() {
        return String.format("%s %s (from: %s to: %s)",
                                this.symbol,
                                super.toString(),
                                this.from,
                                this.to);
    }
}
