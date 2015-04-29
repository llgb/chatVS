package verteilteSysteme;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import verteilteSysteme.couchdb.connection.MessageCouchDbConnection;
import verteilteSysteme.couchdb.connection.UserCouchDbConnection;

public class Bootstrap {
	private static final Logger logger = LoggerFactory.getLogger(Bootstrap.class);
	
	public static void main(String[] args) {
		// Configure the database connection.
		final String host = "http://127.0.0.1:5984/";
		MessageCouchDbConnection.setConnectionDetails(host, "chatvs_messages");
		UserCouchDbConnection.setConnectionDetails(host, "chatvs_users");
		
		// User configuration.
		final String username = "klaus";
		
		// Create the GUI window and start listening for messages.
		try {
			final ChatWindow window = new ChatWindow(username);
			logger.info("Window created.");
			
			final Thread pollthread = new Thread(new PollThread(window));
			pollthread.start();
			logger.info("Started listening for new messages.");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
