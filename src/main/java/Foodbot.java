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


    public Foodbot() {
        System.out.println(String.format("%s\n%s", Foodbot.bannerMessage, Foodbot.greetMessage));
    }

    public boolean addInput(String input) throws FoodException {
        // When user exits chatbot
        if (input.equals("LET ME OUT!")) {
            System.out.println(Foodbot.exitMessage);
            return false;
        }

        // List out all tasks
        if (input.equals("list")) {
           listTasks();
            return true;
        }

        String[] parts = input.trim().split(" ");

        // Check if user want to mark tasks as complete/ incomplete
        if (parts.length == 2 && parts[0].equals("mark")) {
            this.markComplete(parts[1]);
            return true;
        }
        if (parts.length == 2 && parts[0].equals("unmark")) {
            this.markIncomplete(parts[1]);
            return true;
        }

        // Add tasks
        if (parts[0].equals("todo") || parts[0].equals("deadline") || parts[0].equals("event")) {
            this.addTask(input, parts);
            return true;
        }

        // Delete tasks
        if (parts.length == 2 && parts[0].equals("delete")) {
            deleteTask(parts[1]);
            return true;
        }

        // None of the above
        throw new FoodException("OOPS!!! I'm sorry, but I don't know what that means :-(");
    }

    public void addTask(String input, String[] parts) throws FoodException{
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
            taskNo = Integer.parseInt(taskNoStr);
        } catch (NumberFormatException e) {
            throw new FoodException("mark has to be followed by an integer");
        }
        if (taskNo >= this.list.size() || taskNo < 0) {
            throw new FoodException("hey that's not a valid index to mark complete");
        }
        Task task  = this.list.get(taskNo - 1);
        task.markComplete();
        System.out.println(String.format("%s\n%s", Foodbot.completeMessage, task));
    }

    public void markIncomplete(String taskNoStr) throws FoodException{
        int taskNo;
        try {
            taskNo = Integer.parseInt(taskNoStr);
        } catch (NumberFormatException e) {
            throw new FoodException("unmark has to be followed by an integer");
        }
        if (taskNo >= this.list.size() || taskNo < 0) {
            throw new FoodException("hey that's not a valid index to mark incomplete");
        }
        Task task  = this.list.get(taskNo - 1);
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
