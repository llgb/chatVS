package verteilteSysteme.couchdb;

import org.ektorp.support.CouchDbDocument;

public class User extends CouchDbDocument {
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
}
