/**
 * Signals that reading or writing the save file failed, or that its contents are corrupted.
 *
 * <p>Unlike {@link FoodInputException} this is not the user's fault and retrying the same
 * command will not help. The caller should report it and stop, rather than carry on accepting
 * commands whose changes can no longer be saved.
 */
public class FoodStorageException extends FoodException {
    public FoodStorageException(String msg) {
        super(msg);
    }

    public FoodStorageException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
