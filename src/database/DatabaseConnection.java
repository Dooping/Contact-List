package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import authenticator.Account;
import authenticator.IAccount;
import exceptions.AuthenticationError;
import exceptions.LockedAccount;
import exceptions.UndefinedAccount;


public final class DatabaseConnection {

	private static final String URL = "jdbc:mysql://localhost/SS";
	private static final String USER = "root";
	private static final String PASSWORD = "root";
	private static final String REMOVE_SSL_WARING = "?useSSL=false";


	private static Statement connection(){
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

	public static boolean createUser(String username, String password){
		boolean result = false;
		Statement st = connection();
		String createUserQuery = "INSERT INTO accounts values ('"+username+"','"+password+"',0,0);";
		try {
			st.executeUpdate(createUserQuery);
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
	
	public static boolean deleteUser(String username){
		boolean result = false;
		Statement st = connection();
		String deleteUserQuery = "DELETE FROM accounts WHERE name = '"+username+"';";
		try {
			st.executeUpdate(deleteUserQuery);
			result = true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	public static void logout(IAccount acc){
		Statement st = connection();
		String query = "UPDATE accounts SET logged_in = 0 WHERE name = "+acc.getUsername();
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

	public static void login(IAccount acc) throws AuthenticationError{
		Statement st = connection();
		String query = "UPDATE accounts SET logged_in = 1 WHERE (name = '"
				+ acc.getUsername() + "' AND pwdhash = '" + acc.getPassword() + "')";
		
		try {
			st.executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new AuthenticationError();
		} finally {
			try {
				st.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static Account getAccount(String name) throws UndefinedAccount{
		Statement st = connection();
		String query = "SELECT * FROM accounts WHERE name = '"+name+"'";
		try {
//			st.executeQuery(query);
			st.executeQuery(query);
			ResultSet set = st.getResultSet();
			if(!set.first())
				throw new UndefinedAccount();
			String accName = set.getString("name");
			String accPwd = set.getString("pwdhash");
			Boolean accLocked = set.getBoolean("locked");
			Boolean accLogged = set.getBoolean("logged_in");
			Account acc = new Account(accName, accPwd, accLogged, accLocked);
			return acc;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				st.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return null;
	}


}
