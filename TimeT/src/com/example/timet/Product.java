package com.example.timet;

public class Product {
	public String name;
	public String price;
	public Product(String name, String price) {
	    super();
	    this.name = name;
	    this.price = price;
	}
	public String getName() {
	    return name;
	}
	public void setName(String name) {
	    this.name = name;
	}
	public String getPrice() {
	    return price;
	}
	public void setPrice(String price) {
	    this.price = price;
	}
}
