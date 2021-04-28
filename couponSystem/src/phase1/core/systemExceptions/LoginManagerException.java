package phase1.core.systemExceptions;

/**
 * class exception for login manager failure
 *
 */
public class LoginManagerException extends CouponSystemException {

	private static final long serialVersionUID = 1L;

	public LoginManagerException() {
		super();
	}

	public LoginManagerException(String message, Throwable cause) {
		super(message, cause);
	}

	public LoginManagerException(String message) {
		super(message);
	}

}
