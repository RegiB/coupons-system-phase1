package phase1.core.dao;

import java.util.List;

import phase1.core.javaBeans.Customer;
import phase1.core.systemExceptions.DaoException;

/**
 * Customers DAO interface includes abstract methods for class CustomersDBDAO
 * 
 */
public interface CustomersDAO {

	/**
	 * a method that checks if a customer already exists in the database
	 * 
	 * @param email, password
	 * @return boolean
	 * @throws DaoException
	 */
	boolean isCustomerExists(String email, String password) throws DaoException;

	/**
	 * a method that adds a customer to the database
	 * 
	 * @param customer
	 * @throws DaoException
	 */
	void addCustomer(Customer customer) throws DaoException;

	/**
	 * a method that updates an existing customer in the database
	 * 
	 * @param customer
	 * @throws DaoException
	 */
	void updateCustomer(Customer customer) throws DaoException;

	/**
	 * a method that deletes a customer from the database
	 * 
	 * @param customerID
	 * @throws DaoException
	 */
	void deleteCustomer(int customerID) throws DaoException;

	/**
	 * a method that returns a list of all customers saved in the database
	 * 
	 * @return list of customers
	 * @throws DaoException
	 */
	List<Customer> getAllCustomers() throws DaoException;

	/**
	 * a method that returns a single customer from the database
	 * 
	 * @param customerID
	 * @return Customer
	 * @throws DaoException
	 */
	Customer getOneCustomer(int customerID) throws DaoException;

	/**
	 * a method that checks if a similar customer email already exists in the
	 * database
	 * 
	 * @param email
	 * @return boolean
	 * @throws DaoException
	 */
	boolean isCustomerEmailExists(String email) throws DaoException;

	/**
	 * a method that deletes a customer coupons from database
	 * 
	 * @param customerID
	 * @throws DaoException
	 */
	void deleteCustomerCoupons(int customerID) throws DaoException;

	/**
	 * a method that returns a customer from the database
	 * 
	 * @param email, password
	 * @return customer
	 * @throws DaoException
	 */
	Customer getCustomer(String email, String password) throws DaoException;
}
