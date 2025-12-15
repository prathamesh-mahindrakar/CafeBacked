package com.cafe.wrapper;


public class UserWrapper {
	
	private int id;
	private String email;
	private String name;
	private String contactNumber;
	private String status;
	
	public UserWrapper() {
		super();
		// TODO Auto-generated constructor stub
	}

	public UserWrapper(int id, String email, String name, String contactNumber, String status) {
		super();
		this.id = id;
		this.email = email;
		this.name = name;
		this.contactNumber = contactNumber;
		this.status = status;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "UserWrapper [id=" + id + ", email=" + email + ", name=" + name + ", contactNumber=" + contactNumber
				+ ", status=" + status + "]";
	}
	
	
	
	

}
