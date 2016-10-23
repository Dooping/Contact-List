package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import authenticator.Account;
import authenticator.IAccount;
import exceptions.AuthenticationError;

import exceptions.UndefinedAccount;


public final class DatabaseConnection {

	public static final String DATABASE_NAME="SS";
	public static final String TABLE_NAME="accounts";
	private static final String NAME = "name";
	private static final String PWDHASH = "pwdhash";
	private static final String LOGGED_IN = "logged_in";
	private static final String LOCKED = "locked";
	
	
	
	private static final String URL = "jdbc:mysql://localhost/"+DATABASE_NAME;
	private static final String USER = "root";
	private static final String PASSWORD = "root";
	private static final String REMOVE_SSL_WARNING = "?useSSL=false";


	private static Statement connection(){
		Connection connection = null;
		try{
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection(URL+REMOVE_SSL_WARNING, USER, PASSWORD);
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
		//String createUserQuery = "INSERT INTO accounts values ('"+username+"','"+password+"',0,0);";
		String createUserQuery = "INSERT INTO " + TABLE_NAME +" values ('"+username+"','"+password+"',0,0);";
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
		//String deleteUserQuery = "DELETE FROM accounts WHERE name = '"+username+"';";
		String deleteUserQuery = "DELETE FROM "+ TABLE_NAME +" WHERE "+NAME+" = '"+username+"';";
		try {
			st.executeUpdate(deleteUserQuery);
			result = true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public static void lockedUser(String username){
		Statement st = connection();
		//String lockUserQuery = "UPDATE accounts SET locked=1 WHERE name = '"+ username +"'";
		String lockUserQuery = "UPDATE "+TABLE_NAME+" SET "+LOCKED+"=1 WHERE "+NAME+" = '"+ username +"'";
		try{
			st.executeUpdate(lockUserQuery);
		} catch(SQLException e){
			e.printStackTrace();
		}
	}
	
	public static boolean changePassword(String username, String password){
		boolean result = false;
		Statement st = connection();
		//String changePasswordQuery = "UPDATE accounts SET pwdhash = '"+password+"' WHERE name = '"+username+"'";
		String changePasswordQuery = "UPDATE "+TABLE_NAME+" SET "+PWDHASH+" = '"+password+"' WHERE "+NAME+" = '"+username+"'";
		try {
			st.executeUpdate(changePasswordQuery);
			result = true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	public static void logout(IAccount acc){
		Statement st = connection();
		//String query = "UPDATE accounts SET logged_in = 0 WHERE name = '"+acc.getUsername()+"'";
		String query = "UPDATE "+TABLE_NAME+" SET "+LOGGED_IN+" = 0 WHERE "+NAME+" = '"+acc.getUsername()+"'";
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
		//String query = "UPDATE accounts SET logged_in = 1 WHERE (name = '"+ acc.getUsername() + "' AND pwdhash = '" + acc.getPassword() + "')";
		
		String query = "UPDATE "+TABLE_NAME+" SET "+LOGGED_IN+" = 1 WHERE ("+NAME+" = '"
				+ acc.getUsername() + "' AND "+PWDHASH+" = '" + acc.getPassword() + "')";
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
		//String query = "SELECT * FROM accounts WHERE name = '"+name+"'";
		String query = "SELECT * FROM "+TABLE_NAME+" WHERE "+NAME+" = '"+name+"'";
		try {
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
