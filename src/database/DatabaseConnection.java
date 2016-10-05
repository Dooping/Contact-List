package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class DatabaseConnection {

	private static final String URL = "jdbc:mysql://localhost/SS";
	private static final String USER = "root";
	private static final String PASSWORD = "root";
	private static final String REMOVE_SSL_WARING = "?useSSL=false";


	private Statement connection(){
		Connection connection = null;
		try{
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection(URL+REMOVE_SSL_WARING, USER, PASSWORD);
			System.out.println("Connection successfull");
			return connection.createStatement();

		} catch (Exception e){
			System.out.println("Connection failed");
			e.printStackTrace();
		}
		return null;
	}

	public boolean createUser(String username, String password){
		boolean result = false;
		Statement st = connection();
		String insertQuery = "INSERT INTO accounts values ('"+username+"','"+password+"',0,0);";
		try {
			st.executeUpdate(insertQuery);
			result = true;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				st.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	public void logout(){
		Statement st = connection();
		String query = "UPDATE accounts SET logged_in = 0 WHERE logged_in = 1";
		try {
			st.executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				st.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}



}
