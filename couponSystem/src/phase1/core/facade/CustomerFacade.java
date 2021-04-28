package phase1.core.facade;

import java.util.Date;
import java.util.List;

import phase1.core.dao.CouponsDAO;
import phase1.core.dao.CustomersDAO;
import phase1.core.javaBeans.Category;
import phase1.core.javaBeans.Coupon;
import phase1.core.javaBeans.Customer;
import phase1.core.systemExceptions.CouponDoesNotExistException;
import phase1.core.systemExceptions.CouponFeaturesException;
import phase1.core.systemExceptions.CustomerDoesNotExistException;
import phase1.core.systemExceptions.DaoException;

public class CustomerFacade extends ClientFacade {

	private int customerID;

	public CustomerFacade(CustomersDAO customersDAO, CouponsDAO couponsDAO) {
		super();
		this.customersDAO = customersDAO;
		this.couponsDAO = couponsDAO;
	}

	public void setCustomerID(int customerID) {
		this.customerID = customerID;
	}

	public int getCustomerID() {
		return this.customerID;
	}

	/**
	 * a method implemented by customer to login to the coupon system: checks the
	 * login details in the database before login
	 * 
	 * @throws CustomerDoesNotExistException
	 * @throws DaoException
	 */
	@Override
	public boolean login(String email, String password) throws CustomerDoesNotExistException, DaoException {

		Customer customer = customersDAO.getCustomer(email, password);

		if (customer != null) {
			setCustomerID(customer.getId());
			return true;

		} else {
			throw new CustomerDoesNotExistException("<<<customer login - failed - not in the system>>>");
		}
	}

	/**
	 * a method that adds coupon purchase to the system: customer can purchase the
	 * same coupon only once, cannot purchase coupon if its amount is 0, cannot
	 * purchase coupon if its end date has already past
	 * 
	 * @param coupon
	 * @throws CouponDoesNotExistException
	 * @throws DaoException
	 * @throws CouponPropertiesException
	 */
	public void purchaseCoupon(Coupon coupon)
			throws CouponDoesNotExistException, CouponFeaturesException, DaoException {

		Coupon dbCoupon = couponsDAO.getOneCoupon(coupon.getId());

		if (dbCoupon != null) {

			if (!couponsDAO.isCouponPurchaseExists(getCustomerID(), coupon.getId())) {

				if (dbCoupon.getAmount() > 0) {

					if (dbCoupon.getEndDate().after(new Date(System.currentTimeMillis()))) {

						couponsDAO.addCouponPurchase(getCustomerID(), dbCoupon.getId());
						dbCoupon.setAmount(dbCoupon.getAmount() - 1);
						couponsDAO.updateCoupon(dbCoupon);

						System.out.println("<<<coupon purchase added to the system>>>");

					} else {
						throw new CouponFeaturesException("<<<coupon is out of date>>>");
					}
				} else {
					throw new CouponFeaturesException("<<<coupon is out of stock>>>");
				}
			} else {
				throw new CouponFeaturesException("<<<coupon already purchased by customer>>>");
			}
		} else {
			throw new CouponDoesNotExistException("<<<coupon purchase - failed - coupon not in the system>>>");
		}
	}

	/**
	 * a method that returns all customer's coupons (after login)
	 * 
	 * @return list of coupons
	 * @throws CouponDoesNotExistException
	 */
	public List<Coupon> getCustomerCoupons() throws CouponDoesNotExistException, DaoException {

		List<Coupon> customerCoupons = couponsDAO.getAllCustomerCoupons(getCustomerID());

		if (!customerCoupons.isEmpty()) {
			return customerCoupons;

		} else {
			throw new CouponDoesNotExistException("<<<customer has no coupons>>>");
		}
	}

	/**
	 * a method that gets customer's coupons of specific category (after login)
	 * 
	 * @param category
	 * @return list of coupons
	 * @throws CouponDoesNotExistException
	 * @throws DaoException
	 */
	public List<Coupon> getCustomerCoupons(Category category) throws CouponDoesNotExistException, DaoException {

		List<Coupon> customerCouponsByCategory = couponsDAO.getCustomerCouponsByCategory(getCustomerID(), category);

		if (!customerCouponsByCategory.isEmpty()) {
			return customerCouponsByCategory;

		} else {
			throw new CouponDoesNotExistException("<<<customer has no coupons of category " + category.name() + ">>>");
		}
	}

	/**
	 * a method that gets customer's coupons of price lower than the given one
	 * (after login)
	 * 
	 * @param maxPrice
	 * @return list of coupons
	 * @throws CouponDoesNotExistException
	 * @throws DaoException
	 */
	public List<Coupon> getCustomerCoupons(double maxPrice) throws CouponDoesNotExistException, DaoException {

		List<Coupon> customerCouponsByPrice = couponsDAO.getCustomerCouponsByPrice(getCustomerID(), maxPrice);

		if (!customerCouponsByPrice.isEmpty()) {
			return customerCouponsByPrice;

		} else {
			throw new CouponDoesNotExistException("<<<customer has no coupons of price lower than " + maxPrice + ">>>");
		}
	}

	/**
	 * a method that gets a customer's details (after login)
	 * 
	 * @return customer
	 * @throws CustomerDoesNotExistException
	 * @throws DaoException
	 */
	public Customer getCustomerDetails() throws CustomerDoesNotExistException, DaoException {

		Customer loginCustomer = customersDAO.getOneCustomer(getCustomerID());

		if (loginCustomer != null) {
			return loginCustomer;

		} else {
			throw new CustomerDoesNotExistException("<<<getting customer details - failed - not in the system>>>");

		}
	}

	/**
	 * a method that returns coupons that can be purchased (for test MVC): with
	 * amount > 0, not out of date
	 * 
	 * @return list of coupons
	 * @throws CouponDoesNotExistException
	 * @throws DaoException
	 */
	public List<Coupon> showAvailableCoupons() throws CouponDoesNotExistException, DaoException {

		List<Coupon> couponsCanBePurchased = couponsDAO.getAvailableCoupons();

		if (!couponsCanBePurchased.isEmpty()) {
			return couponsCanBePurchased;

		} else {
			throw new CouponDoesNotExistException("<<<no coupons to be purchased at the moment>>>");
		}
	}

}
