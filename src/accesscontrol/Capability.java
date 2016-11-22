package accesscontrol;

public class Capability {
	
	private String owner,grantee,nonce,operation;
	private int resource;
	private long time;
	
	public Capability(String owner, String grantee, String nonce, int resource, String operation,long time ){
		this.owner = owner;
		this.grantee = grantee;
		this.nonce = nonce;
		this.resource = resource;
		this.operation = operation;
		this.time = time;
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

	public String getNonce() {
		return nonce;
	}

	public void setNonce(String nonce) {
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
