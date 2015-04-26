package verteilteSysteme.couchdb;

import org.ektorp.CouchDbConnector;
import org.ektorp.support.CouchDbRepositorySupport;

public class UserRepository extends CouchDbRepositorySupport<User>{
	public UserRepository(final CouchDbConnector connector) {
		super(User.class, connector);
	}
}
