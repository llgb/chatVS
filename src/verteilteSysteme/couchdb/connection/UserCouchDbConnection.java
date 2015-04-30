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
 * Singleton for a CouchDbConnector instance that connects to the message database.
 */
public final class UserCouchDbConnection {
	private static CouchDbConnector db = null;
	
	private static String username     = System.getenv("chatvs_user");
	private static String password     = System.getenv("chatvs_pw");
	
	private static final Logger logger = LoggerFactory.getLogger(UserCouchDbConnection.class);
	
	private static String url           = "http://127.0.0.1:5984/";	// default
	private static String dbName        = "chatvs";
	
	public static synchronized CouchDbConnector get() {
		if (UserCouchDbConnection.db == null) {
			instantiateConnector();
		}
		
		return UserCouchDbConnection.db;
	}
	
	public static synchronized void setConnectionDetails(final String url, final String dbName) {
		UserCouchDbConnection.url    = url;
		UserCouchDbConnection.dbName = dbName;
		
		UserCouchDbConnection.instantiateConnector();
	}
	
	public static String getHost() {
		return UserCouchDbConnection.url;
	}
	
	protected static void instantiateConnector() {
		logger.info("Opening connection to {}", UserCouchDbConnection.dbName);
		final HttpClient client          = buildHttpClientQuietly();
		final CouchDbInstance dbInstance = new StdCouchDbInstance(client);
		UserCouchDbConnection.db      = new StdCouchDbConnector(UserCouchDbConnection.dbName, dbInstance);
		UserCouchDbConnection.db.createDatabaseIfNotExists();
	}
	
	protected static HttpClient buildHttpClientQuietly() {
		if (UserCouchDbConnection.username == null || UserCouchDbConnection.password == null) {
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
