package accesscontrol;

import java.util.LinkedList;
import java.util.List;

import authenticator.AESencrp;
import authenticator.Account;
import database.DatabaseConnection;
import exceptions.AccessControlError;
import exceptions.UndefinedAccount;

import javax.servlet.http.HttpServletRequest;

public class AccessControl {
	public AccessControl(){
		
	}
	
	public Capability makeCapability(String owner, String grantee, String resource, String operation,long time) throws AccessControlError{
		try {
			DatabaseConnection.checkPermission(grantee, resource, operation);
			Account acc = DatabaseConnection.getAccount(owner);
			return new Capability(owner, acc.getNonce(), grantee, resource, operation, time);
		} catch (AccessControlError e) {
			throw new AccessControlError();
		} catch (UndefinedAccount e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public boolean checkPermission(String principal, List<Capability> capabilities, String resource, String operation){
		boolean result = false;
		try {
			Capability c = null;
			for(Capability capability : capabilities)
				if(principal.equals(capability.getGrantee()) && resource.equals(capability.getResource())
						&& operation.equals(capability.getOperation())){
					c = capability;
					break;
				}
			if (c==null)
				return false;
			if (!c.isValid()){
				capabilities.remove(c);
				return false;
			}
			Account acc = DatabaseConnection.getAccount(c.getOwner());
			try {
				if(AESencrp.hash(c.getPayload(), acc.getNonce()).equals(c.getSignature()))
						result = true;
					else
						throw new AccessControlError();
			} catch (AccessControlError e) {
				//e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (UndefinedAccount e) {
			e.printStackTrace();
		}
		return result;
	}
	@SuppressWarnings("unchecked")
	public List<Capability> getCapabilities(HttpServletRequest req){
		List<Capability> list = (List<Capability>)req.getSession().getAttribute("capabilities");
		if(list != null)
			return list;
		return new LinkedList<Capability>();
	}
}
