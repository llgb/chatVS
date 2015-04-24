package verteilteSysteme;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Bootstrap {
	private static final Logger logger = LoggerFactory.getLogger(Bootstrap.class);
	
	public static void main(String[] args) {
		final ServerManager serverManager = new ServerManager();
		serverManager.addServer("jdbc:mysql://localhost/chatvs");
		
		try {
			final MySQLAccess dao = new MySQLAccess(serverManager);
			
			final ChatWindow window = new ChatWindow("klaus1", dao);
			logger.info("Window created.");
			
			final Thread pollthread = new Thread(new PollThread(window, dao));
			pollthread.start();
			logger.info("Started listening for new messages.");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
