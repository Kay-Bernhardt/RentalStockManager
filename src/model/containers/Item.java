package model.containers;

import java.io.Serializable;


public class Item implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private double id;
	private int quantity;
	private String name;

//===================== Constructor ===========================================
	
	public Item()
	{
		this.id = -1;
		this.quantity = 0;
		this.name = "This Item does not exist";
	}
	
	public Item(double id, int quantity, String name)
	{
		this.id = id;
		this.name = name;		
		this.quantity = quantity;
	}
	
	public Item (Item item)
	{
		this.id = item.getId();
		this.name = item.getName();		
		this.quantity = item.getQuantity();
	}

	public String toString()
	{
		return "ID: " + this.id + " Quantity: " + this.quantity + " Name: " + this.name;
	}
	
//======================Getters / Setters =======================================================
	
	public double getId() {
		return id;
	}

	public void setId(double id) {
		this.id = id;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}	
}