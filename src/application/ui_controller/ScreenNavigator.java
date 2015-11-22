package application.ui_controller;

import javafx.fxml.FXMLLoader;
 


import java.io.IOException;
 
/**
 * Utility class for controlling navigation between screens.
 *
 * All methods on the navigator are static to facilitate
 * simple access from anywhere in the application.
 * @author https://gist.github.com/jewelsea/6460130
 */
public class ScreenNavigator {
 
    /**
     * Convenience constants for fxml layouts managed by the navigator.
     */
	public static final String MAIN 		= "/application/fxml/Main.fxml";
    public static final String MANAGER    	= "/application/fxml/Manager.fxml";
    public static final String CREATE_ORDER = "/application/fxml/CreateOrder.fxml";
    public static final String NEW_ITEM 	= "/application/fxml/NewItem.fxml";
    public static final String EDIT_ITEM 	= "/application/fxml/EditItem.fxml";
    public static final String EDIT_ORDER 	= "/application/fxml/EditOrder.fxml";
    public static final String EDIT_STOCK 	= "/application/fxml/EditStock.fxml";
 
    /** The main application layout controller. */
    private static MainController mainController;
 
    /**
     * Stores the main controller for later use in navigation tasks.
     *
     * @param mainController the main application layout controller.
     */
    public static void setMainController(MainController mainController) {
        ScreenNavigator.mainController = mainController;
    }
 
    /**
     * Loads the screen specified by the fxml file into the
     * screenHolder pane of the main application layout.
     *
     * Previously loaded screen for the same fxml file are not cached.
     * The fxml is loaded anew and a new screen node hierarchy generated
     * every time this method is invoked.
     *
     * A more sophisticated load function could potentially add some
     * enhancements or optimizations, for example:
     *   cache FXMLLoaders
     *   cache loaded screen nodes, so they can be recalled or reused
     *   allow a user to specify screen node reuse or new creation
     *   allow back and forward history like a browser
     *
     * @param fxml the fxml file to be loaded.
     */
    public static void loadScreen(String fxml) {
        try {
            mainController.setScreen(FXMLLoader.load(ScreenNavigator.class.getResource(fxml)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void setUserData(Object o)
    {
    	mainController.setUserData(o);
    }
    
    public static Object getUserData()
    {
    	return mainController.getUserData();
    }
 
}
