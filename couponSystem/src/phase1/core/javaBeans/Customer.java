package phase1.core.javaBeans;

import java.util.ArrayList;
import java.util.List;

/**
 * customer objects are created from this class
 *
 */
public class Customer {
	
	private int id;
	private String firstName;
	private String lastName;
	private String email;
	private String password;
	
	private List<Coupon> coupons = new ArrayList<Coupon>();
	
	/**
	 * default constructor
	 */
	public Customer() {
	}
	
	/**
	 * constructor that uses only id field
	 * @param id
	 */
	public Customer(int id) {
		this.id = id;
	}

	/**
	 * constructor uses all fields except id
	 * @param firstName
	 * @param lastName
	 * @param email
	 * @param password
	 * @param coupons
	 */
	public Customer(String firstName, String lastName, String email, String password) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
	}

	/**
	 * full constructor uses all class fields
	 * 
	 * @param id
	 * @param firstName
	 * @param lastName
	 * @param email
	 * @param password
	 */
	public Customer(int id, String firstName, String lastName, String email, String password) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
	}

	// getters and setters
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<Coupon> getCoupons() {
		return coupons;
	}

	@Override
	public String toString() {
		return "[customer id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", email=" + email
				+ ", password=" + password + "]";
	}
	
	

}
