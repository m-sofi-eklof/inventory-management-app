package se.jensen.sofi_n.web_inventory;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/// The Main class
public class Main extends Application { //extends javaFX application args
    /*The start function is a mandatory override function in JavaFX
    * This implementation creates an object of the JavaFXUI passing through the primaryStage Stage object for the
    * main window. An inventory object is created passing the JavaFXUI object, as well as the names of the files
    * for the file handeling portion of the app. The Inventorys ui object is set to the JavaFXUI object
    * and the JavaFXUI function show window is called.*/
    @Override
    public void start(Stage primaryStage) {
        JavaFXUI ui = new JavaFXUI(primaryStage); // creates instance of UI class
        Inventory inventory = new Inventory(ui, "Products.txt", "nutrient_tables.txt"); // creating instance of inventory logic handler
        ui.setInventory(inventory);

        ui.showWindow();
    }
    //main function runs start method
    public static void main(String[] args) {
        launch(args);//getParameters gets called, an object of main is created and runs start function
    }
}
