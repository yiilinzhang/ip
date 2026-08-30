package food;

import java.util.List;
import java.util.Scanner;
import food.exception.FoodStorageException;
import food.task.Task;

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
    private static final String FIND_TASK_MESSAGE = "Here are the matching tasks in your list:";
    private static final String NO_MATCH_MESSAGE = "No matching tasks in your list.";
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
     *
     * @return true if a line is waiting to be read, false once input has run out
     */
    public boolean hasNextCommand() {
        return this.scanner.hasNextLine();
    }

    /**
     * Reads the next line the user typed.
     *
     * @return the raw line, untrimmed, for the {@link Parser} to make sense of
     */
    public String readCommand() {
        return this.scanner.nextLine();
    }

    /** Prints the banner and greeting shown once when the program starts. */
    public void showWelcome() {
        System.out.println(String.format("%s\n%s", BANNER_MESSAGE, GREET_MESSAGE));
    }

    /** Prints the parting message shown when the user asks to exit. */
    public void showGoodbye() {
        System.out.println(EXIT_MESSAGE);
    }

    /**
     * Confirms that a task was added, and says how many there are now.
     *
     * @param task  the task just added
     * @param total how many tasks the list now holds
     */
    public void showTaskAdded(Task task, int total) {
        System.out.println(String.format("%s\n%s\nNow you have %d tasks in the list.",
                ADD_TASK_MESSAGE, task, total));
    }

    /**
     * Confirms that a task was removed, and says how many are left.
     *
     * @param task      the task just removed
     * @param remaining how many tasks are left
     */
    public void showTaskDeleted(Task task, int remaining) {
        System.out.println(String.format(
                "Noted. I've removed this task: \n%s\nNow you have %d tasks in the list.",
                task,
                remaining));
    }

    /**
     * Confirms that a task is now done, showing it in its new state.
     *
     * @param task the task just marked
     */
    public void showMarked(Task task) {
        System.out.println(String.format("%s\n%s", COMPLETE_MESSAGE, task));
    }

    /**
     * Confirms that a task is now not done, showing it in its new state.
     *
     * @param task the task just unmarked
     */
    public void showUnmarked(Task task) {
        System.out.println(String.format("%s\n%s", INCOMPLETE_MESSAGE, task));
    }

    /**
     * Prints the whole list, numbered from 1.
     *
     * <p>Numbering starts at 1 because that is how the user refers to tasks in commands such as
     * "mark 2"; the {@link Parser} converts that back to a 0-based index.
     *
     * @param tasks the tasks to show, in list order
     */
    public void showTaskList(List<Task> tasks) {
        System.out.println(LIST_TASK_MESSAGE);
        this.showNumbered(tasks);
    }

    /**
     * Shows the tasks matching a search, or says so when nothing matched.
     *
     * <p>An empty result is reported explicitly rather than as an empty list, so the user can tell
     * "nothing matched" apart from the program having done nothing.
     *
     * @param tasks the matching tasks, in list order.
     */
    public void showFoundTasks(List<Task> tasks) {
        if (tasks.isEmpty()) {
            System.out.println(NO_MATCH_MESSAGE);
            return;
        }
        System.out.println(FIND_TASK_MESSAGE);
        this.showNumbered(tasks);
    }

    /**
     * Prints tasks numbered from 1, the way the user refers to them in commands such as "mark 2".
     *
     * <p>Note that after a search these numbers count the matches, not the positions of those
     * tasks in the full list.
     *
     * @param tasks the tasks to print, in the order they should appear.
     */
    private void showNumbered(List<Task> tasks) {
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println(String.format("%d. %s", i + 1, tasks.get(i)));
        }
    }

    /**
     * Reports a recoverable problem, such as a command the user mistyped.
     *
     * @param message the explanation to show; the program carries on afterwards
     */
    public void showError(String message) {
        System.out.println(message);
    }

    /**
     * Reports a save-file failure, which is not the user's fault and ends the session.
     * The stack trace is printed because the chained cause is what makes it diagnosable.
     *
     * @param e the storage failure to report
     */
    public void showLoadingError(FoodStorageException e) {
        System.out.println(e.getMessage());
        e.printStackTrace();
    }
}
