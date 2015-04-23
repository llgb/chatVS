package verteilteSysteme;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MySQLAccess {
	private Connection connect                  = null;
	private Statement statement                 = null;
	private PreparedStatement preparedStatement = null;
	private ResultSet resultSet                 = null;
	final String chatusername;
	final String chatpw;
	
	private static final Logger logger = LoggerFactory.getLogger(MySQLAccess.class);

	public MySQLAccess() throws Exception {
		super();
		chatusername = System.getenv("chatvs_user");
		chatpw = System.getenv("chatvs_pw");

		if (chatusername == null || chatpw == null) {
			throw new Exception("Systemvariable nicht gesetzt!");		
		} else {
			logger.info("DB User: {}", chatusername);
			logger.info("DB Password: {}", chatpw);
		}
	}

	public void readDataBase() throws SQLException {
		this.connect = DriverManager.getConnection("jdbc:mysql://localhost/chatvs?"
						+ "user="+ chatusername + "&password="+ chatpw);

		// Statements allow to issue SQL queries to the database
		this.statement = connect.createStatement();
		// Result set get the result of the SQL query
		this.resultSet = statement.executeQuery("select * from chatvs.messages");
		writeResultSet(resultSet);
		closeQuietly();
	}

	public void writeDataBase(Message message) throws SQLException {
		// This will load the MySQL driver, each DB has its own driver
		//Class.forName("com.mysql.jdbc.Driver");
		// Setup the connection with the DB
		this.connect = DriverManager.getConnection("jdbc:mysql://localhost/chatvs?"
						+ "user=" + this.chatusername + "&password=" + this.chatpw); 
		// Statements allow to issue SQL queries to the database
		this.statement = connect.createStatement();
		// Result set get the result of the SQL query
		this.preparedStatement = connect.prepareStatement("insert into  chatvs.messages values (default, ?, ?, ?)");
		// Parameters start with 1
		this.preparedStatement.setString(1, message.getOwner().getUsername());
		this.preparedStatement.setString(2, message.getContent());
		this.preparedStatement.setTimestamp(3, message.getTimestamp());
		this.preparedStatement.executeUpdate();
		closeQuietly();
	}

	private void writeMetaData(ResultSet resultSet) throws SQLException {
		//   Now get some metadata from the database
		// Result set get the result of the SQL query

		System.out.println("The columns in the table are: ");

		System.out.println("Table: " + resultSet.getMetaData().getTableName(1));
		for  (int i = 1; i<= resultSet.getMetaData().getColumnCount(); i++){
			System.out.println("Column " +i  + " "+ resultSet.getMetaData().getColumnName(i));
		}
	}

	private void writeResultSet(ResultSet resultSet) throws SQLException {
		while (resultSet.next()) {
			String zeit = resultSet.getString("timestamp");
			String owner = resultSet.getString("owner");
			String content = resultSet.getString("content");    

			System.out.println("Timestamp: " + zeit);
			System.out.println("Owner: " + owner);
			System.out.println("Content: " + content);

		}
	}

	// You need to close the resultSet
	private void closeQuietly() {
		try {
			if (resultSet != null) {
				resultSet.close();
			}

			if (statement != null) {
				statement.close();
			}

			if (connect != null) {
				connect.close();
			}
		} catch (SQLException e) {
			logger.error("closeQuietly eats exception: {}", e.getMessage());
		}
	}

	public static void main(String[] args) throws Exception {
		MySQLAccess dao = new MySQLAccess();
		dao.readDataBase();
		System.out.println("ausgabe vor Änderung");
		Message nachricht = new Message(new User("andreas"), "hallo tinf12b4");
		dao.writeDataBase(nachricht);
	}	 
}
