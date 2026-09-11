package food;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

/** Controller for the chat window, wiring the input field and button to Foodbot's replies. */
public class MainWindow extends AnchorPane {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;

    private Foodbot foodbot;

    private final Image userImage = new Image(this.getClass().getResourceAsStream("/images/DaUser.png"));
    private final Image foodbotImage = new Image(this.getClass().getResourceAsStream("/images/DaFoodbot.png"));

    /** Keeps the scroll pane pinned to the newest message as the conversation grows. */
    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
    }

    /** Injects the Foodbot instance this window sends user input to. */
    public void setFoodbot(Foodbot foodbot) {
        this.foodbot = foodbot;
    }

    /** Appends the user's message and Foodbot's reply, then clears the input field. */
    @FXML
    private void handleUserInput() {
        String userText = userInput.getText();
        String foodbotText = foodbot.getResponse(userText);
        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(userText, userImage),
                DialogBox.getFoodbotDialog(foodbotText, foodbotImage));
        userInput.clear();
    }
}
