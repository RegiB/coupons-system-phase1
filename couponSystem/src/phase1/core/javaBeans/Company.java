package phase1.core.javaBeans;

import java.util.ArrayList;
import java.util.List;

/**
 * company objects are created from this class
 *
 */
public class Company {
	
	private int id;
	private String name;
	private String email;
	private String password;
	
	private List<Coupon> coupons = new ArrayList<Coupon>();
	
	/**
	 * default constructor
	 */
	public Company() {
	}
	
	/**
	 * constructor that uses only id field
	 * @param id
	 */
	public Company(int id) {
		this.id = id;
	}
	
	/**
	 * constructor that uses all fields except id
	 * @param name
	 * @param email
	 * @param password
	 */
	public Company(String name, String email, String password) {
		this.name = name;
		this.email = email;
		this.password = password;
	}

	/**
	 * full constructor uses all class fields
	 * 
	 * @param id
	 * @param name
	 * @param email
	 * @param password
	 */
	public Company(int id, String name, String email, String password) {
		this.id = id;
		this.name = name;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
		return "[company id=" + id + ", name=" + name + ", email=" + email + ", password=" + password + "]";
	}
	
	

}
