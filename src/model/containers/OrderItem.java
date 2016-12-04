package model.containers;

import java.io.Serializable;

public class OrderItem implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int itemId;
	private int orderId;
	private int quant;
	
	public OrderItem()
	{
		
	}
	
	public OrderItem(int itemId, int orderId, int quant)
	{
		super();
		this.itemId = itemId;
		this.orderId = orderId;
		this.quant = quant;
	}

	public int getItemId()
	{
		return itemId;
	}

	public void setItemId(int itemId)
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

	public int getQuant()
	{
		return quant;
	}

	public void setQuant(int quant)
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
