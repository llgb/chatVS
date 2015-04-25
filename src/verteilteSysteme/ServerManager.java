package verteilteSysteme;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Manages a list of servers.
 */
public class ServerManager {
	private List<String> servers = new ArrayList<String>();
	private int activeServerIndex = 0;
	
	private final static Logger logger = LoggerFactory.getLogger(ServerManager.class);

	/**
	 * Tries to read server connection strings from a text file and save them. The first one is used as
	 * default server.
	 * 
	 * Format:
	 * jdbc:mysql://host[:port]/chatvs
	 */
	public ServerManager(final String configFilename) {
		final File file = new File(configFilename);
		
		if (!file.exists()) {
			logger.warn("Server config file {} doesn't exist.", configFilename);
			return;
		}
		
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			
			String line;
			while ((line = reader.readLine()) != null) {
				if (!line.isEmpty()) {
					this.servers.add(line);
				}
			}
		} catch (IOException e) {
			logger.warn("Proccessing the config file {} failed.", configFilename);
		}
	}
	
	/** Constructor. */
	public ServerManager() {}
	
	/**
	 * Get the active server connection.
	 * 
	 * @return the active server connection
	 */
	public String getActiveServerConnection() {
		return this.servers.size() > this.activeServerIndex ? this.servers.get(this.activeServerIndex) : null;
	}
	
	/**
	 * Add a new server.
	 * 
	 * @param server the server connection
	 */
	public void addServer(final String server) {
		this.servers.add(server);
	}
	
	/**
	 * Get the number of known servers.
	 * 
	 * @return the number of known servers
	 */
	public int getNrOfServers() {
		return this.servers.size();
	}
	
	/**
	 * Get all server connections.
	 * 
	 * @return the server connections
	 */
	public List<String> getAll() {
		return this.servers;
	}
	
	/**
	 * Increments the active server index number.
	 * 
	 * @return the new active server connection
	 */
	public String changeActiveServer() {
		if (this.activeServerIndex < this.servers.size() - 1) {
			this.activeServerIndex++;
		} else {
			this.activeServerIndex = 0;
		}
		
		return this.servers.get(this.activeServerIndex);
	}
	
	/**
	 * Read the known server hosts from the database.
	 * 
	 * @param db the db access object
	 */
	public void syncKnownHostsFromDB(final MySQLAccess db) {
		try {
			this.servers = db.getServers();
			logger.info("Synchronized server hosts from db: {} Hosts.", this.servers.size());
		} catch (SQLException e) {
			logger.error("Failed to sync known hosts from database.");
		}
	}
	
	/** {@inheritDoc} */
	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		
		for (String server : this.servers) {
			builder.append(server);
		}
		
		return builder.toString();
	}
}
