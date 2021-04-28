package phase1.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import phase1.core.systemExceptions.DbException;

/**
 * this class is for creating the coupon system database
 */
public class CreateDB {

	public static void create() throws DbException {

		String url = "jdbc:mysql://localhost:3306?serverTimezone=Israel";
		String user = "root";
		String password = "1234";

		try (Connection connect = DriverManager.getConnection(url, user, password);) {
			System.out.println("connected to: " + url);

			Statement stmt = connect.createStatement();
			String sql = "create database coupon_system_DB;";

			stmt.executeUpdate(sql);

			System.out.println("the coupon_system_DB database created");

		} catch (SQLException e) {
			throw new DbException("failed to create database: " + url, e);
		}

	}

}
