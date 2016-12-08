package application.ui_controller;

import java.net.URL;
import java.util.ResourceBundle;

import utility.Utility;
import model.DBBroker;
import model.containers.Item;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

public class NewItemController implements Initializable
{
	@FXML
	private TextField idTextField;	
	@FXML
	private TextField nameTextField;	
	@FXML
	private TextField quantTextField;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1)
	{
		// TODO Auto-generated method stub		
	}
	
	@FXML
    private void cancelButton(ActionEvent event) 
	{
        ScreenNavigator.loadScreen(ScreenNavigator.EDIT_STOCK);
	}
	
	@FXML
    private void saveButton(ActionEvent event) 
	{
			if(Utility.checkNumber(idTextField.getText()) != 0 && Utility.isUniqueId(Utility.checkNumber(idTextField.getText())) && Utility.checkNumber(quantTextField.getText()) != 0 && nameTextField.getText() != null && !nameTextField.getText().equals(""))
			{
				DBBroker.getInstance().addItem(new Item(Double.parseDouble(idTextField.getText()), Integer.parseInt(quantTextField.getText()), nameTextField.getText()));
		        ScreenNavigator.loadScreen(ScreenNavigator.EDIT_STOCK);
			}
	}

}
