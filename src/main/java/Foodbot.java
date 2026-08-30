/**
 * The chatbot itself. It takes one line of user input at a time and drives the four parts that do
 * the actual work: the {@link Parser} that works out what the line means, the {@link TaskList}
 * that holds the tasks, the {@link Ui} that talks to the user, and the {@link Storage} that
 * remembers the tasks between runs.
 */
public class Foodbot {
    private final TaskList tasks;
    private final Storage storage;
    private final Ui ui;

    public Foodbot(Ui ui) throws FoodStorageException {
        this.ui = ui;
        this.ui.showWelcome();
        this.storage = new Storage();
        this.tasks = new TaskList(this.storage.retrieveSaved());
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
        Parser.Command command = Parser.parse(input);

        switch (command.type()) {
            case EXIT -> {
                this.ui.showGoodbye();
                return false;
            }
            case LIST -> this.listTasks();
            case MARK -> this.markComplete(command.index());
            case UNMARK -> this.markIncomplete(command.index());
            case DELETE -> this.deleteTask(command.index());
            case ADD -> this.addTask(command.rawInput());
        }

        // "list" only reads, so there is nothing new to write for it.
        if (command.type() != Parser.CommandType.LIST) {
            this.storage.save(this.tasks.asList());
        }
        return true;
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
        this.tasks.add(addedTask);
        this.ui.showTaskAdded(addedTask, this.tasks.size());
    }

    public void deleteTask(int index) throws FoodInputException {
        Task task = this.tasks.delete(index, "delete");
        this.ui.showTaskDeleted(task, this.tasks.size());
    }

    public void markComplete(int index) throws FoodInputException {
        Task task = this.tasks.get(index, "mark");
        task.markComplete();
        this.ui.showMarked(task);
    }

    public void markIncomplete(int index) throws FoodInputException {
        Task task = this.tasks.get(index, "unmark");
        task.markIncomplete();
        this.ui.showUnmarked(task);
    }

    public void listTasks() {
        this.ui.showTaskList(this.tasks.asList());
    }
}
