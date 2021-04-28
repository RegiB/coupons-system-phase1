package phase1.testMVC;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
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

public class TestWithMenus {

	private static Scanner input = new Scanner(System.in);
	private static boolean Off = false;

	public static void main(String[] args) {

		mainMenu();
	}

	public static void mainMenu() {

		CouponExpirationDailyJob jobRunnable = new CouponExpirationDailyJob();
		Thread dailyJob = new Thread(jobRunnable);

		try {

			dailyJob.start();
			ConnectionPool.getInstance();
			System.out.println("<<< system is ready >>>");

			while (!Off) {

				System.out.println("      ______________________________________ ");
				System.out.println("     |                                      |");
				System.out.println("     |     WELCOME TO THE COUPON SYSTEM     |");
				System.out.println("     |______________________________________|");

				boolean correctEntryMain = false;

				while (!correctEntryMain) {

					System.out.print("\nEnter the system or Quit? (enter your choice: E / Q) ");
					String choice = input.nextLine().toUpperCase();

					switch (choice) {

					case "E":
						correctEntryMain = true;
						loginMenu();

					case "Q":
						correctEntryMain = true;
						Off = true;
						break;
						
					default:
						System.out.println("incorrect entry, please re-enter your choice");
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
				ConnectionPool.getInstance().closeAllConnections();
				input.close();
				
			} catch (CouponSystemException e) {
				e.printStackTrace();
			}
			System.out.println("\nGoodbye, see you next time!");
			System.out.println("<<< system is off >>>");
			
		}

	}

	public static void loginMenu() throws CouponSystemException {

		boolean correctEntryLogin = false;

		while (!correctEntryLogin) {

			System.out.print("\nLogin as 1-ADMIN / 2-COMPANY / 3-CUSTOMER (enter the number of your choice): ");
			String loginChoice = input.nextLine();

			switch (loginChoice) {

			case "1": {
				correctEntryLogin = true;
				boolean correct = false;
				int count = 3;

				while (!correct && count > 0) {
					System.out.println("\nenter your email:");
					String adminEmail = input.nextLine();
					System.out.println("enter your password:");
					String adminPass = input.nextLine();

					ClientFacade clientAdmin = LoginManager.getInstance().login(adminEmail, adminPass,
							ClientType.ADMINISTRATOR);
					if (clientAdmin != null) {
						correct = true;
						AdminFacade adminFacade = (AdminFacade) clientAdmin;
						while (!Off) {
							adminMenu(adminFacade);
						}
						break;
					} else {
						count--;
						System.out.println("INVALID CREDENTIALS!!!");
						System.out.println("you have: " + count + " more try(tries)");
					}
				}
				if (count == 0) {
					System.out.println("\nlogin failed- system is closing");
					break;
				}
				if (Off) {
					break;
				}

			}

			case "2": {
				correctEntryLogin = true;
				boolean correct = false;
				int count = 3;

				while (!correct && count > 0) {
					System.out.println("\nenter your email:");
					String companyEmail = input.nextLine();
					System.out.println("enter your password:");
					String companyPass = input.nextLine();

					ClientFacade clientCompany = LoginManager.getInstance().login(companyEmail, companyPass,
							ClientType.COMPANY);
					if (clientCompany != null) {
						correct = true;
						CompanyFacade companyFacade = (CompanyFacade) clientCompany;
						while (!Off) {
							companyMenu(companyFacade);
						}
						break;
					} else {
						count--;
						System.out.println("INVALID CREDENTIALS!!!");
						System.out.println("you have: " + count + " more try(tries)");
					}
				}

				if (count == 0) {
					System.out.println("\nlogin failed- system is closing");
					break;
				}
				if (Off) {
					break;
				}

			}
			case "3": {
				correctEntryLogin = true;
				boolean correct = false;
				int count = 3;

				while (!correct && count > 0) {
					System.out.println("\nenter your email:");
					String customerEmail = input.nextLine();
					System.out.println("enter your password:");
					String customerPass = input.nextLine();

					ClientFacade clientCustomer = LoginManager.getInstance().login(customerEmail, customerPass,
							ClientType.CUSTOMER);

					if (clientCustomer != null) {
						correct = true;
						CustomerFacade customerFacade = (CustomerFacade) clientCustomer;
						while (!Off) {
							customerMenu(customerFacade);
						}
						break;
					} else {
						count--;
						System.out.println("INVALID CREDENTIALS!!!");
						System.out.println("you have: " + count + " more try(tries)");
					}
				}

				if (count == 0) {
					System.out.println("\nlogin failed- system is closing");
					break;
				}
				if (Off) {
					break;
				}

			}
			default:
				System.out.println("incorrect entry, please re-enter your choice");
				break;
			}
		}

	}

	private static void customerMenu(CustomerFacade customerFacade) throws CouponSystemException {

		System.out.println("      ______________________________________ ");
		System.out.println("     |                                      |");
		System.out.println("     |               CUSTOMER               |");
		System.out.println("     |______________________________________|");
		System.out.println();
		System.out.println("      purchase coupon....................(1)");
		System.out.println("      all MY coupons details.............(2)");
		System.out.println("      MY coupons details by category.....(3)");
		System.out.println("      MY coupons details by price........(4)");
		System.out.println("      customer details...................(5)");
		System.out.println("      quit...............................(6)");

		System.out.print("\nenter your action number:");
		String command = input.nextLine();

		switch (command) {
		case "1":
			purchaseCoupon(customerFacade);
			break;
		case "2":
			getAllCoupons(customerFacade);
			break;
		case "3":
			couponsByCategory(customerFacade);
			break;
		case "4":
			couponsByPrice(customerFacade);
			break;
		case "5":
			customerDetails(customerFacade);
			break;
		case "6":
			Off = true;
			break;
		default:
			System.out.println("invalid action number");
			break;
		}

	}

	private static void customerDetails(CustomerFacade customerFacade) throws CouponSystemException {

		System.out.println("\n<<< customer details >>>");
		Customer customerLogin = customerFacade.getCustomerDetails();
		System.out.println("\n" + customerLogin);

	}

	private static void couponsByPrice(CustomerFacade customerFacade) throws CouponSystemException {

		System.out.println("\n<<< purchased coupons details by price >>>");
		System.out.print("\nenter maximum price: ");
		double maxPrice = Double.parseDouble(input.nextLine());
		List<Coupon> couponsPrice;
		couponsPrice = customerFacade.getCustomerCoupons(maxPrice);
		System.out.println("\n" + couponsPrice);

	}

	private static void couponsByCategory(CustomerFacade customerFacade) throws CouponSystemException {

		System.out.println("\n<<< purchased coupons details by category >>>");
		List<Coupon> couponsCategory;
		System.out.print("\nenter category, choose from categories " + Category.categories() + ": ");
		Category category = Category.valueOf(input.nextLine().toUpperCase());
		couponsCategory = customerFacade.getCustomerCoupons(category);
		System.out.println("\n" + couponsCategory);

	}

	private static void getAllCoupons(CustomerFacade customerFacade) throws CouponSystemException {

		System.out.println("\n<<< all purchased coupons details >>>");
		List<Coupon> allCoupons;
		allCoupons = customerFacade.getCustomerCoupons();
		System.out.println("\n" + allCoupons);

	}

	private static void purchaseCoupon(CustomerFacade customerFacade) throws CouponSystemException {

		System.out.println("\navailable coupons for purchase: \n" + customerFacade.showAvailableCoupons());
		
		System.out.println("\n<<< purchase coupon >>>");
		System.out.print("\nenter coupon id: ");
		int id = Integer.parseInt(input.nextLine());

		customerFacade.purchaseCoupon(new Coupon(id));

	}

	private static void companyMenu(CompanyFacade companyFacade) throws CouponSystemException {

		System.out.println("      ______________________________________ ");
		System.out.println("     |                                      |");
		System.out.println("     |                COMPANY               |");
		System.out.println("     |______________________________________|");
		System.out.println();
		System.out.println("      add coupon ........................(1)");
		System.out.println("      update coupon......................(2)");
		System.out.println("      delete coupon......................(3)");
		System.out.println("      all coupons details................(4)");
		System.out.println("      coupons details by category........(5)");
		System.out.println("      coupons details by price...........(6)");
		System.out.println("      company details....................(7)");
		System.out.println("      quit...............................(8)");

		System.out.print("\nenter your action number:");
		String command = input.nextLine();

		switch (command) {
		case "1":
			addCoupon(companyFacade);
			break;
		case "2":
			updateCoupon(companyFacade);
			break;
		case "3":
			deleteCoupon(companyFacade);
			break;
		case "4":
			getAllCoupons(companyFacade);
			break;
		case "5":
			couponsByCategory(companyFacade);
			break;
		case "6":
			couponsByPrice(companyFacade);
			break;
		case "7":
			companyDetails(companyFacade);
			break;
		case "8":
			Off = true;
			break;
		default:
			System.out.println("invalid action number!");
			break;
		}
	}

	private static void companyDetails(CompanyFacade companyFacade) throws CouponSystemException {

		System.out.println("\n<<< company details >>>");
		Company companyLogin = companyFacade.getCompanyDetails();
		System.out.println("\n" + companyLogin);

	}

	private static void couponsByPrice(CompanyFacade companyFacade) throws CouponSystemException {

		System.out.println("\n<<< coupons details by price >>>");
		System.out.print("\nenter maximum price: ");
		double maxPrice = Double.parseDouble(input.nextLine());
		List<Coupon> couponsPrice;
		couponsPrice = companyFacade.getCompanyCoupons(maxPrice);
		System.out.println("\n" + couponsPrice);

	}

	private static void couponsByCategory(CompanyFacade companyFacade) throws CouponSystemException {

		System.out.println("\n<<< coupons details by category >>>");
		List<Coupon> couponsCategory;
		System.out.print("\nenter category, choose from categories " + Category.categories() + ": ");
		Category category = Category.valueOf(input.nextLine().toUpperCase());
		couponsCategory = companyFacade.getCompanyCoupons(category);
		System.out.println("\n" + couponsCategory);

	}

	private static void getAllCoupons(CompanyFacade companyFacade) throws CouponSystemException {

		System.out.println("\n<<< all coupons details >>>");
		List<Coupon> allCoupons;
		allCoupons = companyFacade.getAllCompanyCoupons();
		System.out.println("\n" + allCoupons);

	}

	private static void deleteCoupon(CompanyFacade companyFacade) throws CouponSystemException {

		System.out.println("\n<<< deleting coupon >>>");
		System.out.print("\nenter coupon id: ");
		int id = Integer.parseInt(input.nextLine());
		companyFacade.deleteCoupon(id);
	}

	private static void updateCoupon(CompanyFacade companyFacade) throws CouponSystemException{

		System.out.println("\n<<< updating coupon details >>>");
		System.out.print("\nenter coupon id: ");
		int id = Integer.parseInt(input.nextLine());
		int companyID = 0;
		System.out.print("\nenter new coupon category, choose from categories " + Category.categories() + ": ");
		Category category = Category.valueOf(input.nextLine().toUpperCase());
		System.out.print("enter new coupon title: ");
		String title = input.nextLine();
		System.out.print("enter new coupon description: ");
		String description = input.nextLine();
		System.out.print("enter new coupon start date \"dd/mm/yyyy\": ");
		Date startDate = dateConverter(input.nextLine());
		System.out.print("enter new coupon end date \"dd/mm/yyyy\": ");
		Date endDate = dateConverter(input.nextLine());
		System.out.print("enter new amount of coupon: ");
		int amount = Integer.parseInt(input.nextLine());
		System.out.print("enter new coupon price: ");
		double price = Double.parseDouble(input.nextLine());
		System.out.print("enter new coupon image: ");
		String image = input.nextLine();

		companyFacade.updateCoupon(
				new Coupon(id, companyID, category, title, description, startDate, endDate, amount, price, image));

	}

	private static void addCoupon(CompanyFacade companyFacade) throws CouponSystemException {

		System.out.println("\n<<< adding coupon >>>");
		int companyID = 0;
		System.out.print("\nenter coupon category, choose from categories " + Category.categories() + ": ");
		Category category = Category.valueOf(input.nextLine().toUpperCase());
		System.out.print("enter coupon title: ");
		String title = input.nextLine();
		System.out.print("enter coupon description: ");
		String description = input.nextLine();
		Date startDate = new Date(System.currentTimeMillis());
		System.out.print("enter coupon end date \"dd/mm/yyyy\": ");
		Date endDate = dateConverter(input.nextLine());
		System.out.print("enter amount of coupon: ");
		int amount = Integer.parseInt(input.nextLine());
		System.out.print("enter coupon price: ");
		double price = Double.parseDouble(input.nextLine());
		System.out.print("enter coupon image: ");
		String image = input.nextLine();

		companyFacade.addCoupon(
				new Coupon(companyID, category, title, description, startDate, endDate, amount, price, image));
	}

	public static void adminMenu(AdminFacade adminFacade) throws CouponSystemException {

		System.out.println("      ______________________________________ ");
		System.out.println("     |                                      |");
		System.out.println("     |            ADMINISTRATOR             |");
		System.out.println("     |______________________________________|");
		System.out.println();
		System.out.println("      add company........................(1)");
		System.out.println("      update company.....................(2)");
		System.out.println("      delete company.....................(3)");
		System.out.println("      all companies details..............(4)");
		System.out.println("      single company details.............(5)");
		System.out.println("      add customer.......................(6)");
		System.out.println("      update customer....................(7)");
		System.out.println("      delete customer....................(8)");
		System.out.println("      all customers details..............(9)");
		System.out.println("      single customer details............(10)");
		System.out.println("      quit...............................(11)");

		System.out.print("\nenter your action number: ");
		String command = input.nextLine();

		switch (command) {
		case "1":
			addCompany(adminFacade);
			break;
		case "2":
			updateCompany(adminFacade);
			break;
		case "3":
			deleteCompany(adminFacade);
			break;
		case "4":
			getAllCompanies(adminFacade);
			break;
		case "5":
			getCompany(adminFacade);
			break;
		case "6":
			addCustomer(adminFacade);
			break;
		case "7":
			updateCustomer(adminFacade);
			break;
		case "8":
			deleteCustomer(adminFacade);
			break;
		case "9":
			getAllCustomers(adminFacade);
			break;
		case "10":
			getCustomer(adminFacade);
			break;
		case "11":
			Off = true;
			break;
		default:
			System.out.println("invalid action number!");
			break;
		}

	}

	private static void getCustomer(AdminFacade adminFacade) throws CouponSystemException {

		System.out.print("\nenter customer id: ");
		int id = Integer.parseInt(input.nextLine());

		System.out.println("\n<<< customer details >>>");
		Customer customer;
		customer = adminFacade.getOneCustomer(id);
		System.out.println("\n" + customer);

	}

	private static void getAllCustomers(AdminFacade adminFacade) throws CouponSystemException {

		System.out.println("\n<<< all customers details >>>");
		List<Customer> allCustomers;
		allCustomers = adminFacade.getAllCustomers();
		System.out.println("\n" + allCustomers);

	}

	private static void deleteCustomer(AdminFacade adminFacade) throws CouponSystemException {

		System.out.println("\n<<< deleting customer >>>");
		System.out.print("\nenter customer id: ");
		int id = Integer.parseInt(input.nextLine());
		adminFacade.deleteCustomer(id);

	}

	private static void updateCustomer(AdminFacade adminFacade) throws CouponSystemException {

		System.out.println("\n<<< updating customer >>>");
		System.out.print("\nenter customer id: ");
		int id = Integer.parseInt(input.nextLine());
		System.out.print("enter new customer first name: ");
		String firstName = input.nextLine();
		System.out.print("enter new customer last name: ");
		String lastName = input.nextLine();
		System.out.print("enter new customer email: ");
		String email = input.nextLine();
		System.out.print("enter new customer password: ");
		String password = input.nextLine();

		adminFacade.updateCustomer(new Customer(id, firstName, lastName, email, password));

	}

	private static void addCustomer(AdminFacade adminFacade) throws CouponSystemException {

		System.out.println("\n<<< adding customer >>>");
		System.out.print("\nenter customer first name: ");
		String firstName = input.nextLine();
		System.out.print("enter customer last name: ");
		String lastName = input.nextLine();
		System.out.print("enter customer email: ");
		String email = input.nextLine();
		System.out.print("enter customer password: ");
		String password = input.nextLine();

		adminFacade.addCustomer(new Customer(firstName, lastName, email, password));

	}

	private static void getCompany(AdminFacade adminFacade) throws CouponSystemException {

		System.out.print("\nenter company id: ");
		int id = Integer.parseInt(input.nextLine());

		System.out.println("\n<<< company details >>>");
		Company company;
		company = adminFacade.getOneCompany(id);
		System.out.println("\n" + company);

	}

	private static void getAllCompanies(AdminFacade adminFacade) throws CouponSystemException {

		System.out.println("\n<<< all companies details >>>");
		List<Company> allCompanies;
		allCompanies = adminFacade.getAllCompanies();
		System.out.println("\n" + allCompanies);
	}

	private static void deleteCompany(AdminFacade adminFacade) throws CouponSystemException {

		System.out.println("\n<<< deleting company >>>");
		System.out.print("\nenter company id: ");
		int id = Integer.parseInt(input.nextLine());
		adminFacade.deleteCompany(id);
	}

	private static void updateCompany(AdminFacade adminFacade) throws CouponSystemException {

		System.out.println("\n<<< updating company details >>>");
		System.out.print("\nenter company id: ");
		int id = Integer.parseInt(input.nextLine());
		String name = "";
		System.out.print("enter new company email: ");
		String email = input.nextLine();
		System.out.print("enter new company password: ");
		String password = input.nextLine();

		adminFacade.updateCompany(new Company(id, name, email, password));

	}

	private static void addCompany(AdminFacade adminFacade) throws CouponSystemException {

		System.out.println("\n<<< adding company >>>");
		System.out.print("\nenter company name: ");
		String name = input.nextLine();
		System.out.print("enter company email: ");
		String email = input.nextLine();
		System.out.print("enter company password: ");
		String password = input.nextLine();

		adminFacade.addCompany(new Company(name, email, password));
	}

	public static Date dateConverter(String dateString) {

		Date date = null;
		try {
			date = new SimpleDateFormat("dd/MM/yyyy").parse(dateString);
			return date;

		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
}
