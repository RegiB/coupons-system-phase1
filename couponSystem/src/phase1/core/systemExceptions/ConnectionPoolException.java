package phase1.core.systemExceptions;

/**
 * Connection pool exception class
 */
public class ConnectionPoolException extends CouponSystemException {
	
	private static final long serialVersionUID = 1L;

	public ConnectionPoolException() {
		super("connection failed");
}

	public ConnectionPoolException(String message, Throwable cause) {
		super(message, cause);
}

	public ConnectionPoolException(String message) {
		super(message);
}

	
}
