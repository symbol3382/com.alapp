package alapp.model;

public class User {
	private String id;
	private String firstName;
	private String lastName;
	private String username;
	private String password;
	private String active;
	private String userType;
	
	public User() {
		
	}
	
//	constructor injection
	public User(String id, String firstName, String lastName, String username, String password, String active,
			String userType) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.username = username;
		this.password = password;
		this.active = active;
		this.userType = userType;
	}
	
//	ID
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
//	first_name
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
//	last_name
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
//	username
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	
//	password
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
//	active
	public String getActive() {
		return active;
	}
	public void setActive(String active) {
		this.active = active;
	}
	
//	user_type
	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}

	@Override
	public String toString() {
		return firstName + " " + lastName;
	}
	
	
}

