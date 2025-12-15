package com.cafe.POJO;

import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQuery;


@NamedQuery(name = "Product.getAllProduct",query = "select new com.cafe.wrapper.ProductWrapper(p.id, p.name, p.description, p.price,p.status, p.category.id, p.category.name) from Product p")

@NamedQuery(name = "Product.updateProductStatus",query = "update Product p set p.status=:status where p.id=:id")

@NamedQuery(name = "Product.getProductByCategory",query = "select new com.cafe.wrapper.ProductWrapper(p.id,p.name) from Product p where p.category.id=:id and p.status='true'")

@NamedQuery(
	    name = "Product.getProductById",
	    query = "select new com.cafe.wrapper.ProductWrapper(p.id, p.name, p.description, p.price) from Product p where p.id=:id"
	)

@Entity
public class Product implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private String name;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "category_fk",nullable = false)
	private Category category; 
	
	private String description;
	
	private int price;
	
	private String status;

	public Product() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Product(int id, String name, Category category, String description, int price, String status) {
		super();
		this.id = id;
		this.name = name;
		this.category = category;
		this.description = description;
		this.price = price;
		this.status = status;
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

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "Product [id=" + id + ", name=" + name + ", category=" + category + ", description=" + description
				+ ", price=" + price + ", status=" + status + "]";
	}
	
	
	

}
