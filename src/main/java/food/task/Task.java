package food.task;

import food.exception.FoodInputException;
import food.exception.FoodStorageException;

/**
 * A single item on the user's list. Subclasses add the details specific to each kind of task.
 */
public class Task {
    private String title;
    private boolean isCompleted = false;
    private final String input;

    public Task(String title, String input) throws FoodInputException {
        if (title.isBlank()) {
            throw new FoodInputException("not sure why you want an empty task");
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

    /**
     * Rebuilds a Task from a line written by {@link #toSaveFormat}, e.g. "1 | todo read book".
     *
     * <p>The original user input was saved, so the normal constructors can parse it again.
     * A line those constructors reject means the save file is damaged, not that the user typed
     * something wrong, so the FoodInputException they throw is translated into a
     * FoodStorageException before it leaves this method.
     *
     * @param line one line of the save file.
     * @return the reconstructed task, with its completed flag restored.
     * @throws FoodStorageException if the line is not in the expected format.
     */
    public static Task fromSaveFormat(String line) throws FoodStorageException {
        // Limit of 2 so a " | " inside the task description is not split away.
        String[] parts = line.split(" \\| ", 2);
        if (parts.length != 2) {
            throw new FoodStorageException("Save file is corrupted: " + line);
        }
        String input = parts[1];
        String command = input.split(" ")[0];

        Task task;
        try {
            task = switch (command) {
                case "todo" -> new Todo(input);
                case "deadline" -> new Deadline(input);
                case "event" -> new Event(input);
                default -> throw new FoodStorageException("Save file is corrupted: " + line);
            };
        } catch (FoodInputException e) {
            throw new FoodStorageException("Save file is corrupted: " + line, e);
        }

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
