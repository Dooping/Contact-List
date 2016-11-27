package authenticator;

public class Account implements IAccount {
	
	private String username;
	private String password;
	private boolean logged_in;
	private boolean locked;
	private int nonce;
	
	public Account(String username, String password, boolean logged_in, boolean locked, int nonce){
		this.username = username;
		this.password = password;
		this.logged_in = logged_in;
		this.locked = locked;
		this.nonce = nonce;
	}
	public String getUsername() {
		return username;
	}
	
	public String getPassword() {
		return password;
	}

	public boolean isLoggedIn() {
		return logged_in;
	}
	
	public boolean isLocked() {
		return locked;
	}
	
	public int getNonce() {
		return nonce;
	}

}
