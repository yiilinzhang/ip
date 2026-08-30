package food.task;

import food.exception.FoodInputException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The tasks the user is keeping, together with the operations that change them.
 *
 * <p>Pulled out of Foodbot so that exactly one class knows how many tasks exist. That makes this
 * the only place that can answer "is 99 a real task number?", so the bounds check lives here
 * instead of being repeated by every caller that wants to reach into the list.
 */
public class TaskList {
    private final List<Task> tasks;

    /** Creates an empty list, e.g. when there is nothing saved yet. */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Creates a list holding tasks that were loaded elsewhere, e.g. read back from the save file.
     *
     * @param tasks the tasks to start with; copied, so later changes to the caller's list do not
     *              silently change ours
     */
    public TaskList(List<Task> tasks) {
        this.tasks = new ArrayList<>(tasks);
    }

    public int size() {
        return this.tasks.size();
    }

    public void add(Task task) {
        this.tasks.add(task);
    }

    /**
     * Removes and returns the task at the given index.
     *
     * @param index  0-based position of the task
     * @param action the command word, used only to word the error message
     * @throws FoodInputException if no task sits at that index
     */
    public Task delete(int index, String action) throws FoodInputException {
        this.checkIndex(index, action);
        return this.tasks.remove(index);
    }

    /**
     * Returns the task at the given index, leaving it in the list.
     *
     * @param index  0-based position of the task
     * @param action the command word, used only to word the error message
     * @throws FoodInputException if no task sits at that index
     */
    public Task get(int index, String action) throws FoodInputException {
        this.checkIndex(index, action);
        return this.tasks.get(index);
    }

    /**
     * Returns a read-only view of the tasks, for code that only needs to walk over them, such as
     * printing the list or writing it to disk. Read-only so that handing the list out cannot
     * become a second way of modifying it.
     */
    public List<Task> asList() {
        return Collections.unmodifiableList(this.tasks);
    }

    /**
     * The bounds are tested with an if rather than by catching IndexOutOfBoundsException:
     * exceptions are for the unexpected, and a mistyped task number is entirely expected.
     */
    private void checkIndex(int index, String action) throws FoodInputException {
        if (index < 0 || index >= this.tasks.size()) {
            throw new FoodInputException(
                    String.format("hey that's not a valid index to %s", action));
        }
    }
}
