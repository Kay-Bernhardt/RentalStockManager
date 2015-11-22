package utility;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import model.DBBroker;
import model.containers.Item;

public class DataTextReader
{

	public static void main(String[] args)
	{
		// TODO Auto-generated method stub
		//open file
		try
		{
			File f = new File("res/products.txt");
			Scanner in = new Scanner(f);
			Scanner line;
			//String line;
			int id = 0;
			String name = "";
			int quant = 0;

			while (in.hasNext())
			{
				//create items add to list or DB
				line = new Scanner(in.nextLine());
				line.useDelimiter("\\s*\t\\s*");
				id = line.nextInt();
				name = line.next();
				quant = line.nextInt();
				
				DBBroker.getInstance().addItem(new Item(id, quant, name));
			}
			System.out.println("done");
				
			in.close();
			
			/*
			System.out.println("adding an order");
			Order order = new Order(0, "testOrder", LocalDate.now(), LocalDate.now(), false);
			System.out.println(DBBroker.getInstance().addOrder(order));
			
			System.out.println("adding an order item");
			OrderItem oi = new OrderItem(1, 1, 5);
			DBBroker.getInstance().addOrderItem(oi);
			*/

		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
