package application.ui_controller;

import java.net.URL;
import java.util.ResourceBundle;
import utility.Utility;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.broker.DBBroker;
import model.containers.Item;

public class EditItemController implements Initializable
{
	@FXML
	private TextField idTextField;
	@FXML
	private TextField nameTextField;
	@FXML
	private TextField quantTextField;
	@FXML
	private Label topLabel;

	private Item item;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1)
	{
		item = (Item) ScreenNavigator.getUserData();		
		if (item.getId() > -1)
		{
			idTextField.setText(item.getId() + "");
			nameTextField.setText(item.getName());
			quantTextField.setText(item.getQuantity() + "");
		}
		else
		{
			topLabel.setText("New Item");
		}
	}

	@FXML
	private void cancelButton(ActionEvent event)
	{
		ScreenNavigator.loadScreen(ScreenNavigator.EDIT_STOCK);
	}

	@FXML
	private void saveButton(ActionEvent event)
	{
		if (Utility.checkNumber(idTextField.getText()) != 0 && (Utility.isUniqueId(Utility.checkNumber(idTextField.getText())) || Utility.checkNumber(idTextField.getText()) == item.getId()) && Utility.checkNumber(quantTextField.getText()) != 0 && nameTextField.getText() != null
				&& !nameTextField.getText().equals(""))
		{
			if (item.getId() != -1)
			{
				DBBroker.getInstance().removeItem(item.getId());
			}

			DBBroker.getInstance().addItem(new Item(Double.parseDouble(idTextField.getText()), Double.parseDouble(quantTextField.getText()), nameTextField.getText()));
			ScreenNavigator.loadScreen(ScreenNavigator.EDIT_STOCK);
		}
	}

	@FXML
	private void removeButton(ActionEvent event)
	{
		DBBroker.getInstance().removeItem(item.getId());
		ScreenNavigator.loadScreen(ScreenNavigator.EDIT_STOCK);
	}
}
