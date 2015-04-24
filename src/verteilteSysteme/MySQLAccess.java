package verteilteSysteme;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MySQLAccess {
	private Connection connect = null;
	private Statement statement = null;
	private PreparedStatement preparedStatement = null;
	private ResultSet resultSet = null;
	private final String chatusername;
	private final String chatpw;
	private final String server;
	private int port=3306;

	private static final Logger logger = LoggerFactory
			.getLogger(MySQLAccess.class);

	public MySQLAccess() throws Exception {
		super();
		this.chatusername = System.getenv("chatvs_user");
		this.chatpw = System.getenv("chatvs_pw");
		this.server = "localhost";

		if (this.chatusername == null || this.chatpw == null) {
			throw new Exception("Systemvariable nicht gesetzt!");
		} else {
			logger.info("DB User: {}", chatusername);
			logger.info("DB Password: {}", chatpw);
		}
	}
	public MySQLAccess(String server, String username, String password)  {
		super();
		this.server = server;
		this.chatusername = username;
		this.chatpw = password;

			logger.info("DB User: {}", chatusername);
			logger.info("DB Password: {}", chatpw);	
			logger.info("DB Server: {}", server);
			logger.info("DB Port: {}", port);
	}
	public MySQLAccess(String server, int portnumber, String username, String password)  {
		super();
		this.server = server;
		this.chatusername = username;
		this.chatpw = password;
		this.port = portnumber;
		logger.info("DB User: {}", chatusername);
		logger.info("DB Password: {}", chatpw);
		logger.info("DB Server: {}", server);
		logger.info("DB Port: {}", port);
		
	}
	

	public void readDataBase() throws SQLException {
		this.connect = DriverManager
				.getConnection("jdbc:mysql://"+this.server+"/chatvs?" + "user="
						+ this.chatusername + "&password=" + this.chatpw);

		// Statements allow to issue SQL queries to the database
		this.statement = connect.createStatement();
		// Result set get the result of the SQL query
		this.resultSet = statement
				.executeQuery("select * from chatvs.messages");
		writeResultSet(this.resultSet);
		closeQuietly();
	}

	public void writeToDataBase(final Message message) throws SQLException {
		// This will load the MySQL driver, each DB has its own driver
		// Class.forName("com.mysql.jdbc.Driver");
		// Setup the connection with the DB
		this.connect = DriverManager
				.getConnection("jdbc:mysql://localhost/chatvs?" + "user="
						+ this.chatusername + "&password=" + this.chatpw);
		// Statements allow to issue SQL queries to the database
		this.statement = connect.createStatement();
		// Result set get the result of the SQL query
		this.preparedStatement = connect
				.prepareStatement("insert into  chatvs.messages values (default, ?, ?, ?)");
		// Parameters start with 1
		this.preparedStatement.setString(1, message.getOwner().getUsername());
		this.preparedStatement.setString(2, message.getContent());
		this.preparedStatement.setTimestamp(3, message.getTimestamp());
		this.preparedStatement.executeUpdate();
		closeQuietly();
	}

	private void writeMetaData(final ResultSet resultSet) throws SQLException {
		// Now get some metadata from the database
		// Result set get the result of the SQL query

		System.out.println("The columns in the table are: ");

		System.out.println("Table: " + resultSet.getMetaData().getTableName(1));
		for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
			System.out.println("Column " + i + " "
					+ resultSet.getMetaData().getColumnName(i));
		}
	}

	private void writeResultSet(final ResultSet resultSet) throws SQLException {
		while (resultSet.next()) {
			final String zeit = resultSet.getString("timestamp");
			final String owner = resultSet.getString("owner");
			final String content = resultSet.getString("content");

			System.out.println("Timestamp: " + zeit);
			System.out.println("Owner: " + owner);
			System.out.println("Content: " + content);

		}
	}

	// You need to close the resultSet
	private void closeQuietly() {
		try {
			if (this.resultSet != null) {
				this.resultSet.close();
			}

			if (this.statement != null) {
				this.statement.close();
			}

			if (this.connect != null) {
				this.connect.close();
			}
		} catch (SQLException e) {
			logger.error("closeQuietly eats exception: {}", e.getMessage());
		}
	}

	public int countMessages() throws SQLException {
		connect = DriverManager.getConnection("jdbc:mysql://localhost/chatvs?"
				+ "user=" + this.chatusername + "&password=" + this.chatpw);

		// Statements allow to issue SQL queries to the database
		statement = connect.createStatement();
		// Result set get the result of the SQL query
		resultSet = statement
				.executeQuery("select Count(*) from chatvs.messages");
		resultSet.first();
		return resultSet.getInt("COUNT(*)");

	}
	public int countUsers() throws SQLException {
		connect = DriverManager.getConnection("jdbc:mysql://localhost/chatvs?"
				+ "user=" + this.chatusername + "&password=" + this.chatpw);

		// Statements allow to issue SQL queries to the database
		statement = connect.createStatement();
		// Result set get the result of the SQL query
		resultSet = statement
				.executeQuery("select Count(*) from chatvs.users");
		resultSet.first();
		return resultSet.getInt("COUNT(*)");

	}

	public List<Message> getLatestMessages(int nr) throws SQLException {
		this.connect = DriverManager
				.getConnection("jdbc:mysql://localhost/chatvs?" + "user="
						+ this.chatusername + "&password=" + this.chatpw);

		// Statements allow to issue SQL queries to the database
		this.statement = this.connect.createStatement();
		// Result set get the result of the SQL query
		this.resultSet = this.statement
				.executeQuery("select * from chatvs.messages ORDER BY idmessages DESC LIMIT "
						+ nr);

		final List<Message> newestMessagesList = new ArrayList<Message>();
		ResultSetMetaData metadata = this.resultSet.getMetaData();
		int numberOfColumns = metadata.getColumnCount();

		while (this.resultSet.next()) {
			Message tmpMessage = new Message(new User(resultSet.getString(2)),
					resultSet.getString(3), resultSet.getTimestamp(4));
			newestMessagesList.add(tmpMessage);
		}

		return newestMessagesList;
	}

	public List<User> getCurrentUsers() throws SQLException {
		this.connect = DriverManager
				.getConnection("jdbc:mysql://localhost/chatvs?" + "user="
						+ this.chatusername + "&password=" + this.chatpw);

		// Statements allow to issue SQL queries to the database
		this.statement = this.connect.createStatement();
		// Result set get the result of the SQL query
		this.resultSet = this.statement
				.executeQuery("select * from chatvs.users");

		final List<User> currentUsersList = new ArrayList<User>();

		while (this.resultSet.next()) {
			User tmpUser = new User(resultSet.getString(2));
			currentUsersList.add(tmpUser);
		}

		return currentUsersList;
	}

	public void removeUserfromDB(User user) throws SQLException {
		this.connect = DriverManager
				.getConnection("jdbc:mysql://localhost/chatvs?" + "user="
						+ this.chatusername + "&password=" + this.chatpw);
		this.preparedStatement = connect.prepareStatement("DELETE  from chatvs.users WHERE username= ? ; ");
		this.preparedStatement.setString(1, user.getUsername());
		this.preparedStatement.executeUpdate();
		closeQuietly();
		
	}
	public void addUserToDB(User user) throws SQLException {
		this.connect = DriverManager
				.getConnection("jdbc:mysql://localhost/chatvs?" + "user="
						+ this.chatusername + "&password=" + this.chatpw);
		// Statements allow to issue SQL queries to the database
		this.statement = connect.createStatement();
		// Result set get the result of the SQL query
		this.preparedStatement = connect
				.prepareStatement("insert into  chatvs.users values (default, ?, ?)");
		// Parameters start with 1
		this.preparedStatement.setString(1, user.getUsername());
		this.preparedStatement.setString(2, user.getPasswordhash());
		this.preparedStatement.executeUpdate();
		closeQuietly();
	}
	public String getServer() {
		return server;
	}
	public int getPort() {
		return port;
	}
	
}
