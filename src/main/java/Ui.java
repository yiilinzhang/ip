import java.util.List;
import java.util.Scanner;

/**
 * Deals with all interaction with the user: reading commands typed at the console, and printing
 * everything the user sees.
 *
 * <p>Keeping this in one class means the rest of the program never calls System.out directly.
 * Foodbot can then decide <em>what</em> happened without also deciding how it is worded, so
 * changing the wording, or later swapping the console for a GUI, touches only this file.
 */
public class Ui {
    private static final String COMPLETE_MESSAGE = "Nice! I've marked this task as done";
    private static final String INCOMPLETE_MESSAGE = "OK, I've marked this task as not done yet:";
    private static final String LIST_TASK_MESSAGE = "Here are the tasks in your list:";
    private static final String EXIT_MESSAGE = "Bye. Hope to see you soon!";
    private static final String ADD_TASK_MESSAGE = "Got it. I've added this task:";
    private static final String GREET_MESSAGE = "Hello! I am Food.\nWhat can I do for you?";
    private static final String BANNER_MESSAGE = "  _______  _______  _______  ______  \n"
            + " |   ____||   __  ||   __  ||      \\ \n"
            + " |  |___  |  |  | ||  |  | ||  ---  |\n"
            + " |   ___| |  |  | ||  |  | ||  |  | |\n"
            + " |  |     |  |__| ||  |__| ||  ---  |\n"
            + " |__|     |_______||_______||______/ \n";

    /** The single reader of System.in. Two Scanners over one stream would lose buffered input. */
    private final Scanner scanner = new Scanner(System.in);

    /**
     * Returns whether there is another command to read.
     *
     * <p>Callers must check this before readCommand: when input runs out (Ctrl-D, or piped input
     * that ends without the exit phrase) reading anyway throws NoSuchElementException.
     */
    public boolean hasNextCommand() {
        return this.scanner.hasNextLine();
    }

    public String readCommand() {
        return this.scanner.nextLine();
    }

    public void showWelcome() {

        System.out.println(String.format("%s\n%s", BANNER_MESSAGE, GREET_MESSAGE));
    }

    public void showGoodbye() {

        System.out.println(EXIT_MESSAGE);
    }

    /**
     * @param task  the task just added
     * @param total how many tasks the list now holds
     */
    public void showTaskAdded(Task task, int total) {
        System.out.println(String.format("%s\n%s\nNow you have %d tasks in the list.",
                ADD_TASK_MESSAGE, task, total));
    }

    /**
     * @param task      the task just removed
     * @param remaining how many tasks are left
     */
    public void showTaskDeleted(Task task, int remaining) {
        System.out.println(String.format("Noted. I've removed this task: \n%s\nNow you have %d tasks in the list.",
                task,
                remaining));
    }

    public void showMarked(Task task) {

        System.out.println(String.format("%s\n%s", COMPLETE_MESSAGE, task));
    }

    public void showUnmarked(Task task) {

        System.out.println(String.format("%s\n%s", INCOMPLETE_MESSAGE, task));
    }

    public void showTaskList(List<Task> tasks) {
        System.out.println(LIST_TASK_MESSAGE);
        for (int i = 0; i < tasks.size(); i ++) {
            System.out.println(String.format("%d. %s", i + 1, tasks.get(i)));
        }
    }

    /** Reports a recoverable problem, such as a command the user mistyped. */
    public void showError(String message) {

        System.out.println(message);
    }

    /**
     * Reports a save-file failure, which is not the user's fault and ends the session.
     * The stack trace is printed because the chained cause is what makes it diagnosable.
     */
    public void showLoadingError(FoodStorageException e) {
        System.out.println(e.getMessage());
        e.printStackTrace();
    }
}
