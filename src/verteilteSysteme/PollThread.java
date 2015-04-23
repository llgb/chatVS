package verteilteSysteme;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PollThread implements Runnable {
	private int numberOfCachedMessages = 0;
	private final MySQLAccess dao;
	private List<Message> newMessages = new ArrayList<Message>();
	private final ChatWindow window;
	
	private static final Logger logger = LoggerFactory.getLogger(PollThread.class);
	
	public PollThread(ChatWindow window) throws Exception {
		super();
		this.dao = new MySQLAccess();
		this.window = window;
	}

	@Override
	public void run() {
        try {
        	while (true) {
	    		int currentInDBMessages = dao.countMessages();	                		
	    		int messageDifference = currentInDBMessages - this.numberOfCachedMessages;
	    		logger.info("Nachrichtenanzahl beträgt: {}", currentInDBMessages);
	    		logger.info("Nachrichtendifferenz beträgt: {}", messageDifference);
	    		
	    		if (messageDifference > 0 ) {
	    			this.newMessages = dao.getLatestMessages(messageDifference);
	    			this.numberOfCachedMessages = currentInDBMessages;
	    			this.window.addMessageList(newMessages);
	    			this.newMessages.clear();
	    		}
	    		
	    		logger.info("Thread going to sleep ...");
	    		Thread.sleep(500);	
        	}
		} catch (InterruptedException | SQLException e) {
			e.printStackTrace();
		}
    }
}
