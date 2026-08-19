import java.util.Scanner;

public class Food {
    public static void main(String[] args) {
        Scanner userIput = new Scanner(System.in);

        String banner = "  _______  _______  _______  ______  \n"
                + " |   ____||   __  ||   __  ||      \\ \n"
                + " |  |___  |  |  | ||  |  | ||  ---  |\n"
                + " |   ___| |  |  | ||  |  | ||  |  | |\n"
                + " |  |     |  |__| ||  |__| ||  ---  |\n"
                + " |__|     |_______||_______||______/ \n";
        String greet = "Hello! I am Food.\nWhat can I do for you?";
        String exit = "Bye. Hope to see you soon!";
        System.out.println(banner);
        System.out.println(greet);

        while (true) {
            String input = userIput.nextLine();
            if (input.equals("LET ME OUT!")) {
                System.out.println(exit);
                break;
            }
            System.out.println(input);
        }
    }
}
