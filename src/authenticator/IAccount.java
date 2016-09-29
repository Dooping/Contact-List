package authenticator;

public interface IAccount {
	
	String getUsername();
	String getPassword();
	boolean isLoggedIn();
	boolean isLocked();
}
