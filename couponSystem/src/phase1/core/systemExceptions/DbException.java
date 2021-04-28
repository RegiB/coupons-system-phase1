package phase1.core.systemExceptions;

/**
 * Database exception class
 */
public class DbException extends CouponSystemException {
	
	private static final long serialVersionUID = 1L;

	public DbException() {
		super();
}

	public DbException(String message, Throwable cause) {
		super(message, cause);
}

	public DbException(String message) {
		super(message);
}



}
