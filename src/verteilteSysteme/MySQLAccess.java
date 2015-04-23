package verteilteSysteme;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;

import com.mysql.jdbc.Driver;

public class MySQLAccess {
	private Connection connect = null;
	private Statement statement = null;
	private PreparedStatement preparedStatement = null;
	private ResultSet resultSet = null;
	final String chatusername;
	final String chatpw;

	public MySQLAccess() throws Exception {
		super();
		chatusername = System.getenv("chatvs_user");
		chatpw = System.getenv("chatvs_pw");

		if (chatusername == null || chatpw == null) {
			throw new Exception("Systemvariable nicht gesetzt!");		
		} else {
			System.out.println(chatusername);
			System.out.println(chatpw);
		}
	}

	public void readDataBase() throws Exception {
		try {
			// This will load the MySQL driver, each DB has its own driver
			Class.forName("com.mysql.jdbc.Driver");
			// Setup the connection with the DB
			connect = DriverManager
					.getConnection("jdbc:mysql://localhost/chatvs?"
							+ "user="+ chatusername + "&password="+ chatpw);

			// Statements allow to issue SQL queries to the database
			statement = connect.createStatement();
			// Result set get the result of the SQL query
			resultSet = statement
					.executeQuery("select * from chatvs.messages");
			writeResultSet(resultSet);

			/*

    // PreparedStatements can use variables and are more efficient
    preparedStatement = connect
        .prepareStatement("insert into  chatvs.messages values (default, ?, ?, ?, ? , ?, ?)");
    // "myuser, webpage, datum, summery, COMMENTS from feedback.comments");
    // Parameters start with 1
    preparedStatement.setString(1, "Test");
    preparedStatement.setString(2, "TestEmail");
    preparedStatement.setString(3, "TestWebpage");
    preparedStatement.setDate(4, new java.sql.Date(2009, 12, 11));
    preparedStatement.setString(5, "TestSummary");
    preparedStatement.setString(6, "TestComment");
    preparedStatement.executeUpdate();

    preparedStatement = connect
        .prepareStatement("SELECT myuser, webpage, datum, summery, COMMENTS from feedback.comments");
    resultSet = preparedStatement.executeQuery();
    writeResultSet(resultSet);

    // Remove again the insert comment
    preparedStatement = connect
    .prepareStatement("delete from feedback.comments where myuser= ? ; ");
    preparedStatement.setString(1, "Test");
    preparedStatement.executeUpdate();

    resultSet = statement
    .executeQuery("select * from feedback.comments");
    writeMetaData(resultSet);

			 */

		} catch (Exception e) {
			throw e;
		} finally {
			close();
		}

	}

	public void writeDataBase(Message message) throws Exception {
		try {
			// This will load the MySQL driver, each DB has its own driver
			Class.forName("com.mysql.jdbc.Driver");
			// Setup the connection with the DB
			connect = DriverManager
					.getConnection("jdbc:mysql://localhost/chatvs?"
							+ "user=" + this.chatusername + "&password=" + this.chatpw); 
			// Statements allow to issue SQL queries to the database
			statement = connect.createStatement();
			// Result set get the result of the SQL query
			preparedStatement = connect
					.prepareStatement("insert into  chatvs.messages values (default, ?, ?, ?)");
			// Parameters start with 1
			preparedStatement.setString(1, message.getOwner().getUsername());
			preparedStatement.setString(2, message.getContent());
			preparedStatement.setTimestamp(3, message.getTimestamp());
			preparedStatement.executeUpdate();

		} catch (Exception e) {
			throw e;
		} finally {
			close();
		}

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
	private void close() {
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
		} catch (Exception e) {

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
