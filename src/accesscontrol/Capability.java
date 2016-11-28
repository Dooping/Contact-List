package accesscontrol;

import org.json.simple.JSONObject;

import authenticator.AESencrp;

public class Capability {
	
	private String owner,grantee,operation, signature, resource;
	private long time;
	
	public Capability(String owner, String grantee, String resource, String operation,long time ){
		this.owner = owner;
		this.grantee = grantee;
		this.resource = resource;
		this.operation = operation;
		this.time = time;
	}
	
	public Capability(JSONObject token){
		JSONObject payload = (JSONObject)token.get("payload");
		this.owner = (String)payload.get("owner");
		this.grantee = (String)payload.get("grantee");
		this.resource = (String)payload.get("resource");
		this.operation = (String)payload.get("operation");
		this.time = (Long)payload.get("time");
		this.signature = (String)token.get("token");
	}
	
	@SuppressWarnings("unchecked")
	public String buildToken(int nonce) throws Exception{
		JSONObject payload = new JSONObject();
		payload.put("owner", owner);
		payload.put("grantee", grantee);
		payload.put("resource", new Integer(resource));
		payload.put("operation", operation);
		payload.put("time", new Long(time));
		String encrPayload = AESencrp.hash(payload.toJSONString(), nonce);
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

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public String getResource() {
		return resource;
	}

	public void setResource(String resource) {
		this.resource = resource;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}
	
	@SuppressWarnings("unchecked")
	public String toString(){
		JSONObject payload = new JSONObject();
		payload.put("owner", owner);
		payload.put("grantee", grantee);
		payload.put("resource", new Integer(resource));
		payload.put("operation", operation);
		payload.put("time", new Long(time));
		return payload.toJSONString();
	}

	public String getSignature() {
		return signature;
	}
	
	
}
