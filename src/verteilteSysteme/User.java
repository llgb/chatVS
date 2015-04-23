package verteilteSysteme;

public class User {
	private String username;
	private String passwordhash="";


	public String getPasswordhash() {
		return passwordhash;
	}

	public void setPasswordhash(String passwordhash) {
		this.passwordhash = passwordhash;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public User(String username) {
		super();
		this.username = username;
	}

	public User(String username, String passwordhash) {
		super();
		this.username = username;
		this.passwordhash = passwordhash;
	}
}
