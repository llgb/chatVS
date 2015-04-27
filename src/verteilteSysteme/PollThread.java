package verteilteSysteme;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import verteilteSysteme.couchdb.CouchDbConnection;
import verteilteSysteme.couchdb.Message;
import verteilteSysteme.couchdb.MessageRepository;
import verteilteSysteme.couchdb.User;
import verteilteSysteme.couchdb.UserRepository;

public class PollThread implements Runnable {
	private int numberOfCachedMessages                = 0;
	private int numberOfCachedUsers                   = 0;
	private List<Message> newMessages                 = new ArrayList<Message>();
	private List<User> newUsers                       = new ArrayList<User>();
	private final ChatWindow window;
	private final MessageRepository messageRepository = new MessageRepository(CouchDbConnection.get());
	private final UserRepository userRepository       = new UserRepository(CouchDbConnection.get());
	
	private static final Logger logger = LoggerFactory.getLogger(PollThread.class);
	
	public PollThread(final ChatWindow window) throws Exception {
		super();
		this.window = window;
	}

	@Override
	public void run() {
        try {
        	while (true) {
        		int currentInDBMessages = this.messageRepository.getNrOfMessages();                		
	    		int messageDifference = currentInDBMessages - this.numberOfCachedMessages;
	    		logger.info("Nachrichtenanzahl beträgt: {}", currentInDBMessages);
	    		logger.info("Nachrichtendifferenz beträgt: {}", messageDifference);
	    		
	    		if (messageDifference > 0 ) {
	    			this.newMessages = this.messageRepository.getRecentMessages(messageDifference);
	    			this.numberOfCachedMessages = currentInDBMessages;
	    			this.window.addMessageList(newMessages);
	    			this.newMessages.clear();
	    		}
	    		
	    		//Check for new Users
	    		int currentInDBUsers = this.userRepository.getNrOfUsers();	                		
	    		int userDifference = currentInDBUsers - this.numberOfCachedUsers;
	    		logger.info("Useranzahl beträgt: {}", currentInDBUsers);
	    		logger.info("userdifferenz beträgt: {}", userDifference);
	    		
	    		if (userDifference != 0 ) {
	    			this.newUsers = this.userRepository.getAll();
	    			this.numberOfCachedUsers = currentInDBUsers;
	    			this.window.addMemberListToList(newUsers);
	    			this.newMessages.clear();
	    		}
	    		
	    		logger.info("Thread going to sleep ...");
	    		Thread.sleep(500);	
        	}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    }
}
