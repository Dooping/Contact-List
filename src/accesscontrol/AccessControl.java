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
	
	public Capability makeCapability(String owner, String grantee, String resource, String operation,long time){
		try {
			DatabaseConnection.checkPermission(grantee, resource, operation);
			return new Capability(owner, grantee, resource, operation, time);
		} catch (PermissionNotExistsException e) {
			// ask??
			//popup?
			e.printStackTrace();
		}
		
		return null;
	}
	
	public boolean checkPermission(String principal, Capability capability, String resource, String operation){
		boolean result = false;
		try {
			Account acc = DatabaseConnection.getAccount(capability.getOwner());
			if(AESencrp.hash(capability.toString(), acc.getNonce()).equals(capability.getSignature()))
				if(principal.equals(capability.getGrantee()) && resource.equals(capability.getResource())
						&& operation.equals(capability.getOperation()) && capability.isValid())
					result = true;
				else
					throw new AccessControlError();
			else
				throw new AccessControlError();
		} catch (UndefinedAccount e) {
			e.printStackTrace();
		} catch (AccessControlError e) {
			//make capability??
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	public List<Capability> getCapabilities(HttpServletRequest req){
		String[] tokens = req.getParameterValues("capabilities");
		List<Capability> capabilities = new LinkedList<Capability>();
		JSONParser parser = new JSONParser();
		for(String t : tokens){
			try {
				capabilities.add(new Capability((JSONObject)parser.parse(t)));
					
			} catch (ParseException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return capabilities;
	}
}
