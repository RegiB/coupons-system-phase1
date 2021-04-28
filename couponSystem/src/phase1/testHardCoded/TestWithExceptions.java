package phase1.testHardCoded;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

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
 * class for testing all methods of the 3 client facades - hard coded with
 * try&catch for exceptions
 */
public class TestWithExceptions {

	private static Scanner input = new Scanner(System.in);
	private static boolean Off;

	public static void main(String[] args) {

		menu();
	}

	public static void menu() {

		CouponExpirationDailyJob jobRunnable = new CouponExpirationDailyJob();
		Thread dailyJob = new Thread(jobRunnable);

		try {
			CreateDB.create();
			CreateTables.create();
			dailyJob.start();
			ConnectionPool.getInstance();

			System.out.println("<<< system is ready >>>");
			System.out.println("      ______________________________________________ ");
			System.out.println("     |                                              |");
			System.out.println("     |     WELCOME TO THE COUPON SYSTEM TESTING     |");
			System.out.println("     |______________________________________________|");

			while (!Off) {
				boolean correctEntry = false;

				while (!correctEntry) {
					System.out.println("\nStart testing for one of the clients or quit the system: ");
					System.out.println("1- Administrator");
					System.out.println("2- Company");
					System.out.println("3- Customer");
					System.out.println("4- quit");
					System.out.print("\nenter your choice number: ");

					String testChoice = input.nextLine();

					switch (testChoice) {
					case "1": {
						correctEntry = true;

						ClientFacade clientAdmin = LoginManager.getInstance().login("admin@admin.com", "admin",
								ClientType.ADMINISTRATOR);
						AdminFacade adminFacade = (AdminFacade) clientAdmin;
						adminMethods(adminFacade);
					}
						break;
					case "2": {
						correctEntry = true;

						ClientFacade clientCompany = LoginManager.getInstance().login("company1@company1.com", "12345",
								ClientType.COMPANY);
						CompanyFacade companyFacade = (CompanyFacade) clientCompany;
						companyMethods(companyFacade);
					}
						break;
					case "3": {
						correctEntry = true;

						ClientFacade clientCustomer = LoginManager.getInstance().login("customer1@customer1.com",
								"11111", ClientType.CUSTOMER);
						CustomerFacade customerFacade = (CustomerFacade) clientCustomer;
						customerMethods(customerFacade);
					}
						break;
					case "4": {
						correctEntry = true;
						Off = true;
						System.out.println();

					}
						break;
					default:
						System.out.print("\nincorrect entry, please re-enter your choice \n");
						break;
					}
				}
			}

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
				try {
					DeleteDB.delete();
				} catch (Exception e) {
					e.printStackTrace();
				}
				ConnectionPool.getInstance().closeAllConnections();
				input.close();

			} catch (CouponSystemException e) {
				e.printStackTrace();
			}
			System.out.println("\nGoodbye!");
			System.out.println("<<< system is off >>>");
		}
	}

	private static void customerMethods(CustomerFacade customerFacade) throws CouponSystemException {

		System.out.println("\n====================Customer Facade====================\n");

		// purchases coupons
		try {
			customerFacade.purchaseCoupon(new Coupon(1));
			customerFacade.purchaseCoupon(new Coupon(6));
			customerFacade.purchaseCoupon(new Coupon(7));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			customerFacade.purchaseCoupon(new Coupon(1));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			customerFacade.purchaseCoupon(new Coupon(11));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			customerFacade.purchaseCoupon(new Coupon(4));
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			customerFacade.purchaseCoupon(new Coupon(8));
		} catch (Exception e) {
			e.printStackTrace();
		}
		// get all coupons
		try {
			System.out.println(customerFacade.getCustomerCoupons());
		} catch (Exception e) {
			e.printStackTrace();
		}

		// gets coupons by category
		try {
			System.out.println(customerFacade.getCustomerCoupons(Category.ELECTRICITY));
			System.out.println(customerFacade.getCustomerCoupons(Category.SPORTS));
		} catch (Exception e) {
			e.printStackTrace();
		}

		// gets coupons with lower price
		try {
			System.out.println(customerFacade.getCustomerCoupons(50));
			System.out.println(customerFacade.getCustomerCoupons(3));
		} catch (Exception e) {
			e.printStackTrace();
		}

		// gets customer's details
		try {
			System.out.println(customerFacade.getCustomerDetails());
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("\n=======================================================\n");
	}

	private static void companyMethods(CompanyFacade companyFacade) throws CouponSystemException {

		System.out.println("\n====================Company Facade====================\n");

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
		companyFacade.addCoupon(new Coupon(0, Category.VACATION, "coupon7", "bora bora", dateConverter("10/10/2018"),
				dateConverter("10/10/2022"), 5, 550.5, "ggg"));
		companyFacade.addCoupon(new Coupon(0, Category.BEAUTY, "coupon8", "makeup products",
				dateConverter("10/05/2019"), dateConverter("10/10/2023"), 0, 45.5, "hhh"));

		try {
			companyFacade.addCoupon(new Coupon(0, Category.ELECTRICITY, "coupon1", "electronics",
					dateConverter("10/11/2022"), dateConverter("10/01/2024"), 3, 30.5, "aaa"));
		} catch (Exception e) {
			e.printStackTrace();
		}

		// updates coupon
		companyFacade.updateCoupon(new Coupon(3, 0, Category.RESTAURANT, "coupon for rest", "rest food",
				dateConverter("10/12/2022"), dateConverter("15/01/2023"), 3, 60, "vvv"));

		try {
			companyFacade.updateCoupon(new Coupon(11, 0, Category.RESTAURANT, "coupon for rest", "rest food",
					dateConverter("10/12/2022"), dateConverter("15/01/2023"), 3, 60, "vvv"));
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			companyFacade.updateCoupon(new Coupon(8, 0, Category.BEAUTY, "coupon8", "makeup products",
					dateConverter("10/05/2019"), dateConverter("10/10/2023"), 0, 45.5, "hhh"));
		} catch (Exception e) {
			e.printStackTrace();
		}

		// deletes coupon
		try {
			companyFacade.deleteCoupon(2);
			companyFacade.deleteCoupon(9);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// gets all coupons
		try {
			System.out.println(companyFacade.getAllCompanyCoupons());
		} catch (Exception e) {
			e.printStackTrace();
		}

		// gets coupons of the category
		try {
			System.out.println(companyFacade.getCompanyCoupons(Category.BABY));
			System.out.println(companyFacade.getCompanyCoupons(Category.SPORTS));
		} catch (Exception e) {
			e.printStackTrace();
		}

		// gets coupons with lower price
		try {
			System.out.println(companyFacade.getCompanyCoupons(50));
			System.out.println(companyFacade.getCompanyCoupons(3));
		} catch (Exception e) {
			e.printStackTrace();
		}

		// gets company's details
		try {
			System.out.println(companyFacade.getCompanyDetails());
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("\n=======================================================\n");

	}

	private static void adminMethods(AdminFacade adminFacade) throws CouponSystemException {

		System.out.println("\n====================Admin Facade====================\n");

		// adds companies
		adminFacade.addCompany(new Company("company1", "company1@company1.com", "12345"));
		adminFacade.addCompany(new Company("company2", "company2@company2.com", "54321"));
		adminFacade.addCompany(new Company("company3", "company3@company3.com", "11111"));
		adminFacade.addCompany(new Company("company4", "company4@company4.com", "22222"));
		adminFacade.addCompany(new Company("company5", "company5@company5.com", "12312"));

		try {
			adminFacade.addCompany(new Company("company1", "company1@company1.com", "12345"));
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			adminFacade.addCompany(new Company("company5", "abc@abc5.com", "252252"));
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			adminFacade.addCompany(new Company("cmp5", "company5@company5.com", "232454"));
		} catch (Exception e) {
			e.printStackTrace();
		}

		// updates company
		adminFacade.updateCompany(new Company(3, "company3333", "company33@company33.com", "45245"));

		try {
			adminFacade.updateCompany(new Company(3, "company1", "company1@company1.com", "245242"));
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			adminFacade.updateCompany(new Company(10, "company1", "company1@company1.com", "67466"));
		} catch (Exception e) {
			e.printStackTrace();
		}

		// deletes company
		try {
			adminFacade.deleteCompany(5);
			adminFacade.deleteCompany(10);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// gets all companies
		try {
			System.out.println(adminFacade.getAllCompanies());
		} catch (Exception e) {
			e.printStackTrace();
		}

		// gets one company
		try {
			System.out.println(adminFacade.getOneCompany(2));
			System.out.println(adminFacade.getOneCompany(10));
		} catch (Exception e) {
			e.printStackTrace();
		}

		// adds customers
		adminFacade.addCustomer(new Customer("firstcust1", "lastcust1", "customer1@customer1.com", "11111"));
		adminFacade.addCustomer(new Customer("firstcust2", "lastcust2", "customer2@customer2.com", "22222"));
		adminFacade.addCustomer(new Customer("firstcust3", "lastcust3", "customer3@customer3.com", "12345"));
		adminFacade.addCustomer(new Customer("firstcust4", "lastcust4", "customer4@customer4.com", "45454"));
		adminFacade.addCustomer(new Customer("firstcust5", "lastcust5", "customer5@customer5.com", "56565"));

		try {
			adminFacade.addCustomer(new Customer("firstcust1", "lastcust1", "customer1@customer1.com", "11111"));
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			adminFacade.addCustomer(new Customer("abc1", "abcLast1", "customer1@customer1.com", "12345"));
		} catch (Exception e) {
			e.printStackTrace();
		}

		// updates customer
		adminFacade.updateCustomer(new Customer(2, "regina", "brand", "abc@abc.com", "12312"));

		try {
			adminFacade.updateCustomer(new Customer(7, "regina", "brand", "abc@abc.com", "12312"));
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			adminFacade.updateCustomer(new Customer(2, "mike", "bbb", "abc@abc.com", "65455"));
		} catch (Exception e) {
			e.printStackTrace();
		}

		// deletes customer
		try {
			adminFacade.deleteCustomer(5);
			adminFacade.deleteCustomer(8);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// gets all customers
		try {
			System.out.println(adminFacade.getAllCustomers());
		} catch (Exception e) {
			e.printStackTrace();
		}

		// gets one customer
		try {
			System.out.println(adminFacade.getOneCustomer(2));
			System.out.println(adminFacade.getOneCustomer(8));
		} catch (Exception e) {
			e.printStackTrace();
		}

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
