package food;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import food.exception.FoodStorageException;
import food.task.Task;

/**
 * Stores task history in data/tasks.md, creating the file if it does not exist.
 *
 * <p>This class is the boundary between the chatbot and the file system. Every IOException is
 * translated into a FoodStorageException here, so the rest of the program never needs to know
 * that tasks happen to live in a file.
 */
class Storage {
    private final Path storagePath;

    public Storage() throws FoodStorageException {
        this.storagePath = Path.of("data", "tasks.md");
        try {
            Files.createDirectories(this.storagePath.getParent());
            // createFile throws if the file is already there, so only create it the first time.
            if (!Files.exists(this.storagePath)) {
                Files.createFile(this.storagePath);
            }
        } catch (IOException e) {
            throw new FoodStorageException("Error creating save file", e);
        }
    }

    /**
     * Writes the whole task list to disk, replacing whatever was there before.
     *
     * @param taskList the tasks to save.
     * @throws FoodStorageException if the file could not be written.
     */
    public void save(List<Task> taskList) throws FoodStorageException {
        try {
            List<String> saveList = taskList.stream().map(Task::toSaveFormat).toList();
            Files.write(storagePath, saveList);
        } catch (IOException e) {
            throw new FoodStorageException("Error saving to storage", e);
        }
    }

    /**
     * Reads the save file back into tasks.
     *
     * @return the saved tasks, in the order they were written.
     * @throws FoodStorageException if the file could not be read or contains a bad line.
     */
    public List<Task> retrieveSaved() throws FoodStorageException {
        List<String> savedLines;
        try {
            savedLines = Files.readAllLines(this.storagePath);
        } catch (IOException e) {
            throw new FoodStorageException("Error retrieving saved files", e);
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
