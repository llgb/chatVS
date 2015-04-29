package verteilteSysteme;

import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import verteilteSysteme.couchdb.ServerManager;
import verteilteSysteme.couchdb.User;
import verteilteSysteme.couchdb.UserRepository;
import verteilteSysteme.couchdb.connection.MessageCouchDbConnection;
import verteilteSysteme.couchdb.connection.UserCouchDbConnection;

public class Bootstrap {
	private static final Logger logger = LoggerFactory.getLogger(Bootstrap.class);
	
	public static void main(String[] args) {
		// Configure the database connection.
		final ServerManager serverManager = new ServerManager("serverhosts.conf");
		MessageCouchDbConnection.setConnectionDetails(serverManager.getActiveHost(), "chatvs_messages");
		UserCouchDbConnection.setConnectionDetails(serverManager.getActiveHost(), "chatvs_users");
		
		final String username = getUsernameAndCheckIfReserved();
		if (username == null) {
			// Cancel button. -> Exit.
			return;
		}
		
		// Create the GUI window and start listening for messages.
		try {
			final String lookAndFeel = UIManager.getSystemLookAndFeelClassName();
			logger.info("System look and feel: {} Trying to set it.", lookAndFeel);
			UIManager.setLookAndFeel(lookAndFeel);
			
			final ChatWindow window = new ChatWindow(username, serverManager);
			logger.info("Window created.");
			
			final Thread pollthread = new Thread(new PollThread(window));
			pollthread.start();
			logger.info("Started listening for new messages.");
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
			logger.warn("Unable to set the look and feel of the user interface elements.");
		}
		
	}
	
	private static String getUsernameAndCheckIfReserved() {
		final UserRepository userRepository = new UserRepository(UserCouchDbConnection.get());
				
		do {
			String username = JOptionPane.showInputDialog(null, "Geben Sie Ihren Nicknamen ein", "Nicknamen auswählen", JOptionPane.PLAIN_MESSAGE);
			
			if (username == null) {
				// Cancel button.
				return null;
			}
			
			if (username.isEmpty()) {
				JOptionPane.showMessageDialog(null, "Der eingegebene Nickname darf nicht leer sein");
			} else {
				// Check if the username is reserved.
				if (userRepository.exists(new User(username))) {
					JOptionPane.showMessageDialog(null, "Der gewünschte Nickname " + username + " wird leider bereits verwendet.");
				} else {
					return username;
				}
			}
		} while (true);
	}
}
