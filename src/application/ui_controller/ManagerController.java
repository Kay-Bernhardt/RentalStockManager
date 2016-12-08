package application.ui_controller;

import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import model.DBBroker;
import model.containers.Item;
import model.containers.Order;
import application.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.util.StringConverter;

public class ManagerController implements Initializable
{
	@FXML
	private DatePicker datePicker;
	@FXML
	private Label greetingLabel;
	@FXML
	private Button editButton;

	// STOCK TABLE
	@FXML
	private TableView<Item> iTable;
	@FXML
	private TableColumn<Item, Double> iID;
	@FXML
	private TableColumn<Item, Integer> iQuant;
	@FXML
	private TableColumn<Item, String> iName;

	@FXML
	public ListView<Order> orderListView;
	private ObservableList<Order> orderList = FXCollections.observableArrayList();

	private final ObservableList<Item> tableList = FXCollections.observableArrayList();

	@Override
	public void initialize(URL arg0, ResourceBundle arg1)
	{
		datePicker.setEditable(false);
		datePicker.setValue(Main.date);
		editButton.disableProperty().bind(orderListView.getSelectionModel().selectedItemProperty().isNull());
		showStock();
		showOrders();
		orderListView.setOnMouseClicked(new EventHandler<MouseEvent>()
		{
			// double click
			@Override
			public void handle(MouseEvent click)
			{
				if (click.getClickCount() == 2)
				{
					// Use ListView's getSelected Item
					Order currentItemSelected = orderListView.getSelectionModel().getSelectedItem();
					// use this to do whatever you want to.
					ScreenNavigator.setUserData(currentItemSelected);
					ScreenNavigator.loadScreen(ScreenNavigator.EDIT_ORDER);
				}
			}
		});
	}

	private void showStock()
	{
		ArrayList<Item> list = DBBroker.getInstance().getStockForDate(Main.date);
		tableList.setAll(list);

		iID.setCellValueFactory(new PropertyValueFactory<Item, Double>("id"));
		iQuant.setCellValueFactory(new PropertyValueFactory<Item, Integer>("quantity"));
		iName.setCellValueFactory(new PropertyValueFactory<Item, String>("name"));

		iID.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<Double>()
		{
			private final NumberFormat nf = DecimalFormat.getNumberInstance();
			{
				nf.setMaximumFractionDigits(1);
				nf.setMinimumFractionDigits(0);
			}

			@Override
			public String toString(final Double value)
			{
				return nf.format(value);
			}

			@Override
			public Double fromString(final String s)
			{
				// Don't need this, unless table is editable, see
				// DoubleStringConverter if needed
				return null;
			}
		}));

		iTable.setItems(tableList);
	}

	private void showOrders()
	{
		// get orders for date
		ArrayList<Order> temp = DBBroker.getInstance().getOrdersForDate(Main.date);
		orderList.setAll(temp);

		// add orders to list
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

	/**
	 * Event handler fired when the user requests a new screen.
	 *
	 * @param event
	 *           the event that triggered the handler.
	 */
	@FXML
	private void createOrder(ActionEvent event)
	{
		ScreenNavigator.loadScreen(ScreenNavigator.CREATE_ORDER);
	}

	@FXML
	private void editOrder(ActionEvent even)
	{
		ScreenNavigator.setUserData(orderListView.getSelectionModel().getSelectedItem());
		ScreenNavigator.loadScreen(ScreenNavigator.EDIT_ORDER);
	}

	@FXML
	private void editStock(ActionEvent event)
	{
		ScreenNavigator.loadScreen(ScreenNavigator.EDIT_STOCK);
	}

	@FXML
	private void updateDB(ActionEvent event)
	{
		DBBroker.getInstance().updateDB();
	}
	
	@FXML
	public void removeOrderButton(ActionEvent event)
	{
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Remove Order");
		alert.setHeaderText("Do you really want to delete this Order?");
		// alert.setContentText("Choose your option.");

		ButtonType buttonTypeYes = new ButtonType("Yes");
		// ButtonType buttonTypeTwo = new ButtonType("Two");
		// ButtonType buttonTypeThree = new ButtonType("Three");
		ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);

		alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeCancel);

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == buttonTypeYes)
		{
			// ... user chose "Yes"
			// remove this order/go back to main screen
			DBBroker.getInstance().removeOrder(orderListView.getSelectionModel().getSelectedItem().getId());
			showOrders();
		} else
		{
			// ... user chose CANCEL or closed the dialog
		}
	}
}
