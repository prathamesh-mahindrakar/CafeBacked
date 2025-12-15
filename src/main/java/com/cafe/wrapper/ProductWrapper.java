package com.cafe.wrapper;


public class ProductWrapper {
	
	private int id;
	
	private String name;
	
	private String description;
	
	private int price;
	
	private String status;
	
	private int cid;
	
	private String cname;

	public ProductWrapper() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ProductWrapper(int id, String name, String description, int price, String status, int cid, String cname) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.price = price;
		this.status = status;
		this.cid = cid;
		this.cname = cname;
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

	public int getCid() {
		return cid;
	}

	public void setCid(int cid) {
		this.cid = cid;
	}

	public String getCname() {
		return cname;
	}

	public void setCname(String cname) {
		this.cname = cname;
	}

	@Override
	public String toString() {
		return "ProductWrapper [id=" + id + ", name=" + name + ", description=" + description + ", price=" + price
				+ ", status=" + status + ", cid=" + cid + ", cname=" + cname + "]";
	}
	
	public ProductWrapper(int id, String name) {
		this.id =id;
		this.name = name;
	}
	
	public ProductWrapper(int id,String name,String description,int price) {
		this.id =id;
		this.name=name;
		this.description = description;
		this.price = price;
	}
	
	

}
