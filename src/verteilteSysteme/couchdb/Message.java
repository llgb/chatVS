package verteilteSysteme.couchdb;

import org.ektorp.support.CouchDbDocument;
import org.joda.time.DateTime;

public class Message extends CouchDbDocument {
	private static final long serialVersionUID = 1L;

	private String owner;
	
	private String content;
	
	private DateTime created;
	
	public Message() {
		super();
	}

	public Message(final String owner, final String content, final DateTime created) {
		super();
		this.owner   = owner;
		this.content = content;
		this.created = created;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(final String owner) {
		this.owner = owner;
	}

	public String getContent() {
		return content;
	}

	public void setContent(final String content) {
		this.content = content;
	}
	
	public DateTime getCreated() {
		return created;
	}
	
	public void setCreated(final DateTime created) {
		this.created = created;
	}
}

