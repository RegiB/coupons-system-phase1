package phase1.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import phase1.core.systemExceptions.DbException;

/**
 * this class is for creating the coupon system database
 */
public class DeleteDB {

	public static void delete() throws DbException {

		String url = "jdbc:mysql://localhost:3306/coupon_system_DB?serverTimezone=Israel";
		String user = "root";
		String password = "1234";

		try (Connection connect = DriverManager.getConnection(url, user, password);) {
	
			Statement stmt = connect.createStatement();
			String sql = "drop database coupon_system_DB;";

			stmt.executeUpdate(sql);

			System.out.println("the coupon_system_DB database deleted");

		} catch (SQLException e) {
			throw new DbException("failed to delete database: " + url, e);
		}

	}

}
