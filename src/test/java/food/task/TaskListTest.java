package food.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.Test;
import food.exception.FoodInputException;

/**
 * Tests {@link TaskList#find(String)}.
 *
 * <p>The search is worth testing on its own because it is the one part of the feature with real
 * logic: matching is case-insensitive and looks only at the description, never at the dates.
 */
public class TaskListTest {

    /** Builds a list holding one matching todo, one matching deadline, and one non-match. */
    private TaskList buildSampleList() throws FoodInputException {
        TaskList tasks = new TaskList();
        tasks.add(new Todo("todo read book"));
        tasks.add(new Deadline("deadline return book /by 2026-09-07"));
        tasks.add(new Todo("todo buy milk"));
        return tasks;
    }

    @Test
    public void find_keywordInSeveralTasks_allMatchesReturnedInListOrder() throws FoodInputException {
        List<Task> matches = this.buildSampleList().find("book");

        assertEquals(2, matches.size());
        assertEquals("[T] [] read book", matches.get(0).toString());
        assertEquals("[D] [] return book (by: 7/9/2026)", matches.get(1).toString());
    }

    @Test
    public void find_keywordDifferentCase_matchesAnyway() throws FoodInputException {
        assertEquals(2, this.buildSampleList().find("BOOK").size());
    }

    @Test
    public void find_keywordIsPartOfAWord_matchesAsSubstring() throws FoodInputException {
        // "boo" is not a whole word in "read book", but a substring search still finds it.
        assertEquals(2, this.buildSampleList().find("boo").size());
    }

    @Test
    public void find_keywordAbsent_emptyListReturned() throws FoodInputException {
        assertTrue(this.buildSampleList().find("homework").isEmpty());
    }

    @Test
    public void find_keywordMatchesOnlyADate_emptyListReturned() throws FoodInputException {
        // Only descriptions are searched, so the deadline's 2026 date is not a match.
        assertTrue(this.buildSampleList().find("2026").isEmpty());
    }

    @Test
    public void find_emptyList_emptyListReturned() {
        assertTrue(new TaskList().find("book").isEmpty());
    }
}
