package phase1.core.connections;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import phase1.core.systemExceptions.ConnectionPoolException;
import phase1.core.systemExceptions.CouponSystemException;

/**
 * class connectionPool, SINGLETON manages all connections to the database
 *
 */
public class ConnectionPool {

	private Set<Connection> connections = new HashSet<Connection>();
	private String url = "jdbc:mysql://localhost:3306/coupon_system_DB?serverTimezone=Israel";
	private String user = "root";
	private String password = "1234";
	public static final int MAX_CONNECTIONS = 10;

	boolean system_off = false;

	private static ConnectionPool instance;

	/**
	 * constructor, fills the connection pool
	 * 
	 * @throws ConnectionPoolException
	 */
	private ConnectionPool() throws ConnectionPoolException {

		try {
			for (int i = 1; i <= MAX_CONNECTIONS; i++) {
				Connection connection = DriverManager.getConnection(url, user, password);
				connections.add(connection);
			}

		} catch (SQLException e) {
			throw new ConnectionPoolException("intializing connection pool- failed", e);
		}
	}

	/**
	 * a method, initializes the single object (instance) of connection pool
	 * 
	 * @return instance
	 * @throws ConnectionPoolException
	 */
	public static ConnectionPool getInstance() throws ConnectionPoolException {
		if (instance == null) {
			instance = new ConnectionPool();
		}
		return instance;
	}

	/**
	 * a method that provides an available connection
	 * 
	 * @return connection
	 * @throws CouponSystemException
	 */
	public synchronized Connection getConnection() throws ConnectionPoolException {

		if (system_off == false) {

			while (this.connections.isEmpty()) {
				try {
					wait();
				} catch (InterruptedException e) {
					throw new ConnectionPoolException("failed to get connection", e);
				}
			}
			Iterator<Connection> iterator = this.connections.iterator();
			Connection connection = iterator.next();
			iterator.remove();
			return connection;

		} else {
			throw new ConnectionPoolException("connection pool is closing");
		}
	}

	/**
	 * a method returns a connection to the pool
	 * 
	 * @param connection
	 */
	public synchronized void restoreConnection(Connection connection) {

		if (connection != null) {
			this.connections.add(connection);
			notifyAll();
		}
	}

	/**
	 * a method closes all connections
	 * 
	 * @throws ConnectionPoolException
	 */
	public synchronized void closeAllConnections() throws ConnectionPoolException {

		system_off = true;

		try {

			while (this.connections.size() < MAX_CONNECTIONS) {
				try {
					wait(3000);
				} catch (InterruptedException e) {
					throw new ConnectionPoolException();
				}
			}

			for (Connection connection : connections) {
				connection.close();
			}

			System.out.println("all connections closed");

		} catch (SQLException e) {
			throw new ConnectionPoolException("failed to close all connections ", e);
		}

	}

}
