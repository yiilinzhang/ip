import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/*
 * Stores tasks history in data/tasks.md. Creates new file if it does not exist.
 */
class Storage {
    private final Path storagePath;

    public Storage() throws FoodException{
        this.storagePath = Path.of("data", "tasks.md");
        try {
            Files.createDirectories(this.storagePath.getParent());
            // createFile throws if the file is already there, so only create it the first time.
            if (!Files.exists(this.storagePath)) {
                Files.createFile(this.storagePath);
            }
            } catch (IOException e) {
                throw new FoodException("Error creating save file");
        }
    }

    /*
     * Add a task to storage by appending the string at the EOF
     */
    public void save(List<Task> taskList) throws FoodException{
        try {
            List<String> saveList = taskList.stream().map(Task::toSaveFormat).toList();
            Files.write(storagePath,saveList);
        } catch (IOException e) {
            throw new FoodException("Error saving to storage");
        }
    }

    /*
     * Converts saved strings in storage to a List<Task>
     */
    public List<Task> retrieveSaved() throws FoodException {
        List<String> savedLines;
        try {
            savedLines = Files.readAllLines(this.storagePath);
        } catch (IOException e) {
            throw new FoodException("Error retrieving saved files");
        }
        // A plain loop rather than a stream, because fromSaveFormat throws a checked
        // exception and lambdas cannot propagate those.
        List<Task> tasks = new ArrayList<>();
        for (String line : savedLines) {
            tasks.add(Task.fromSaveFormat(line));
        }
        return tasks;
    }


}