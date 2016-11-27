package accesscontrol;

import java.security.*;

import org.json.simple.JSONObject;

import authenticator.AESencrp;

public class Capability {
	
	private String owner,grantee,operation;
	private int resource,nonce;
	private long time;
	
	public Capability(String owner, String grantee, int nonce, int resource, String operation,long time ){
		this.owner = owner;
		this.grantee = grantee;
		this.nonce = nonce;
		this.resource = resource;
		this.operation = operation;
		this.time = time;
	}
	
	@SuppressWarnings("unchecked")
	public String build() throws Exception{
		JSONObject payload = new JSONObject();
		payload.put("owner", owner);
		payload.put("grantee", grantee);
		payload.put("nonce", new Integer(nonce));
		payload.put("resource", new Integer(resource));
		payload.put("operation", operation);
		payload.put("time", new Long(time));
		String encrPayload = AESencrp.encrypt(payload.toJSONString());
		JSONObject result = new JSONObject();
		result.put("payload", payload);
		result.put("token", encrPayload);
		return result.toJSONString();
	}
	
	public boolean isValid(){
		return System.currentTimeMillis()<=time;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getGrantee() {
		return grantee;
	}

	public void setGrantee(String grantee) {
		this.grantee = grantee;
	}

	public int getNonce() {
		return nonce;
	}

	public void setNonce(int nonce) {
		this.nonce = nonce;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public int getResource() {
		return resource;
	}

	public void setResource(int resource) {
		this.resource = resource;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}
	
	
}
