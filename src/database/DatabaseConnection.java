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
			
			/*String query = "SELECT * FROM accounts";
			
			Statement statement = connection.createStatement();
			
			ResultSet rs = statement.executeQuery(query);
			
			while(rs.next()){
				System.out.println("username:" + rs.getString("name") + " Password : " +rs.getString("pwdhash")); 
			} */
			
		} catch (Exception e){
			System.out.println("Connection failed");
			e.printStackTrace();
		}
		return null;
	}
	
	public void logout(){
		Statement st = connection();
		String query = "UPDATE accounts SET logged_in = 0 WHERE logged_in = 1";
		//String query = "SELECT * FROM ACCOUNTS";
		try {
			st.executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	

}
