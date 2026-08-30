import java.util.ArrayList;
import java.util.List;

public class Foodbot {
    private List<Task> list = new ArrayList<>();
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


    public Foodbot() throws FoodException{
        System.out.println(String.format("%s\n%s", Foodbot.bannerMessage, Foodbot.greetMessage));
        Storage save = new Storage();
        this.list = save.retrieveSaved();
    }

    /**
     * Handles one line of user input.
     *
     * @param input the raw line the user typed
     * @return false if the user asked to exit, true to keep the chatbot running
     * @throws FoodException if the command is unknown or badly formed
     */
    public boolean addInput(String input) throws FoodException {
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
            default -> throw new FoodException("OOPS!!! I'm sorry, but I don't know what that means :-(");
        }

        Storage taskStorage = new Storage();
        taskStorage.save(this.list);
        return true;
    }

    /**
     * Returns the single argument that follows a command word, e.g. the "2" in "mark 2".
     *
     * @param parts the input split on spaces
     * @return the argument after the command word
     * @throws FoodException if the command was not given exactly one argument
     */
    private static String getArgument(String[] parts) throws FoodException {
        if (parts.length != 2) {
            throw new FoodException(String.format("%s has to be followed by exactly one task number",
                    parts[0]));
        }
        return parts[1];
    }

    public void addTask(String input) throws FoodException{
        String[] parts = input.trim().split(" ");
        Task addedTask = null;
        if (parts[0].equals("todo")) {
            addedTask = new Todo(input);
        }
        if (parts[0].equals("deadline")) {
            addedTask = new Deadline(input);
        }
        if (parts[0].equals("event")) {
            addedTask = new Event(input);
        }
        this.list.add(addedTask);
        System.out.println(String.format("%s\n%s\nNow you have %d tasks in the list.",
                Foodbot.addTaskMessage, addedTask, this.list.size()));
    }

    public void deleteTask(String taskNo) throws FoodException{
        int index;
        try {
            index = Integer.parseInt(taskNo) - 1;
        } catch (NumberFormatException e) {
            throw new FoodException("delete has to be followed by a number");
        }
        if (index >= this.list.size() || index < 0) {
            throw new FoodException("hey that's not a valid index to delete");
        }
        Task task = this.list.get(index);
        this.list.remove(task);
        System.out.println(String.format("Noted. I've removed this task: \n%s\nNow you have %d tasks in the list.",
                task,
                this.list.size()));
    }

    public void markComplete(String taskNoStr) throws FoodException{
        int taskNo;
        try {
            taskNo = Integer.parseInt(taskNoStr) - 1;
        } catch (NumberFormatException e) {
            throw new FoodException("mark has to be followed by an integer");
        }
        if (taskNo >= this.list.size() || taskNo < 0) {
            throw new FoodException("hey that's not a valid index to mark complete");
        }
        Task task  = this.list.get(taskNo);
        task.markComplete();
        System.out.println(String.format("%s\n%s", Foodbot.completeMessage, task));
    }

    public void markIncomplete(String taskNoStr) throws FoodException{
        int taskNo;
        try {
            taskNo = Integer.parseInt(taskNoStr) - 1;
        } catch (NumberFormatException e) {
            throw new FoodException("unmark has to be followed by an integer");
        }
        if (taskNo >= this.list.size() || taskNo < 0) {
            throw new FoodException("hey that's not a valid index to mark incomplete");
        }
        Task task  = this.list.get(taskNo);
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
