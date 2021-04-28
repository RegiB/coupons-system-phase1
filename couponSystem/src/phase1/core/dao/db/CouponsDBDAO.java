package phase1.core.dao.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.sql.Date;

import phase1.core.connections.ConnectionPool;
import phase1.core.dao.CouponsDAO;
import phase1.core.javaBeans.Category;
import phase1.core.javaBeans.Coupon;
import phase1.core.systemExceptions.ConnectionPoolException;
import phase1.core.systemExceptions.DaoException;

/**
 * a class that implements the interface CouponsDAO
 *
 */
public class CouponsDBDAO implements CouponsDAO {

	private ConnectionPool connectionPool;

	@Override
	public void addCoupon(Coupon coupon) throws DaoException {

		Connection connect = null;

		try {
			connectionPool = ConnectionPool.getInstance();
			connect = connectionPool.getConnection();

			String sql = "insert into coupons(`company_id`,`category_name`, `title`, `description`, `start_date`, `end_date`, `amount`, `price`, `image`) values(? ,? ,? ,? ,? ,? ,? ,? ,? );";
			PreparedStatement pstmt = connect.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

			pstmt.setInt(1, coupon.getCompanyID());
			pstmt.setString(2, coupon.getCategory().name());
			pstmt.setString(3, coupon.getTitle());
			pstmt.setString(4, coupon.getDescription());
			pstmt.setDate(5, new Date(coupon.getStartDate().getTime()));
			pstmt.setDate(6, new Date(coupon.getEndDate().getTime()));
			pstmt.setInt(7, coupon.getAmount());
			pstmt.setDouble(8, coupon.getPrice());
			pstmt.setString(9, coupon.getImage());

			pstmt.executeUpdate();

			ResultSet rsKeys = pstmt.getGeneratedKeys();
			rsKeys.next();
			coupon.setId(rsKeys.getInt(1));

			System.out.println("coupon: " + coupon + " is saved in the database");

		} catch (SQLException | ConnectionPoolException e) {
			throw new DaoException("adding coupon: " + coupon + " to the database- failed", e);

		} finally {
			if (connect != null) {
				connectionPool.restoreConnection(connect);
			}
		}

	}

	@Override
	public void updateCoupon(Coupon coupon) throws DaoException {

		Connection connect = null;

		try {
			connectionPool = ConnectionPool.getInstance();
			connect = connectionPool.getConnection();

			String sql = "update coupons set `company_id` = ?, `category_name` = ?, `title` = ?, `description` = ?, `start_date` = ?, "
					+ "`end_date` = ?, `amount` = ?, `price` = ?, `image` = ? WHERE `id` = ?;";
			PreparedStatement pstmt = connect.prepareStatement(sql);

			pstmt.setInt(1, coupon.getCompanyID());
			pstmt.setString(2, coupon.getCategory().name());
			pstmt.setString(3, coupon.getTitle());
			pstmt.setString(4, coupon.getDescription());
			pstmt.setDate(5, new Date(coupon.getStartDate().getTime()));
			pstmt.setDate(6, new Date(coupon.getEndDate().getTime()));
			pstmt.setInt(7, coupon.getAmount());
			pstmt.setDouble(8, coupon.getPrice());
			pstmt.setString(9, coupon.getImage());
			pstmt.setInt(10, coupon.getId());

			int rowCount = pstmt.executeUpdate();

			if (rowCount == 0) {
				throw new DaoException("updating coupon: " + coupon.getId() + " - failed");
			}

			System.out.println("coupon: " + coupon.getId() + " is updated in the database");

		} catch (SQLException | ConnectionPoolException e) {
			throw new DaoException("updating coupon: " + coupon.getId() + " - failed", e);

		} finally {
			if (connect != null) {
				connectionPool.restoreConnection(connect);
			}
		}

	}

	@Override
	public void deleteCoupon(int couponID) throws DaoException {

		Connection connect = null;

		try {
			connectionPool = ConnectionPool.getInstance();
			connect = connectionPool.getConnection();

			String sql = "delete from coupons WHERE `id` = ?;";
			PreparedStatement pstmt = connect.prepareStatement(sql);

			pstmt.setInt(1, couponID);

			int rowCount = pstmt.executeUpdate();

			if (rowCount == 0) {
				throw new DaoException("deleting coupon of id: " + couponID + " - failed");
			}

			System.out.println("coupon of id: " + couponID + " is deleted from the database");

		} catch (SQLException | ConnectionPoolException e) {
			throw new DaoException("deleting coupon of id: " + couponID + " - failed", e);

		} finally {
			if (connect != null) {
				connectionPool.restoreConnection(connect);

			}
		}

	}

	@Override
	public List<Coupon> getAllCoupons() throws DaoException {

		List<Coupon> coupons = new ArrayList<Coupon>();
		Connection connect = null;

		try {
			connectionPool = ConnectionPool.getInstance();
			connect = connectionPool.getConnection();

			String sql = "select * from coupons;";
			Statement stmt = connect.createStatement();

			ResultSet rs = stmt.executeQuery(sql);

			while (rs.next()) {

				int id = rs.getInt("id");
				int companyID = rs.getInt("company_id");
				Category category = Category.valueOf(rs.getString("category_name"));
				String title = rs.getString("title");
				String description = rs.getString("description");
				Date startDate = rs.getDate("start_date");
				Date endDate = rs.getDate("end_date");
				int amount = rs.getInt("amount");
				Double price = rs.getDouble("price");
				String image = rs.getString("image");

				coupons.add(new Coupon(id, companyID, category, title, description, startDate, endDate, amount, price,
						image));
			}

		} catch (SQLException | ConnectionPoolException e) {
			throw new DaoException("getting all coupons from database - failed", e);

		} finally {
			if (connect != null) {
				connectionPool.restoreConnection(connect);
			}
		}
		return coupons;
	}

	@Override
	public Coupon getOneCoupon(int couponID) throws DaoException {

		Coupon coupon = null;
		Connection connect = null;

		try {
			connectionPool = ConnectionPool.getInstance();
			connect = connectionPool.getConnection();

			String sql = "select * from coupons where `id`=?;";
			PreparedStatement pstmt = connect.prepareStatement(sql);

			pstmt.setInt(1, couponID);

			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {

				int companyID = rs.getInt("company_id");
				Category category = Category.valueOf(rs.getString("category_name"));
				String title = rs.getString("title");
				String description = rs.getString("description");
				Date startDate = rs.getDate("start_date");
				Date endDate = rs.getDate("end_date");
				int amount = rs.getInt("amount");
				Double price = rs.getDouble("price");
				String image = rs.getString("image");

				coupon = new Coupon(couponID, companyID, category, title, description, startDate, endDate, amount,
						price, image);
			}

		} catch (SQLException | ConnectionPoolException e) {
			throw new DaoException("getting coupon of id: " + couponID + " - failed", e);

		} finally {
			if (connect != null) {
				connectionPool.restoreConnection(connect);
			}
		}
		return coupon;

	}

	@Override
	public void addCouponPurchase(int customerID, int couponID) throws DaoException {

		Connection connect = null;

		try {
			connectionPool = ConnectionPool.getInstance();
			connect = connectionPool.getConnection();

			String sql = "insert into customers_vs_coupons values(?, ?);";
			PreparedStatement pstmt = connect.prepareStatement(sql);

			pstmt.setInt(1, customerID);
			pstmt.setInt(2, couponID);

			pstmt.executeUpdate();

			System.out.println("purchase of coupon id: " + couponID + " by customer id: " + customerID + " was added");

		} catch (SQLException | ConnectionPoolException e) {
			throw new DaoException(
					"adding purchase of coupon id: " + couponID + " by customer id: " + customerID + " - failed", e);

		} finally {
			if (connect != null) {
				connectionPool.restoreConnection(connect);
			}
		}

	}

	@Override
	public void deleteCouponPurchase(int customerID, int couponID) throws DaoException {

		Connection connect = null;

		try {
			connectionPool = ConnectionPool.getInstance();
			connect = connectionPool.getConnection();

			String sql = "delete from customers_vs_coupons WHERE `customer_id` = ? and `coupon_id` = ?;";
			PreparedStatement pstmt = connect.prepareStatement(sql);

			pstmt.setInt(1, customerID);
			pstmt.setInt(2, couponID);

			pstmt.executeUpdate();

		} catch (SQLException | ConnectionPoolException e) {
			throw new DaoException(
					"deleting purchase of coupon id: " + couponID + " by customer id: " + customerID + " - failed", e);

		} finally {
			if (connect != null) {
				connectionPool.restoreConnection(connect);

			}
		}

	}

	@Override
	public boolean isCouponExists(int companyID, String title) throws DaoException {

		Connection connect = null;
		boolean exist = false;

		try {
			connectionPool = ConnectionPool.getInstance();
			connect = connectionPool.getConnection();

			String sql = "select * from coupons where `company_id` = ? and `title` = ?;";
			PreparedStatement pstmt = connect.prepareStatement(sql);

			pstmt.setInt(1, companyID);
			pstmt.setString(2, title);

			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				exist = true;
			}

		} catch (SQLException | ConnectionPoolException e) {
			throw new DaoException("checking if coupon already exists in the database - failed", e);

		} finally {
			if (connect != null) {
				connectionPool.restoreConnection(connect);
			}
		}
		return exist;

	}

	@Override
	public void deleteCouponPurchases(int couponID) throws DaoException {

		Connection connect = null;

		try {
			connectionPool = ConnectionPool.getInstance();
			connect = connectionPool.getConnection();

			String sql = "delete from customers_vs_coupons WHERE `coupon_id` = ?;";
			PreparedStatement pstmt = connect.prepareStatement(sql);

			pstmt.setInt(1, couponID);
			pstmt.executeUpdate();

		} catch (SQLException | ConnectionPoolException e) {
			throw new DaoException("deleting purchase of coupon id: " + couponID + " - failed", e);

		} finally {
			if (connect != null) {
				connectionPool.restoreConnection(connect);
			}
		}

	}

	@Override
	public List<Coupon> getAllCompanyCoupons(int companyID) throws DaoException {

		List<Coupon> allCompanyCoupons = new ArrayList<Coupon>();

		Connection connect = null;

		try {
			connectionPool = ConnectionPool.getInstance();
			connect = connectionPool.getConnection();

			String sql = "select * from coupons where `company_id` = ?;";
			PreparedStatement pstmt = connect.prepareStatement(sql);

			pstmt.setInt(1, companyID);

			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {

				int id = rs.getInt("id");
				Category category = Category.valueOf(rs.getString("category_name"));
				String title = rs.getString("title");
				String description = rs.getString("description");
				Date startDate = rs.getDate("start_date");
				Date endDate = rs.getDate("end_date");
				int amount = rs.getInt("amount");
				Double price = rs.getDouble("price");
				String image = rs.getString("image");

				allCompanyCoupons.add(new Coupon(id, companyID, category, title, description, startDate, endDate,
						amount, price, image));
			}

		} catch (SQLException | ConnectionPoolException e) {
			throw new DaoException("getting all coupons of company " + companyID + " from database - failed", e);

		} finally {
			if (connect != null) {
				connectionPool.restoreConnection(connect);
			}
		}
		return allCompanyCoupons;
	}

	@Override
	public List<Coupon> getCompanyCouponsByCategory(int companyID, Category category) throws DaoException {

		List<Coupon> companyCouponsByCategory = new ArrayList<Coupon>();
		Connection connect = null;

		try {
			connectionPool = ConnectionPool.getInstance();
			connect = connectionPool.getConnection();

			String sql = "select * from coupons where `company_id` = ? and `category_name` = ?;";
			PreparedStatement pstmt = connect.prepareStatement(sql);

			pstmt.setInt(1, companyID);
			pstmt.setString(2, category.name());

			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {

				int id = rs.getInt("id");
				String title = rs.getString("title");
				String description = rs.getString("description");
				Date startDate = rs.getDate("start_date");
				Date endDate = rs.getDate("end_date");
				int amount = rs.getInt("amount");
				Double price = rs.getDouble("price");
				String image = rs.getString("image");

				companyCouponsByCategory.add(new Coupon(id, companyID, category, title, description, startDate, endDate,
						amount, price, image));
			}

		} catch (SQLException | ConnectionPoolException e) {
			throw new DaoException("getting coupons of company " + companyID + " of category " + category.name()
					+ " from database - failed", e);

		} finally {
			if (connect != null) {
				connectionPool.restoreConnection(connect);
			}
		}
		return companyCouponsByCategory;

	}

	@Override
	public List<Coupon> getCompanyCouponsByPrice(int companyID, double maxPrice) throws DaoException {

		List<Coupon> companyCouponsByPrice = new ArrayList<Coupon>();
		Connection connect = null;

		try {
			connectionPool = ConnectionPool.getInstance();
			connect = connectionPool.getConnection();

			String sql = "select * from coupons where `company_id` = ? and `price` < ?;";
			PreparedStatement pstmt = connect.prepareStatement(sql);

			pstmt.setInt(1, companyID);
			pstmt.setDouble(2, maxPrice);

			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {

				int id = rs.getInt("id");
				Category category = Category.valueOf(rs.getString("category_name"));
				String title = rs.getString("title");
				String description = rs.getString("description");
				Date startDate = rs.getDate("start_date");
				Date endDate = rs.getDate("end_date");
				int amount = rs.getInt("amount");
				Double price = rs.getDouble("price");
				String image = rs.getString("image");

				companyCouponsByPrice.add(new Coupon(id, companyID, category, title, description, startDate, endDate,
						amount, price, image));
			}

		} catch (SQLException | ConnectionPoolException e) {
			throw new DaoException("getting coupons of company " + companyID + " that cost less than " + maxPrice
					+ " from database - failed", e);

		} finally {
			if (connect != null) {
				connectionPool.restoreConnection(connect);
			}
		}
		return companyCouponsByPrice;
	}

	@Override
	public boolean isCouponPurchaseExists(int customerID, int couponID) throws DaoException {

		Connection connect = null;
		boolean exist = false;

		try {
			connectionPool = ConnectionPool.getInstance();
			connect = connectionPool.getConnection();

			String sql = "select * from customers_vs_coupons WHERE `customer_id` = ? and `coupon_id` = ?;";
			PreparedStatement pstmt = connect.prepareStatement(sql);

			pstmt.setInt(1, customerID);
			pstmt.setInt(2, couponID);

			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				exist = true;
			}

		} catch (SQLException | ConnectionPoolException e) {
			throw new DaoException("checking if coupon " + couponID + " purchase by customer " + customerID
					+ " exists in the database - failed", e);

		} finally {
			if (connect != null) {
				connectionPool.restoreConnection(connect);
			}
		}
		return exist;
	}

	@Override
	public int couponAmount(int couponID) throws DaoException {

		Connection connect = null;
		int amount = 0;

		try {
			connectionPool = ConnectionPool.getInstance();
			connect = connectionPool.getConnection();

			String sql = "select `amount` from coupons where `id` = ?;";
			PreparedStatement pstmt = connect.prepareStatement(sql);

			pstmt.setInt(1, couponID);

			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				amount = rs.getInt("amount");
			}

		} catch (SQLException | ConnectionPoolException e) {
			throw new DaoException("getting amount of coupon id: " + couponID + " from the database - failed", e);

		} finally {
			if (connect != null) {
				connectionPool.restoreConnection(connect);

			}
		}
		return amount;

	}

	@Override
	public Date couponEndDate(int couponID) throws DaoException {

		Connection connect = null;
		Date endDate = null;

		try {
			connectionPool = ConnectionPool.getInstance();
			connect = connectionPool.getConnection();

			String sql = "select `end_date` from coupons where `id` = ?;";
			PreparedStatement pstmt = connect.prepareStatement(sql);

			pstmt.setInt(1, couponID);

			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				endDate = rs.getDate("end_date");
			}

		} catch (SQLException | ConnectionPoolException e) {
			throw new DaoException("getting end date of coupon id: " + couponID + " from the database - failed", e);

		} finally {
			if (connect != null) {
				connectionPool.restoreConnection(connect);
			}
		}
		return endDate;
	}

	@Override
	public List<Coupon> getAllCustomerCoupons(int customerID) throws DaoException {

		List<Coupon> allCustomerCoupons = new ArrayList<Coupon>();
		Connection connect = null;

		try {
			connectionPool = ConnectionPool.getInstance();
			connect = connectionPool.getConnection();

			String sql = "select * from coupons join customers_vs_coupons on coupons.id = customers_vs_coupons.coupon_id where customer_id = ?;";
			PreparedStatement pstmt = connect.prepareStatement(sql);

			pstmt.setInt(1, customerID);

			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {

				int id = rs.getInt("id");
				int companyID = rs.getInt("company_id");
				Category category = Category.valueOf(rs.getString("category_name"));
				String title = rs.getString("title");
				String description = rs.getString("description");
				Date startDate = rs.getDate("start_date");
				Date endDate = rs.getDate("end_date");
				int amount = rs.getInt("amount");
				Double price = rs.getDouble("price");
				String image = rs.getString("image");

				allCustomerCoupons.add(new Coupon(id, companyID, category, title, description, startDate, endDate,
						amount, price, image));
			}

		} catch (SQLException | ConnectionPoolException e) {
			throw new DaoException("getting all coupons of a customer " + customerID + " from database - failed", e);

		} finally {
			if (connect != null) {
				connectionPool.restoreConnection(connect);
			}
		}
		return allCustomerCoupons;
	}

	@Override
	public List<Coupon> getCustomerCouponsByCategory(int customerID, Category category) throws DaoException {

		List<Coupon> customerCouponsByCategory = new ArrayList<Coupon>();
		Connection connect = null;

		try {
			connectionPool = ConnectionPool.getInstance();
			connect = connectionPool.getConnection();

			String sql = "select * from coupons join customers_vs_coupons on coupons.id = customers_vs_coupons.coupon_id where `customer_id`= ? and `category_name` = ?;";
			PreparedStatement pstmt = connect.prepareStatement(sql);

			pstmt.setInt(1, customerID);
			pstmt.setString(2, category.name());

			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {

				int id = rs.getInt("id");
				int companyID = rs.getInt("company_id");
				String title = rs.getString("title");
				String description = rs.getString("description");
				Date startDate = rs.getDate("start_date");
				Date endDate = rs.getDate("end_date");
				int amount = rs.getInt("amount");
				Double price = rs.getDouble("price");
				String image = rs.getString("image");

				customerCouponsByCategory.add(new Coupon(id, companyID, category, title, description, startDate,
						endDate, amount, price, image));
			}

		} catch (SQLException | ConnectionPoolException e) {
			throw new DaoException("getting coupons of customer " + customerID + " of category " + category.name()
					+ " from database - failed", e);

		} finally {
			if (connect != null) {
				connectionPool.restoreConnection(connect);
			}
		}
		return customerCouponsByCategory;
	}

	@Override
	public List<Coupon> getCustomerCouponsByPrice(int customerID, double maxPrice) throws DaoException {

		List<Coupon> customerCouponsByPrice = new ArrayList<Coupon>();
		Connection connect = null;

		try {
			connectionPool = ConnectionPool.getInstance();
			connect = connectionPool.getConnection();

			String sql = "select * from coupons join customers_vs_coupons on coupons.id = customers_vs_coupons.coupon_id where customer_id = ? and price < ?;";
			PreparedStatement pstmt = connect.prepareStatement(sql);

			pstmt.setInt(1, customerID);
			pstmt.setDouble(2, maxPrice);

			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {

				int id = rs.getInt("id");
				int companyID = rs.getInt("company_id");
				Category category = Category.valueOf(rs.getString("category_name"));
				String title = rs.getString("title");
				String description = rs.getString("description");
				Date startDate = rs.getDate("start_date");
				Date endDate = rs.getDate("end_date");
				int amount = rs.getInt("amount");
				Double price = rs.getDouble("price");
				String image = rs.getString("image");

				customerCouponsByPrice.add(new Coupon(id, companyID, category, title, description, startDate, endDate,
						amount, price, image));
			}

		} catch (SQLException | ConnectionPoolException e) {
			throw new DaoException("getting coupons of customer " + customerID + " that cost less than " + maxPrice
					+ " from database - failed", e);

		} finally {
			if (connect != null) {
				connectionPool.restoreConnection(connect);

			}
		}

		return customerCouponsByPrice;

	}

	@Override
	public List<Coupon> getExpiredCoupons() throws DaoException {

		List<Coupon> expiredCoupons = new ArrayList<Coupon>();
		Connection connect = null;

		try {
			connectionPool = ConnectionPool.getInstance();
			connect = connectionPool.getConnection();

			String sql = "select * from `coupons` where `end_date` <= ?;";
			PreparedStatement pstmt = connect.prepareStatement(sql);

			pstmt.setDate(1, new Date(System.currentTimeMillis()));

			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {

				int id = rs.getInt("id");
				int companyID = rs.getInt("company_id");
				Category category = Category.valueOf(rs.getString("category_name"));
				String title = rs.getString("title");
				String description = rs.getString("description");
				Date startDate = rs.getDate("start_date");
				Date endDate = rs.getDate("end_date");
				int amount = rs.getInt("amount");
				Double price = rs.getDouble("price");
				String image = rs.getString("image");

				expiredCoupons.add(new Coupon(id, companyID, category, title, description, startDate, endDate,
						amount, price, image));
			}

		} catch (SQLException | ConnectionPoolException e) {
			throw new DaoException("getting expired coupons from database - failed", e);

		} finally {
			if (connect != null) {
				connectionPool.restoreConnection(connect);
			}
		}
		return expiredCoupons;
	}

	@Override
	public boolean isCompanyCouponPurchaseExists(int companyID) throws DaoException {

		Connection connect = null;
		boolean exist = false;

		try {
			connectionPool = ConnectionPool.getInstance();
			connect = connectionPool.getConnection();

			String sql = "select * from `coupons` join `customers_vs_coupons` on `coupons`.`id` = `customers_vs_coupons`.`coupon_id` where `company_id` = ?;";
			PreparedStatement pstmt = connect.prepareStatement(sql);

			pstmt.setInt(1, companyID);

			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {

				exist = true;
			}

		} catch (SQLException | ConnectionPoolException e) {
			throw new DaoException(
					"checking if coupon of company " + companyID + " purchase exists in the database - failed", e);

		} finally {
			if (connect != null) {
				connectionPool.restoreConnection(connect);
			}
		}
		return exist;
	}

	@Override
	public boolean isCouponExists(int companyID) throws DaoException {

		Connection connect = null;
		boolean exist = false;

		try {
			connectionPool = ConnectionPool.getInstance();
			connect = connectionPool.getConnection();

			String sql = "select * from coupons where `company_id` = ?;";
			PreparedStatement pstmt = connect.prepareStatement(sql);

			pstmt.setInt(1, companyID);

			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				// if we are here, than there is a row with the given values
				exist = true;
			}

		} catch (SQLException | ConnectionPoolException e) {
			throw new DaoException("checking if company " + companyID + " has coupons in the database - failed", e);

		} finally {
			if (connect != null) {
				connectionPool.restoreConnection(connect);
			}
		}
		return exist;
	}

	@Override
	public boolean isCustomerCouponPurchaseExists(int customerID) throws DaoException {

		Connection connect = null;
		boolean exist = false;

		try {
			connectionPool = ConnectionPool.getInstance();
			connect = connectionPool.getConnection();

			String sql = "select * from customers_vs_coupons WHERE `customer_id` = ?;";
			PreparedStatement pstmt = connect.prepareStatement(sql);

			pstmt.setInt(1, customerID);

			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				exist = true;
			}

		} catch (SQLException | ConnectionPoolException e) {
			throw new DaoException(
					"checking if customer " + customerID + " has coupon purchases in the database - failed", e);

		} finally {
			if (connect != null) {
				connectionPool.restoreConnection(connect);
			}
		}
		return exist;
	}

	@Override
	public boolean isCouponPurchaseExists(int couponID) throws DaoException {

		Connection connect = null;
		boolean exist = false;

		try {
			connectionPool = ConnectionPool.getInstance();
			connect = connectionPool.getConnection();

			String sql = "select * from customers_vs_coupons WHERE `coupon_id` = ?;";
			PreparedStatement pstmt = connect.prepareStatement(sql);

			pstmt.setInt(1, couponID);

			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {

				exist = true;
			}

		} catch (SQLException | ConnectionPoolException e) {
			throw new DaoException("checking if coupon " + couponID + " purchase exists in the database - failed", e);

		} finally {
			if (connect != null) {
				connectionPool.restoreConnection(connect);
			}
		}
		return exist;
	}

	@Override
	public List<Coupon> getAvailableCoupons() throws DaoException {
		
		List<Coupon> availableCoupons = new ArrayList<Coupon>();
		Connection connect = null;

		try {
			connectionPool = ConnectionPool.getInstance();
			connect = connectionPool.getConnection();

			String sql = "select * from coupons where `amount` > 0 and `end_date` > ?;";
			PreparedStatement pstmt = connect.prepareStatement(sql);

			pstmt.setDate(1, new Date(System.currentTimeMillis()));

			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {

				int id = rs.getInt("id");
				int companyID = rs.getInt("company_id");
				Category category = Category.valueOf(rs.getString("category_name"));
				String title = rs.getString("title");
				String description = rs.getString("description");
				Date startDate = rs.getDate("start_date");
				Date endDate = rs.getDate("end_date");
				int amount = rs.getInt("amount");
				Double price = rs.getDouble("price");
				String image = rs.getString("image");

				availableCoupons.add(new Coupon(id, companyID, category, title, description, startDate, endDate,
						amount, price, image));
			}

		} catch (SQLException | ConnectionPoolException e) {
			throw new DaoException("getting available coupons from database - failed", e);

		} finally {
			if (connect != null) {
				connectionPool.restoreConnection(connect);
			}
		}
		return availableCoupons;
	}
	
	

}
