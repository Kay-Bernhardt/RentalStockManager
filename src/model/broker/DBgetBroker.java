package model.broker;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;

import model.containers.Item;
import model.containers.Order;
import model.containers.OrderItem;
import utility.Utility;

public class DBgetBroker
{
	public static ArrayList<Item> getAllItems(Connection conn)
	{
		ArrayList<Item> itemList = new ArrayList<Item>();
		try
		{
			Statement s = conn.createStatement();
			ResultSet rs = s.executeQuery("SELECT * FROM ITEM");

			while (rs.next())
			{
				Item item = new Item(rs.getDouble("item_id"), rs.getDouble("total_quant"), rs.getString("item_name"));
				itemList.add(item);
			}
			s.close();
			rs.close();

		} catch (SQLException e)
		{
			e.printStackTrace();
		}
		Utility.sortById(itemList);
		return itemList;
	}

	public static ArrayList<OrderItem> getAllOrderItems(Connection conn)
	{
		ArrayList<OrderItem> itemList = new ArrayList<OrderItem>();
		try
		{
			Statement s = conn.createStatement();
			ResultSet rs = s.executeQuery("SELECT * FROM ORDERITEM");

			while (rs.next())
			{
				OrderItem orderItem = new OrderItem(rs.getDouble("item_id"), rs.getInt("order_id"), rs.getDouble("quant"));
				itemList.add(orderItem);
			}
			rs.close();
			s.close();
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
		return itemList;
	}

	public static ArrayList<Order> getAllOrders(Connection conn)
	{
		ArrayList<Order> orderList = new ArrayList<Order>();

		try
		{
			Statement s = conn.createStatement();
			ResultSet rs = s.executeQuery("SELECT * FROM ORDERS");

			while (rs.next())
			{
				Order order = new Order(rs.getInt("order_id"), rs.getString("order_name"), (rs.getDate("order_in_date").toLocalDate()), (rs.getDate("order_out_date").toLocalDate()), rs.getBoolean("order_confirmed"));
				orderList.add(order);
			}
			s.close();
			rs.close();
		} catch (SQLException e)
		{
			e.printStackTrace();
		}

		return orderList;
	}

	public static ArrayList<Order> getOrdersForDate(LocalDate ld, Connection conn)
	{
		Date date = Date.valueOf(ld.toString());
		ArrayList<Order> orderList = new ArrayList<Order>();
		String sql = "SELECT * FROM ORDERS WHERE ? BETWEEN order_out_date AND order_in_date";
		try
		{
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setDate(1, date);
			ResultSet rs = ps.executeQuery();
			while (rs.next())
			{
				Order order = new Order(rs.getInt("order_id"), rs.getString("order_name"), (rs.getDate("order_in_date").toLocalDate()), (rs.getDate("order_out_date").toLocalDate()), rs.getBoolean("order_confirmed"));
				orderList.add(order);
			}
			rs.close();
			ps.close();
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
		return orderList;
	}

	public static ArrayList<Item> getStockForDate(LocalDate ld, Connection conn)
	{
		System.out.println("get stock for date");
		ArrayList<Item> stockList = getAllItems(conn);
		ArrayList<Item> oiList;
		ArrayList<Order> orderList = getOrdersForDate(ld, conn);
		boolean itemFound = false;
		System.out.println("Orders: " + orderList);
		for (int i = 0; orderList.size() != 0 && i < orderList.size(); i++)
		{
			oiList = getOrderItemsForOrder(orderList.get(i).getId(), conn);
			System.out.println("orderItemList " + i + ": " + oiList);
			for (int j = 0; oiList.size() != 0 && j < oiList.size(); j++)
			{
				itemFound = false;
				// for each item search the stock array(id)
				for (int x = 0; x < stockList.size() && !itemFound; x++)
				{
					System.out.println("searching stockList for ID: " + oiList.get(j).getId());
					System.out.println("stockList item searched: " + stockList.get(x).getId());
					// remove quantity from stock array
					if (stockList.get(x).getId() == oiList.get(j).getId())
					{
						System.out.println("found item");
						stockList.get(x).setQuantity(stockList.get(x).getQuantity() - oiList.get(j).getQuantity());
						itemFound = true;
					}
				}
			}
		}
		Utility.sortById(stockList);
		return stockList;
	}

	public static ArrayList<Item> getOrderItemsForOrder(int id, Connection conn)
	{
		ArrayList<Item> itemList = new ArrayList<Item>();
		try
		{
			Statement s = conn.createStatement();
			ResultSet rs = s.executeQuery("SELECT i.item_id, i.item_name, oi.quant FROM ITEM i JOIN ORDERITEM oi" + " ON i.item_id = oi.item_id" + " WHERE oi.order_id = " + id);

			while (rs.next())
			{
				Item item = new Item(rs.getDouble("item_id"), rs.getDouble("quant"), rs.getString("item_name"));
				itemList.add(item);
			}
			s.close();
			rs.close();

		} catch (SQLException e)
		{
			e.printStackTrace();
		}
		Utility.sortById(itemList);
		return itemList;
	}
}
