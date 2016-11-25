package accesscontrol;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

public class AccessControl {
	public AccessControl(){
		
	}
	
	public Capability makeCapability(){
		// Parâmetros: Owner,Grantee, nonce, Resource, Operation, Time
		return null;
	}
	
	public boolean checkPermission(){
		//Parâmetros: Principal, nonce, Capability, Resource, Operation
		return true;
	}
	public List<Capability> getCapabilities(HttpServletRequest req){
		//Parâmetros. HTTPRequest req
		return null;
	}
}
