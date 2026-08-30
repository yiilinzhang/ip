/**
 * Signals that the user typed something the chatbot cannot make sense of, such as an unknown
 * command, a missing argument, or a task number that does not exist.
 *
 * <p>This is an expected, recoverable situation: the caller should show the message and carry
 * on reading commands. Nothing is broken.
 */
public class FoodInputException extends FoodException {
    public FoodInputException(String msg) {
        super(msg);
    }

    public FoodInputException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
