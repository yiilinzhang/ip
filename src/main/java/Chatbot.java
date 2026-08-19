import java.util.ArrayList;
import java.util.List;

public class Chatbot {
    private List<String> list = new ArrayList<>();

    public Chatbot() {
        String banner = "  _______  _______  _______  ______  \n"
                + " |   ____||   __  ||   __  ||      \\ \n"
                + " |  |___  |  |  | ||  |  | ||  ---  |\n"
                + " |   ___| |  |  | ||  |  | ||  |  | |\n"
                + " |  |     |  |__| ||  |__| ||  ---  |\n"
                + " |__|     |_______||_______||______/ \n";
        String greet = "Hello! I am Food.\nWhat can I do for you?";
        System.out.println(banner);
        System.out.println(greet);
    }

    public boolean addInput(String input) {
        String exit = "Bye. Hope to see you soon!";
        if (input.equals("LET ME OUT!")) {
            System.out.println(exit);
            return false;
        }
        if (input.equals("list")) {
            for (int i = 0; i < list.size(); i ++) {
                System.out.println(i + 1 + "." + list.get(i));
            }
        } else {
            list.add(input);
            System.out.println("added: " + input);
        }
        return true;
    }
}
