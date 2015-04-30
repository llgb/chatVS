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
	private List<String> hosts  = new ArrayList<String>();
	private int activeHostIndex = 0;
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
			this.hosts.add("http://127.0.0.1:5984/");
			return;
		}
		
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			
			String line;
			while ((line = reader.readLine()) != null) {
				if (!line.isEmpty()) {
					this.hosts.add(line);
				}
			}
			
			logger.info("Successfully loaded {} hostnames from {}.", this.getNrOfHosts(), this.configFilename);
			logger.info("Active host is now {}", this.getActiveHost());
		} catch (IOException e) {
			logger.warn("Proccessing the config file {} failed.", this.configFilename);
		}
	}
	
	/**
	 * Get the active host connection.
	 * 
	 * @return the active host connection
	 */
	public String getActiveHost() {
		return this.hosts.size() > this.activeHostIndex ? this.hosts.get(this.activeHostIndex) : null;
	}
	
	/**
	 * Add a new host.
	 * 
	 * @param host the host
	 */
	public void addHost(final String host) {
		this.hosts.add(host);
	}
	
	/**
	 * Get the number of known hosts.
	 * 
	 * @return the number of known hosts
	 */
	public int getNrOfHosts() {
		return this.hosts.size();
	}
	
	/**
	 * Get all hosts.
	 * 
	 * @return the hosts
	 */
	public List<String> getAll() {
		return this.hosts;
	}
	
	/**
	 * Increments the active host index number.
	 * 
	 * @return the new active host
	 */
	public String changeActiveHost() {
		if (this.activeHostIndex < this.hosts.size() - 1) {
			this.activeHostIndex++;
		} else {
			this.activeHostIndex = 0;
		}
		
		return this.hosts.get(this.activeHostIndex);
	}
	
	/**
	 * Writes all known hosts to a text file.
	 */
	public void writeHostListToDisk() {
		final File file = new File(this.configFilename);
		
		// Empty the file if it already exists.
		if (file.exists()) {
			file.delete();
		}
		
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(file));
			
			int loopCounter        = 0;
			int serverIndexCounter = this.activeHostIndex;
			while (loopCounter < this.getNrOfHosts()) {
				writer.append(this.hosts.get(serverIndexCounter)).append(System.lineSeparator());
				
				if (serverIndexCounter < this.getNrOfHosts() - 1) {
					serverIndexCounter++;
				} else {
					serverIndexCounter = 0;
				}
				
				loopCounter++;
			}
			
			logger.info("Successfully write {} server hosts to {}.", this.getNrOfHosts(), this.configFilename);
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
		
		for (String server : this.hosts) {
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
