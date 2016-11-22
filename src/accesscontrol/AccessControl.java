package accesscontrol;

import java.util.List;

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
	public List<Capability> getCapabilities(){
		//Parâmetros. HTTPRequest req
		return null;
	}
}
