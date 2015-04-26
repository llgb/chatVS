package verteilteSysteme.couchdb;

import java.net.MalformedURLException;

import org.ektorp.CouchDbConnector;
import org.ektorp.CouchDbInstance;
import org.ektorp.http.HttpClient;
import org.ektorp.http.StdHttpClient;
import org.ektorp.impl.StdCouchDbConnector;
import org.ektorp.impl.StdCouchDbInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class CouchDbConnection {
	private static CouchDbConnector db = null;
	
	private static final Logger logger = LoggerFactory.getLogger(CouchDbConnection.class);
	
	public static String url           = "http://127.0.0.1:5984/";
	public static String dbName        = "chatvs";
	
	private CouchDbConnection() {}
	
	public static synchronized CouchDbConnector get() {
		if (CouchDbConnection.db == null) {
			logger.info("Opening connection to couchdb ...");
			
			final HttpClient client          = buildHttpClientQuietly();
			final CouchDbInstance dbInstance = new StdCouchDbInstance(client);
			CouchDbConnection.db             = new StdCouchDbConnector(CouchDbConnection.dbName, dbInstance);
			CouchDbConnection.db.createDatabaseIfNotExists();
		}
		
		return CouchDbConnection.db;
	}
	
	public static synchronized void setConnectionDetails(final String url, final String dbName) {
		CouchDbConnection.url    = url;
		CouchDbConnection.dbName = dbName;
	}
	
	private static HttpClient buildHttpClientQuietly() {
		try {
			return new StdHttpClient.Builder()
						.url(url)
						.build();
		} catch (MalformedURLException e) {
			logger.error("Unexpected MalformedURLException.");
			return null;
		}
	}
}
