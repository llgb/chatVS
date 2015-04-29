package verteilteSysteme.couchdb;

import org.ektorp.CouchDbConnector;
import org.ektorp.support.CouchDbRepositorySupport;

/**
 * Provides CRUD methods and more for user objects.
 */
public class UserRepository extends CouchDbRepositorySupport<User>{
	public UserRepository(final CouchDbConnector connector) {
		super(User.class, connector);
	}
	
	/**
	 * Get the number of users that exist.
	 * 
	 * @return the number of users
	 */
	public int getNrOfUsers() {
		return this.getAll().size();
	}
	
	/**
	 * Check whether a given user already exists.
	 * 
	 * @param userToCheck the user to check
	 * @return true if the user already exists, otherwise false
	 */
	public boolean exists(final User userToCheck) {
		for (User user : this.getAll()) {
			if (user.equals(userToCheck)) {
				return true;
			}
		}
		
		return false;
	}
}
