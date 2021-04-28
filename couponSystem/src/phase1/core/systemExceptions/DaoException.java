package phase1.core.systemExceptions;

/**
 * DAO (data access object) exception class
 */
public class DaoException extends CouponSystemException {

	private static final long serialVersionUID = 1L;

	public DaoException() {
		super("database error");
}

	public DaoException(String message, Throwable cause) {
		super(message, cause);
}

	public DaoException(String message) {
		super(message);
}

	
}
