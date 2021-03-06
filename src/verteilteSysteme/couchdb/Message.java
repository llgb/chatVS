package verteilteSysteme.couchdb;


import java.util.Locale;
import java.util.TimeZone;

import org.ektorp.support.CouchDbDocument;
import org.ektorp.support.TypeDiscriminator;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

/**
 * Represents a chat message.
 */
@TypeDiscriminator("doc.owner && doc.content && doc.created")
public class Message extends CouchDbDocument {
	private static final long serialVersionUID = -3904251765374033818L;

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
	
	@Override
	public String toString() {
		return new StringBuilder()
					.append(this.created.toDateTime(DateTimeZone.forTimeZone(TimeZone.getTimeZone("Europe/Berlin") )).toString("dd/MM/yyyy - HH:mm:ss", new Locale("de","DE")))
					.append(" ")
					.append(this.owner)
					.append(":	 ")
					.append(this.content)
					.toString();
	}
}

