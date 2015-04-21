package verteilteSysteme;

public class Message {
	private User owner;
	private String content;
	
	public User getUser() {
		return owner;
	}
	public void setUser(User user) {
		this.owner = user;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}

	
}
