package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.util.LinkedList;
import java.util.List;

import authenticator.Account;
import authenticator.IAccount;
import contact_list.ContactDetailed;
import exceptions.AccessControlError;
import exceptions.AuthenticationError;
import exceptions.UndefinedAccount;


public final class DatabaseConnection {

	
	private static final String URL = "jdbc:mysql://localhost/SS";
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

	public static boolean createUser(String username, String password, int nonce){
		boolean result = false;
		Connection conn = connection();
		String sql = "INSERT INTO accounts (name, pwdhash, logged_in, locked, nonce) values (?,?,0,0,?);";
		
		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, username);
			st.setString(2, password);
			st.setInt(3, nonce);
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
		String sql = "DELETE FROM accounts WHERE name = ?;";
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
	
	public static void lockUser(String username){
		Connection conn = connection();
		//String lockUserQuery = "UPDATE accounts SET locked=1 WHERE name = '"+ username +"'";
		String sql = "UPDATE accounts SET locked=1 WHERE name = ?";
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
	
	public static void unlockUser(String username){
		Connection conn = connection();
		String sql = "UPDATE accounts SET locked=0 WHERE name = ?";
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
	
	public static void resetNonce(String username, int nonce){
		Connection conn = connection();
		String sql = "UPDATE accounts SET nonce = ? WHERE name = ?";
		try{
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, nonce);
			st.setString(2, username);
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
		String sql = "UPDATE accounts SET pwdhash = ? WHERE name = ?";
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
		String sql = "UPDATE accounts SET logged_in = 0 WHERE name = ?";
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
		String sql = "UPDATE accounts SET logged_in = 1 WHERE (name = ?"
				+ " AND pwdhash = ?)";
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
		String sql = "SELECT * FROM accounts WHERE name = ?";
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
			int nonce = set.getInt("nonce");
			Account acc = new Account(accName, accPwd, accLogged, accLocked, nonce);
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
				+ " inner join accounts as a on f.requester = a.name"
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
	
	public static List<Account> getUserList(Boolean withLocked){
		Connection conn = connection();
		
		String sql;
		if (withLocked)
			sql = "select name, locked from accounts";
		else
			sql = "select name, locked from accounts where locked = 0";


		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet set = st.executeQuery();
			List<Account> names = new LinkedList<Account>();
			while (set.next()) {
				  String s = set.getString("name");
				  boolean l = set.getBoolean("locked");
				  names.add(new Account(s,null,true,l,-1));
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
	
	public static void checkPermission(String principal, String resource, String operation) throws AccessControlError{
		Connection conn = connection();
		String sql = "select * from accesscontrol as a "
				+ "inner join resources as r on a.resource = r.id where principal = ? and r.name = ?"
				+ " and a.operation = ?";
		
		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, principal);
			st.setString(2, resource);
			st.setString(3, operation);
			ResultSet set = st.executeQuery();
			if(!set.first())
				throw new AccessControlError();
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
	
	public static ContactDetailed getUserDetails(String name) throws UndefinedAccount{
		Connection conn = DatabaseConnection.connection();
		String sql = "SELECT * FROM DETAILS WHERE NAME = ?";
		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, name);
			ResultSet set = st.executeQuery();
			if(!set.first())
				throw new UndefinedAccount();
			char sex = set.getString("sex").charAt(0);
			String work = set.getString("work");
			Date birthdate = set.getDate("birthdate");
			String location = set.getString("location");
			String origin = set.getString("origin");
			String email = set.getString("email");
			String phone = set.getString("phone");
			String internal_statement = set.getString("internal_statement");
			String external_statement = set.getString("external_statement");
			ContactDetailed contactdetailed = new ContactDetailed(name, sex, work, birthdate, location, origin, email, phone, internal_statement, external_statement);
			st.close();
			return contactdetailed;
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
	
	public static boolean setUserDetails(ContactDetailed cd){
		boolean result = false;
		Connection conn = DatabaseConnection.connection();
		String sql = "UPDATE DETAILS SET "
				+ "sex = ? ," + "work = ? ," + "birthdate = ? ," + "location = ? ," + "origin = ? ," + "email = ? ,"
				+ "phone = ? ," + "internal_statement = ? ," + "external_statement = ? " +  
				"WHERE NAME = ?;";
		
		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, String.valueOf(cd.getSex()));
			st.setString(2, cd.getWork());
			st.setDate(3, cd.getBirthdate());
			st.setString(4, cd.getLocation());
			st.setString(5, cd.getOrigin());
			st.setString(6, cd.getEmail());
			st.setString(7, cd.getPhone());
			st.setString(8, cd.getInternal_statement());
			st.setString(9, cd.getExternal_statement());
			st.setString(10, cd.getName());
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
	
	
	public static void createUserDetails(String name){
		Connection conn = connection();
		String sql = "INSERT INTO details(name,sex) VALUES (?,'-')";
		
		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, name);
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
	
	public static void createUserResources(String name){
		Connection conn = connection();
		String sql = "INSERT INTO resources(owner, name, permission) VALUES (?,?,'internal'),(?,?,'internal'),(?,?, 'private'),(?,?,'internal')";
		
		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, name);
			st.setString(2, "profile"+name);
			st.setString(3, name);
			st.setString(4, "contacts"+name);
			st.setString(5, name);
			st.setString(6, "friends"+name);
			st.setString(7, name);
			st.setString(8, "internal"+name);
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
	
	
	
	public static void createUserAccessControl(String name){
		Connection conn = connection();
		String sql = "INSERT INTO accesscontrol(principal, resource, operation) "
				+ "select ?, id, 'read' from resources "
				+ "where owner = ? and name in (?,?,?,?)";
		
		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, name);
			st.setString(2, name);
			st.setString(3, "profile"+name);
			st.setString(4, "contacts"+name);
			st.setString(5, "friends"+name);
			st.setString(6, "internal"+name);
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
	
	public static void addFriendAccessControl(String name, String friend){
		Connection conn = connection();
		String sql = "INSERT INTO accesscontrol(principal, resource, operation) "
				+ "select ?, id, 'read' from resources "
				+ "where owner = ? and name in (select name from resources where permission = 'internal' and owner = ?)";
		
		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, name);
			st.setString(2, friend);
			st.setString(3, friend);
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
	
	public static void deleteFriendAccessControl(String name, String friend){
		Connection conn = connection();
		String sql = "delete from accesscontrol where principal = ?"
				+ " and resource in (select id from resources where owner = ?)";
		
		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, name);
			st.setString(2, friend);
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
	
	public static String checkInformationPermission(String owner, String resourceName) throws AccessControlError{
		String result = null;
		Connection conn = connection();
		String sql = "SELECT PERMISSION FROM RESOURCES WHERE OWNER = ? AND NAME = ?;";
		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, owner);
			st.setString(2, resourceName);
			ResultSet set = st.executeQuery();
			if(!set.first())
				throw new AccessControlError();
			result = set.getString("permission");
			st.close();
			return result;
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
	
	public static boolean setInformationPermission(String permission, String owner, String resourceName){
		boolean result = false;
		Connection conn = DatabaseConnection.connection();
		String sql = "UPDATE RESOURCES SET permission = ? where owner = ? and name = ? ;";
		
		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, permission);
			st.setString(2, owner);
			st.setString(3, resourceName);
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
	
	public static void refreshFriendsAccessControl(String name, String resource){
		Connection conn = connection();
		String sql = "replace INTO accesscontrol(principal, resource, operation) "
				+ "select name2, id, 'read' from resources as r join (select name as name2 "
				+ "from ((SELECT accepter as 'name' FROM friendships WHERE requester = ? and accepted = 1)"
				+ " union "
				+ "(SELECT requester as name2 FROM friendships WHERE accepter = ? and accepted = 1)) as results "
				+ "INNER JOIN accounts using (name) where accounts.locked = 0) as n "
				+ "where r.name = ?";
		
		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, name);
			st.setString(2, name);
			st.setString(3, resource);
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
	
	public static void deleteFriendsAccessControl(String resource, String owner){
		Connection conn = connection();
		String sql = "delete from accesscontrol where resource in (select id from resources where name = ?) and principal <> ?";
		
		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, resource);
			st.setString(2, owner);
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
	
	public static boolean isLocked(String name) throws UndefinedAccount{
		Connection conn = connection();
		//String query = "SELECT * FROM accounts WHERE name = '"+name+"'";
		String sql = "SELECT locked FROM accounts WHERE name = ?";
		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, name);
			ResultSet set = st.executeQuery();
			if(!set.first())
				throw new UndefinedAccount();
			Boolean accLocked = set.getBoolean("locked");
			st.close();
			return accLocked;
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
		return false;
	}
	
}
