package verteilteSysteme.couchdb.connection;

import java.net.MalformedURLException;

import org.ektorp.CouchDbConnector;
import org.ektorp.CouchDbInstance;
import org.ektorp.http.HttpClient;
import org.ektorp.http.StdHttpClient;
import org.ektorp.impl.StdCouchDbConnector;
import org.ektorp.impl.StdCouchDbInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Singleton for a CouchDbConnector instance that connects to the user database.
 */
public final class MessageCouchDbConnection {
	private static CouchDbConnector db = null;
	
	private static String username     = System.getenv("chatvs_user");
	private static String password     = System.getenv("chatvs_pw");
	
	private static final Logger logger = LoggerFactory.getLogger(MessageCouchDbConnection.class);
	
	public static String url           = "http://127.0.0.1:5984/";	// default
	public static String dbName        = "chatvs";
	
	public static synchronized CouchDbConnector get() {
		if (MessageCouchDbConnection.db == null) {
			logger.info("Opening connection to couchdb ...");
			
			final HttpClient client          = buildHttpClientQuietly();
			final CouchDbInstance dbInstance = new StdCouchDbInstance(client);
			MessageCouchDbConnection.db             = new StdCouchDbConnector(MessageCouchDbConnection.dbName, dbInstance);
			MessageCouchDbConnection.db.createDatabaseIfNotExists();
		}
		
		return MessageCouchDbConnection.db;
	}
	
	public static synchronized void setConnectionDetails(final String url, final String dbName) {
		MessageCouchDbConnection.url    = url;
		MessageCouchDbConnection.dbName = dbName;
	}
	
	public static String getHost() {
		return MessageCouchDbConnection.url;
	}
	
	protected static HttpClient buildHttpClientQuietly() {
		if (MessageCouchDbConnection.username == null || MessageCouchDbConnection.password == null) {
			logger.error("No username and/or password known. Set the environment variables!");
			return null;
		}
		
		try {
//			return new StdHttpClient.Builder()
//						.url(url)
//						.username(CouchDbConnection.username)
//						.password(CouchDbConnection.password)
//						.build();
			return new StdHttpClient.Builder()
			.url(url)
			.build();
		} catch (MalformedURLException e) {
			logger.error("Unexpected MalformedURLException.");
			return null;
		}
	}
}
