package model;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;

import utility.Utility;
import model.containers.Item;
import model.containers.Order;
import model.containers.OrderItem;

public class DBBroker
{
	private String driver = "org.apache.derby.jdbc.EmbeddedDriver";
	private String dbName = "RSM";
	//private String dbNameNew = "RSMDB";
	private String connectionURL = "jdbc:derby:" + dbName + ";create=false";
	//private String connectionURLNew = "jdbc:derby:" + dbName + ";create=true";
	
	//private Connection connNew = null;
	private Connection conn = null;
	private static DBBroker instance = null;
	
	//TODO make 3 brokers for saving loading removing
	protected DBBroker()
	{
		System.out.println("dbbroker constructor");
		try
		{
			//connNew = DriverManager.getConnection(connectionURL);
			conn = DriverManager.getConnection(connectionURL);
			
			//check if tables exist	
			if(!SqlProcedure.tablesExist(conn))
			{
				//create tables
				SqlProcedure.createTables(conn);
			}
		} catch (SQLException e)
		{
			e.printStackTrace();
		}		
	}
	
	public static DBBroker getInstance()
	{
	      if(instance == null) 
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
		
        //   ## DATABASE SHUTDOWN SECTION ## 
        /*** In embedded mode, an application should shut down Derby.
           Shutdown throws the XJ015 exception to confirm success. ***/			
        if (driver.equals("org.apache.derby.jdbc.EmbeddedDriver")) {
           boolean gotSQLExc = false;
           try {
              DriverManager.getConnection("jdbc:derby:;shutdown=true");
           } catch (SQLException se)  {	
              if ( se.getSQLState().equals("XJ015") ) {		
                 gotSQLExc = true;
              }
           }
           if (!gotSQLExc) {
           	  System.out.println("Database did not shut down normally");
           }  else  {
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
	
	//===========  Add methods ==============================
	
	public void addItem(Item item)
	{
		try
		{
			PreparedStatement ps = conn.prepareStatement("insert into ITEM(item_id, item_name, total_quant) values (?, ?, ?)");
            ps.setDouble(1,item.getId());
            ps.setString(2, item.getName());
            ps.setInt(3, item.getQuantity());
            
            ps.executeUpdate();
            ps.close();
		} catch (SQLException e)
		{
			e.printStackTrace();
		};
	}
	
	public int addOrder(Order o)
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
			if(rs.next())
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

	public void addOrderItem(OrderItem oi)
	{		
		PreparedStatement ps;
		try
		{
			ps = conn.prepareStatement("INSERT INTO ORDERITEM(item_id, order_id, quant)VALUES(?, ?, ?)");
			ps.setDouble(1, oi.getItemId());
			ps.setInt(2, oi.getOrderId());
			ps.setInt(3, oi.getQuant());

			ps.execute();
			ps.close();
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
	}

	//================= Get methods ===================================================
	
	public ArrayList<Item> getAllItems()
	{
		ArrayList<Item> itemList = new ArrayList<Item>();
		try
		{
			Statement s = conn.createStatement();
			ResultSet rs = s.executeQuery("SELECT * FROM ITEM");
			
			while(rs.next()) 
			{
				Item item = new Item(rs.getDouble("item_id"),rs.getInt("total_quant"), rs.getString("item_name"));
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
	
	public ArrayList<OrderItem> getAllOrderItems()
	{
		ArrayList<OrderItem> itemList = new ArrayList<OrderItem>();					
			try
			{				
				Statement s = conn.createStatement();
				ResultSet rs = s.executeQuery("SELECT * FROM ORDERITEM");
				
				while(rs.next()) 
				{
					OrderItem orderItem = new OrderItem(rs.getDouble("item_id"),rs.getInt("order_id"), rs.getInt("quant"));
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
	
	public ArrayList<Order> getAllOrders()
	{
		ArrayList<Order> orderList = new ArrayList<Order>();
		
		try
		{
			Statement s = conn.createStatement();
			ResultSet rs = s.executeQuery("SELECT * FROM ORDERS");
			
			while(rs.next()) 
			{
				Order order = new Order(rs.getInt("order_id"),rs.getString("order_name"),(rs.getDate("order_in_date").toLocalDate()), (rs.getDate("order_out_date").toLocalDate()), rs.getBoolean("order_confirmed"));
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
	
	public ArrayList<Order> getOrdersForDate(LocalDate ld)
	{
		Date date = Date.valueOf(ld.toString());
		ArrayList<Order> orderList = new ArrayList<Order>();
		String sql = "SELECT * FROM ORDERS WHERE ? BETWEEN order_out_date AND order_in_date";
		try
		{
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setDate(1, date);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) 
			{
				Order order = new Order(rs.getInt("order_id"),rs.getString("order_name"),(rs.getDate("order_in_date").toLocalDate()), (rs.getDate("order_out_date").toLocalDate()), rs.getBoolean("order_confirmed"));
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
	
	public ArrayList<Item> getStockForDate(LocalDate ld)
	{
		System.out.println("get stock for date");
		ArrayList<Item> stockList = this.getAllItems();
		ArrayList<Item> oiList;
		ArrayList<Order> orderList = this.getOrdersForDate(ld);
		boolean itemFound = false;
		System.out.println("Orders: " + orderList);
		for (int i = 0; orderList.size() != 0 && i < orderList.size(); i++)
		{
			oiList = this.getOrderItemsForOrder(orderList.get(i).getId());
			System.out.println("orderItemList " + i + ": " + oiList);
			for(int j = 0; oiList.size() != 0 && j < oiList.size(); j++)
			{
				itemFound = false;
				//for each item search the stock array(id)
				for(int x = 0; x < stockList.size() && !itemFound; x++)
				{
					System.out.println("searching stockList for ID: " + oiList.get(j).getId());
					System.out.println("stockList item searched: " + stockList.get(x).getId());
					//remove quant from stock array
					if(stockList.get(x).getId() == oiList.get(j).getId())
					{
						System.out.println("found item");
						stockList.get(x).setQuantity(stockList.get(x).getQuantity() - oiList.get(j).getQuantity());
						itemFound = true;
					}
				}				
			}
		}
		//TODO ???
		//stock - order items for date
			//get orders for date
				//add items f
		//get stock array
		//get order items array
		//loop order items
		Utility.sortById(stockList);
		return stockList;
	}

	public ArrayList<Item> getOrderItemsForOrder(int id)
	{		
		ArrayList<Item> itemList = new ArrayList<Item>();
		try
		{
			Statement s = conn.createStatement();
			ResultSet rs = s.executeQuery("SELECT i.item_id, i.item_name, oi.quant FROM ITEM i JOIN ORDERITEM oi"
											+ " ON i.item_id = oi.item_id"
											+ " WHERE oi.order_id = " + id);
											//+ " AND i.item_id = oi.item_id");
			
			while(rs.next()) 
			{
				Item item = new Item(rs.getDouble("item_id"),rs.getInt("quant"), rs.getString("item_name"));
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

	//============== Remove methods =========================================
	
	public void removeOrder(int order_id)
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
	
	public void removeItem(double id)
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
