package phase1.testHardCoded;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import phase1.core.clientsLogin.ClientType;
import phase1.core.clientsLogin.LoginManager;
import phase1.core.connections.ConnectionPool;
import phase1.core.dailyJob.CouponExpirationDailyJob;
import phase1.core.facade.AdminFacade;
import phase1.core.facade.ClientFacade;
import phase1.core.facade.CompanyFacade;
import phase1.core.facade.CustomerFacade;
import phase1.core.javaBeans.Category;
import phase1.core.javaBeans.Company;
import phase1.core.javaBeans.Coupon;
import phase1.core.javaBeans.Customer;
import phase1.core.systemExceptions.CouponSystemException;
import phase1.database.CreateDB;
import phase1.database.CreateTables;
import phase1.database.DeleteDB;

/**
 * class for testing all methods of the 3 client facades- hard coded
 */
public class Test {

	public static void main(String[] args) {

		startTest();
	}

	public static void startTest() {

		CouponExpirationDailyJob jobRunnable = new CouponExpirationDailyJob();
		Thread dailyJob = new Thread(jobRunnable);

		try {

			CreateDB.create();
			CreateTables.create();
			dailyJob.start();
			ConnectionPool.getInstance();
			System.out.println("<<< system is on >>>");

			System.out.println("\n====================Admin Facade====================\n");
			
			ClientFacade clientAdmin = LoginManager.getInstance().login("admin@admin.com", "admin",
					ClientType.ADMINISTRATOR);
			AdminFacade adminFacade = (AdminFacade) clientAdmin;
			adminMethods(adminFacade);

			System.out.println("====================Company Facade====================\n");
			
			ClientFacade clientCompany = LoginManager.getInstance().login("company1@company1.com", "12345",
					ClientType.COMPANY);
			CompanyFacade companyFacade = (CompanyFacade) clientCompany;
			companyMethods(companyFacade);
			
			System.out.println("====================Customer Facade====================\n");
			
			ClientFacade clientCustomer = LoginManager.getInstance().login("customer1@customer1.com", "11111",
					ClientType.CUSTOMER);
			CustomerFacade customerFacade = (CustomerFacade) clientCustomer;
			customerMethods(customerFacade);

		} catch (CouponSystemException e) {
			e.printStackTrace();

		} finally {

			try {
				jobRunnable.stop();
				dailyJob.interrupt();
				try {
					dailyJob.join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				DeleteDB.delete();
				ConnectionPool.getInstance().closeAllConnections();

			} catch (CouponSystemException e) {
				System.out.println(e);
			}
			System.out.println("\nGoodbye!");
			System.out.println("<<< system is off >>>");
		}
	}

	private static void customerMethods(CustomerFacade customerFacade) throws CouponSystemException {

		// purchases coupons
		customerFacade.purchaseCoupon(new Coupon(1));
		customerFacade.purchaseCoupon(new Coupon(6));
		customerFacade.purchaseCoupon(new Coupon(7));

		// gets all coupons
		System.out.println(customerFacade.getCustomerCoupons());

		// gets coupons by category
		System.out.println(customerFacade.getCustomerCoupons(Category.ELECTRICITY));

		// gets coupons with lower price
		System.out.println(customerFacade.getCustomerCoupons(50));

		// gets customer's details
		System.out.println(customerFacade.getCustomerDetails());

		System.out.println("\n=======================================================\n");
	}

	private static void companyMethods(CompanyFacade companyFacade) throws CouponSystemException {

		// adds coupons
		companyFacade.addCoupon(new Coupon(0, Category.ELECTRICITY, "coupon1", "electronics",
				dateConverter("10/11/2022"), dateConverter("10/01/2024"), 3, 30.5, "aaa"));
		companyFacade.addCoupon(new Coupon(0, Category.RESTAURANT, "coupon2", "rest food", dateConverter("10/03/2018"),
				dateConverter("10/01/2020"), 5, 15.99, "bbb"));
		companyFacade.addCoupon(new Coupon(0, Category.BABY, "coupon3", "baby stuff", dateConverter("10/05/2021"),
				dateConverter("10/07/2021"), 2, 50.88, "ccc"));
		companyFacade.addCoupon(new Coupon(0, Category.BABY, "coupon4", "baby stuff", dateConverter("10/04/2017"),
				dateConverter("10/10/2019"), 2, 50.88, "ddd"));
		companyFacade.addCoupon(new Coupon(0, Category.RESTAURANT, "coupon5", "rest food", dateConverter("10/01/2018"),
				dateConverter("10/08/2021"), 10, 5.5, "eee"));
		companyFacade.addCoupon(new Coupon(0, Category.HEALTH, "coupon6", "multi vitamin", dateConverter("10/11/2021"),
				dateConverter("10/09/2023"), 15, 45.88, "fff"));
		companyFacade.addCoupon(new Coupon(0, Category.VACATION, "coupon7", "visit Europe", dateConverter("10/10/2018"),
				dateConverter("10/10/2022"), 5, 550.5, "ggg"));

		// updates coupon
		companyFacade.updateCoupon(new Coupon(3, 0, Category.RESTAURANT, "coupon for rest", "rest food",
				dateConverter("10/12/2022"), dateConverter("15/01/2023"), 3, 60, "vvv"));

		// deletes coupon
		companyFacade.deleteCoupon(2);

		// gets all coupons
		System.out.println(companyFacade.getAllCompanyCoupons());

		// gets coupons of the category
		System.out.println(companyFacade.getCompanyCoupons(Category.BABY));

		// gets coupons with lower price
		System.out.println(companyFacade.getCompanyCoupons(30));

		// gets company's details
		System.out.println(companyFacade.getCompanyDetails());

		System.out.println("\n=======================================================\n");

	}

	private static void adminMethods(AdminFacade adminFacade) throws CouponSystemException {

		// adds companies
		adminFacade.addCompany(new Company("company1", "company1@company1.com", "12345"));
		adminFacade.addCompany(new Company("company2", "company2@company2.com", "54321"));
		adminFacade.addCompany(new Company("company3", "company3@company3.com", "11111"));
		adminFacade.addCompany(new Company("company4", "company4@company4.com", "22222"));
		adminFacade.addCompany(new Company("company5", "company5@company5.com", "12312"));

		// updates company
		adminFacade.updateCompany(new Company(3, "company3333", "company33@company33.com", "67890"));

		// deletes company
		adminFacade.deleteCompany(5);

		// gets all companies
		System.out.println(adminFacade.getAllCompanies());

		// gets one company
		System.out.println(adminFacade.getOneCompany(2));

		// adds customers
		adminFacade.addCustomer(new Customer("firstcust1", "lastcust1", "customer1@customer1.com", "11111"));
		adminFacade.addCustomer(new Customer("firstcust2", "lastcust2", "customer2@customer2.com", "22222"));
		adminFacade.addCustomer(new Customer("firstcust3", "lastcust3", "customer3@customer3.com", "12345"));
		adminFacade.addCustomer(new Customer("firstcust4", "lastcust4", "customer4@customer4.com", "45454"));
		adminFacade.addCustomer(new Customer("firstcust5", "lastcust5", "customer5@customer5.com", "56565"));

		// updates customer
		adminFacade.updateCustomer(new Customer(2, "regina", "brand", "abc@abc.com", "12312"));

		// deletes customer
		adminFacade.deleteCustomer(5);

		// gets all customers
		System.out.println(adminFacade.getAllCustomers());

		// gets one customer
		System.out.println(adminFacade.getOneCustomer(2));

		System.out.println("\n====================================================\n");
	}

	/**
	 * a method that converts a string date to Date
	 * 
	 * @param dateString
	 * @return date
	 */
	public static Date dateConverter(String dateString) {

		Date date = null;
		try {
			date = new SimpleDateFormat("dd/MM/yyyy").parse(dateString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

}
