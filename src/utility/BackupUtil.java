package utility;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import application.ui_controller.ScreenNavigator;
import javafx.fxml.FXML;
import model.broker.DBBroker;
import model.containers.Item;
import model.containers.Order;
import model.containers.OrderItem;

/**
 * Currently useless
 * 
 * @author Lord Administrator
 *
 */
public class BackupUtil
{
	private static String ORDERS 		= "res/orders.ser";
	private static String ITEMS 		= "res/items.ser";
	private static String ORDERITEMS = "res/orderitems.ser";
	
	public static void writeDBToFile()
	{
		System.out.println("Writing DB backup");
		// make 3 lists
		// write all lists to different files
		ArrayList<Order> oList = DBBroker.getInstance().getAllOrders();
		ArrayList<Item> iList = DBBroker.getInstance().getAllItems();
		ArrayList<OrderItem> oiList = DBBroker.getInstance().getAllOrderItems();

		try
		{
			ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(ORDERS));
			out.writeObject(oList);
			out.close();

			out = new ObjectOutputStream(new FileOutputStream(ITEMS));
			out.writeObject(iList);
			out.close();

			out = new ObjectOutputStream(new FileOutputStream(ORDERITEMS));
			out.writeObject(oiList);
			out.close();

		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		System.out.println("writeDB done");
	}

	//@SuppressWarnings("unchecked")
	@SuppressWarnings("unchecked")
	@FXML
	public static void loadFromFile()
	{
		ArrayList<Object> iList = new ArrayList<Object>();
		ArrayList<Object> oiList = new ArrayList<Object>();
		ArrayList<Object> oList = new ArrayList<Object>();
		if (Utility.fileExists(ORDERS) && Utility.fileExists(ITEMS) && Utility.fileExists(ORDERITEMS))
		{
			try
			{
				ObjectInputStream in = new ObjectInputStream(new FileInputStream(ORDERS));
				oList = (ArrayList<Object>) in.readObject();
				in.close();

				in = new ObjectInputStream(new FileInputStream(ITEMS));
				iList = (ArrayList<Object>) in.readObject();
				in.close();

				in = new ObjectInputStream(new FileInputStream(ORDERITEMS));
				oiList = (ArrayList<Object>) in.readObject();
				in.close();

			} catch (ClassNotFoundException e)
			{
				e.printStackTrace();
			} catch (FileNotFoundException e)
			{
				e.printStackTrace();
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		
		saveToDB(iList);
		saveToDB(oList);
		saveToDB(oiList);
		
		
		//TODO ???
		ScreenNavigator.loadScreen(ScreenNavigator.MANAGER);
	}
	
	@SuppressWarnings("unchecked")
	private static void saveToDB(ArrayList<Object> list)
	{
		if(list == null || list.isEmpty())
		{
			System.out.println("list is null");
			return;
		}
		
		if(list.get(0).getClass().isInstance(new Item()))
		{
			ArrayList<Item> iList = (ArrayList<Item>)(ArrayList<?>)(list);
			for (int i = 0; i < iList.size(); i++)
			{
				//TODO why no use Item(Item) constructor?
				DBBroker.getInstance()
						.addItem(new Item(iList.get(i).getId(), iList.get(i).getQuantity(), iList.get(i).getName()));
			}
		}
		else if(list.get(0).getClass().isInstance(new Order()))
		{
			ArrayList<Order> oList = (ArrayList<Order>)(ArrayList<?>)(list);
			for (int i = 0; i < oList.size(); i++)
			{
				DBBroker.getInstance().addOrder(oList.get(i));
			}
			
		}
		else if(list.get(0).getClass().isInstance(new OrderItem()))
		{
			ArrayList<OrderItem> oiList = (ArrayList<OrderItem>)(ArrayList<?>)(list);
			for (int i = 0; i < oiList.size(); i++)
			{
				DBBroker.getInstance().addOrderItem(oiList.get(i));
			}
		}
	}
}
