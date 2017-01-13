package model.broker;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

import javafx.application.Platform;
import model.SqlProcedure;
import model.containers.Item;
import model.containers.Order;
import model.containers.OrderItem;

public class DBBroker
{
	private String driver = "org.apache.derby.jdbc.EmbeddedDriver";
	private String dbName = "RSM";
	private String connectionURL = "jdbc:derby:" + dbName + ";create=true";

	private Connection conn = null;
	private static DBBroker instance = null;

	protected DBBroker()
	{
		System.out.println("dbbroker constructor");
		try
		{
			conn = DriverManager.getConnection(connectionURL);

			// check if tables exist
			if (!SqlProcedure.tablesExist(conn))
			{
				// create tables
				SqlProcedure.createTables(conn);
			}
		} catch (SQLException e)
		{
			e.printStackTrace();
			Platform.exit();
			System.exit(1);
		}
	}

	public static DBBroker getInstance()
	{
		if (instance == null)
		{
			instance = new DBBroker();
		}
		return instance;
	}

	public void close()
	{
		System.out.println("closing DB");
		try
		{
			conn.close();
		} catch (SQLException e)
		{
			e.printStackTrace();
		}

		// ## DATABASE SHUTDOWN SECTION ##
		/***
		 * In embedded mode, an application should shut down Derby. Shutdown throws the XJ015 exception to confirm success.
		 ***/
		if (driver.equals("org.apache.derby.jdbc.EmbeddedDriver"))
		{
			boolean gotSQLExc = false;
			try
			{
				DriverManager.getConnection("jdbc:derby:;shutdown=true");
			} catch (SQLException se)
			{
				if (se.getSQLState().equals("XJ015"))
				{
					gotSQLExc = true;
				}
			}
			if (!gotSQLExc)
			{
				System.out.println("Database did not shut down normally");
			} else
			{
				System.out.println("Database shut down normally");
			}
		}
	}

	public void updateDB()
	{
		try
		{
			SqlProcedure.updateItemID(conn);
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
	}

	public void updateItem()
	{
		try
		{
			SqlProcedure.updateItemQuantity(conn);
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
	}

	// =========== Add methods ==============================

	public void addItem(Item item)
	{
		DBaddBroker.addItem(item, conn);
	}

	public int addOrder(Order o)
	{
		return DBaddBroker.addOrder(o, conn);
	}

	public void addOrderItem(OrderItem oi)
	{
		DBaddBroker.addOrderItem(oi, conn);
	}

	// ================= Get methods ===================================================

	public ArrayList<Item> getAllItems()
	{
		return DBgetBroker.getAllItems(conn);
	}

	public ArrayList<OrderItem> getAllOrderItems()
	{
		return DBgetBroker.getAllOrderItems(conn);
	}

	public ArrayList<Order> getAllOrders()
	{
		return DBgetBroker.getAllOrders(conn);
	}

	public ArrayList<Order> getOrdersForDate(LocalDate ld)
	{
		return DBgetBroker.getOrdersForDate(ld, conn);
	}

	public ArrayList<Item> getStockForDate(LocalDate ld)
	{
		return DBgetBroker.getStockForDate(ld, conn);
	}

	public ArrayList<Item> getOrderItemsForOrder(int id)
	{
		return DBgetBroker.getOrderItemsForOrder(id, conn);
	}

	// ============== Remove methods =========================================

	public void removeOrder(int order_id)
	{
		DBremoveBroker.removeOrder(order_id, conn);
	}

	public void removeItem(double id)
	{
		DBremoveBroker.removeItem(id, conn);
	}
}
