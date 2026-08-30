package food.task;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import food.exception.FoodInputException;

public class Todo extends Task {
    private static final String SYMBOL = "[T]";

    public Todo(String task) throws FoodInputException {
        Pattern p = Pattern.compile("^todo (?<name>.+?)$");
        Matcher m = p.matcher(task);
        if (!m.matches()) {
            throw new FoodInputException("hey that's not a right format...");
        }
        super(m.group("name"), task);
    }

    @Override
    public String toString() {
        return String.format("%s %s",
                SYMBOL,
                super.toString());
    }
}
