package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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


	private static Connection connection(){
		Connection connection = null;
		try{
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection(URL+REMOVE_SSL_WARNING, USER, PASSWORD);
			return connection;

		} catch (Exception e){
			System.out.println("Connection failed");
			e.printStackTrace();
		}
		return null;
	}

	public static boolean createUser(String username, String password){
		boolean result = false;
		Connection conn = connection();
		String sql = "INSERT INTO " + TABLE_NAME +" values (?,?,0,0);";
		
		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, username);
			st.setString(2, password);
			st.executeUpdate();
			result = true;
			st.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	public static boolean deleteUser(String username){
		boolean result = false;
		Connection conn = connection();
		//String deleteUserQuery = "DELETE FROM accounts WHERE name = '"+username+"';";
		String sql = "DELETE FROM "+ TABLE_NAME +" WHERE "+NAME+" = ?;";
		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, username);
			st.executeUpdate();
			result = true;
			st.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	public static void lockedUser(String username){
		Connection conn = connection();
		//String lockUserQuery = "UPDATE accounts SET locked=1 WHERE name = '"+ username +"'";
		String sql = "UPDATE "+TABLE_NAME+" SET "+LOCKED+"=1 WHERE "+NAME+" = ?";
		try{
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, username);
			st.executeUpdate();
			st.close();
		} catch(SQLException e){
			e.printStackTrace();
		} finally {
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static boolean changePassword(String username, String password){
		boolean result = false;
		Connection conn = connection();
		//String changePasswordQuery = "UPDATE accounts SET pwdhash = '"+password+"' WHERE name = '"+username+"'";
		String sql = "UPDATE "+TABLE_NAME+" SET "+PWDHASH+" = ? WHERE "+NAME+" = ?";
		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, password);
			st.setString(2, username);
			st.executeUpdate();
			result = true;
			st.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	public static void logout(IAccount acc){
		Connection conn = connection();
		//String query = "UPDATE accounts SET logged_in = 0 WHERE name = '"+acc.getUsername()+"'";
		String sql = "UPDATE "+TABLE_NAME+" SET "+LOGGED_IN+" = 0 WHERE "+NAME+" = ?";
		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, acc.getUsername());
			st.executeUpdate();
			st.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static void login(IAccount acc) throws AuthenticationError{
		Connection conn = connection();
		//String query = "UPDATE accounts SET logged_in = 1 WHERE (name = '"+ acc.getUsername() + "' AND pwdhash = '" + acc.getPassword() + "')";
		String sql = "UPDATE "+TABLE_NAME+" SET "+LOGGED_IN+" = 1 WHERE ("+NAME+" = ?"
				+ " AND "+PWDHASH+" = ?)";
		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, acc.getUsername());
			st.setString(2, acc.getPassword());
			st.executeUpdate();
			st.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new AuthenticationError();
		} finally {
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static Account getAccount(String name) throws UndefinedAccount{
		Connection conn = connection();
		//String query = "SELECT * FROM accounts WHERE name = '"+name+"'";
		String sql = "SELECT * FROM "+TABLE_NAME+" WHERE "+NAME+" = ?";
		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, name);
			ResultSet set = st.executeQuery();
			if(!set.first())
				throw new UndefinedAccount();
			String accName = set.getString("name");
			String accPwd = set.getString("pwdhash");
			Boolean accLocked = set.getBoolean("locked");
			Boolean accLogged = set.getBoolean("logged_in");
			Account acc = new Account(accName, accPwd, accLogged, accLocked);
			st.close();
			return acc;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public static List<String> getFriends(String name){
		Connection conn = connection();
		String sql = "select name from ((SELECT accepter as 'name' FROM friendships WHERE requester = ? and accepted = 1)"
				+ "union "
				+ "(SELECT requester as 'name' FROM friendships WHERE accepter = ? and accepted = 1)) as results"
				+ "INNER JOIN accounts using (name) where accounts.locked = 0";


		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, name);
			st.setString(2, name);
			ResultSet set = st.executeQuery();
			List<String> friends = new LinkedList<String>();
			while (set.next()) {
				  String s = set.getString("name");
				  friends.add(s);
			}
			return friends;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public static List<String> getFriendRequests(String name){
		Connection conn = connection();
		String sql = "select requester as 'name' from friendships as f"
				+ " inner join accounts as a on f.name = a.name"
				+ " where accepter = ? and accepted = 0 and locked = 0";


		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, name);
			ResultSet set = st.executeQuery();
			List<String> friends = new LinkedList<String>();
			while (set.next()) {
				  String s = set.getString("name");
				  friends.add(s);
			}
			st.close();
			return friends;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public static boolean checkFriendship(String requester, String accepter){
		boolean result = false;
		Connection conn = connection();
		String sql = "select * from friendships WHERE (requester = ? and accepter = ?)"
				+ " or (requester = ? and accepter = ?)";
		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, requester);
			st.setString(2, accepter);
			st.setString(3, accepter);
			st.setString(4, requester);
			ResultSet set = st.executeQuery();
			if(set.first())
				result = true;
			st.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	public static boolean newFriendRequest(String requester, String accepter){
		boolean result = false;
		if(!checkFriendship(requester,accepter)){
			Connection conn = connection();
			String sql = "INSERT INTO friendships values (?,?,0);";
			
			try {
				PreparedStatement st = conn.prepareStatement(sql);
				st.setString(1, requester);
				st.setString(2, accepter);
				st.executeUpdate();
				result = true;
				st.close();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				try {
					if (conn != null)
						conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}
	
	public static boolean acceptFriendRequest(String requester, String accepter){
		boolean result = false;
		Connection conn = connection();
		String sql = "UPDATE friendships SET accepted = 1 WHERE requester = ? and accepter = ?";
		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, requester);
			st.setString(2, accepter);
			st.executeUpdate();
			result = true;
			st.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	public static boolean denyFriendRequest(String requester, String accepter){
		boolean result = false;
		Connection conn = connection();
		String sql = "delete from friendships WHERE requester = ? and accepter = ?;";
		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, requester);
			st.setString(2, accepter);
			st.executeUpdate();
			result = true;
			st.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	public static boolean deleteFriend(String user, String friend){
		boolean result = false;
		Connection conn = connection();
		String sql = "delete from friendships WHERE (requester = ? and accepter = ?)"
				+ " or (requester = ? and accepter = ?)";
		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, user);
			st.setString(2, friend);
			st.setString(3, friend);
			st.setString(4, user);
			st.executeUpdate();
			result = true;
			st.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	public static List<String> getUserList(Boolean withLocked){
		Connection conn = connection();
		
		String sql;
		if (withLocked)
			sql = "select name from accounts";
		else
			sql = "select name from accounts where locked = 0";


		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet set = st.executeQuery();
			List<String> names = new LinkedList<String>();
			while (set.next()) {
				  String s = set.getString("name");
				  names.add(s);
			}
			st.close();
			return names;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public static Map<String,String> getUserPermissions(String username){
		Connection conn = connection();
		
		String sql = "select name, operation from accesscontrol as a "
				+ "inner join resources as r on a.resource = r.id where principal = ?";


		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, username);
			ResultSet set = st.executeQuery();
			Map<String,String> permissions = new HashMap<String,String>();
			while (set.next()) {
				  String resource = set.getString("name");
				  String operation = set.getString("operation");
				  permissions.put(resource,  operation);
			}
			st.close();
			return permissions;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public static boolean setNewPermission(String principal, String owner, String resource, String operation){
		boolean result = false;
		Connection conn = connection();
		String sql = "INSERT INTO accesscontrol('principal','resource','operation') "
				+ "select ?, id, ? from resources where owner = ? and name = ?";
		
		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, principal);
			st.setString(2, operation);
			st.setString(3, owner);
			st.setString(4, resource);
			st.executeUpdate();
			result = true;
			st.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	public static int getAccountId(String name) throws UndefinedAccount{
		Connection conn = connection();
		String sql = "SELECT * FROM ACCOUNTS WHERE NAME = ?;";
		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, name);
			ResultSet set = st.executeQuery();
			if(!set.first())
				throw new UndefinedAccount();
			int id = set.getInt("id");	
			st.close();
			return id;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return -1;
	}
}
