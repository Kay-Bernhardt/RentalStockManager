package model.containers;

import java.io.Serializable;
import java.time.LocalDate;

public class Order implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private String name;
	private LocalDate dateIn;
	private LocalDate dateOut;
	private boolean confirmed;

	public Order(int id, String name, LocalDate dateIn, LocalDate dateOut, boolean confirmed)
	{
		this.id = id;
		this.name = name;
		this.dateIn = dateIn;
		this.dateOut = dateOut;
		this.confirmed = confirmed;
	}
	
	public Order(Order o)
	{
		this.id = o.getId();
		this.name = o.getName();
		this.dateIn = o.getDateIn();
		this.dateOut = o.getDateOut();
		this.confirmed = o.isConfirmed();
	}

	public int getId()
	{
		return id;
	}

	public String getName()
	{
		return name;
	}

	public LocalDate getDateIn()
	{
		return dateIn;
	}
	
	public LocalDate getDateOut()
	{
		return dateOut;
	}

	public boolean isConfirmed()
	{
		return confirmed;
	}

	public void setConfirmed(boolean confirmed)
	{
		this.confirmed = confirmed;
	}
	
	public void setId(int id)
	{
		this.id = id;
	}

	public String toString()
	{
		return name;
	}
}
