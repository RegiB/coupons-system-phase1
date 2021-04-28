package phase1.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import phase1.core.systemExceptions.DbException;

/**
 * class for creating coupon system tables: companies, coupons, customers and
 * customers_vs_coupons (no categories table- category name in coupons table
 * instead)
 *
 */
public class CreateTables {

	public static void create() throws DbException {

		String url = "jdbc:mysql://localhost:3306/coupon_system_DB?serverTimezone=Israel";
		String user = "root";
		String password = "1234";

		String sqlCompany = "CREATE TABLE companies(";
		sqlCompany += "id int AUTO_INCREMENT, ";
		sqlCompany += "name varchar(255), ";
		sqlCompany += "email varchar(255), ";
		sqlCompany += "password varchar(255), ";
		sqlCompany += "PRIMARY KEY (id)";
		sqlCompany += ");";

		String sqlCustomer = "CREATE TABLE customers(";
		sqlCustomer += "id int AUTO_INCREMENT, ";
		sqlCustomer += "first_name varchar(255), ";
		sqlCustomer += "last_name varchar(255), ";
		sqlCustomer += "email varchar(255), ";
		sqlCustomer += "password varchar(255), ";
		sqlCustomer += "PRIMARY KEY (id)";
		sqlCustomer += ");";

		String sqlCoupon = "CREATE TABLE coupons(";
		sqlCoupon += "id int AUTO_INCREMENT, ";
		sqlCoupon += "company_id int, ";
		sqlCoupon += "category_name varchar(255), ";
		sqlCoupon += "title varchar(255), ";
		sqlCoupon += "description varchar(255), ";
		sqlCoupon += "start_date date, ";
		sqlCoupon += "end_date date, ";
		sqlCoupon += "amount int, ";
		sqlCoupon += "price float, ";
		sqlCoupon += "image varchar(255), ";
		sqlCoupon += "PRIMARY KEY (id), ";
		sqlCoupon += "FOREIGN KEY (company_id) REFERENCES companies(id) ";
		sqlCoupon += ");";

		String sqlCustVsCoup = "CREATE TABLE customers_vs_coupons(";
		sqlCustVsCoup += "customer_id int, ";
		sqlCustVsCoup += "coupon_id int, ";
		sqlCustVsCoup += "PRIMARY KEY (customer_id, coupon_id), ";
		sqlCustVsCoup += "FOREIGN KEY (customer_id) REFERENCES customers(id), ";
		sqlCustVsCoup += "FOREIGN KEY (coupon_id) REFERENCES coupons(id)";
		sqlCustVsCoup += ");";

		try (Connection con = DriverManager.getConnection(url, user, password);) {

			Statement stmt = con.createStatement();

			stmt.execute(sqlCompany);
			stmt.execute(sqlCustomer);
			stmt.execute(sqlCoupon);
			stmt.execute(sqlCustVsCoup);

			System.out.println("all tables created");

		} catch (SQLException e) {
			throw new DbException("creating tables failed", e);
		}
	}

}
