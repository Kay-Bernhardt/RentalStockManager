package model.containers;

import java.io.Serializable;

public class OrderItem implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private double itemId;
	private int orderId;
	private double quant;
	
	public OrderItem()
	{
		
	}
	
	public OrderItem(double itemId, int orderId, double quant)
	{
		super();
		this.itemId = itemId;
		this.orderId = orderId;
		this.quant = quant;
	}

	public double getItemId()
	{
		return itemId;
	}

	public void setItemId(double itemId)
	{
		this.itemId = itemId;
	}

	public int getOrderId()
	{
		return orderId;
	}

	public void setOrderId(int orderId)
	{
		this.orderId = orderId;
	}

	public double getQuant()
	{
		return quant;
	}

	public void setQuant(double quant)
	{
		this.quant = quant;
	}

	@Override
	public String toString()
	{
		return "OrderItem [itemId=" + itemId + ", orderId=" + orderId
				+ ", quant=" + quant + "]\n";
	}	
}
