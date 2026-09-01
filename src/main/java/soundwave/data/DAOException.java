package soundwave.data;

import java.io.Serial;

/**
 * Custom runtime exception used to wrap exceptions coming from DAO objects.
 */
public final class DAOException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new DAOException with the specified detail message.
     *
     * @param message the detail message.
     */
    public DAOException(final String message) {
        super(message);
    }

    /**
     * Constructs a new DAOException with the specified cause.
     *
     * @param cause the cause of the exception.
     */
    public DAOException(final Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a new DAOException with the specified detail message and cause.
     *
     * @param message the detail message.
     * @param cause the cause of the exception.
     */
    public DAOException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
