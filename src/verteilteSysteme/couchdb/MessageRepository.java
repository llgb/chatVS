package verteilteSysteme.couchdb;

import java.util.ArrayList;
import java.util.List;

import org.ektorp.CouchDbConnector;
import org.ektorp.support.CouchDbRepositorySupport;

public class MessageRepository extends CouchDbRepositorySupport<Message> {
	public MessageRepository(final CouchDbConnector connector) {
		super(Message.class, connector);
	}
	
	// TODO: use a query instead of fetching all docs
	public int getNrOfMessages() {
		return this.getAll().size();
	}
	
	// TODO: use a query instead of fetching all docs
	public List<Message> getRecentMessages(final int n) {
		int total = this.getNrOfMessages();
		List<Message> messages = new ArrayList<Message>(n);
		List<Message> allMessages = this.getAll();
		
		for (int i = total - n; i < total; i++) {
			messages.add(allMessages.get(i));
		}
		
		return messages;
	}
}