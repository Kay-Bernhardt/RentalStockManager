package application.ui_controller;

import java.net.URL;
import java.util.ResourceBundle;

import model.DBBroker;
import model.containers.Item;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

public class EditStockController implements Initializable
{
	@FXML
	private Button editItemButton;
	
	private final ObservableList<Item> stockTableList = FXCollections.observableArrayList();
	
	//STOCK TABLE
	@FXML
	private TableView<Item> iTable;	
	@FXML
	private TableColumn<Item, Integer> iID;	
	@FXML
	private TableColumn<Item, Integer> iQuant;	
	@FXML
	private TableColumn<Item, String> iName;
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		editItemButton.disableProperty().bind(iTable.getSelectionModel().selectedItemProperty().isNull());
        stockTableList.setAll(DBBroker.getInstance().getAllItems());
        
		iID.setCellValueFactory(new PropertyValueFactory<Item, Integer>("id"));
		iQuant.setCellValueFactory(new PropertyValueFactory<Item, Integer>("quantity"));
		iName.setCellValueFactory(new PropertyValueFactory<Item, String>("name"));
		
		iTable.setItems(stockTableList);
		
		iTable.setOnMouseClicked(new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent click)
			{
				if (click.getClickCount() == 2)
				{
					// Use ListView's getSelected Item
					Item currentItemSelected = iTable.getSelectionModel().getSelectedItem();
					// use this to do whatever you want to.
					ScreenNavigator.setUserData(currentItemSelected);
					ScreenNavigator.loadScreen(ScreenNavigator.EDIT_ITEM);
				}
			}
		});
	}
	
	@FXML
    private void cancelButton(ActionEvent event) 
	{
        ScreenNavigator.loadScreen(ScreenNavigator.MANAGER);
	}
	
	@FXML
    private void newItemButton(ActionEvent event) 
	{
        ScreenNavigator.loadScreen(ScreenNavigator.NEW_ITEM);
	}
	
	@FXML
    private void editItemButton(ActionEvent event) 
	{
		ScreenNavigator.setUserData(iTable.getSelectionModel().getSelectedItem());
        ScreenNavigator.loadScreen(ScreenNavigator.EDIT_ITEM);
	}
}
