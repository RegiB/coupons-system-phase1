package phase1.core.facade;

import java.util.List;

import phase1.core.dao.CompaniesDAO;
import phase1.core.dao.CouponsDAO;
import phase1.core.javaBeans.Category;
import phase1.core.javaBeans.Company;
import phase1.core.javaBeans.Coupon;
import phase1.core.systemExceptions.CompanyDoesNotExistException;
import phase1.core.systemExceptions.CouponAlreadyExistsException;
import phase1.core.systemExceptions.CouponDoesNotExistException;
import phase1.core.systemExceptions.CouponFeaturesException;
import phase1.core.systemExceptions.CouponSystemException;
import phase1.core.systemExceptions.DaoException;

public class CompanyFacade extends ClientFacade {

	private int companyID;

	public CompanyFacade(CompaniesDAO companiesDAO, CouponsDAO couponsDAO) {
		super();
		this.companiesDAO = companiesDAO;
		this.couponsDAO = couponsDAO;
	}

	public void setCompanyID(int companyID) {
		this.companyID = companyID;
	}

	public int getCompanyID() {
		return this.companyID;
	}

	/**
	 * a method implemented by company to login to the coupon system, checks login
	 * details in the database before login
	 * 
	 * @throws CouponSystemException
	 */
	@Override
	public boolean login(String email, String password) throws CouponSystemException, DaoException {

		Company company = companiesDAO.getCompany(email, password);

		if (company != null) {
			setCompanyID(company.getId());
			return true;

		} else {
			throw new CompanyDoesNotExistException("<<<company login - failed - not in the system>>>");
		}

	}

	/**
	 * a method that adds a coupon: cannot add a coupon with the same title of an
	 * existing coupon of the same company
	 * 
	 * @param coupon
	 * @throws CouponAlreadyExistsException
	 * @throws DaoException
	 */
	public void addCoupon(Coupon coupon) throws CouponAlreadyExistsException, DaoException {

		if (!couponsDAO.isCouponExists(getCompanyID(), coupon.getTitle())) {

			couponsDAO.addCoupon(new Coupon(getCompanyID(), coupon.getCategory(), coupon.getTitle(),
					coupon.getDescription(), coupon.getStartDate(), coupon.getEndDate(), coupon.getAmount(),
					coupon.getPrice(), coupon.getImage()));

			System.out.println("<<<coupon added to the system>>>");

		} else {
			throw new CouponAlreadyExistsException(
					"<<<adding coupon - failed - company already has a coupon with similar title>>>");
		}
	}

	/**
	 * a method that updates details of an existing coupon: cannot update coupon id
	 * and company id
	 * 
	 * @param coupon
	 * @throws CouponDoesNotExistException
	 * @throws CouponFeaturesException
	 * @throws DaoException
	 */
	public void updateCoupon(Coupon coupon) throws CouponDoesNotExistException, CouponFeaturesException, DaoException {

		Coupon dbCoupon = couponsDAO.getOneCoupon(coupon.getId());

		if (dbCoupon != null) {

			if (dbCoupon.getCompanyID() == getCompanyID()) {

				if (!couponsDAO.isCouponExists(getCompanyID(), coupon.getTitle())) {
					coupon.setCompanyID(dbCoupon.getCompanyID());
					couponsDAO.updateCoupon(coupon);

					System.out.println("<<<coupon updated in the system>>>");

				} else {
					throw new CouponFeaturesException("<<<company already has a coupon with a similar title>>>");
				}
			} else {
				throw new CouponFeaturesException("<<<coupon doesn't belong to company>>>");
			}
		} else {
			throw new CouponDoesNotExistException("<<<updating coupon - failed - not in the system>>>");
		}

	}

	/**
	 * a method that deletes a coupon: deletes historical purchases of the coupon
	 * 
	 * @param couponID
	 * @throws CouponDoesNotExistException
	 * @throws DaoException
	 */
	public void deleteCoupon(int couponID) throws CouponDoesNotExistException, DaoException {

		Coupon dbCoupon = couponsDAO.getOneCoupon(couponID);

		if (dbCoupon != null) {

			couponsDAO.deleteCouponPurchases(couponID);
			couponsDAO.deleteCoupon(couponID);

			System.out.println("<<<coupon deleted from the system>>>");

		} else {
			throw new CouponDoesNotExistException("<<<deleting coupon - failed - not in the system>>>");
		}
	}

	/**
	 * a method that gets all company's coupons (after login)
	 * 
	 * @return list of coupons
	 * @throws CouponDoesNotExistException
	 * @throws DaoException
	 */
	public List<Coupon> getAllCompanyCoupons() throws CouponDoesNotExistException, DaoException {

		List<Coupon> companyCoupons = couponsDAO.getAllCompanyCoupons(getCompanyID());

		if (!companyCoupons.isEmpty()) {
			return companyCoupons;

		} else {
			throw new CouponDoesNotExistException("<<<company has no coupons>>>");
		}
	}

	/**
	 * a method that gets company's coupons of specific category (after login)
	 * 
	 * @param category
	 * @return list of coupons
	 * @throws CouponDoesNotExistException
	 * @throws DaoException
	 */
	public List<Coupon> getCompanyCoupons(Category category) throws CouponDoesNotExistException, DaoException {

		List<Coupon> companyCouponsByCategory = couponsDAO.getCompanyCouponsByCategory(getCompanyID(), category);

		if (!companyCouponsByCategory.isEmpty()) {
			return companyCouponsByCategory;

		} else {
			throw new CouponDoesNotExistException("<<<company has no coupons of category " + category.name() + ">>>");
		}

	}

	/**
	 * a method that gets company's coupons of price lower than the given one (after
	 * login)
	 * 
	 * @param maxPrice
	 * @return list of coupons
	 * @throws CouponDoesNotExistException
	 * @throws DaoException
	 */
	public List<Coupon> getCompanyCoupons(double maxPrice) throws CouponDoesNotExistException, DaoException {

		List<Coupon> companyCouponsByPrice = couponsDAO.getCompanyCouponsByPrice(getCompanyID(), maxPrice);

		if (!companyCouponsByPrice.isEmpty()) {
			return companyCouponsByPrice;

		} else {
			throw new CouponDoesNotExistException("<<<company has no coupons of price lower than " + maxPrice + ">>>");
		}
	}

	/**
	 * a method that gets a company details (after login)
	 * 
	 * @return company
	 * @throws CompanyDoesNotExistException
	 * @throws DaoException
	 */
	public Company getCompanyDetails() throws CompanyDoesNotExistException, DaoException {

		Company loginCompany = companiesDAO.getOneCompany(getCompanyID());

		if (loginCompany != null) {
			return loginCompany;

		} else {
			throw new CompanyDoesNotExistException("<<<getting company details - failed - not in the system>>>");
		}
	}
}
