import java.util.Scanner;

public class Food {
    public static void main(String[] args) {
        Scanner userInput = new Scanner(System.in);

        Foodbot food = new Foodbot();
        while (true) {
            String input = userInput.nextLine();
            boolean cont = true;
            try {
                cont = food.addInput(input);
            } catch (FoodException e) {
                System.out.println(e.getMessage());
            }
            if (!cont) {
                break;
            }
        }
    }
}
