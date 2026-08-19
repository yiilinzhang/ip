public class Task {
    private String title;
    private boolean completed = false;

    public Task(String title) throws FoodException{
        if (title.trim().equals("")) {
            throw new FoodException("not sure why you want an empty task");
        }
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
        if (this.completed) {
            status = "X";
        }
        return String.format("[%s] %s", status, this.title);
    }
}
