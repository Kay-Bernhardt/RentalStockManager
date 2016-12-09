package utility;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import javafx.collections.ObservableList;
import model.broker.DBBroker;
import model.containers.Item;

public class Utility
{

	/**
	 * 
	 * @param item
	 * @param list
	 * @return the index of the searched item in the list. -1 if not found
	 */
	public static int findIndex(Item item, ObservableList<Item> list)
	{
		boolean flag = true;
		int index = -1;
		if (list.size() > 0)
		{
			for (int i = 0; i < list.size() && flag; i++)
			{
				if (list.get(i).getId() == item.getId())
				{
					flag = false;
					index = i;
				}
			}
		}
		return index;
	}

	public static double checkNumber(String str)
	{
		System.out.println("checking number: " + str);
		double number = 0;
		boolean flag = true;
		for (int i = 0; i < str.length() && flag; i++)
		{
			System.out.println("Char at i: " + str.charAt(i));
			if (!Character.isDigit(str.charAt(i)) && str.charAt(i) != '.')
			{
				flag = false;
			}
		}
		if (flag)
		{
			if(str.contains("."))
			{
				number = Double.parseDouble(str);
			}
			else
			{
				number = Integer.parseInt(str);
			}			
		}
		System.out.println("number: " + number);
		return number;
	}

	/**
	 * Checks if an item ID already exists
	 * 
	 * @param id
	 * @return true if it is a unique id false otherwise
	 */
	public static boolean isUniqueId(double id)
	{
		System.out.println("checking for id: " + id);
		boolean flag = true;
		ArrayList<Item> itemList = DBBroker.getInstance().getAllItems();
		for (int i = 0; i < itemList.size(); i++)
		{
			if (id == itemList.get(i).getId())
			{
				flag = false;
			}
		}
		System.out.println(flag);
		return flag;
	}

	public static ArrayList<Item> sortById(ArrayList<Item> list)
	{
		// bubble sort
		boolean swapped = true;
		while (swapped)
		{
			swapped = false;

			for (int i = 0; i < list.size() - 1; i++)
			{
				if (Double.compare(list.get(i).getId(), list.get(i + 1).getId()) > 0)
				{
					Collections.swap(list, i, i + 1);
					swapped = true;
				}
			}
		}
		return list;
	}

	public static boolean fileExists(String file)
	{
		File f = new File(file);
		boolean flag = false;

		if (f.exists())
		{
			flag = true;
		}

		return flag;
	}
}
