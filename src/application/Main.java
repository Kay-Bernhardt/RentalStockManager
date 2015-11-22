package application;
	
import java.io.IOException;
import java.time.LocalDate;


import application.ui_controller.MainController;
import application.ui_controller.ScreenNavigator;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;


public class Main extends Application 
{
	public static LocalDate date;
	
	@Override
	public void start(Stage stage) 
	{
		Main.date = LocalDate.now();
		
		try {			
			stage.setTitle("Rental Stock Manager");
	        stage.setScene(createScene(loadMainPane()));
			stage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
    /**
     * Loads the main fxml layout.
     * Sets up the vista switching VistaNavigator.
     * Loads the first vista into the fxml layout.
     *
     * @return the loaded pane.
     * @throws IOException if the pane could not be loaded.
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
     * @param mainPane the main application layout.
     *
     * @return the created scene.
     */
    private Scene createScene(Pane mainPane) {
        Scene scene = new Scene(
            mainPane
        ); 
        //scene.getStylesheets().setAll(getClass().getResource("vista.css").toExternalForm());
 
        return scene;
    }
	
	public static void main(String[] args) {
		launch(args);
	}
}
