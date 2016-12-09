package model.broker;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DBremoveBroker
{
	public static void removeOrder(int order_id, Connection conn)
	{
		try
		{
			String sql = "DELETE FROM ORDERITEM WHERE order_id = ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, order_id);
			ps.executeUpdate();

			sql = "DELETE FROM ORDERS WHERE order_id = ?";
			ps = conn.prepareStatement(sql);
			ps.setInt(1, order_id);
			ps.executeUpdate();

			ps.close();

		} catch (SQLException e)
		{
			e.printStackTrace();
		}
	}

	public static void removeItem(double id, Connection conn)
	{
		try
		{
			String sql = "DELETE FROM ORDERITEM WHERE item_id = ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setDouble(1, id);
			ps.executeUpdate();

			sql = "DELETE FROM ITEM WHERE item_id = ?";
			ps = conn.prepareStatement(sql);
			ps.setDouble(1, id);
			ps.executeUpdate();

			ps.close();

		} catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
}
