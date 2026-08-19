import java.util.Scanner;

public class Food {
    public static void main(String[] args) {
        Scanner userIput = new Scanner(System.in);

        Chatbot food = new Chatbot();
        while (true) {
            String input = userIput.nextLine();
            boolean cont = food.addInput(input);
            if (!cont) {
                break;
            }
        }
    }
}
