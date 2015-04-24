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
	private final ServerManager serverManager;

	private static final Logger logger = LoggerFactory
			.getLogger(MySQLAccess.class);

	public MySQLAccess(final ServerManager serverManager) throws Exception {
		super();
		this.serverManager = serverManager;
		this.chatusername  = System.getenv("chatvs_user");
		this.chatpw        = System.getenv("chatvs_pw");

		if (this.chatusername == null || this.chatpw == null) {
			throw new Exception("Systemvariable nicht gesetzt!");
		} else {
			logger.info("DB connection: {}", this.serverManager.getActiveServerConnection());
			logger.info("DB User: {}", this.chatusername);
			logger.info("DB Password: {}", this.chatpw);
		}
	}

	public void readDataBase() throws SQLException {
		this.connect = this.getConnection();

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
		this.connect = this.getConnection();
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
		this.connect = this.getConnection();

		// Statements allow to issue SQL queries to the database
		statement = connect.createStatement();
		// Result set get the result of the SQL query
		resultSet = statement
				.executeQuery("select Count(*) from chatvs.messages");
		resultSet.first();
		return resultSet.getInt("COUNT(*)");

	}

	public int countUsers() throws SQLException {
		this.connect = this.getConnection();

		// Statements allow to issue SQL queries to the database
		statement = connect.createStatement();
		// Result set get the result of the SQL query
		resultSet = statement.executeQuery("select Count(*) from chatvs.users");
		resultSet.first();
		return resultSet.getInt("COUNT(*)");

	}

	public List<Message> getLatestMessages(int nr) throws SQLException {
		this.connect = this.getConnection();

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
		this.connect = this.getConnection();

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
		this.connect = this.getConnection();
		this.preparedStatement = connect
				.prepareStatement("DELETE  from chatvs.users WHERE username= ? ; ");
		this.preparedStatement.setString(1, user.getUsername());
		this.preparedStatement.executeUpdate();
		closeQuietly();

	}

	public void addUserToDB(User user) throws SQLException {
		this.connect = this.getConnection();
		// Statements allow to issue SQL queries to the database
		this.statement = connect.createStatement();
		// Result set get the result of the SQL query
		this.preparedStatement = connect
				.prepareStatement("insert into  chatvs.users values (default, ?)");
		// Parameters start with 1
		this.preparedStatement.setString(1, user.getUsername());
		this.preparedStatement.executeUpdate();
		closeQuietly();
	}
	
	public ServerManager getServerManager() {
		return this.serverManager;
	}
	
	private Connection getConnection() throws SQLException {
		final StringBuilder connection = new StringBuilder()
			.append(this.serverManager.getActiveServerConnection())
			.append("?user=").append(this.chatusername)
			.append("&password=").append(this.chatpw);
		return DriverManager.getConnection(connection.toString());
	}

}
