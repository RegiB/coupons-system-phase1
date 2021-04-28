package phase1.core.dao;

import java.util.List;

import phase1.core.javaBeans.Company;
import phase1.core.systemExceptions.DaoException;

/**
 * Companies DAO interface includes abstract methods for class CompaniesDBDAO
 * 
 */
public interface CompaniesDAO {

	/**
	 * a method that checks if a company already exists in the database
	 * 
	 * @param email, password
	 * @return boolean
	 * @throws DaoException
	 */
	boolean isCompanyExists(String email, String password) throws DaoException;

	/**
	 * a method that adds a company to the database
	 * 
	 * @param company
	 * @throws DaoException
	 */
	void addCompany(Company company) throws DaoException;

	/**
	 * a method that updates an existing company in the database
	 * 
	 * @param company
	 * @throws DaoException
	 */
	void updateCompany(Company company) throws DaoException;

	/**
	 * a method that deletes a company from the database
	 * 
	 * @param companyID
	 * @throws DaoException
	 */
	void deleteCompany(int companyID) throws DaoException;

	/**
	 * a method that returns a list of all companies saved in the database
	 * 
	 * @return list of companies
	 * @throws DaoException
	 */
	List<Company> getAllCompanies() throws DaoException;

	/**
	 * a method that gets a single company from the database
	 * 
	 * @param companyID
	 * @return Company
	 * @throws DaoException
	 */
	Company getOneCompany(int companyID) throws DaoException;

	/**
	 * a method that checks if a similar company name already exists in the database
	 * 
	 * @param companyName
	 * @return boolean
	 * @throws DaoException
	 */
	boolean isCompanyNameExists(String companyName) throws DaoException;

	/**
	 * a method that checks if a similar company email already exists in the
	 * database
	 * 
	 * @param email
	 * @return boolean
	 * @throws DaoException
	 */
	boolean isCompanyEmailExists(String email) throws DaoException;

	/**
	 * a method that deletes a company coupons from database
	 * 
	 * @param companyID
	 * @throws DaoException
	 */
	void deleteCompanyCoupons(int companyID) throws DaoException;

	/**
	 * a method that deletes historical purchases of a company coupons from the
	 * database
	 * 
	 * @param companyID
	 * @throws DaoException
	 */
	void deleteCompanyCouponsPurchases(int companyID) throws DaoException;

	/**
	 * a method that gets a company from the database
	 * 
	 * @param email, password
	 * @return company
	 * @throws DaoException
	 */
	Company getCompany(String email, String password) throws DaoException;

}
