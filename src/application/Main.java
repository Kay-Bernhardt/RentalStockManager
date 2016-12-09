package application;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.time.LocalDate;

import application.ui_controller.MainController;
import application.ui_controller.ScreenNavigator;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.broker.DBBroker;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Pane;

public class Main extends Application
{
	public static LocalDate date;

	@Override
	public void start(Stage stage)
	{
		Main.date = LocalDate.now();

		try
		{
			stage.setTitle("Rental Stock Manager");
			stage.setScene(createScene(loadMainPane()));
			stage.setOnCloseRequest(new EventHandler<WindowEvent>()
			{
				@Override
				public void handle(WindowEvent t)
				{
					DBBroker.getInstance().close();
					Platform.exit();
					System.exit(0);
				}
			});
			stage.show();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Loads the main fxml layout. Sets up the vista switching VistaNavigator.
	 * Loads the first vista into the fxml layout.
	 *
	 * @return the loaded pane.
	 * @throws IOException
	 *            if the pane could not be loaded.
	 */
	private Pane loadMainPane() throws IOException
	{
		FXMLLoader loader = new FXMLLoader();
		Pane mainPane = (Pane) loader.load(getClass().getResourceAsStream(ScreenNavigator.MAIN));

		MainController mainController = loader.getController();

		ScreenNavigator.setMainController(mainController);
		ScreenNavigator.loadScreen(ScreenNavigator.MANAGER);

		return mainPane;
	}

	/**
	 * Creates the main application scene.
	 *
	 * @param mainPane
	 *           the main application layout.
	 *
	 * @return the created scene.
	 */
	private Scene createScene(Pane mainPane)
	{
		Scene scene = new Scene(mainPane);
		scene.getStylesheets().add(this.getClass().getResource("style/application.css").toExternalForm());
		// scene.getStylesheets().setAll(getClass().getResource("vista.css").toExternalForm());

		return scene;
	}

	public static void main(String[] args)
	{
		launch(args);
		
		{
			String path = "meh";
			try
			{
				path = getProgramPath2();
			} catch (UnsupportedEncodingException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//System.out.println(this.getClass().getProtectionDomain().getCodeSource().getLocation());
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Information Dialog");
			alert.setHeaderText("Look, an Information Dialog");
			alert.setContentText(path);

			alert.showAndWait();
		}
	}
	
	public static String getProgramPath2() throws UnsupportedEncodingException {
      URL url = Main.class.getProtectionDomain().getCodeSource().getLocation();
      String jarPath = URLDecoder.decode(url.getFile(), "UTF-8");
      String parentPath = new File(jarPath).getParentFile().getPath();
      return parentPath;
   }
}
