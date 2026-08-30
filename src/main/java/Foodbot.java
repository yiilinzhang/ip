import java.util.ArrayList;
import java.util.List;

public class Foodbot {
    private final List<Task> list;
    private final Storage storage;
    private static final String completeMessage = "Nice! I've marked this task as done";
    private static final String incompleteMessage = "OK, I've marked this task as not done yet:";
    private static final String listTaskMessage = "Here are the tasks in your list:";
    private static final String exitMessage = "Bye. Hope to see you soon!";
    private static final String addTaskMessage = "Got it. I've added this task:";
    private static final String greetMessage = "Hello! I am Food.\nWhat can I do for you?";
    private static final String bannerMessage = "  _______  _______  _______  ______  \n"
            + " |   ____||   __  ||   __  ||      \\ \n"
            + " |  |___  |  |  | ||  |  | ||  ---  |\n"
            + " |   ___| |  |  | ||  |  | ||  |  | |\n"
            + " |  |     |  |__| ||  |__| ||  ---  |\n"
            + " |__|     |_______||_______||______/ \n";


    public Foodbot() throws FoodStorageException {
        System.out.println(String.format("%s\n%s", Foodbot.bannerMessage, Foodbot.greetMessage));
        this.storage = new Storage();
        this.list = this.storage.retrieveSaved();
    }

    /**
     * Handles one line of user input.
     *
     * @param input the raw line the user typed
     * @return false if the user asked to exit, true to keep the chatbot running
     * @throws FoodInputException   if the command is unknown or badly formed
     * @throws FoodStorageException if the resulting task list could not be saved
     */
    public boolean addInput(String input) throws FoodInputException, FoodStorageException {
        // Checked before splitting, since the exit phrase is several words long.
        if (input.equals("LET ME OUT!")) {
            System.out.println(Foodbot.exitMessage);
            return false;
        }

        String[] parts = input.trim().split(" ");
        String command = parts[0];

        // Arrow labels: each case runs only its own branch, so no break/fall-through.
        switch (command) {
            case "list" -> this.listTasks();
            case "mark" -> this.markComplete(getArgument(parts));
            case "unmark" -> this.markIncomplete(getArgument(parts));
            case "delete" -> this.deleteTask(getArgument(parts));
            case "todo", "deadline", "event" -> this.addTask(input);
            default -> throw new FoodInputException(
                    "OOPS!!! I'm sorry, but I don't know what that means :-(");
        }

        // "list" only reads, so there is nothing new to write for it.
        if (!command.equals("list")) {
            this.storage.save(this.list);
        }
        return true;
    }

    /**
     * Returns the single argument that follows a command word, e.g. the "2" in "mark 2".
     *
     * @param parts the input split on spaces
     * @return the argument after the command word
     * @throws FoodInputException if the command was not given exactly one argument
     */
    private static String getArgument(String[] parts) throws FoodInputException {
        if (parts.length != 2) {
            throw new FoodInputException(String.format("%s has to be followed by exactly one task number",
                    parts[0]));
        }
        return parts[1];
    }

    /**
     * Converts a task number typed by the user into a valid index into the list.
     *
     * <p>Shared by mark, unmark and delete, which previously repeated this check verbatim.
     * The bounds are tested with an if rather than by catching IndexOutOfBoundsException:
     * exceptions are for the unexpected, and a mistyped number is entirely expected.
     *
     * @param taskNo  the argument the user typed, e.g. "2"
     * @param command the command name, used only to build a helpful message
     * @return the matching 0-based index into the task list
     * @throws FoodInputException if it is not a number, or not a task that exists
     */
    private int parseTaskIndex(String taskNo, String command) throws FoodInputException {
        int index;
        try {
            index = Integer.parseInt(taskNo) - 1;
        } catch (NumberFormatException e) {
            throw new FoodInputException(
                    String.format("%s has to be followed by a number", command), e);
        }
        if (index >= this.list.size() || index < 0) {
            throw new FoodInputException(
                    String.format("hey that's not a valid index to %s", command));
        }
        return index;
    }

    public void addTask(String input) throws FoodInputException {
        String[] parts = input.trim().split(" ");
        // A switch expression rather than three ifs, so addedTask can never be left null:
        // an unrecognised command fails here instead of surfacing as a NullPointerException below.
        final Task addedTask = switch (parts[0]) {
            case "todo" -> new Todo(input);
            case "deadline" -> new Deadline(input);
            case "event" -> new Event(input);
            default -> throw new FoodInputException(
                    String.format("%s is not a kind of task I can add", parts[0]));
        };
        this.list.add(addedTask);
        System.out.println(String.format("%s\n%s\nNow you have %d tasks in the list.",
                Foodbot.addTaskMessage, addedTask, this.list.size()));
    }

    public void deleteTask(String taskNo) throws FoodInputException {
        int index = this.parseTaskIndex(taskNo, "delete");
        Task task = this.list.remove(index);
        System.out.println(String.format("Noted. I've removed this task: \n%s\nNow you have %d tasks in the list.",
                task,
                this.list.size()));
    }

    public void markComplete(String taskNo) throws FoodInputException {
        Task task = this.list.get(this.parseTaskIndex(taskNo, "mark"));
        task.markComplete();
        System.out.println(String.format("%s\n%s", Foodbot.completeMessage, task));
    }

    public void markIncomplete(String taskNo) throws FoodInputException {
        Task task = this.list.get(this.parseTaskIndex(taskNo, "unmark"));
        task.markIncomplete();
        System.out.println(String.format("%s\n%s", Foodbot.incompleteMessage, task));
    }

    public void listTasks() {
        System.out.println(Foodbot.listTaskMessage);
        for (int i = 0; i < list.size(); i ++) {
            System.out.println(String.format("%d. %s", i + 1, this.list.get(i)));
        }
    }
}
