package model;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SqlProcedure
{
	public static boolean tablesExist(Connection conn) throws SQLException
	{
		System.out.println("checking tables");
		Statement s = conn.createStatement();
		DatabaseMetaData dbmd = conn.getMetaData();
		ResultSet rs = dbmd.getTables(null, null, null, null);		
		
		boolean ordersFlag = false;
		boolean itemFlag = false;
		boolean orderItemFlag = false;
		//System.out.println("printing tables rs");
		while (rs.next()) 
		{
		    String strTableName = rs.getString("TABLE_NAME");
		    //System.out.println("TABLE_NAME is " + strTableName);
		    if (strTableName.equals("ORDERS"))//TODO change to ORDER
		    {
		    	ordersFlag = true;
		    }
		    else if(strTableName.equals("ITEM"))
		    {
		    	itemFlag = true;
		    }
		    else if(strTableName.equals("ORDERITEM"))
		    {
		    	orderItemFlag = true;
		    }
		}
		
		if (ordersFlag && itemFlag && orderItemFlag)
		{
			System.out.println("all tables exist");
			return true;
		}
		
		if (ordersFlag)
		{
			System.out.println("drop orders");
			s.executeUpdate("DROP TABLE ORDERS");
		}		
		if (itemFlag)
		{
			System.out.println("drop item");
			s.executeUpdate("DROP TABLE ITEM");
		}		
		if(orderItemFlag)
		{
			System.out.println("drop orderitem");
			s.executeUpdate("DROP TABLE ORDERITEM");
		}		
		
		s.close();
		rs.close();
		return false;
	}
	
	public static void createTables(Connection conn) throws SQLException
	{
		//TODO Change table here
		Statement s = conn.createStatement();
		
		System.out.println("Creating table Orders");
		s.execute("CREATE TABLE ORDERS "
					+	"(order_id INT NOT NULL GENERATED ALWAYS AS IDENTITY CONSTRAINT ORDERS_PK PRIMARY KEY,"
					+	"order_name 		VARCHAR(255) 	NOT NULL, "
					+	"order_out_date	DATE			NOT NULL, "
					+	"order_in_date 	DATE 			NOT NULL,"
					+	"order_confirmed	BOOLEAN 		NOT NULL)");
		System.out.println("Orders created");
		
		System.out.println("Creating table Item");
		s.execute("CREATE TABLE ITEM	( " +
						"item_id 	DOUBLE	CONSTRAINT ITEM_PK PRIMARY KEY, " +
						"item_name 	VARCHAR(255) 	NOT NULL, " +
						"total_quant DOUBLE 			NOT NULL )");
		System.out.println("Item created");
		
		System.out.println("Creating table OrderItem");
		s.execute("CREATE TABLE ORDERITEM (" +
						"item_id 	DOUBLE, " +
						"order_id 	INT, " +
						"quant 		DOUBLE 		NOT NULL, " +
						"PRIMARY KEY 	(item_id, order_id), " +
						"CONSTRAINT fk_orderitem_item FOREIGN KEY (item_id) REFERENCES ITEM(item_id), " +
						"CONSTRAINT fk_orderitem_order FOREIGN KEY (order_id) REFERENCES ORDERS(order_id))");
		System.out.println("OrderItem created");
		s.close();
	}
	
	public static void updateItemID(Connection conn) throws SQLException
	{
		//change data type of item and orderItem from int to double
		Statement s = conn.createStatement();
		
		System.out.println("updating item");
		s.execute("ALTER TABLE ITEM ADD COLUMN NEW_ID DOUBLE");
		s.execute("UPDATE ITEM SET NEW_ID=item_id");
		s.execute("ALTER TABLE ITEM DROP COLUMN item_id");
		s.execute("RENAME COLUMN ITEM.NEW_ID TO item_id");
		System.out.println("updating item done");
		
		System.out.println("updating orderItem");
		s.execute("ALTER TABLE ORDERITEM ADD COLUMN NEW_ID DOUBLE");
		s.execute("UPDATE ORDERITEM SET NEW_ID=item_id");
		s.execute("ALTER TABLE ORDERITEM DROP COLUMN item_id");
		s.execute("RENAME COLUMN ORDERITEM.NEW_ID TO item_id");
		System.out.println("updating orderItem done");
		
		s.close();
	}
	
	public static void updateItemQuantity(Connection conn)throws SQLException
	{
		Statement s = conn.createStatement();
		
		System.out.println("updating item");
		s.execute("ALTER TABLE ITEM ADD COLUMN NEW_QUANT DOUBLE");
		s.execute("UPDATE ITEM SET NEW_QUANT=total_quant");
		s.execute("ALTER TABLE ITEM DROP COLUMN total_quant");
		s.execute("RENAME COLUMN ITEM.NEW_QUANT TO total_quant");
		System.out.println("updating item done");
		
		System.out.println("updating orderItem");
		s.execute("ALTER TABLE ORDERITEM ADD COLUMN NEW_QUANT DOUBLE");
		s.execute("UPDATE ORDERITEM SET NEW_QUANT=quant");
		s.execute("ALTER TABLE ORDERITEM DROP COLUMN quant");
		s.execute("RENAME COLUMN ORDERITEM.NEW_QUANT TO quant");
		System.out.println("updating orderItem done");
		
		s.close();
	}
}
