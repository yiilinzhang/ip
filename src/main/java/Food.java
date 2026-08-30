import java.util.Scanner;

public class Food {
    public static void main(String[] args) {
        Scanner userInput = new Scanner(System.in);

        Foodbot food;
        try {
            food = new Foodbot();
        } catch (FoodStorageException e) {
            // Without a usable save file there is nothing to chat about, so report and stop.
            System.out.println(e.getMessage());
            return;
        }

        while (userInput.hasNextLine()) {
            String input = userInput.nextLine();
            try {
                if (!food.addInput(input)) {
                    break;
                }
            } catch (FoodInputException e) {
                System.out.println(e.getMessage());
            } catch (FoodStorageException e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
                break;
            }
        }
    }
}
