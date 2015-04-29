package verteilteSysteme.couchdb;

import org.ektorp.CouchDbConnector;
import org.ektorp.support.CouchDbRepositorySupport;

public class UserRepository extends CouchDbRepositorySupport<User>{
	public UserRepository(final CouchDbConnector connector) {
		super(User.class, connector);
	}
	
	public int getNrOfUsers() {
		return this.getAll().size();
	}
	
	public boolean exists(final User userToCheck) {
		for (User user : this.getAll()) {
			if (user.equals(userToCheck)) {
				return true;
			}
		}
		
		return false;
	}
}
