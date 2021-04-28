package phase1.core.facade;

import java.util.List;

import phase1.core.dao.CompaniesDAO;
import phase1.core.dao.CouponsDAO;
import phase1.core.dao.CustomersDAO;
import phase1.core.javaBeans.Company;
import phase1.core.javaBeans.Customer;
import phase1.core.systemExceptions.CompanyAlreadyExistsException;
import phase1.core.systemExceptions.CompanyDoesNotExistException;
import phase1.core.systemExceptions.CustomerAlreadyExistsException;
import phase1.core.systemExceptions.CustomerDoesNotExistException;
import phase1.core.systemExceptions.DaoException;

public class AdminFacade extends ClientFacade {

	private final String email = "admin@admin.com";
	private final String password = "admin";

	public AdminFacade(CompaniesDAO companiesDAO, CustomersDAO customersDAO, CouponsDAO couponsDAO) {
		super();
		this.companiesDAO = companiesDAO;
		this.customersDAO = customersDAO;
		this.couponsDAO = couponsDAO;
	}

	/**
	 * a method implemented by administrator to log in to the coupon system
	 * 
	 */
	@Override
	public boolean login(String email, String password) {

		boolean login = false;

		if (this.email.equals(email) && this.password.equals(password)) {
			login = true;
		}
		return login;
	}

	/**
	 * a method that adds a company: cannot add company with the same name or email
	 * of an existing company
	 * 
	 * @param company
	 * @throws CompanyAlreadyExistsException
	 * @throws DaoException
	 */
	public void addCompany(Company company) throws CompanyAlreadyExistsException, DaoException {

		if (!companiesDAO.isCompanyExists(company.getEmail(), company.getPassword())) {

			if (!companiesDAO.isCompanyNameExists(company.getName())) {

				if (!companiesDAO.isCompanyEmailExists(company.getEmail())) {
					companiesDAO.addCompany(company);

					System.out.println("<<<company added to the system>>>");

				} else {
					throw new CompanyAlreadyExistsException("<<<company with the same email already exists>>>");
				}
			} else {
				throw new CompanyAlreadyExistsException("<<<company with the same name already exists>>>");
			}

		} else {
			throw new CompanyAlreadyExistsException("<<<adding company - failed - already in the system>>>");
		}

	}

	/**
	 * a method that updates an existing company: cannot update companyID and
	 * company name
	 * 
	 * @param company
	 * @throws CompanyDoesNotExistException
	 * @throws CompanyAlreadyExistsException
	 * @throws DaoException
	 */
	public void updateCompany(Company company)
			throws CompanyDoesNotExistException, CompanyAlreadyExistsException, DaoException {

		Company dbCompany = companiesDAO.getOneCompany(company.getId());

		if (dbCompany != null) {

			if (!companiesDAO.isCompanyEmailExists(company.getEmail())) {

				company.setName(dbCompany.getName());
				companiesDAO.updateCompany(company);

				System.out.println("<<<company updated in the system>>>");

			} else {
				throw new CompanyAlreadyExistsException("<<<company with the same email already exists>>>");
			}
		} else {
			throw new CompanyDoesNotExistException(
					"<<<updating company " + company.getId() + " - failed - not in the system>>>");
		}

	}

	/**
	 * a method that deletes a company and all its coupons and historical coupon
	 * purchases
	 * 
	 * @param companyID
	 * @throws CompanyDoesNotExistException
	 * @throws DaoException
	 */
	public void deleteCompany(int companyID) throws CompanyDoesNotExistException, DaoException {

		Company dbCompany = companiesDAO.getOneCompany(companyID);

		if (dbCompany != null) {
			companiesDAO.deleteCompanyCouponsPurchases(companyID);
			companiesDAO.deleteCompanyCoupons(companyID);
			companiesDAO.deleteCompany(companyID);

			System.out.println("<<<company deleted from the system>>>");

		} else {
			throw new CompanyDoesNotExistException("<<<deleting company " + companyID + " - failed - not in the system>>> ");
		}
	}

	/**
	 * a method that returns all companies from the system
	 * 
	 * @return list of companies
	 * @throws CompanyDoesNotExistException
	 * @throws DaoException
	 */
	public List<Company> getAllCompanies() throws CompanyDoesNotExistException, DaoException {

		List<Company> companies = companiesDAO.getAllCompanies();

		if (!companies.isEmpty()) {
			return companies;

		} else {
			throw new CompanyDoesNotExistException("<<<getting all companies - failed - no companies in the system>>>");
		}
	}

	/**
	 * a method that returns a company
	 * 
	 * @param companyID
	 * @return company
	 * @throws CompanyDoesNotExistException
	 * @throws DaoException
	 */
	public Company getOneCompany(int companyID) throws CompanyDoesNotExistException, DaoException {

		Company dbCompany = companiesDAO.getOneCompany(companyID);

		if (dbCompany != null) {
			return dbCompany;

		} else {
			throw new CompanyDoesNotExistException(
					"<<<getting company: " + companyID + " - failed - not in the system>>>");
		}
	}

	/**
	 * a method that adds a customer: cannot add a customer with the same email of
	 * an existing customer
	 * 
	 * @param customer
	 * @throws CustomerAlreadyExistsException
	 * @throws DaoException
	 */
	public void addCustomer(Customer customer) throws CustomerAlreadyExistsException, DaoException {

		if (!customersDAO.isCustomerExists(customer.getEmail(), customer.getPassword())) {

			if (!customersDAO.isCustomerEmailExists(customer.getEmail())) {
				customersDAO.addCustomer(customer);

				System.out.println("<<<customer added to the system>>>");

			} else {
				throw new CustomerAlreadyExistsException("<<<customer with the same email already exists>>>");
			}
		} else {
			throw new CustomerAlreadyExistsException("<<<adding customer - failed - already in the system>>>");
		}
	}

	/**
	 * a method that updates an existing customer: cannot update customer id
	 * 
	 * @param customer
	 * @throws CustomerDoesNotExistException
	 * @throws CustomerAlreadyExistsException
	 * @throws DaoException
	 */
	public void updateCustomer(Customer customer)
			throws CustomerDoesNotExistException, CustomerAlreadyExistsException, DaoException {

		Customer dbCustomer = customersDAO.getOneCustomer(customer.getId());

		if (dbCustomer != null) {

			if (!customersDAO.isCustomerEmailExists(customer.getEmail())) {
				customersDAO.updateCustomer(customer);

				System.out.println("<<<customer updated in the system>>>");

			} else {
				throw new CustomerAlreadyExistsException("<<<customer with the same email already exists>>>");
			}
		} else {
			throw new CustomerDoesNotExistException("<<<updating customer: " + customer.getId() + " - failed - not in the system>>>");
		}

	}

	/**
	 * a method that deletes a customer and his coupon purchases
	 * 
	 * @param customerID
	 * @throws CustomerDoesNotExistException
	 * @throws DaoException
	 */
	public void deleteCustomer(int customerID) throws CustomerDoesNotExistException, DaoException {

		Customer dbCustomer = customersDAO.getOneCustomer(customerID);

		if (dbCustomer != null) {
			customersDAO.deleteCustomerCoupons(customerID);
			customersDAO.deleteCustomer(customerID);

			System.out.println("<<<customer deleted from the system>>>");

		} else {
			throw new CustomerDoesNotExistException("<<<deleting customer: " + customerID + " - failed - not in the system>>>");
		}
	}

	/**
	 * a method the gets all customers from the system
	 * 
	 * @return a list of customers
	 * @throws CustomerDoesNotExistException
	 * @throws DaoException
	 */
	public List<Customer> getAllCustomers() throws CustomerDoesNotExistException, DaoException {

		List<Customer> customers = customersDAO.getAllCustomers();

		if (!customers.isEmpty()) {
			return customers;

		} else {
			throw new CustomerDoesNotExistException("<<<getting all customers - failed - no customers in the system>>>");
		}
	}

	/**
	 * a method that returns only one customer
	 * 
	 * @param customerID
	 * @return customer
	 * @throws CustomerDoesNotExistException
	 * @throws DaoException
	 */
	public Customer getOneCustomer(int customerID) throws CustomerDoesNotExistException, DaoException {

		Customer customer = customersDAO.getOneCustomer(customerID);
		
		if (customer != null) {
			return customer;

		} else {
			throw new CustomerDoesNotExistException(
					"<<<getting customer: " + customerID + " - failed - not in the system>>>");
		}
	}

}
