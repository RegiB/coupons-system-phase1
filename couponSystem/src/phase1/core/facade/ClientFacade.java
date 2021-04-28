package phase1.core.facade;

import phase1.core.dao.CompaniesDAO;
import phase1.core.dao.CouponsDAO;
import phase1.core.dao.CustomersDAO;
import phase1.core.systemExceptions.CouponSystemException;

/**
 * abstract class for the 3 types of clients 
 * includes references for DAO classes
 *
 */
public abstract class ClientFacade {
	
	protected CompaniesDAO companiesDAO;
	protected CustomersDAO customersDAO;
	protected CouponsDAO couponsDAO;

	/**
	 * an abstract method of login
	 * @param email
	 * @param password
	 * @return boolean
	 * @throws CouponSystemException 
	 */
	public abstract boolean login(String email, String password) throws CouponSystemException;
}
