package verteilteSysteme.couchdb;

import org.ektorp.support.CouchDbDocument;
import org.ektorp.support.TypeDiscriminator;

/**
 * Represents a chat user.
 */
@TypeDiscriminator("doc.name")
public class User extends CouchDbDocument {
	private static final long serialVersionUID = -7617735665846447374L;
	
	private String name;
	
	public User() {
		super();
	}
	
	public User(final String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return this.name;
	}
	
	public boolean equals(final User user) {
		return this.name.equals(user.getName());
	}
}
