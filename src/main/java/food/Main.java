package food;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import food.exception.FoodStorageException;

/** Entry point of the JavaFX GUI, which loads the chat window from FXML. */
public class Main extends Application {
    @Override
    public void start(Stage stage) {
        try {
            Foodbot foodbot = new Foodbot(new Ui());
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/MainWindow.fxml"));
            AnchorPane root = fxmlLoader.load();
            fxmlLoader.<MainWindow>getController().setFoodbot(foodbot);

            stage.setTitle("Foodbot");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException | FoodStorageException e) {
            e.printStackTrace();
        }
    }
}
