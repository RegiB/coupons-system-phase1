package phase1.core.dao.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import phase1.core.connections.ConnectionPool;
import phase1.core.dao.CompaniesDAO;
import phase1.core.javaBeans.Company;
import phase1.core.systemExceptions.ConnectionPoolException;
import phase1.core.systemExceptions.DaoException;

/**
 * a class that implements the interface CompaniesDAO
 *
 */
public class CompaniesDBDAO implements CompaniesDAO {

	private static ConnectionPool connectionPool;

	@Override
	public boolean isCompanyExists(String email, String password) throws DaoException {

		Connection connect = null;
		boolean exist = false;

		try {
			connectionPool = ConnectionPool.getInstance();
			connect = connectionPool.getConnection();

			String sql = "select * from companies where `email` = ? and `password` = ?;";
			PreparedStatement pstmt = connect.prepareStatement(sql);

			pstmt.setString(1, email);
			pstmt.setString(2, password);

			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				// if we are here, than there is a row with the given values
				exist = true;
			}

		} catch (SQLException | ConnectionPoolException e) {
			throw new DaoException("checking if company exists in the database - failed", e);

		} finally {
			if (connect != null) {
				connectionPool.restoreConnection(connect);

			}
		}
		return exist;

	}

	@Override
	public void addCompany(Company company) throws DaoException {

		Connection connect = null;

		try {
			connectionPool = ConnectionPool.getInstance();
			connect = connectionPool.getConnection();

			String sql = "insert into companies(`name`, `email`, `password`) values(?, ?, ?);";
			PreparedStatement pstmt = connect.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

			pstmt.setString(1, company.getName());
			pstmt.setString(2, company.getEmail());
			pstmt.setString(3, company.getPassword());

			pstmt.executeUpdate();

			ResultSet rsKeys = pstmt.getGeneratedKeys();
			rsKeys.next();
			company.setId(rsKeys.getInt(1));

			System.out.println("company: " + company + " is added to the database");

		} catch (SQLException | ConnectionPoolException e) {
			throw new DaoException("adding company: " + company + " - failed", e);

		} finally {
			if (connect != null) {
				connectionPool.restoreConnection(connect);
			}
		}
	}

	@Override
	public void updateCompany(Company company) throws DaoException {

		Connection connect = null;

		try {
			connectionPool = ConnectionPool.getInstance();
			connect = connectionPool.getConnection();

			String sql = "update companies set `name` = ?, `email` = ?, `password` = ? WHERE `id` = ?;";
			PreparedStatement pstmt = connect.prepareStatement(sql);

			pstmt.setString(1, company.getName());
			pstmt.setString(2, company.getEmail());
			pstmt.setString(3, company.getPassword());
			pstmt.setInt(4, company.getId());

			int rowCount = pstmt.executeUpdate();

			if (rowCount == 0) { // if the row was not changed
				throw new DaoException("updating company: " + company.getId() + " - failed");
			}

			System.out.println("company: " + company.getId() + " is updated in the database");

		} catch (SQLException | ConnectionPoolException e) {
			throw new DaoException("updating company: " + company.getId() + " - failed", e);

		} finally {
			if (connect != null) {
				connectionPool.restoreConnection(connect);
			}
		}

	}

	@Override
	public void deleteCompany(int companyID) throws DaoException {

		Connection connect = null;

		try {
			connectionPool = ConnectionPool.getInstance();
			connect = connectionPool.getConnection();

			String sql = "delete from companies WHERE `id` = ?;";
			PreparedStatement pstmt = connect.prepareStatement(sql);

			pstmt.setInt(1, companyID);

			int rowCount = pstmt.executeUpdate();

			if (rowCount == 0) { // if the row was not deleted
				throw new DaoException("deleting company of id: " + companyID + " - failed");
			}

			System.out.println("company of id: " + companyID + " is deleted from database");

		} catch (SQLException | ConnectionPoolException e) {
			throw new DaoException("deleting company of id: " + companyID + " - failed", e);

		} finally {
			if (connect != null) {
				connectionPool.restoreConnection(connect);

			}
		}

	}

	@Override
	public List<Company> getAllCompanies() throws DaoException {

		List<Company> companies = new ArrayList<Company>();
		Connection connect = null;

		try {
			connectionPool = ConnectionPool.getInstance();
			connect = connectionPool.getConnection();

			String sql = "select * from companies";
			Statement stmt = connect.createStatement();

			ResultSet rs = stmt.executeQuery(sql);

			while (rs.next()) {

				int id = rs.getInt("id");
				String name = rs.getString("name");
				String email = rs.getString("email");
				String password = rs.getString("password");

				companies.add(new Company(id, name, email, password));
			}
			return companies;

		} catch (SQLException | ConnectionPoolException e) {
			throw new DaoException("getting all companies - failed", e);

		} finally {
			if (connect != null) {
				connectionPool.restoreConnection(connect);

			}
		}


	}

	@Override
	public Company getOneCompany(int companyID) throws DaoException {

		Company company = null;
		Connection connect = null;

		try {
			connectionPool = ConnectionPool.getInstance();
			connect = connectionPool.getConnection();

			String sql = "select * from companies where `id`=?;";
			PreparedStatement pstmt = connect.prepareStatement(sql);

			pstmt.setInt(1, companyID);

			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {

				String name = rs.getString("name");
				String email = rs.getString("email");
				String password = rs.getString("password");

				company = new Company(companyID, name, email, password);
			}

		} catch (SQLException | ConnectionPoolException e) {
			throw new DaoException("getting company of id: " + companyID + " from the database - failed", e);

		} finally {
			if (connect != null) {
				connectionPool.restoreConnection(connect);
			}
		}
		return company;

	}

	@Override
	public boolean isCompanyNameExists(String companyName) throws DaoException {

		Connection connect = null;
		boolean exist = false;

		try {
			connectionPool = ConnectionPool.getInstance();
			connect = connectionPool.getConnection();

			String sql = "select * from companies where `name` = ?;";
			PreparedStatement pstmt = connect.prepareStatement(sql);

			pstmt.setString(1, companyName);

			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				exist = true;
			}

		} catch (SQLException | ConnectionPoolException e) {
			throw new DaoException("checking if company name exists in the database - failed", e);

		} finally {
			if (connect != null) {
				connectionPool.restoreConnection(connect);

			}
		}
		return exist;

	}

	@Override
	public boolean isCompanyEmailExists(String email) throws DaoException {

		Connection connect = null;
		boolean exist = false;

		try {
			connectionPool = ConnectionPool.getInstance();
			connect = connectionPool.getConnection();

			String sql = "select * from companies where `email` = ?;";
			PreparedStatement pstmt = connect.prepareStatement(sql);

			pstmt.setString(1, email);

			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				exist = true;
			}

		} catch (SQLException | ConnectionPoolException e) {
			throw new DaoException("checking if company email exists in the database - failed", e);

		} finally {
			if (connect != null) {
				connectionPool.restoreConnection(connect);

			}
		}
		return exist;

	}

	@Override
	public void deleteCompanyCoupons(int companyID) throws DaoException {

		Connection connect = null;

		try {
			connectionPool = ConnectionPool.getInstance();
			connect = connectionPool.getConnection();

			String sql = "delete from coupons WHERE `company_id` = ?;";
			PreparedStatement pstmt = connect.prepareStatement(sql);

			pstmt.setInt(1, companyID);
			pstmt.executeUpdate();

		} catch (SQLException | ConnectionPoolException e) {
			throw new DaoException("deleting coupons of company id: " + companyID + " - failed", e);

		} finally {
			if (connect != null) {
				connectionPool.restoreConnection(connect);

			}
		}

	}

	@Override
	public void deleteCompanyCouponsPurchases(int companyID) throws DaoException {

		Connection connect = null;

		try {
			connectionPool = ConnectionPool.getInstance();
			connect = connectionPool.getConnection();

			String sql = "delete from `customers_vs_coupons` where `coupon_id` in (select `id` from `coupons` where `company_id` = ?);";
			PreparedStatement pstmt = connect.prepareStatement(sql);

			pstmt.setInt(1, companyID);
			pstmt.executeUpdate();

		} catch (SQLException | ConnectionPoolException e) {
			throw new DaoException("deleting coupons purchases of company id: " + companyID + " - failed", e);

		} finally {
			if (connect != null) {
				connectionPool.restoreConnection(connect);

			}
		}

	}

	@Override
	public Company getCompany(String email, String password) throws DaoException {

		Company company = null;
		Connection connect = null;

		try {
			connectionPool = ConnectionPool.getInstance();
			connect = connectionPool.getConnection();

			String sql = "select * from companies where `email` = ? and `password` = ?;";
			PreparedStatement pstmt = connect.prepareStatement(sql);

			pstmt.setString(1, email);
			pstmt.setString(2, password);

			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {

				int companyID = rs.getInt("id");
				String name = rs.getString("name");

				company = new Company(companyID, name, email, password);
			}

		} catch (SQLException | ConnectionPoolException e) {
			throw new DaoException("getting company with email: " + email + " and password: " + password
					+ " from the database - failed", e);

		} finally {
			if (connect != null) {
				connectionPool.restoreConnection(connect);

			}
		}
		return company;

	}

}
