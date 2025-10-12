package se.jensen.sofi_n.web_inventory;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import java.util.LinkedHashMap;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.util.Stack;
import java.util.Map;
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
    private StackPane root;
    private final Stack<Runnable> viewHistory = new Stack<>(); //tracking history for back button
    private Inventory inventory;
    private final int MAIN_CONTENT_INDEX = 1;
    private VBox currentContentBox;
    private Button backButton;


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
        this.STAGE = stage;
    }

    //inventory setter
    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }


    /// Override functions from implemented UI interface

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

        contentBox.getChildren().add(promptBox);

        //
        goToView(() -> showContent(contentBox));

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
        });
        //Add to box
        productDetailsBox.getChildren().addAll(info, nameLabel, nameField, priceLabel, priceField, descriptionLabel, descriptionField, submitButton);

        //Clear previous and show box
        goToView(()-> showContent(productDetailsBox));
    }

    /* The showTextWithTitle function takes two string arguments, a title and a message
     * the title is displayed in styles from the CuteTheme class and the message is displayed in a textbox
     * under it*/
    @Override
    public void showTextWithTitle(String title, String message) {
        //VBox
        VBox showDialogBox = new VBox(CONTENT_MARGIN);
        showDialogBox.setMaxWidth(MAIN_BOX_WIDTH);
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
        showDialogBox.getChildren().addAll(textBox);

        //clear previous and show
        goToView(() -> showContent(showDialogBox));
    }

    @Override
    public void showCategorySelection(Consumer<String> onCategorySelected) {
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

        categoryBox.getChildren().addAll(accButton, eDrinkButton, powderButton, barButton);
        contentBox.getChildren().addAll(title, categoryBox);

        goToView(() -> showContent(contentBox));
    }


    @Override
    public void promptNutrientInfo(FoodProduct foodProduct, Consumer<NutrientTableValues> values) {
        //outer box
        VBox contentBox = new VBox(CONTENT_MARGIN);
        contentBox.setPrefSize(MAIN_BOX_WIDTH,MAIN_BOX_HEIGHT);
        contentBox.setAlignment(Pos.CENTER);

        //promptBox
        VBox promptBox = new VBox(CONTENT_MARGIN);
        promptBox.setMaxWidth(BUTTONWIDTH);
        promptBox.setStyle(cuteTheme.VBoxHBoxStyleString());
        promptBox.setAlignment(Pos.TOP_LEFT);

        //Title
        Label pageTitle = new Label("Enter nutrient Info");
        pageTitle.setStyle("-fx-text-fill: Black; -fx-font-size: 18; -fx-font-weight: bold;");

        //Labels and textfields
        Label kcalLabel = new Label("Kcal:");
        TextField kcalField = new TextField();
        Label fatLabel = new Label("Fat:");
        TextField fatField = new TextField();
        Label carbsLabel = new Label("Carbs:");
        TextField carbsField = new TextField();
        Label proteinLabel = new Label("Protein:");
        TextField proteinField = new TextField();

        //input HBoxes
        HBox kcalBox = new HBox(CONTENT_MARGIN*2);
        kcalBox.getChildren().addAll(kcalLabel, kcalField);
        HBox fatBox  = new HBox(CONTENT_MARGIN*2);
        fatBox.getChildren().addAll(fatLabel,fatField);
        HBox carbsBox = new HBox(CONTENT_MARGIN*2);
        carbsBox.getChildren().addAll(carbsLabel, carbsField);
        HBox proteinBox = new HBox(CONTENT_MARGIN*2);
        proteinBox.getChildren().addAll(proteinLabel, proteinField);

        promptBox.getChildren().addAll(pageTitle,kcalBox,fatBox,carbsBox,proteinBox);

        //make object
        NutrientTableValues nutrientValues = new  NutrientTableValues();
        //Submitbutton
        Button submitButton = new Button("Okay");
        submitButton.setOnAction(event -> {
            //Store inputs from textfields
            nutrientValues.setStandardValues(
                    kcalField.getText(),
                    fatField.getText(),
                    carbsField.getText(),
                    proteinField.getText()
            );
            //Does passed actions
            values.accept(nutrientValues);
            //Goes back to menu
            goToMenu();
        });


        //if energy drink
        if(foodProduct instanceof EnergyDrink){
            //Also add caffeine prompt
            Label caffeineLabel = new Label("Caffeine:");
            TextField caffeineField = new TextField();
            HBox caffeineBox = new HBox(CONTENT_MARGIN*2);
            caffeineBox.getChildren().addAll(caffeineLabel, caffeineField);
            promptBox.getChildren().add(caffeineBox);


            //store original submitButton actions
            submitButton.setOnAction(event->{
                //set values
                nutrientValues.setStandardValues(
                        kcalField.getText(),
                        fatField.getText(),
                        carbsField.getText(),
                        proteinField.getText()
                );
                //set caffiene string
                nutrientValues.setCaffeineString(caffeineField.getText());

                //Does passed actions
                values.accept(nutrientValues);
                //Goes back to menu
                goToMenu();
            });
        }

        //clear and show
        contentBox.getChildren().addAll(promptBox,submitButton);
        goToView(()->showContent(contentBox));
    }

    @Override
    public void displayProductInformation(Product product){
        //HBox outer box
        HBox outerBox = new HBox(CONTENT_MARGIN);
        outerBox.setAlignment(Pos.CENTER);

        //product info box
        VBox leftBox = new VBox(CONTENT_MARGIN);
        leftBox.setAlignment(Pos.CENTER);
        leftBox.setStyle(cuteTheme.VBoxHBoxStyleString());

        Label pageTitle = new Label(CuteTheme.spacedOut("Product information"));
        pageTitle.setFont(cuteTheme.headerFont(HEADERFONTSIZE));
        pageTitle.setStyle("-fx-text-fill: Black;");

        leftBox.getChildren().add(pageTitle);
        leftBox.setAlignment(Pos.TOP_CENTER);

        VBox textBox = new VBox(CONTENT_MARGIN);
        Text showText = new Text(
                "ArticleID: "+ product.getArticleID()
                +"\nName: "+ product.getName()
                +"\nPrice: " +product.getPrice()
                +"\nDescription: "+ product.getDescription()
                +"\nStock: "+ product.getStock()
        );
        textBox.getChildren().add(showText);
        textBox.setAlignment(Pos.TOP_LEFT); //left aligned
        leftBox.getChildren().add(textBox);

        //VBox remove button n nutrient table
        VBox rightBox = new VBox(CONTENT_MARGIN);
        rightBox.setAlignment(Pos.CENTER);

        if (product instanceof FoodProduct){
            if (((FoodProduct) product).getNutrientTable()!=null){
                VBox nutrientTableBox = new VBox(10);
                nutrientTableBox.setMaxWidth(300);
                nutrientTableBox.setStyle(cuteTheme.VBoxHBoxStyleString());

                //title
                Label title = new Label("Nutrition information");
                title.setFont(cuteTheme.headerFont(18));

                //table header
                Label tableName = new Label(product.getName() + " nutrients per 100g:");
                tableName.setStyle("-fx-font-size: 12; -fx-font-weight: bold;");

                //table
                GridPane nutrientTableView = new GridPane();
                int row = 0;
                for (Map.Entry<String, Integer> entry : ((FoodProduct) product).getNutrientTable().entrySet()) {
                    Label key = new Label(entry.getKey());
                    Label value = new Label(entry.getValue().toString());
                    key.setPrefHeight(25);
                    value.setPrefHeight(25);
                    key.setPrefWidth(225);
                    value.setPrefWidth(75);
                    key.setStyle("-fx-border-color:black; -fx-padding: 6;");
                    value.setStyle("-fx-border-color:black; -fx-padding: 6;");
                    nutrientTableView.add(key, 0, row);
                    nutrientTableView.add(value, 1, row);
                    row++;
                }
                nutrientTableBox.getChildren().addAll(title, tableName, nutrientTableView);
                rightBox.getChildren().add(nutrientTableBox);
            }
        }

        //remove product button
        Button removeButton = mainButton("Remove product", ()->{inventory.removeProduct(product.articleID);});
        rightBox.getChildren().add(removeButton);

        outerBox.getChildren().addAll(leftBox, rightBox);

        goToView(()->showContent(outerBox));
    }


    /// Window and initial setup

    /* The showWindow function takes a Pane root and displays the programs main menu scene on it. It also sets the
     * JavaFXU attribute root to the pane passed. */
    public void showWindow(StackPane root) {
        this.root = root;
        STAGE.setScene(createAppScene(root));
        STAGE.show();
        goToMenu();
    }

    /*The showContent function takes a pane or VBox and adds it to the index for main content*/
    public void showContent(Pane box) {
        if (currentContentBox.getChildren().size() > MAIN_CONTENT_INDEX) {
            currentContentBox.getChildren().remove(MAIN_CONTENT_INDEX);
        }
        currentContentBox.getChildren().add(MAIN_CONTENT_INDEX, box);
        currentContentBox.setAlignment(Pos.TOP_CENTER);
    }

    /* The clearContent function checks if mainContent has the index MAIN_CONTENT_INDEX and removes objects from
     * that index if true*/
    public void clearContent(Runnable viewRunnable) {
        //storing content
        viewHistory.push(viewRunnable);
        //clearing content
        while (currentContentBox.getChildren().size() > MAIN_CONTENT_INDEX) { //has at least 0 to MAIN_CONTENT_INDEX indexes
            currentContentBox.getChildren().remove(MAIN_CONTENT_INDEX); //removes this index until empty
        }
        //resetting original postition settings
        currentContentBox.setAlignment(Pos.TOP_CENTER);
        currentContentBox.setTranslateY(TOP_MARGIN);
        currentContentBox.setPadding(new Insets(0)); // reset padding if set dynamically

        updateBackButtonVisibility();
    }

    private void updateBackButtonVisibility(){
        backButton.setVisible(!viewHistory.isEmpty());
    }

    private void goToMenu(){
        Runnable menuView = () -> showContent(makeMenuBox(inventory));
        //Clear content and add "menuview" to history
        clearContent((menuView));
        //show menubox as the content
        menuView.run();
    }

    private void goToView(Runnable viewRunnable) {
        clearContent(viewRunnable);
        viewRunnable.run();
    }

    /*The private function menuScene takes the pane on which to set the scene and creates the look and function
     * of everything displayed in the window upon program start*/
    private Scene createAppScene(StackPane root) {
        //background
        final Pane background = cuteTheme.generateBackground();

        //Logo and header box
        VBox logoBox = makeLogoAndTitleBox();

        //back button
        backButton = new Button("←");
        backButton.setVisible(false); //hidden in original view
        backButton.setFocusTraversable(false); //reduce accidental action activation
        backButton.setStyle(cuteTheme.lowkeyButtonStyleString());
        backButton.setOnAction(event -> {
            if (!viewHistory.isEmpty()) {
                viewHistory.pop();
                if (!viewHistory.isEmpty()) {
                    viewHistory.peek().run(); // This re-calls clearContent and restores that view *and pushes it to history*
                } else {
                    goToMenu();
                }
            }
            updateBackButtonVisibility();
        });

        //create main content and add title
        currentContentBox = new VBox(CONTENT_MARGIN, logoBox);
        currentContentBox.setAlignment(Pos.TOP_CENTER);
        currentContentBox.setTranslateY(TOP_MARGIN);
        currentContentBox.setPrefHeight(MAIN_BOX_HEIGHT);
        currentContentBox.setPrefWidth(MAIN_BOX_WIDTH);


        // add to root
        root.getChildren().addAll(background, currentContentBox, backButton);
        StackPane.setAlignment(backButton, Pos.TOP_LEFT);

        //return
        return new Scene(root, 800, 600);
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
        //clearing history bc root
        viewHistory.clear();
        //create box
        VBox mainMenuBox = new VBox(CONTENT_MARGIN);
        mainMenuBox.setAlignment(Pos.TOP_CENTER);
        mainMenuBox.setMaxWidth(MAIN_BOX_WIDTH);

        ///add - button
        Button addButton = mainButton("Add product", inventory::startAddProductFlow);

        /// list - button
        //displays list of products and stock
        Button listButton = mainButton("List products", inventory::listProducts);

        /// info - button
        Button infoButton = mainButton("Info", inventory::startProductInfoFlow);
        /// Stock - button
        Button manageStockButton = mainButton("Manage stock", this::showStockOptions);

        /// exit - buttton
        Button exitButton = mainButton("Exit", Platform::exit);

        //adding all buttons
        mainMenuBox.getChildren().addAll(addButton, listButton, infoButton, manageStockButton, exitButton);

        //return
        return mainMenuBox;
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
            inventory.userPromptID(id -> //on success actions - might change
                    inventory.userIncreaseStock(id,
                            //on sucess action - might change
                            this::goToMenu,
                            //on failure action - might change
                            this::goToMenu));
        });
        increaseStockButton.setPrefWidth(160);

        Button decreaseStockButton = mainButton("Decrease stock", () -> {
            inventory.userPromptID(id -> inventory.userDecreaseStock(id,
                    //on sucess action - might change
                    this::goToMenu,
                    //on failure action - might change
                    this::goToMenu));
        });
        decreaseStockButton.setPrefWidth(160);

        //add to boxes
        optionsBox.getChildren().addAll(increaseStockButton, decreaseStockButton);
        contentBox.getChildren().addAll(pageTitle, optionsBox);

        //clear and show
        goToView(()->showContent(contentBox));
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
}