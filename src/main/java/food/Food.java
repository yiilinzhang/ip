package food;

import food.exception.FoodInputException;
import food.exception.FoodStorageException;

public class Food {
    public static void main(String[] args) {
        // One Ui for the whole program, shared with Foodbot, so System.in is read in one place.
        Ui ui = new Ui();

        Foodbot food;
        try {
            food = new Foodbot(ui);
        } catch (FoodStorageException e) {
            // Without a usable save file there is nothing to chat about, so report and stop.
            ui.showError(e.getMessage());
            return;
        }

        while (ui.hasNextCommand()) {
            String input = ui.readCommand();
            try {
                if (!food.addInput(input)) {
                    break;
                }
            } catch (FoodInputException e) {
                ui.showError(e.getMessage());
            } catch (FoodStorageException e) {
                ui.showLoadingError(e);
                break;
            }
        }
    }
}
