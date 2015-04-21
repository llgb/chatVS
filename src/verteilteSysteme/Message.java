package verteilteSysteme;

import java.sql.Timestamp;
import java.util.Calendar;

public class Message {
	private User owner;
	private String content;
	private Timestamp timestamp;
	
	
	public User getOwner() {
		return owner;
	}
	public void setOwner(User user) {
		this.owner = user;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	public Timestamp getTimestamp() {
		return timestamp;
	}

	public String toString() {
		return this.timestamp  + " " + this.getOwner().getUsername() + ": " + this.getContent();
	}
	
	public Timestamp generateTimestamp() {
		// 1) create a java calendar instance
		Calendar calendar = Calendar.getInstance();
		 
		// 2) get a java.util.Date from the calendar instance.
//		    this date will represent the current instant, or "now".
		java.util.Date now = calendar.getTime();
		 
		// 3) a java current time (now) instance
		java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(now.getTime());
		
		return currentTimestamp;
	}
	public Message(User owner, String content, Timestamp timestamp) {
		super();
		this.owner = owner;
		this.content = content;
		this.timestamp = generateTimestamp();
	}
	
	
}
