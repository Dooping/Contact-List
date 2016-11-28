package accesscontrol;

import org.json.simple.JSONObject;

import authenticator.AESencrp;

public class Capability {
	
	private String owner,grantee,operation, signature, resource;
	private long time;
	
	@SuppressWarnings("unchecked")
	public Capability(String owner, int nonce, String grantee, String resource, String operation,long time ){
		this.owner = owner;
		this.grantee = grantee;
		this.resource = resource;
		this.operation = operation;
		this.time = time;
		if(nonce >= 0){
			JSONObject payload = new JSONObject();
			payload.put("owner", owner);
			payload.put("grantee", grantee);
			payload.put("resource", resource);
			payload.put("operation", operation);
			payload.put("time", new Long(time));
			try {
				this.signature = AESencrp.hash(payload.toJSONString(), nonce);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
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
		payload.put("resource", resource);
		payload.put("operation", operation);
		payload.put("time", new Long(time));
		payload.put("signature", signature);
		return payload.toJSONString();
	}
	
	@SuppressWarnings("unchecked")
	public String getPayload(){
		JSONObject payload = new JSONObject();
		payload.put("owner", owner);
		payload.put("grantee", grantee);
		payload.put("resource", resource);
		payload.put("operation", operation);
		payload.put("time", new Long(time));
		return payload.toJSONString();
	}

	public String getSignature() {
		return signature;
	}
	
	
}
