public class Task {
    private String title;
    private boolean isCompleted = false;
    private final String input;

    public Task(String title, String input) throws FoodException{
        if (title.trim().equals("")) {
            throw new FoodException("not sure why you want an empty task");
        }
        this.input = input;
        this.title = title;
    }

    /*
     * Converts input to storage format by adding 0 / 1 for incomplete and completed respectively
     */
    public String toSaveFormat() {
        return String.format("%d | %s", this.isCompleted ? 1 : 0, this.input);
    }

    /*
     * Rebuilds a Task from a line written by toSaveFormat, e.g. "1 | todo read book".
     * The original user input was saved, so the normal constructors can parse it again.
     */
    public static Task fromSaveFormat(String line) throws FoodException {
        // Limit of 2 so a " | " inside the task description is not split away.
        String[] parts = line.split(" \\| ", 2);
        if (parts.length != 2) {
            throw new FoodException("Save file is corrupted: " + line);
        }
        String input = parts[1];
        String command = input.split(" ")[0];
        Task task = switch (command) {
            case "todo" -> new Todo(input);
            case "deadline" -> new Deadline(input);
            case "event" -> new Event(input);
            default -> throw new FoodException("Save file is corrupted: " + line);
        };
        if (parts[0].equals("1")) {
            task.markComplete();
        }
        return task;
    }

    public void markComplete() {
        this.isCompleted = true;
    }

    public void markIncomplete() {
        this.isCompleted = false;
    }

    @Override
    public String toString() {
        String status = "";
        if (this.isCompleted) {
            status = "X";
        }
        return String.format("[%s] %s", status, this.title);
    }
}
