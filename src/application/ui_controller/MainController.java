package application.ui_controller;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
 
/**
 * Main controller class for the entire layout.
 * @author https://gist.github.com/jewelsea/6460130
 */
public class MainController {
 
    /** Holder of a switchable screens. */
    @FXML
    private StackPane screenHolder;
 
    /**
     * Replaces the screen displayed in the screen holder with a new screen.
     *
     * @param node the vista node to be swapped in.
     */
    public void setScreen(Node node) {
        screenHolder.getChildren().setAll(node);
    }
    
    public void setUserData(Object o)
    {
    	screenHolder.getParent().setUserData(o);
    }
    
    public Object getUserData()
    {
    	return screenHolder.getParent().getUserData();
    }
 
}
