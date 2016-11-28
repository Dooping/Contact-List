package accesscontrol;

import java.util.LinkedList;
import java.util.List;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import authenticator.AESencrp;
import authenticator.Account;
import database.DatabaseConnection;
import exceptions.AccessControlError;
import exceptions.PermissionNotExistsException;
import exceptions.UndefinedAccount;

import javax.servlet.http.HttpServletRequest;

public class AccessControl {
	public AccessControl(){
		
	}
	
	public Capability makeCapability(String owner, String grantee, String resource, String operation,long time, boolean ask) throws PermissionNotExistsException{
		try {
			DatabaseConnection.checkPermission(grantee, resource, operation);
			Account acc = DatabaseConnection.getAccount(owner);
			return new Capability(owner, acc.getNonce(), grantee, resource, operation, time);
		} catch (PermissionNotExistsException e) {
			if(ask){
				// ask??
				//popup?
			}
			else
				throw new PermissionNotExistsException();
		} catch (UndefinedAccount e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public boolean checkPermission(String principal, Capability capability, String resource, String operation){
		boolean result = false;
		try {
			Account acc = DatabaseConnection.getAccount(capability.getOwner());
			try {
				if(AESencrp.hash(capability.getPayload(), acc.getNonce()).equals(capability.getSignature()))
					if(principal.equals(capability.getGrantee()) && resource.equals(capability.getResource())
							&& operation.equals(capability.getOperation()) && capability.isValid())
						result = true;
					else
						throw new AccessControlError();
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
		System.out.println(list);
		List<Capability> capabilities = new LinkedList<Capability>();
		//JSONParser parser = new JSONParser();
		if(list != null)
			/*for(String t : list){
				try {
					capabilities.add(new Capability((JSONObject)parser.parse(t)));
						
				} catch (ParseException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}*/
			capabilities = list;
		return capabilities;
	}
}
