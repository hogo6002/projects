package pacman.util;

/**
 * Exception thrown when a game file cannot be unpacked.
 * @ass2
 */
public class UnpackableException extends Exception {

    /**
     * Standard UnpackableException which takes no parameters, like 
     * {@link Exception#Exception()}
     * @ass2
     */
    public UnpackableException() {
        super();
    }

    /**
     * A UnpackableException that contains a message.
     *
     * <p>
     *     <b>Note:</b> for the assignment you can use this form if you
     *     wish but it is not required. None of the methods need a
     *     certain message to be included but some students find it
     *     helpful when debugging.
     * </p>
     *
     * <p>
     *     <b>Note: Becareful to not test our implementation to have
     *     certain error messages.</b>
     * </p>
     *
     * @param message to add to the exception.
     * @ass2
     */
    public UnpackableException(String message) {
        super(message);
    }
}
