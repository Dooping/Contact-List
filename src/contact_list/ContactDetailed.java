package contact_list;

import java.sql.Date;

public class ContactDetailed {
	private String name;
	private char sex;
	private String work;
	private Date birthdate;
	private String location;
	private String origin;
	private String email;
	private String phoneNumber;
	private String internal_statment;
	private String external_statement;
	
	public ContactDetailed(String name, char sex, String work, Date birthdate, String location, String origin, String email, String phoneNumber, String internal_statment, String external_statement){
		this.name = name;
		this.sex = sex;
		this.work = work;
		this.birthdate = birthdate;
		this.location = location;
		this.origin = origin;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.internal_statment = internal_statment;
		this.external_statement = external_statement;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public char getSex() {
		return sex;
	}

	public void setSex(char sex) {
		this.sex = sex;
	}

	public String getWork() {
		return work;
	}

	public void setWork(String work) {
		this.work = work;
	}

	public Date getBirthdate() {
		return birthdate;
	}

	public void setBirthdate(Date birthdate) {
		this.birthdate = birthdate;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getInternal_statment() {
		return internal_statment;
	}

	public void setInternal_statment(String internal_statment) {
		this.internal_statment = internal_statment;
	}

	public String getExternal_statement() {
		return external_statement;
	}

	public void setExternal_statement(String external_statement) {
		this.external_statement = external_statement;
	}
}
