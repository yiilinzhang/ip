public class Chatbot {
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
        }
        System.out.println(input);
        return true;
    }
}
