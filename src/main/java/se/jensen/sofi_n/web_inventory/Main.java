package se.jensen.sofi_n.web_inventory;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application { //extends javaFX application args

    @Override //application overide function
    public void start(Stage primaryStage) {
        JavaFXUI ui = new JavaFXUI(primaryStage); // creates instance of UI class
        Inventory inventory = new Inventory(ui, "Products.txt", "nutrient_tables.txt"); // creating instance of inventory logic handler
        ui.setInventory(inventory);

        StackPane root = new StackPane(); //creating root stackpane
        ui.showWindow(root); //
    }
    public static void main(String[] args) {
        launch(args);
    }
}
