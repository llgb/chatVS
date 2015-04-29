package verteilteSysteme;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

import org.apache.http.auth.UsernamePasswordCredentials;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import verteilteSysteme.couchdb.User;
import verteilteSysteme.couchdb.UserRepository;
import verteilteSysteme.couchdb.connection.MessageCouchDbConnection;
import verteilteSysteme.couchdb.connection.UserCouchDbConnection;

public class Bootstrap {
	private static String username;
	private static boolean exists;
	private static final Logger logger = LoggerFactory.getLogger(Bootstrap.class);
	private static void checkUser(){
		UserRepository userRepository = new UserRepository(UserCouchDbConnection.get());
		// User configuration.
				username = JOptionPane.showInputDialog(null,"Geben Sie Ihren Nicknamen ein", "Nicknamen auswählen", JOptionPane.PLAIN_MESSAGE);
				
				if (username == null) {
					//handle the Cancel Button
					System.exit(0);
				}
				while(username.isEmpty()) {
					JOptionPane.showMessageDialog(null, "Der eingegebene Nickname darf nicht leer sein");
					username = JOptionPane.showInputDialog(null,"Geben Sie Ihren Nicknamen ein", "Nicknamen auswählen", JOptionPane.PLAIN_MESSAGE);
				}
		 
				exists = userRepository.exists(new User(username));
	}
	public static void main(String[] args) {
		// Configure the database connection.
		final String host = "http://127.0.0.1:5984/";
		MessageCouchDbConnection.setConnectionDetails(host, "chatvs_messages");
		UserCouchDbConnection.setConnectionDetails(host, "chatvs_users");
		UserRepository userRepository = new UserRepository(UserCouchDbConnection.get());
		
		checkUser();
		
		while (exists) {
			JOptionPane.showMessageDialog(null, "Der gewünschte Nickname "+username + " wird leider bereits verwendet");
			username = JOptionPane.showInputDialog(null,"Geben Sie Ihren Nicknamen ein", "Nicknamen auswählen", JOptionPane.PLAIN_MESSAGE);
			checkUser();
		}		
		
		// Create the GUI window and start listening for messages.
		try {
			final String lookAndFeel = UIManager.getSystemLookAndFeelClassName();
			logger.info("System look and feel: {} Trying to set it.", lookAndFeel);
			UIManager.setLookAndFeel(lookAndFeel);
			
			final ChatWindow window = new ChatWindow(username);
			logger.info("Window created.");
			
			final Thread pollthread = new Thread(new PollThread(window));
			pollthread.start();
			logger.info("Started listening for new messages.");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	public static void setUsername(String username) {
		Bootstrap.username = username;
	}
	
}
