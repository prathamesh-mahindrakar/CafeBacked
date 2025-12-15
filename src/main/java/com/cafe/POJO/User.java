package com.cafe.POJO;

import java.io.Serializable;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table; 
import lombok.Data;

@NamedQuery(name = "User.findByEmailId", query = "select u from User u where u.email = :email")

@NamedQuery(name = "User.getAllUser", query = "select new com.cafe.wrapper.UserWrapper(u.id, u.name,u.contactNumber, u.email, u.status) from User u where u.role ='user'")

@NamedQuery(name = "User.updateStatus",query = "update User u set u.status=:status where u.id=:id")

@NamedQuery(name = "User.getAllAdmin", query = "select u.email from User u where u.role ='admin'")


@Entity
@Data
@DynamicInsert
@DynamicUpdate
@Table(name = "users")
public class User implements Serializable{
	
	private static final long serialVersionUID=1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private String name;
	
	private String contactNumber;
	
	private String email;
	
	private String password;
	
	private String status;
	
	private String role;

	public User(int id, String name, String contactNumber, String email, String password, String status, String role) {
		super();
		this.id = id;
		this.name = name;
		this.contactNumber = contactNumber;
		this.email = email;
		this.password = password;
		this.status = status;
		this.role = role;
	}

	public User() {
		super();
		// TODO Auto-generated constructor stub
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", contactNumber=" + contactNumber + ", email=" + email
				+ ", password=" + password + ", status=" + status + ", role=" + role + "]";
	}
	
	
	

}


