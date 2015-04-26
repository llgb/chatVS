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
	
	private CouchDbConnection() {}
	
	public static synchronized CouchDbConnector get() {
		if (db == null) {
			logger.info("Opening connection to couchdb ...");
			
			final HttpClient client = buildHttpClientQuietly();
			final CouchDbInstance dbInstance = new StdCouchDbInstance(client);
			db = new StdCouchDbConnector("test-db", dbInstance);
			db.createDatabaseIfNotExists();
		}
		
		return db;
	}
	
	private static HttpClient buildHttpClientQuietly() {
		try {
			return new StdHttpClient.Builder()
						.url("http://127.0.0.1:5984/")
						.build();
		} catch (MalformedURLException e) {
			logger.error("Unexpected MalformedURLException.");
			return null;
		}
	}
}
