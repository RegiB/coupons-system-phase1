package phase1.core.dao;

import java.sql.Date;
import java.util.List;

import phase1.core.javaBeans.Category;
import phase1.core.javaBeans.Coupon;
import phase1.core.systemExceptions.DaoException;

/**
 * Coupons DAO interface 
 * includes abstract methods for class CouponsDBDAO
 * 
 */
public interface CouponsDAO {

	/**
	 * a method that adds a coupon to the database
	 * 
	 * @param coupon
	 * @throws DaoException
	 */
	void addCoupon(Coupon coupon) throws DaoException;

	/**
	 * a method that updates an existing coupon in the database
	 * 
	 * @param coupon
	 * @throws DaoException
	 */
	void updateCoupon(Coupon coupon) throws DaoException;

	/**
	 * a method that deletes a coupon from the database
	 * 
	 * @param couponID
	 * @throws DaoException
	 */
	void deleteCoupon(int couponID) throws DaoException;

	/**
	 * a method that returns a list of coupons saved in the database
	 * 
	 * @return list of coupons
	 * @throws DaoException
	 */
	List<Coupon> getAllCoupons() throws DaoException;

	/**
	 * a method that returns a single coupon
	 * 
	 * @param couponID
	 * @return Coupon
	 * @throws DaoException
	 */
	Coupon getOneCoupon(int couponID) throws DaoException;

	/**
	 * a method that adds a purchase of coupon to the database
	 * 
	 * @param customerID, couponID
	 * @throws DaoException
	 */
	void addCouponPurchase(int customerID, int couponID) throws DaoException;

	/**
	 * a method that deletes a purchase of coupon from the database
	 * 
	 * @param customerID, couponID
	 * @throws DaoException
	 */
	void deleteCouponPurchase(int customerID, int couponID) throws DaoException;

	/**
	 * a method that checks if a coupon already in the system
	 * 
	 * @param companyID, title
	 * @return boolean
	 * @throws DaoException
	 */
	boolean isCouponExists(int companyID, String title) throws DaoException;

	/**
	 * a method that deletes coupon purchases using coupon id
	 * 
	 * @param couponID
	 * @throws DaoException
	 */
	void deleteCouponPurchases(int couponID) throws DaoException;

	/**
	 * a method that returns all coupons of a company
	 * 
	 * @param companyID
	 * @return list of coupons
	 * @throws DaoException
	 */
	List<Coupon> getAllCompanyCoupons(int companyID) throws DaoException;

	/**
	 * a method that returns company coupons of specific category
	 * 
	 * @param companyID, category
	 * @return list of coupons
	 * @throws DaoException
	 */
	List<Coupon> getCompanyCouponsByCategory(int companyID, Category category) throws DaoException;

	/**
	 * a method that returns company coupons with price lower than a max price
	 * 
	 * @param comapnyID, maxPrice
	 * @return list of coupons
	 * @throws DaoException
	 */
	List<Coupon> getCompanyCouponsByPrice(int companyID, double maxPrice) throws DaoException;

	/**
	 * a method that checks if a coupon purchase exists
	 * 
	 * @param customerID, id
	 * @return boolean
	 * @throws DaoException
	 */
	boolean isCouponPurchaseExists(int customerID, int id) throws DaoException;

	/**
	 * a method that returns the amount of specific coupon
	 * 
	 * @param couponID
	 * @return int
	 * @throws DaoException
	 */
	int couponAmount(int couponID) throws DaoException;

	/**
	 * a method that returns the end date of a coupon
	 * 
	 * @param couponID
	 * @return date
	 * @throws DaoException
	 */
	Date couponEndDate(int couponID) throws DaoException;

	/**
	 * a method that returns all coupons of a customer
	 * 
	 * @param customerID
	 * @return list of coupons
	 * @throws DaoException
	 */
	List<Coupon> getAllCustomerCoupons(int customerID) throws DaoException;

	/**
	 * a method that returns a customer coupons of specific category
	 * 
	 * @param customerID, category
	 * @return list of coupons
	 * @throws DaoException
	 */
	List<Coupon> getCustomerCouponsByCategory(int customerID, Category category) throws DaoException;

	/**
	 * a method that returns a customer coupons with price lower than a max price
	 * 
	 * @param customerID, maxPrice
	 * @return list of coupons
	 * @throws DaoException
	 */
	List<Coupon> getCustomerCouponsByPrice(int customerID, double maxPrice) throws DaoException;

	/**
	 * a method that returns a list of expired coupons
	 * 
	 * @return list of coupons
	 * @throws DaoException
	 */
	List<Coupon> getExpiredCoupons() throws DaoException;

	/**
	 * a method that checks if a coupon purchase exists by company id
	 * 
	 * @param companyID
	 * @return boolean
	 * @throws DaoException
	 */
	boolean isCompanyCouponPurchaseExists(int companyID) throws DaoException;

	/**
	 * a method that checks if a coupon already in the system
	 * 
	 * @param companyID	 
	 * @return boolean
	 * @throws DaoException
	 */
	boolean isCouponExists(int companyID) throws DaoException;

	/**
	 * a method that checks if a coupon purchase exists by customer id
	 * 
	 * @param customerID
	 * @return boolean
	 * @throws DaoException
	 */
	boolean isCustomerCouponPurchaseExists(int customerID) throws DaoException;

	/**
	 * a method that checks if a coupon purchase exists in the system by coupon id
	 * 
	 * @param couponID
	 * @return boolean
	 * @throws DaoException
	 */
	boolean isCouponPurchaseExists(int couponID) throws DaoException;
	
	/**
	 * a method that returns all coupons that can be purchased based on amount and end date
	 * @return list of coupons
	 * @throws DaoException
	 */
	List<Coupon> getAvailableCoupons() throws DaoException;
	

}
