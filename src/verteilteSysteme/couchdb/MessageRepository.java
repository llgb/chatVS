package verteilteSysteme.couchdb;

import java.util.ArrayList;
import java.util.List;

import org.ektorp.CouchDbConnector;
import org.ektorp.support.CouchDbRepositorySupport;

/**
 * Provides CRUD methods and more for message objects.
 */
public class MessageRepository extends CouchDbRepositorySupport<Message> {
	public MessageRepository(final CouchDbConnector connector) {
		super(Message.class, connector);
	}
	
	/**
	 * Get the number of messages that exist.
	 * 
	 * @return the number of messages
	 */
	public int getNrOfMessages() {
		return this.getAll().size();
	}
	
	/**
	 * Get the most recent messages.
	 * 
	 * @param n the number of most recent messages to get
	 * @return the most recent messages
	 */
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