package old;

import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ResourceBundle;
import utility.Utility;
import model.broker.DBBroker;
import model.containers.Item;
import model.containers.Order;
import model.containers.OrderItem;
import application.Main;
import application.ui_controller.ScreenNavigator;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.StringConverter;

/*
public class CreateOrderController implements Initializable
{
	@FXML
	private TextField nameTextField;
	@FXML
	private TextField addTextField;
	@FXML
	private TextField removeTextField;
	@FXML
	private Button addButton;
	@FXML
	private Button removeButton;
	@FXML
	private Button saveButton;
	@FXML
	private DatePicker outDatePicker;
	@FXML
	private DatePicker inDatePicker;
	@FXML
	private CheckBox confirmedCheckBox;

	// StockTable
	@FXML
	private TableView<Item> iStockTable;
	@FXML
	private TableColumn<Item, Double> stID;
	@FXML
	private TableColumn<Item, Integer> stQuant;
	@FXML
	private TableColumn<Item, String> stName;

	// OrderTable
	@FXML
	private TableView<Item> iOrderTable;
	@FXML
	private TableColumn<Item, Double> oID;
	@FXML
	private TableColumn<Item, Integer> oQuant;
	@FXML
	private TableColumn<Item, String> oName;

	private final ObservableList<Item> orderTableList = FXCollections.observableArrayList();
	private final ObservableList<Item> stockTableList = FXCollections.observableArrayList();

	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		outDatePicker.setEditable(false);
		inDatePicker.setEditable(false);
		outDatePicker.setValue(Main.date);
		inDatePicker.setValue(Main.date);

		stockTableList.setAll(DBBroker.getInstance().getStockForDate(Main.date));

		stID.setCellValueFactory(new PropertyValueFactory<Item, Double>("id"));
		stQuant.setCellValueFactory(new PropertyValueFactory<Item, Integer>("quantity"));
		stName.setCellValueFactory(new PropertyValueFactory<Item, String>("name"));

		stID.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<Double>()
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

		oID.setCellValueFactory(new PropertyValueFactory<Item, Double>("id"));
		oQuant.setCellValueFactory(new PropertyValueFactory<Item, Integer>("quantity"));
		oName.setCellValueFactory(new PropertyValueFactory<Item, String>("name"));

		oID.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<Double>()
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

		addTextField.setText("0");
		removeTextField.setText("0");
		confirmedCheckBox.setSelected(false);

		// disable buttons if nothing is selected
		addButton.disableProperty().bind(iStockTable.getSelectionModel().selectedItemProperty().isNull());
		removeButton.disableProperty().bind(iOrderTable.getSelectionModel().selectedItemProperty().isNull());

		iStockTable.setItems(stockTableList);
		iOrderTable.setItems(orderTableList);

		saveButton.setDisable(true);
		nameTextField.textProperty().addListener(new ChangeListener<String>()
		{

			@Override
			public void changed(ObservableValue<? extends String> ov, String t, String t1)
			{
				if (t1.equals(""))
					saveButton.setDisable(true);
				else
					saveButton.setDisable(false);
			}
		});
	}

	@FXML
	private void cancelButton(ActionEvent event)
	{
		ScreenNavigator.loadScreen(ScreenNavigator.MANAGER);
	}

	@FXML
	private void addButton(ActionEvent event)
	{
		// add selected item to orderTableList
		Item temp = iStockTable.getSelectionModel().getSelectedItem();
		Item item = new Item(temp);
		item.setQuantity(Utility.checkNumber(addTextField.getText()));
		int index = -1;

		// remove quant form stock list
		index = Utility.findIndex(item, stockTableList);
		if (index != -1 && item.getQuantity() != 0)
		{
			int newQuant = (stockTableList.get(index).getQuantity()) - (item.getQuantity());
			if (newQuant > -1)
			{
				stockTableList.get(index).setQuantity(newQuant);
				iStockTable.getColumns().get(0).setVisible(false);
				iStockTable.getColumns().get(0).setVisible(true);

				// check if item already exists
				index = Utility.findIndex(item, orderTableList);
				if (index == -1)
				{
					orderTableList.add(item);
				} else
				{
					orderTableList.get(index).setQuantity(orderTableList.get(index).getQuantity() + item.getQuantity());
					iOrderTable.getColumns().get(0).setVisible(false);
					iOrderTable.getColumns().get(0).setVisible(true);
				}
			}
		}
	}

	@FXML
	private void removeButton(ActionEvent event)
	{
		Item temp = iOrderTable.getSelectionModel().getSelectedItem();
		Item item = new Item(temp);
		item.setQuantity(Utility.checkNumber(removeTextField.getText()));

		// find item in order list and remove quant if quant will be 0 remove item
		int index = Utility.findIndex(item, orderTableList);
		if (orderTableList.get(index).getQuantity() - item.getQuantity() > -1)
		{
			if (orderTableList.get(index).getQuantity() - item.getQuantity() == 0)
			{
				// remove from list
				orderTableList.remove(index);
			} else
			{
				orderTableList.get(index).setQuantity(orderTableList.get(index).getQuantity() - item.getQuantity());
			}
			// find item in stock list and add
			index = Utility.findIndex(item, stockTableList);
			stockTableList.get(index).setQuantity(stockTableList.get(index).getQuantity() + item.getQuantity());
		} else
		{
			// TODO remove from order list and add correct amount to stock
			// does nothing right now if quant is too large
		}

		iStockTable.getColumns().get(0).setVisible(false);
		iStockTable.getColumns().get(0).setVisible(true);
		iOrderTable.getColumns().get(0).setVisible(false);
		iOrderTable.getColumns().get(0).setVisible(true);
	}

	@FXML
	private void saveButton(ActionEvent event)
	{
		if (nameTextField.getText() != null && !nameTextField.getText().equals(""))
		{
			Order o = new Order(-1, nameTextField.getText(), inDatePicker.getValue(), outDatePicker.getValue(),
					confirmedCheckBox.isSelected());
			o.setId(DBBroker.getInstance().addOrder(o));
			OrderItem oi;

			for (int i = 0; i < orderTableList.size(); i++)
			{
				oi = new OrderItem(orderTableList.get(i).getId(), o.getId(), orderTableList.get(i).getQuantity());
				DBBroker.getInstance().addOrderItem(oi);
			}

			ScreenNavigator.loadScreen(ScreenNavigator.MANAGER);
		}
	}
}
*/