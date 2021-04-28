package phase1.core.clientsLogin;

import phase1.core.dao.CompaniesDAO;
import phase1.core.dao.CouponsDAO;
import phase1.core.dao.CustomersDAO;
import phase1.core.dao.db.CompaniesDBDAO;
import phase1.core.dao.db.CouponsDBDAO;
import phase1.core.dao.db.CustomersDBDAO;
import phase1.core.facade.AdminFacade;
import phase1.core.facade.ClientFacade;
import phase1.core.facade.CompanyFacade;
import phase1.core.facade.CustomerFacade;
import phase1.core.systemExceptions.CouponSystemException;
import phase1.core.systemExceptions.LoginManagerException;

/**
 * class SINGLETON manages the login of clients to the system
 *
 */
public class LoginManager {

	private static LoginManager instance;
	private CompaniesDAO companiesDAO;
	private CustomersDAO customersDAO;
	private CouponsDAO couponsDAO;

	private LoginManager() {
		super();
		companiesDAO = new CompaniesDBDAO();
		customersDAO = new CustomersDBDAO();
		couponsDAO = new CouponsDBDAO();
	}

	/**
	 * getInstance method
	 * 
	 * @return instance
	 */
	public static LoginManager getInstance() {

		if (instance == null) {
			instance = new LoginManager();
		}
		return instance;
	}

	/**
	 * a method that checks email and password according to client type
	 * 
	 * @param email
	 * @param password
	 * @param clientType
	 * @return clientFacade or null if not logged in
	 * @throws LoginManagerException if login failed
	 */
	public ClientFacade login(String email, String password, ClientType clientType) throws LoginManagerException {

		ClientFacade clientFacade = null;

		try {

			switch (clientType) {

			case ADMINISTRATOR:
				clientFacade = new AdminFacade(companiesDAO, customersDAO, couponsDAO);
				if (clientFacade.login(email, password)) {
					System.out.println("<<< you are logged in >>>");
					return clientFacade;
				} else {
					return null;
				}

			case COMPANY:
				clientFacade = new CompanyFacade(companiesDAO, couponsDAO);
				if (clientFacade.login(email, password)) {
					System.out.println("<<< you are logged in >>>");
					return clientFacade;
				} else {
					return null;
				}

			case CUSTOMER:
				clientFacade = new CustomerFacade(customersDAO, couponsDAO);
				if (clientFacade.login(email, password)) {
					System.out.println("<<< you are logged in >>>");
					return clientFacade;
				} else {
					return null;
				}
			}
		} catch (CouponSystemException e) {
			throw new LoginManagerException("login - failed", e);
		}
		return clientFacade;

	}
}
