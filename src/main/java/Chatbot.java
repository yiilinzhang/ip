import java.util.ArrayList;
import java.util.List;

public class Chatbot {
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


    public Chatbot() {
        System.out.println(String.format("%s\n%s", Chatbot.bannerMessage, Chatbot.greetMessage));
    }

    public boolean addInput(String input) {
        // When user exits chatbot
        if (input.equals("LET ME OUT!")) {
            System.out.println(Chatbot.exitMessage);
            return false;
        }

        // List out all tasks
        if (input.equals("list")) {
            System.out.println(Chatbot.listTaskMessage);
            for (int i = 0; i < list.size(); i ++) {
                System.out.println(String.format("%d. %s", i + 1, this.list.get(i)));
            }
            return true;
        }

        // Check if user want to mark tasks as complete/ incomplete
        String[] parts = input.trim().split(" ");
        if (parts.length == 2 && parts[0].equals("mark")) {
            // TODO: Add error catching
            int taskNo = Integer.parseInt(parts[1]);
            Task task  = this.list.get(taskNo - 1);
            task.markComplete();
            System.out.println(String.format("%s\n%s", Chatbot.completeMessage, task));
            return true;
        }
        if (parts.length == 2 && parts[0].equals("unmark")) {
            // TODO: Add error catching
            int taskNo = Integer.parseInt(parts[1]);
            Task task  = this.list.get(taskNo - 1);
            task.markIncomplete();
            System.out.println(String.format("%s\n%s", Chatbot.incompleteMessage, task));
            return true;
        }

        Task addedTask = null;
        if (parts[0].equals("todo")) {
            addedTask = new ToDos(input);
        }
        if (parts[0].equals("deadline")) {
            addedTask = new Deadlines(input);
        }
        if (parts[0].equals("event")) {
            addedTask = new Events(input);
        }
        list.add(addedTask);
        System.out.println(String.format("%s\n%s\nNow you have %d tasks in the list.",
                Chatbot.addTaskMessage, addedTask, this.list.size()));
        return true;
    }
}
