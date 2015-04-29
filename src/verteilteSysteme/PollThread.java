package verteilteSysteme;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import verteilteSysteme.couchdb.Message;
import verteilteSysteme.couchdb.MessageRepository;
import verteilteSysteme.couchdb.User;
import verteilteSysteme.couchdb.UserRepository;
import verteilteSysteme.couchdb.connection.MessageCouchDbConnection;
import verteilteSysteme.couchdb.connection.UserCouchDbConnection;

/**
 * A thread implementation that polls for new messages in a database and updates
 * the chat window on demand.
 */
public class PollThread implements Runnable {
	private final ChatWindow window;
	
	private static final Logger logger = LoggerFactory.getLogger(PollThread.class);
	
	/**
	 * Initiates the thread.
	 * 
	 * @param window the corresponding chat window to be updated with new messages
	 */
	public PollThread(final ChatWindow window) {
		super();
		this.window = window;
	}

	/** {@inheritDoc} */
	@Override
	public void run() {
		int numberOfCachedMessages                = 0;
		int numberOfCachedUsers                   = 0;
		List<Message> newMessages                 = new ArrayList<Message>();
		List<User> newUsers                       = new ArrayList<User>();
		final MessageRepository messageRepository = new MessageRepository(MessageCouchDbConnection.get());
		final UserRepository userRepository       = new UserRepository(UserCouchDbConnection.get());
		
        try {
        	while (true) {
        		int currentInDBMessages = messageRepository.getNrOfMessages();                		
	    		int messageDifference = currentInDBMessages - numberOfCachedMessages;
	    		logger.info("Nachrichtenanzahl beträgt: {}", currentInDBMessages);
	    		logger.info("Nachrichtendifferenz beträgt: {}", messageDifference);
	    		
	    		if (messageDifference > 0 ) {
	    			newMessages            = messageRepository.getRecentMessages(messageDifference);
	    			numberOfCachedMessages = currentInDBMessages;
	    			this.window.addMessageList(newMessages);
	    			newMessages.clear();
	    		}
	    		
	    		//Check for new Users
	    		int currentInDBUsers = userRepository.getNrOfUsers();	                		
	    		int userDifference   = currentInDBUsers - numberOfCachedUsers;
	    		logger.info("Useranzahl beträgt: {}", currentInDBUsers);
	    		logger.info("userdifferenz beträgt: {}", userDifference);
	    		
	    		if (userDifference != 0 ) {
	    			newUsers            = userRepository.getAll();
	    			numberOfCachedUsers = currentInDBUsers;
	    			this.window.addMemberListToList(newUsers);
	    			newMessages.clear();
	    		}
	    		
	    		logger.info("Thread going to sleep ...");
	    		Thread.sleep(500);	
        	}
		} catch (InterruptedException e) {
			logger.error("Thread interrupted.");
			e.printStackTrace();
		}
    }
}
