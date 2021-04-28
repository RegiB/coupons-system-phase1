package phase1.core.dao.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import phase1.core.connections.ConnectionPool;
import phase1.core.dao.CustomersDAO;
import phase1.core.javaBeans.Customer;
import phase1.core.systemExceptions.ConnectionPoolException;
import phase1.core.systemExceptions.DaoException;

/**
 * a class that implements the interface CustomersDAO
 *
 */
public class CustomersDBDAO implements CustomersDAO {

	private ConnectionPool connectionPool;

	@Override
	public boolean isCustomerExists(String email, String password) throws DaoException {

		Connection connect = null;
		boolean exist = false;

		try {
			connectionPool = ConnectionPool.getInstance();
			connect = connectionPool.getConnection();

			String sql = "select * from customers where `email` = ? and `password` = ?;";
			PreparedStatement pstmt = connect.prepareStatement(sql);

			pstmt.setString(1, email);
			pstmt.setString(2, password);

			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				// if we are here, than there is a row with the given values
				exist = true;
			}

		} catch (SQLException | ConnectionPoolException e) {
			throw new DaoException("checking if customer exists in the database - failed", e);

		} finally {
			if (connect != null) {
				connectionPool.restoreConnection(connect);

			}
		}
		return exist;

	}

	@Override
	public void addCustomer(Customer customer) throws DaoException {

		Connection connect = null;

		try {
			connectionPool = ConnectionPool.getInstance();
			connect = connectionPool.getConnection();

			String sql = "insert into customers(`first_name`, `last_name`, `email`, `password`) values(?, ?, ?, ?);";
			PreparedStatement pstmt = connect.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

			pstmt.setString(1, customer.getFirstName());
			pstmt.setString(2, customer.getLastName());
			pstmt.setString(3, customer.getEmail());
			pstmt.setString(4, customer.getPassword());

			pstmt.executeUpdate();

			ResultSet rsKeys = pstmt.getGeneratedKeys();
			rsKeys.next();
			customer.setId(rsKeys.getInt(1));

			System.out.println("customer: " + customer + " is saved in the database");

		} catch (SQLException | ConnectionPoolException e) {
			throw new DaoException("adding customer: " + customer + " into database- failed", e);

		} finally {
			if (connect != null) {
				connectionPool.restoreConnection(connect);
			}
		}

	}

	@Override
	public void updateCustomer(Customer customer) throws DaoException {

		Connection connect = null;

		try {
			connectionPool = ConnectionPool.getInstance();
			connect = connectionPool.getConnection();

			String sql = "update customers set `first_name` = ?, `last_name` = ?, `email` = ?, `password` = ? WHERE `id` = ?;";
			PreparedStatement pstmt = connect.prepareStatement(sql);

			pstmt.setString(1, customer.getFirstName());
			pstmt.setString(2, customer.getLastName());
			pstmt.setString(3, customer.getEmail());
			pstmt.setString(4, customer.getPassword());
			pstmt.setInt(5, customer.getId());

			int rowCount = pstmt.executeUpdate();

			if (rowCount == 0) { // if the row was not changed
				throw new DaoException("updating customer: " + customer + " - failed");
			}

			System.out.println("customer: " + customer + " is updated in the database");

		} catch (SQLException | ConnectionPoolException e) {
			throw new DaoException("updating customer: " + customer + " - failed", e);

		} finally {
			if (connect != null) {
				connectionPool.restoreConnection(connect);
			}
		}

	}

	@Override
	public void deleteCustomer(int customerID) throws DaoException {

		Connection connect = null;

		try {
			connectionPool = ConnectionPool.getInstance();
			connect = connectionPool.getConnection();

			String sql = "delete from customers WHERE `id` = ?;";
			PreparedStatement pstmt = connect.prepareStatement(sql);

			pstmt.setInt(1, customerID);

			int rowCount = pstmt.executeUpdate();

			if (rowCount == 0) { // if the row was not deleted
				throw new DaoException("deleting customer of id: " + customerID + " - failed");
			}

			System.out.println("customer of id: " + customerID + " is deleted from the database");

		} catch (SQLException | ConnectionPoolException e) {
			throw new DaoException("deleting customer of id: " + customerID + " - failed", e);

		} finally {
			if (connect != null) {
				connectionPool.restoreConnection(connect);

			}
		}

	}

	@Override
	public List<Customer> getAllCustomers() throws DaoException {

		List<Customer> customers = new ArrayList<Customer>();

		Connection connect = null;

		try {
			connectionPool = ConnectionPool.getInstance();
			connect = connectionPool.getConnection();

			String sql = "select * from customers;";
			Statement stmt = connect.createStatement();

			ResultSet rs = stmt.executeQuery(sql);

			while (rs.next()) {

				int id = rs.getInt("id");
				String firstName = rs.getString("first_name");
				String lastName = rs.getString("last_name");
				String email = rs.getString("email");
				String password = rs.getString("password");

				customers.add(new Customer(id, firstName, lastName, email, password));
			}

		} catch (SQLException | ConnectionPoolException e) {
			throw new DaoException("getting all customers from database - failed", e);

		} finally {
			if (connect != null) {
				connectionPool.restoreConnection(connect);

			}
		}

		return customers;

	}

	@Override
	public Customer getOneCustomer(int customerID) throws DaoException {

		Customer customer = null;
		Connection connect = null;

		try {
			connectionPool = ConnectionPool.getInstance();
			connect = connectionPool.getConnection();

			String sql = "select * from customers where `id`=?;";
			PreparedStatement pstmt = connect.prepareStatement(sql);

			pstmt.setInt(1, customerID);

			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {

				String firstName = rs.getString("first_name");
				String lastName = rs.getString("last_name");
				String email = rs.getString("email");
				String password = rs.getString("password");

				customer = new Customer(customerID, firstName, lastName, email, password);
			}

		} catch (SQLException | ConnectionPoolException e) {
			throw new DaoException("getting customer of id: " + customerID + " - failed", e);

		} finally {
			if (connect != null) {
				connectionPool.restoreConnection(connect);

			}
		}
		return customer;
	}

	@Override
	public boolean isCustomerEmailExists(String email) throws DaoException {

		Connection connect = null;
		boolean exist = false;

		try {
			connectionPool = ConnectionPool.getInstance();
			connect = connectionPool.getConnection();

			String sql = "select * from customers where `email` = ?;";
			PreparedStatement pstmt = connect.prepareStatement(sql);

			pstmt.setString(1, email);

			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				exist = true;
			}

		} catch (SQLException | ConnectionPoolException e) {
			throw new DaoException("checking if customer email: " + email + " exists in the database - failed", e);

		} finally {
			if (connect != null) {
				connectionPool.restoreConnection(connect);

			}
		}
		return exist;
	}

	@Override
	public void deleteCustomerCoupons(int customerID) throws DaoException {

		Connection connect = null;

		try {
			connectionPool = ConnectionPool.getInstance();
			connect = connectionPool.getConnection();

			String sql = "delete from customers_vs_coupons WHERE `customer_id` = ?;";
			PreparedStatement pstmt = connect.prepareStatement(sql);

			pstmt.setInt(1, customerID);
			pstmt.executeUpdate();

		} catch (SQLException | ConnectionPoolException e) {
			throw new DaoException("deleting customer coupons of id: " + customerID + " - failed", e);

		} finally {
			if (connect != null) {
				connectionPool.restoreConnection(connect);

			}
		}

	}

	@Override
	public Customer getCustomer(String email, String password) throws DaoException {

		Customer customer = null;
		Connection connect = null;

		try {
			connectionPool = ConnectionPool.getInstance();
			connect = connectionPool.getConnection();

			String sql = "select * from customers where `email` = ? and `password` = ?;";
			PreparedStatement pstmt = connect.prepareStatement(sql);

			pstmt.setString(1, email);
			pstmt.setString(2, password);

			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {

				int customerID = rs.getInt("id");
				String firstName = rs.getString("first_name");
				String lastName = rs.getString("last_name");

				customer = new Customer(customerID, firstName, lastName, email, password);
			}

		} catch (SQLException | ConnectionPoolException e) {
			throw new DaoException(
					"getting customer with email " + email + " and password " + password + " from database - failed",
					e);

		} finally {
			if (connect != null) {
				connectionPool.restoreConnection(connect);

			}
		}
		return customer;
	}

}
