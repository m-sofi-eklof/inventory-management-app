package se.jensen.sofi_n.web_inventory;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.function.Consumer;

/// The JavaFX UI class
/*
* This class implements the interface UI which has the abstract methods showDialog(), promptProductDetails(), and
* showError().
* The class is intended to only use one window so stage is final and private.
* Some of the styling is stored in a separate class CuteTheme so as to further separate logic from this exact
* implementation.
* Other than the override methods, the JavaFXUI also includes functions for setting up a scene and switching between
* functional content such as action driven buttons and input prompts to implement the logic in the inventory class.
*  */
public class JavaFXUI implements UI {
    /// Logic related variables
    private final Stage STAGE;
    private Pane root;
    private Inventory inventory;
    private final int MAIN_CONTENT_INDEX = 1;
    private VBox pageContent;
    private Button backToMenuButton;


    /// Styling variables
    private final CuteTheme cuteTheme = new CuteTheme();
    private final int BUTTONWIDTH = 300;
    private final int BUTTONHEIGHT = 60;
    private final int SMALL_BUTTONWIDTH = 100;
    private final int SMALL_BUTTONHEIGHT = 45;
    private final double MAIN_BOX_WIDTH = 400;
    private final double MAIN_BOX_HEIGHT = 250;
    private final int TOP_MARGIN = 50;
    private final int CONTENT_MARGIN = 20;
    private final int TITLEFONTSIZE = 56;
    private final int HEADERFONTSIZE = 18;
    private final int BODYFONTSIZE = 12;


    /// Constructor
    /*The constructor for this UI class takes a stage argument and passes it to the constant stage variable
     * to use as the main window for the program
     * */
    public JavaFXUI(Stage stage) {
        this.backToMenuButton = makeBackToMenuButton();
        this.STAGE = stage;
    }

    //inventory setter
    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }


    /// Override functions from implemented UI interface
    /* The showDialog() function takes a String variable as argument and displays it in a text box*/
    @Override
    public void showDialog(String message, Runnable onBackToMenu) {
        //create box
        VBox dialogBox = new VBox(CONTENT_MARGIN);
        //create text
        Text messageText = new Text(message);
        //creating backButton
        Button backButton = mainButton("Back", onBackToMenu);
        backButton.setPrefSize(SMALL_BUTTONWIDTH, SMALL_BUTTONHEIGHT);

        dialogBox.getChildren().addAll(messageText, backToMenuButton);
        dialogBox.setMinHeight(MAIN_BOX_HEIGHT);
        dialogBox.setMaxWidth(MAIN_BOX_WIDTH + 100);

        clearContent();
        dialogBox.setAlignment(Pos.TOP_LEFT);
        showContent(dialogBox);
    }

    /* The showTextWithTitle function takes two string arguments, a title and a message
     * the title is displayed in styles from the CuteTheme class and the message is displayed in a textbox
     * under it*/
    @Override
    public void showTextWithTitle(String title, String message) {
        //box to contain everything
        VBox showDialogBox = new VBox(CONTENT_MARGIN);
        showDialogBox.setMaxWidth(MAIN_BOX_WIDTH+100);
        showDialogBox.setStyle(cuteTheme.VBoxHBoxStyleString());

        //make title label
        Label pageTitle = new Label(CuteTheme.spacedOut(title));
        pageTitle.setFont(cuteTheme.headerFont(HEADERFONTSIZE));
        pageTitle.setStyle("-fx-text-fill: Black;");

        //add label and set position
        showDialogBox.getChildren().add(pageTitle);
        showDialogBox.setAlignment(Pos.TOP_CENTER);

        //box containing message
        VBox textBox = new VBox(CONTENT_MARGIN);
        Text showText = new Text(message);
        textBox.getChildren().addAll(showText);
        textBox.setAlignment(Pos.TOP_LEFT); //left aligned

        //add message box to big box
        showDialogBox.getChildren().add(textBox);

        //setting  big box to content box sizing
        showDialogBox.setPrefHeight(MAIN_BOX_HEIGHT);
        showDialogBox.setMaxWidth(MAIN_BOX_WIDTH);

        //clear previous and show
        clearContent();
        showContent(showDialogBox);
    }


    /* The promptInput function takes a String prompt to display and a Consumer<String> onResult for the desired action
     * for the input String. Returns void. */
    @Override
    public void promptInput(String prompt, Consumer<String> onResult) {
        //outer box
        VBox contentBox = new VBox(CONTENT_MARGIN);
        contentBox.setAlignment(Pos.TOP_CENTER);


        //Box to contain
        VBox promptBox = new VBox(CONTENT_MARGIN);
        promptBox.setAlignment(Pos.TOP_CENTER);
        promptBox.setMaxWidth(BUTTONWIDTH);

        //label to show prompt
        Label promptLabel = new Label(prompt);

        //textfield for input
        TextField inputField = new TextField();

        //Submit prompt button
        Button submitButton = new Button("Okay");
        submitButton.setOnAction(event -> onResult.accept(inputField.getText()));

        //add to box
        promptBox.getChildren().addAll(promptLabel, inputField, submitButton); // adds label, field and submit to box
        promptBox.setStyle(cuteTheme.VBoxHBoxStyleString());

        contentBox.getChildren().addAll(promptBox, backToMenuButton);

        //clear and show
        clearContent();
        showContent(contentBox);

    }

    /* The showError function takes a String describing the error and returns void. The error message is displeyed in a
     * popup. */
    @Override
    public void showError(String error) {
        //make error type alert popup
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("An error occurred");
        //show error message
        alert.setContentText(error);
        alert.showAndWait(); // shows until closed
    }

    /* The promptProducitDetails function takes an integer for the articeID and a Consumer variable of the type ProductInputDetails
     * (See ProductsInputDetails class). It uses user input to create an object of ProductInputDetails and preforms the
     * onResult actions passed.*/
    @Override
    public void promptProductDetails(int id, Consumer<ProductInputDetails> onResult) {
        //creating content box
        VBox contentBox = new VBox(CONTENT_MARGIN);
        contentBox.setAlignment(Pos.TOP_CENTER);

        //creating produuct details box
        VBox productDetailsBox = new VBox(10);
        productDetailsBox.setStyle(cuteTheme.VBoxHBoxStyleString());
        productDetailsBox.setMaxWidth(BUTTONWIDTH);
        productDetailsBox.setPrefHeight(MAIN_BOX_HEIGHT);
        productDetailsBox.setPrefWidth(MAIN_BOX_WIDTH);

        //info label
        Label info = new Label("Creating article: " + id);
        info.setStyle("-fx-text-fill: Black; -fx-font-size: 16; -fx-font-weight: bold;");

        //Labels and textfields
        Label nameLabel = new Label("Product name:");
        TextField nameField = new TextField();
        Label priceLabel = new Label("Price:");
        TextField priceField = new TextField();
        Label descriptionLabel = new Label("Description:");
        TextField descriptionField = new TextField();

        //Submitbutton
        Button submitButton = new Button("Okay");
        submitButton.setOnAction(event -> {
            //Store inputs from textfields
            String name = nameField.getText();
            String priceStr = priceField.getText();
            String description = descriptionField.getText();
            //Does passed actions
            onResult.accept(new ProductInputDetails(name, priceStr, description));
            //Goes back to menu MAYBE MOVE THIS TO PASSED ACTION???
            clearContent();
            showContent(makeMenuBox(inventory));
        });
        //Add to box
        productDetailsBox.getChildren().addAll(info, nameLabel, nameField, priceLabel, priceField, descriptionLabel, descriptionField, submitButton);

        //back-button
        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(event -> {

        });


        //Clear previous and show box
        clearContent();
        showContent(productDetailsBox);

    }


    /// Window and layout functions

    /* The showWindow function takes a Pane root and displays the programs main menu scene on it. It also sets the
     * JavaFXU attribute root to the pane passed. */
    public void showWindow(Pane root) {
        STAGE.setScene(menuScene(root));
        STAGE.show();
        this.root = root;
    }

    /*The showContent function takes a pane or VBox and adds it to the index for main content*/
    public void showContent(Pane box) {
        pageContent.getChildren().add(MAIN_CONTENT_INDEX, box);
        pageContent.setAlignment(Pos.TOP_CENTER);
    }

    /* The clearContent function checks if mainContent has the index MAIN_CONTENT_INDEX and removes objects from
     * that index if true*/
    public void clearContent() {
        //clearing content
        while (pageContent.getChildren().size() > MAIN_CONTENT_INDEX) { //has at least 0 to MAIN_CONTENT_INDEX indexes
            pageContent.getChildren().remove(MAIN_CONTENT_INDEX); //removes this index until empty
        }
        //resetting original postition settings
        pageContent.setAlignment(Pos.TOP_CENTER);
        pageContent.setTranslateY(TOP_MARGIN);
        pageContent.setPadding(new Insets(0)); // reset padding if set dynamically
    }

    /// Scenes
    /*The private function menuScene takes the pane on which to set the scene and creates the look and function
     * of everything displayed in the window upon program start*/
    private Scene menuScene(Pane parent) {

        //background
        final Pane background = cuteTheme.generateBackground();

        //Logo and header box
        VBox logoBox = makeLogoAndTitleBox();

        //Main menu box
        VBox menuBox = makeMenuBox(inventory);
        menuBox.setAlignment(Pos.CENTER);

        //create main content and add title
        pageContent = new VBox(CONTENT_MARGIN, logoBox, menuBox);
        pageContent.setAlignment(Pos.TOP_CENTER);
        pageContent.setTranslateY(TOP_MARGIN);
        pageContent.setPrefHeight(MAIN_BOX_HEIGHT);
        pageContent.setPrefWidth(MAIN_BOX_WIDTH);


        // add to parent
        parent.getChildren().addAll(background, pageContent);

        //return
        return new Scene(parent, 800, 600);
    }


    /// VBoxes
    private VBox makeLogoAndTitleBox() {
        //color
        String LOGO_PINK = "#FF69b4;";
        //spacing
        final int TITLESPACING = -5;

        //title
        final Label logo = new Label("Pop Protein");
        logo.setFont(cuteTheme.titleFont(TITLEFONTSIZE));
        logo.setStyle("-fx-text-fill: " + LOGO_PINK);
        cuteTheme.titleDropShadow(logo);

        //header
        final Label INVENTORY_LABEL = new Label("I n v e n t o r y");
        INVENTORY_LABEL.setFont(cuteTheme.headerFont(HEADERFONTSIZE));
        INVENTORY_LABEL.setStyle("-fx-text-fill: Black;");

        //title and header box
        VBox logoBox = new VBox(TITLESPACING, logo, INVENTORY_LABEL);
        logoBox.setAlignment(Pos.TOP_CENTER);

        //return
        return logoBox;
    }

    /* The makeMenuBox function is called in the JavaFXUI attributes to make a private VBox containing the main menu
     * the main menu consists of four button, one for adding a product, one for listing current products and stock,
     * one for showing information on a specific product and one for exiting the program.
     *
     * The buttons are created with the mainButton function which takes a string to display on the button and actions
     * to preform on click event as parameters. */
    private VBox makeMenuBox(Inventory inventory) {
        //create box
        VBox mainMenuBox = new VBox(CONTENT_MARGIN);
        mainMenuBox.setAlignment(Pos.TOP_CENTER);
        mainMenuBox.setMaxWidth(MAIN_BOX_WIDTH);

        ///add - button
        Button addButton = mainButton("Add product", () -> {
            inventory.startAddProductFlow(() -> {
                clearContent();
                showContent(makeMenuBox(inventory));
            });
        });

        /// list - button
        Button listButton = mainButton("List products", () -> {
            inventory.listProducts(); //displays list of products and stock

            //make okay - button
            Button okayButton = new Button("Okay");
            okayButton.setStyle(cuteTheme.buttonStyleString());
            okayButton.setOnAction(event -> {
                //remove everything but log/title and show main menu
                clearContent();
                showContent(makeMenuBox(inventory));
            });
            //add okay button to content box
            pageContent.getChildren().add(okayButton);
        });
        /// info - button
        Button infoButton = mainButton("Info", () -> {
            inventory.startProductInfoFlow(() -> {
                clearContent();
                showContent(makeMenuBox(inventory));
            });
        });
        /// Stock - button
        Button manageStockButton = mainButton("Manage stock", this::showStockOptions);

        /// exit - buttton
        Button exitButton = mainButton("Exit", Platform::exit);

        //adding all buttons
        mainMenuBox.getChildren().addAll(addButton, listButton, infoButton, manageStockButton, exitButton);

        //return
        return mainMenuBox;
    }

    /* The makeCategoryAddBox function creates a box with buttons for each category of product as well as a backToMenuButton button
     * The function takes an object of the inventory class as argument and passes it to the addOfCategoryButton function along with
     * a String representing the category value of the product that is to be added */

    /// Alternative to make category info box
    @Override
    public void showCategorySelection(Consumer<String> onCategorySelected, Runnable onBackToMenu) {
        //outer content box
        VBox contentBox = new VBox(CONTENT_MARGIN);
        contentBox.setAlignment(Pos.CENTER);

        //Title
        Label title = new Label(CuteTheme.spacedOut("Choose category"));
        title.setFont(cuteTheme.headerFont(HEADERFONTSIZE));

        //Category selection box
        VBox categoryBox = new VBox(CONTENT_MARGIN);
        categoryBox.setAlignment(Pos.CENTER);
        categoryBox.setMaxSize(MAIN_BOX_WIDTH, MAIN_BOX_HEIGHT);

        //buttons
        Button accButton = mainButton("Accessories", () -> onCategorySelected.accept("Accessories"));
        Button eDrinkButton = mainButton("Energy drinks", () -> onCategorySelected.accept("Energy drinks"));
        Button powderButton = mainButton("Protein powders", () -> onCategorySelected.accept("Protein powders"));
        Button barButton = mainButton("Protein bars", () -> onCategorySelected.accept("Protein bars"));
        Button backButton = mainButton("Back", onBackToMenu);

        categoryBox.getChildren().addAll(accButton, eDrinkButton, powderButton, barButton, backButton);
        contentBox.getChildren().addAll(title, categoryBox);

        clearContent();
        showContent(categoryBox);
    }

    private void showStockOptions(){
        //create outer box
        VBox contentBox = new VBox(CONTENT_MARGIN);
        contentBox.setAlignment(Pos.CENTER);

        //create content box
        VBox optionsBox = new VBox(CONTENT_MARGIN);
        optionsBox.setAlignment(Pos.CENTER);

        //page title
        Label pageTitle = new Label(CuteTheme.spacedOut("Manage stock"));
        pageTitle.setFont(cuteTheme.headerFont(HEADERFONTSIZE));
        pageTitle.setStyle("-fx-text-fill: Black;");

        //Buttons for stock options
        Button increaseStockButton = mainButton("Increase stock", () -> {
            clearContent();
            inventory.userPromptID(id -> inventory.userIncreaseStock(id,
                    () -> {
                        //on success actions - might change
                        clearContent();
                        showContent(makeMenuBox(inventory));
                    },
                    ()-> {
                        //on failure action - might change
                        clearContent();
                        showContent(makeMenuBox(inventory));
                    }));
        });
        increaseStockButton.setPrefWidth(160);

        Button decreaseStockButton = mainButton("Decrease stock", () -> {
            clearContent();
            inventory.userPromptID(id -> inventory.userDecreaseStock(id,
                    () -> {
                        //on success actions - might change
                        clearContent();
                        showContent(makeMenuBox(inventory));
                    },
                    ()-> {
                        //on failure action - might change
                        clearContent();
                        showContent(makeMenuBox(inventory));
                    }));
        });
        decreaseStockButton.setPrefWidth(160);

        //add to boxes
        optionsBox.getChildren().addAll(increaseStockButton, decreaseStockButton);
        contentBox.getChildren().addAll(pageTitle, optionsBox, backToMenuButton);

        //clear and show
        clearContent();
        showContent(contentBox);
    }

    /// Buttons
    /* The mainButton function creates, sets style variables for, sets event action for, and returns, a button
     * The function takes two arguments; a String for the text displayed on the button and logic fo run on click
     * The function uses the supporting class CuteTheme to set some of these*/
    private Button mainButton(String text, Runnable action) {
        Button button = new Button(text);
        button.setFont(Font.font("Ariel", BODYFONTSIZE));
        button.setMaxWidth(BUTTONWIDTH);
        button.setMinHeight(BUTTONHEIGHT);
        button.setOnAction(event -> action.run());
        button.setStyle(cuteTheme.buttonStyleString());
        return button;
    }

    /* The function makeBackToMenuButton creates a mainButton with the display text "Back" and runable action
     * resetting the inventory objects currentSearchKey by calling the Inventory function clearSearchKey,
     * clearing any content that may be in the main content index of the page content box using clearContent,
     * and showing the programs main menu through the showContent and makeMenuBox functions*/
    private Button makeBackToMenuButton() {
        //create button
        Button back = mainButton("Back", () -> {
            //pass action
            clearContent();
            showContent(makeMenuBox(inventory));
        });
        //set size
        back.setMaxSize(SMALL_BUTTONWIDTH, SMALL_BUTTONHEIGHT);
        //return
        return back;
    }
}