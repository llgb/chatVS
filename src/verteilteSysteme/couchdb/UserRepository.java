package verteilteSysteme.couchdb;

import java.util.List;

import org.ektorp.CouchDbConnector;
import org.ektorp.support.CouchDbRepositorySupport;
import org.ektorp.support.GenerateView;

public class UserRepository extends CouchDbRepositorySupport<User>{
	public UserRepository(final CouchDbConnector connector) {
		super(User.class, connector);
	}
	
//	@GenerateView
//	public List<User> findByTag(String tag) {
//		return queryView("by_tag", tag);
//	}
//	
//	@Override
//	public List<User> getAll() {
//		return this.findByTag("user_document");
//	}
	
	public int getNrOfUsers() {
		return this.getAll().size();
	}
}
