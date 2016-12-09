package model.broker;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import model.containers.Item;
import model.containers.Order;
import model.containers.OrderItem;

public class DBaddBroker
{
	public static void addItem(Item item, Connection conn)
	{
		try
		{
			PreparedStatement ps = conn.prepareStatement("insert into ITEM(item_id, item_name, total_quant) values (?, ?, ?)");
			ps.setDouble(1, item.getId());
			ps.setString(2, item.getName());
			ps.setDouble(3, item.getQuantity());

			ps.executeUpdate();
			ps.close();
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
		;
	}

	public static int addOrder(Order o, Connection conn)
	{
		int order_id = -1;
		try
		{
			PreparedStatement ps = conn.prepareStatement("INSERT INTO ORDERS(order_name, order_in_date, order_out_date, order_confirmed) VALUES(?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, o.getName());
			ps.setDate(2, java.sql.Date.valueOf(o.getDateIn()));
			ps.setDate(3, java.sql.Date.valueOf(o.getDateOut()));
			ps.setBoolean(4, o.isConfirmed());

			ps.execute();
			ResultSet rs = ps.getGeneratedKeys();
			if (rs.next())
			{
				order_id = rs.getInt(1);
			}

			rs.close();
			ps.close();

		} catch (SQLException e)
		{
			e.printStackTrace();
		}
		return order_id;
	}

	public static void addOrderItem(OrderItem oi, Connection conn)
	{
		PreparedStatement ps;
		try
		{
			ps = conn.prepareStatement("INSERT INTO ORDERITEM(item_id, order_id, quant)VALUES(?, ?, ?)");
			ps.setDouble(1, oi.getItemId());
			ps.setInt(2, oi.getOrderId());
			ps.setDouble(3, oi.getQuant());

			ps.execute();
			ps.close();
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
}
