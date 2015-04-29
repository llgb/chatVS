package verteilteSysteme.couchdb;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Manages a list of servers.
 */
public class ServerManager {
	private List<String> servers  = new ArrayList<String>();
	private int activeServerIndex = 0;
	private String configFilename;
	
	private final static Logger logger = LoggerFactory.getLogger(ServerManager.class);

	/**
	 * Tries to read server connection strings from a text file and save them. The first one is used as
	 * default server.
	 * 
	 * Format:
	 * jdbc:mysql://host[:port]/
	 */
	public ServerManager(final String configFilename) {
		this.configFilename = configFilename;
		final File file = new File(this.configFilename);
		
		if (!file.exists()) {
			logger.info("Server config file {} doesn't exist. Will try using localhost as server host.", this.configFilename);
			this.servers.add("http://127.0.0.1:5984/");
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
			logger.warn("Proccessing the config file {} failed.", this.configFilename);
		}
	}
	
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
	 * Writes all known server hosts to a text file.
	 */
	public void writeServerListToDisk() {
		final File file = new File(this.configFilename);
		
		// Empty the file if it already exists.
		if (file.exists()) {
			file.delete();
		}
		
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(file));
			
			int loopCounter        = 0;
			int serverIndexCounter = this.activeServerIndex;
			while (loopCounter < this.getNrOfServers()) {
				writer.append(this.servers.get(serverIndexCounter));
				
				if (serverIndexCounter < this.getNrOfServers() - 1) {
					serverIndexCounter++;
				} else {
					serverIndexCounter = 0;
				}
				
				loopCounter++;
			}
			
			logger.info("Successfully write {} server hosts to {}.", this.getNrOfServers(), this.configFilename);
		} catch (IOException e) {
			logger.warn("Writing to {} failed.", file.getName());
		} finally {
			closeWriterQuietly(writer);
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
	
	private static void closeWriterQuietly(final Writer writer) {
		try {
			writer.close();
		} catch (IOException e) {
			logger.error("Could not close BufferedWriter correctly.");
		}
	}
}
