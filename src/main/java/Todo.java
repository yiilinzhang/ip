import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Todo extends Task{
    private final String symbol = "[T]";

    public Todo(String task) throws FoodException{
        Pattern p = Pattern.compile("^todo (?<name>.+?)$");
        Matcher m = p.matcher(task);
        if (!m.matches()) {
            throw new FoodException("hey that's not a right format...");
        }
        super(m.group("name"));
    }

    @Override
    public String toString() {
        return String.format("%s %s",
                this.symbol,
                super.toString());
    }
}
