public class Task {
    private String title;
    private boolean completed = false;

    public Task(String title) {
        this.title = title;
    }

    public void markComplete() {
        this.completed = true;
    }

    public void markIncomplete() {
        this.completed = false;
    }

    @Override
    public String toString() {
        String status = "";
        if (this.completed == true) {
            status = "X";
        }
        return String.format("[%s] %s", status, this.title);
    }
}
