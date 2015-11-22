package application.ui_controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

import model.DBBroker;
import model.containers.Item;
import model.containers.Order;
import model.containers.OrderItem;
import application.Main;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class ManagerController implements Initializable
{
	@FXML
	private DatePicker datePicker;
	@FXML
	private Label greetingLabel;
	@FXML
	private Button editButton;
	
	//STOCK TABLE
	@FXML
	private TableView<Item> iTable;	
	@FXML
	private TableColumn<Item, Integer> iID;	
	@FXML
	private TableColumn<Item, Integer> iQuant;	
	@FXML
	private TableColumn<Item, String> iName;
	
	@FXML
	private ListView<Order> orderListView;
	
	private final ObservableList<Item> tableList = FXCollections.observableArrayList();
	private ObservableList<Order> orderList = FXCollections.observableArrayList();
	
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1)
	{
		datePicker.setEditable(false);
		datePicker.setValue(Main.date);
		editButton.disableProperty().bind(orderListView.getSelectionModel().selectedItemProperty().isNull());
		showStock();
		showOrders();
	}
	
	private void showStock()
	{
		ArrayList<Item> list = DBBroker.getInstance().getStockForDate(Main.date);
		tableList.setAll(list);
		
		iID.setCellValueFactory(new PropertyValueFactory<Item, Integer>("id"));
		iQuant.setCellValueFactory(new PropertyValueFactory<Item, Integer>("quantity"));
		iName.setCellValueFactory(new PropertyValueFactory<Item, String>("name"));
		
		iTable.setItems(tableList);
	}
	
	private void showOrders()
	{
		//TODO
		//get orders for date
		ArrayList<Order> temp = DBBroker.getInstance().getOrdersForDate(Main.date);
		orderList.setAll(temp);

		//add orders to list
		if (orderList.size() > 0)
		{
			orderListView.setItems(orderList);
		}
	}
	
	public void selectDate(ActionEvent event)
	{
		LocalDate ld = datePicker.getValue();
		Main.date = ld;
		showStock();
		showOrders();
	}
	
	public void doExit()
	{
		Platform.exit();
	}
	
    /**
     * Event handler fired when the user requests a new screen.
     *
     * @param event the event that triggered the handler.
     */
    @FXML
    private void createOrder(ActionEvent event) 
    {
        ScreenNavigator.loadScreen(ScreenNavigator.CREATE_ORDER);
    }
    
    @FXML
    private void editOrder(ActionEvent even) 
    {
    	ScreenNavigator.setUserData(new Order(orderListView.getSelectionModel().getSelectedItem()));
    	ScreenNavigator.loadScreen(ScreenNavigator.EDIT_ORDER);
    }
    
    @FXML
    private void editStock(ActionEvent event) 
    {
        ScreenNavigator.loadScreen(ScreenNavigator.EDIT_STOCK);
    }
    
    @FXML
    private void writeDB(ActionEvent event)
    {
    	//make 3 lists
    	//write all lists to different files
    	ArrayList<Order> oList = DBBroker.getInstance().getAllOrders();
    	ArrayList<Item> iList = DBBroker.getInstance().getAllItems();
    	ArrayList<OrderItem> oiList = DBBroker.getInstance().getAllOrderItems();
    	
		try
		{
			ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("res/orders.ser"));
			out.writeObject(oList);
			out.close();
			
			out = new ObjectOutputStream(new FileOutputStream("res/items.ser"));
			out.writeObject(iList);
			out.close();
			
			out = new ObjectOutputStream(new FileOutputStream("res/orderitems.ser"));
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
    
    @SuppressWarnings("unchecked")
	@FXML
    private void loadFromFile(ActionEvent event)
    {
		ArrayList<Item> iList = new ArrayList<Item>();
		ArrayList<OrderItem> oiList = new ArrayList<OrderItem>();
		ArrayList<Order> oList = new ArrayList<Order>();
		if (fileExists("res/orders.ser") && fileExists("res/items.ser") && fileExists("res/orderitems.ser"))
		{
			try
			{
				ObjectInputStream in = new ObjectInputStream(new FileInputStream("res/orders.ser"));
				oList = (ArrayList<Order>) in.readObject();
				in.close();
				
				in = new ObjectInputStream(new FileInputStream("res/items.ser"));
				iList = (ArrayList<Item>) in.readObject();
				in.close();
				
				in = new ObjectInputStream(new FileInputStream("res/orderitems.ser"));
				oiList = (ArrayList<OrderItem>) in.readObject();				
				in.close();

			}catch (ClassNotFoundException e)
			{
				e.printStackTrace();
			}catch (FileNotFoundException e)
			{
				e.printStackTrace();
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		
		if(iList != null && oList != null && oiList != null)
		{
			for(int i = 0; i < iList.size(); i++)
			{
				DBBroker.getInstance().addItem(new Item(iList.get(i).getId(), iList.get(i).getQuantity(), iList.get(i).getName()));
			}
			
			for(int i = 0; i < oList.size(); i++)
			{
				DBBroker.getInstance().addOrder(oList.get(i));
			}
			
			for(int i = 0; i < oiList.size(); i++)
			{
				DBBroker.getInstance().addOrderItem(oiList.get(i));
			}
		}
		System.out.println("loading done");
		ScreenNavigator.loadScreen(ScreenNavigator.MANAGER);
    }
    
	private boolean fileExists(String file)
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
