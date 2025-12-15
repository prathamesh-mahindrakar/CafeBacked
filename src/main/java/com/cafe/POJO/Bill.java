package com.cafe.POJO;

import java.io.Serializable;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;


@NamedQuery(name = "Bill.getAllBills",query = "select b from Bill b order by b.id desc")

@NamedQuery(name = "Bill.getBillByUserName" , query = "select b from Bill b where b.createdBy=:username order by b.id desc")


@Entity
public class Bill implements Serializable{
	
	private static final long serialVersionID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private String uuid;
	
	private String name;
	
	private String email;
	
	private String contactNumber;
	
	private String paymentMethod;
	
	private int total;
	
	@Column(columnDefinition = "json")
	private String productDetail;
	
	private String createdBy;
	
	
	public Bill() {
		super();
		// TODO Auto-generated constructor stub
	}




	public Bill(int id, String uuid, String name, String email, String contactNumber, String paymentMethod, int total,
			String productDetail, String createdBy) {
		super();
		this.id = id;
		this.uuid = uuid;
		this.name = name;
		this.email = email;
		this.contactNumber = contactNumber;
		this.paymentMethod = paymentMethod;
		this.total = total;
		this.productDetail = productDetail;
		this.createdBy = createdBy;
	}




	public int getId() {
		return id;
	}




	public void setId(int id) {
		this.id = id;
	}




	public String getUuid() {
		return uuid;
	}




	public void setUuid(String uuid) {
		this.uuid = uuid;
	}




	public String getName() {
		return name;
	}




	public void setName(String name) {
		this.name = name;
	}




	public String getEmail() {
		return email;
	}




	public void setEmail(String email) {
		this.email = email;
	}




	public String getContactNumber() {
		return contactNumber;
	}




	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}




	public String getPaymentMethod() {
		return paymentMethod;
	}




	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}




	public int getTotal() {
		return total;
	}




	public void setTotal(int total) {
		this.total = total;
	}




	public String getProductDetail() {
		return productDetail;
	}




	public void setProductDetail(String productDetail) {
		this.productDetail = productDetail;
	}




	public String getCreatedBy() {
		return createdBy;
	}




	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}




	public static long getSerialversionid() {
		return serialVersionID;
	}




	@Override
	public String toString() {
		return "Bill [id=" + id + ", uuid=" + uuid + ", name=" + name + ", email=" + email + ", contactNumber="
				+ contactNumber + ", paymentMethod=" + paymentMethod + ", total=" + total + ", productDetail="
				+ productDetail + ", createdBy=" + createdBy + "]";
	}
	
	
	
	

}
