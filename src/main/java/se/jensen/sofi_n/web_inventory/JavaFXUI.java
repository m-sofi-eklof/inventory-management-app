package se.jensen.sofi_n.web_inventory;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.util.Stack;
import java.util.Map;
import java.util.function.Consumer;

/// The JavaFXUI class
///This class is for creating and running the front end of the application responsible for running and displaying the
/// logic in the Inventory class according to user interaction.
///
public class JavaFXUI implements UI {
    /// Logic related variables
    private final Stage MAIN_WINDOW;
    private final Stack<Runnable> viewHistory = new Stack<>(); //stores view history as Runnables
    private Inventory inventory;
    private final int MAIN_CONTENT_INDEX = 1;
    private VBox mainContentBox;
    private Button backButton;

    /// Styling variables
    private final CuteTheme cuteTheme = new CuteTheme();
    private final int BUTTON_WIDTH = 300;
    private final int BUTTON_HEIGHT = 60;
    private final double MAIN_BOX_WIDTH = 400;
    private final double MAIN_BOX_HEIGHT = 250;
    private final int TOP_MARGIN = 50;
    private final int CONTENT_MARGIN = 20;
    private final int TITLE_FONT_SIZE = 56;
    private final int HEADER_FONT_SIZE = 18;
    private final int BODY_FONT_SIZE = 12;


    /// Constructor
    /*The constructor for this UI class takes a stage argument and sets the constant stage attribute
     * to use as the main window for the program
     * */
    public JavaFXUI(Stage stage) {
        this.MAIN_WINDOW = stage;
    }

    ///inventory setter
    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }


    /// Override functions from UI interface
    /// all override function from the implemented UI interface
    ///
    /* The promptInput function takes a String prompt to display and a Consumer<String> onResult for the desired action
     * for the input String. Returns void. */
    @Override
    public void promptInput(String prompt, Consumer<String> onResult) {
        //Verical box to contain everything
        VBox promptBox = new VBox(CONTENT_MARGIN);
        promptBox.setAlignment(Pos.TOP_CENTER);
        promptBox.setMaxWidth(BUTTON_WIDTH);

        //promptlabel
        Label promptLabel = new Label(prompt);

        //textfield for input
        TextField inputField = new TextField();

        //Submit prompt button
        Button submitButton = new Button("Okay");
        submitButton.setOnAction(event -> onResult.accept(inputField.getText())); //runs comsumer String actions on input-String

        //add to box
        promptBox.getChildren().addAll(promptLabel, inputField, submitButton); // adds label, field and submit to box
        promptBox.setStyle(cuteTheme.paneStyleString()); // box style from CuteTheme

        //clear show and add to history
        goToView(() -> showContent(promptBox));

    }

    /* The showError function takes a String describing the error and returns void. In this implementation of the
     * function the error message is displayed in a popup. */
    @Override
    public void showError(String error) {
        //make error type alert popup
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("An error occurred");
        //show error message
        alert.setContentText(error);
        alert.showAndWait(); // no further action until popup is closed
    }

    /* The showTextWithTitle function takes two string arguments, a title and a message, and displays them.
     * In this implementation of the function, current page content is cleared and the title is displayed over a VBox
     * with the passed message. The box styling and font styling is made using the CuteTheme class object in attributes
     * */
    @Override
    public void showTextWithTitle(String title, String message) {
        //Creating clear vertical box to contain label and message
        VBox showDialogBox = new VBox(CONTENT_MARGIN);
        showDialogBox.setMaxWidth(MAIN_BOX_WIDTH);
        showDialogBox.setStyle(cuteTheme.paneStyleString());

        //make title label
        Label pageTitle = new Label(CuteTheme.spacedOut(title));
        pageTitle.setFont(cuteTheme.headerFont(HEADER_FONT_SIZE));
        pageTitle.setStyle("-fx-text-fill: Black;");

        //Vertical box containing message
        VBox textBox = new VBox(CONTENT_MARGIN);
        Text showText = new Text(message);
        textBox.getChildren().addAll(showText);
        textBox.setAlignment(Pos.TOP_LEFT); //left aligned

        //add message and label box to outer box
        showDialogBox.getChildren().addAll(pageTitle, textBox);

        //clear previous, update history stack and show
        goToView(() -> showContent(showDialogBox));
    }

    /* The promptProducitDetails function takes an integer for the articeID and a Consumer variable of the type
     * ProductInputDetails (See ProductsInputDetails class). It uses user input to create an object of
     * ProductInputDetails and preforms the onResult actions passed.*/
    @Override
    public void promptProductDetails(int id, Consumer<ProductInputDetails> onResult) {
        //creating vertical product details box
        VBox productDetailsBox = new VBox(10);
        productDetailsBox.setStyle(cuteTheme.paneStyleString());
        productDetailsBox.setPrefHeight(MAIN_BOX_HEIGHT);
        productDetailsBox.setMaxWidth(MAIN_BOX_WIDTH);

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
        //button actions
        submitButton.setOnAction(event -> {
            //Store inputs from textfields
            String name = nameField.getText();
            String priceStr = priceField.getText();
            String description = descriptionField.getText();
            //Creates ProductInputDetails objects and runs passed actions on it
            onResult.accept(new ProductInputDetails(name, priceStr, description));
        });
        //Add to box
        productDetailsBox.getChildren().addAll(info, nameLabel, nameField, priceLabel, priceField, descriptionLabel, descriptionField, submitButton);

        //Clear, update history stack, and show
        goToView(()-> showContent(productDetailsBox));
    }

    /*The showCategorySelection function takes a consumer String type with actions to preform on resulting String as
    * argument. The function shows the category selection then runs passed actions on a String variable representing
    * the selected category.
    * This implementation of the function displays the categories as buttons under the label "Choose category" and
    * generates the resulting string based on button clicked. The label font and button styling is created using the
    * CuteTheme object attribute.*/
    @Override
    public void showCategorySelection(Consumer<String> onCategorySelected) {
        //outer vertical content box
        VBox contentBox = new VBox(CONTENT_MARGIN);
        contentBox.setAlignment(Pos.CENTER);

        //Title
        Label title = new Label(CuteTheme.spacedOut("Choose category"));
        title.setFont(cuteTheme.headerFont(HEADER_FONT_SIZE));
        title.setStyle("-fx-text-fill: Black;");

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

        //Clear page content, update viewhistory, and show
        goToView(() -> showContent(contentBox));
    }


    /* The promptNutrientInfo function takes a FoodProduct type object, and a Consumer argument of NutrientTableValues
    * type with actions to perform on resulting NutrientTableValues object. The function then prompts the user
    * for nutrient information, with an added prompt Caffeine for products of EnergyDrink type, then creates an
    * object of the helper class NutrientTableValues and performes the passed action
    * This implementation of the function displays the prompts with labels and promptboxes and creates the object and
    * performs the actions in the submit-button actions. */
    @Override
    public void promptNutrientInfo(FoodProduct foodProduct, Consumer<NutrientTableValues> values) {
        //outer box
        VBox contentBox = new VBox(CONTENT_MARGIN);
        contentBox.setPrefSize(MAIN_BOX_WIDTH,MAIN_BOX_HEIGHT);
        contentBox.setAlignment(Pos.CENTER);

        //promptBox
        VBox promptBox = new VBox(CONTENT_MARGIN);
        promptBox.setMaxWidth(BUTTON_WIDTH);
        promptBox.setStyle(cuteTheme.paneStyleString());
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

        //input Horizontal boxes for each prompt and corresponding label
        HBox kcalBox = new HBox();
        kcalBox.getChildren().addAll(kcalLabel, kcalField);
        HBox fatBox  = new HBox();
        fatBox.getChildren().addAll(fatLabel,fatField);
        HBox carbsBox = new HBox();
        carbsBox.getChildren().addAll(carbsLabel, carbsField);
        HBox proteinBox = new HBox();
        proteinBox.getChildren().addAll(proteinLabel, proteinField);

        //add to vertical box
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

    /*The displayNutrientInformation function takes a Product type object as argument and displays it's information to
    * the user.
    * This implementation of the function shows the general product information on the left side of the window and
    * a button to remove product on the left. If the passed Product object is a FoodProduct object with nutrienttable
    * then the nutrienttable is displayed on the right above the remove product button.
    * Buttons, fonts and box styling is created using the class's CuteTheme object. */
    @Override
    public void displayProductInformation(Product product){
        //HBox outer box
        HBox outerBox = new HBox(CONTENT_MARGIN);
        outerBox.setAlignment(Pos.CENTER);

        //product info box
        VBox leftBox = new VBox(CONTENT_MARGIN);
        leftBox.setAlignment(Pos.CENTER);
        leftBox.setStyle(cuteTheme.paneStyleString());

        //Page title
        Label pageTitle = new Label(CuteTheme.spacedOut("Product information"));
        pageTitle.setFont(cuteTheme.headerFont(HEADER_FONT_SIZE));
        pageTitle.setStyle("-fx-text-fill: Black;");

        //add
        leftBox.getChildren().add(pageTitle);
        leftBox.setAlignment(Pos.TOP_CENTER);

        //information box
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

        //nutrient table
        if (product instanceof FoodProduct){
            if (((FoodProduct) product).getNutrientTable()!=null){
                //nutrient table vertical box
                VBox nutrientTableBox = new VBox(10); //ten margin between title header and table
                nutrientTableBox.setMaxWidth(300);
                nutrientTableBox.setStyle(cuteTheme.paneStyleString()); //box styling

                //title
                Label title = new Label("Nutrition information");
                title.setFont(cuteTheme.headerFont(18));

                //table header
                Label tableName = new Label(product.getName() + " nutrients per 100g:");
                tableName.setStyle("-fx-font-size: 12; -fx-font-weight: bold;");

                //creates gridpane displaying both values of the FoodProduct objects LinkedHashMap<String, Integer>
                //String key describing which nutrient the value is of in the left column, and values in the right
                GridPane nutrientTableView = new GridPane();
                //goes through entries from row 0 and adds to GridPane
                int row = 0;
                for (Map.Entry<String, Integer> entry : ((FoodProduct) product).getNutrientTable().entrySet()) {
                    Label key = new Label(entry.getKey()); //Nutrient label
                    Label value = new Label(entry.getValue().toString()); //value label
                    //sizing and styling
                    key.setPrefHeight(25);
                    value.setPrefHeight(25);
                    key.setPrefWidth(225);
                    value.setPrefWidth(75);
                    key.setStyle("-fx-border-color:black; -fx-padding: 6;");
                    value.setStyle("-fx-border-color:black; -fx-padding: 6;");
                    //add to table
                    nutrientTableView.add(key, 0, row);
                    nutrientTableView.add(value, 1, row);
                    //go to next row
                    row++;
                }
                //Add to nutrienttable box
                nutrientTableBox.getChildren().addAll(title, tableName, nutrientTableView);
                //add nutrienttable to right side box
                rightBox.getChildren().add(nutrientTableBox);
            }
        }

        //remove product button
        Button removeButton = mainButton("Remove product", ()->{
            inventory.removeProduct(product.articleID);
            goToMenu();
        });

        //add to right side box
        rightBox.getChildren().add(removeButton);

        //add left and right to outer horizontal box
        outerBox.getChildren().addAll(leftBox, rightBox);

        //clear pagecontent, update history and show outerbox as content
        goToView(()->showContent(outerBox));
    }


    /// Window and initial setup
    /// all functions for setting up initial window
    /* The showWindow function creates a Pane root and displays the programs main menu scene on it. */
    public void showWindow() {
        StackPane root = new StackPane(); //creating root stackpane
        MAIN_WINDOW.setScene(createAppScene(root));
        MAIN_WINDOW.show();
        goToMenu();
    }

    /*The showContent function takes a pane and adds it to the index for main content*/
    public void showContent(Pane box) {
        if (mainContentBox.getChildren().size() > MAIN_CONTENT_INDEX) {
            mainContentBox.getChildren().remove(MAIN_CONTENT_INDEX);
        }
        mainContentBox.getChildren().add(MAIN_CONTENT_INDEX, box);
        mainContentBox.setAlignment(Pos.TOP_CENTER);
    }

    /* The clearContent function has 4 main functions
     * Storing the lastest view as a runnable and pushing it to the viewHistory stack
     * Checking if mainContent has the index MAIN_CONTENT_INDEX and removes objects from that index if true
     * Resetting alignments to avoid padding buildup, translateY buildup or old positional settings
     * Updating the visibility of the backButton depending on if viewHistory is empty or not */
    public void clearContent(Runnable viewRunnable) {
        if (viewRunnable!=null){
            //storing content
            viewHistory.push(viewRunnable);
        }
        //clearing content
        while (mainContentBox.getChildren().size() > MAIN_CONTENT_INDEX) { //has at least 0 to MAIN_CONTENT_INDEX indexes
            mainContentBox.getChildren().remove(MAIN_CONTENT_INDEX); //removes this index until empty as higher indexes moves to down to maincontentindex
        }
        //resetting original postition settings
        mainContentBox.setAlignment(Pos.TOP_CENTER); //position
        mainContentBox.setTranslateY(TOP_MARGIN); //spacing above set with translateY
        mainContentBox.setPadding(new Insets(0)); //no padding

        updateBackButtonVisibility();
    }

    //Sets backbutton visibility to the True/False value of the condition viewHistory not empty
    private void updateBackButtonVisibility(){
        backButton.setVisible(!viewHistory.isEmpty());
    }


    /*The goToMenu function clears viewHistory stack, clear content and shows the menubox as content*/
    private void goToMenu(){
        viewHistory.clear(); //clears viewHistory so history is always null at root menu
        Runnable menuView = () -> showContent(makeMenuBox(inventory)); //
        //Clear content, adding no runnable view history
        clearContent((null));
        //show menubox as the content
        menuView.run();
    }

    /*The goToView function takes a Runnable with code to create the next view as argument, stores the view in history and clears content with the
    * clearContent function, then runs the Runnable code */
    private void goToView(Runnable viewRunnable) {
        clearContent(viewRunnable);
        viewRunnable.run();
    }

    /*The private function menuScene takes the pane on which to set the scene and creates the look and function
     * of everything displayed in the window upon program start
     * Styling of buttons and panes is created using the CuteTheme object attribute*/
    private Scene createAppScene(StackPane root) {
        //create background
        final Pane background = cuteTheme.generateBackground();

        //Create logo and header box
        VBox logoBox = makeLogoAndTitleBox();

        //Create back button
        backButton = new Button("←");
        backButton.setVisible(false); //hidden in original view
        backButton.setFocusTraversable(false); //reduce accidental action activation by disabling keyboard shortcut access
        backButton.setStyle(cuteTheme.lowkeyButtonStyleString());
        //backButton actions
        backButton.setOnAction(event -> {
            if (!viewHistory.isEmpty()) {
                viewHistory.pop(); //remove latest entry(current view)
                if (!viewHistory.isEmpty()) {
                    viewHistory.peek().run(); // Re-calls clearContent, restores that view, and pushes it to history
                } else {
                    goToMenu(); //go to menu if nothing else in history
                }
            }else{
                goToMenu();//go to menu if viewHistory is empty
            }
        });

        //create main content and add title
        mainContentBox = new VBox(CONTENT_MARGIN, logoBox);
        mainContentBox.setAlignment(Pos.TOP_CENTER);
        mainContentBox.setTranslateY(TOP_MARGIN);
        mainContentBox.setPrefHeight(MAIN_BOX_HEIGHT);
        mainContentBox.setPrefWidth(MAIN_BOX_WIDTH);


        // add to root
        root.getChildren().addAll(background, mainContentBox, backButton);
        StackPane.setAlignment(backButton, Pos.TOP_LEFT);

        //return
        return new Scene(root, 800, 600);
    }

    /*The makeLogoAndTitleBox creates the logo and title at the top of the window (mainContent[0]), above all
    * other content in the window.
    * The fonts and styling are created using the CuteTheme object attribute*/
    private VBox makeLogoAndTitleBox() {
        //spacing
        final int TITLESPACING = -5; //overlapping

        //title
        final Label logo = new Label("Pop Protein");
        logo.setFont(cuteTheme.titleFont(TITLE_FONT_SIZE));
        logo.setStyle("-fx-text-fill: " + CuteTheme.LOGO_PINK);
        cuteTheme.titleDropShadow(logo);

        //header
        final Label INVENTORY_LABEL = new Label("I n v e n t o r y");
        INVENTORY_LABEL.setFont(cuteTheme.headerFont(HEADER_FONT_SIZE));
        INVENTORY_LABEL.setStyle("-fx-text-fill: Black;");

        //title and header box
        VBox logoBox = new VBox(TITLESPACING, logo, INVENTORY_LABEL);
        logoBox.setAlignment(Pos.TOP_CENTER);

        //return
        return logoBox;
    }

    /* The makeMenuBox function returns a VBox with a button for each of the options in the main menu.
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


    /// Stocking management
    ///
    /* The showStockOptions function creates and shows two buttons, one for increating stock, one for decreasing stock
     * The button actions are set to call the promptProductID, and passing through the Consumer<Integer>
     * action to call userIncreaseStock or userDecreaseStock with the resulting ID*/
    private void showStockOptions(){
        //create outer box
        VBox contentBox = new VBox(CONTENT_MARGIN);
        contentBox.setAlignment(Pos.CENTER);

        //create content box
        VBox optionsBox = new VBox(CONTENT_MARGIN);
        optionsBox.setAlignment(Pos.CENTER);

        //page title
        Label pageTitle = new Label(CuteTheme.spacedOut("Manage stock"));
        pageTitle.setFont(cuteTheme.headerFont(HEADER_FONT_SIZE));
        pageTitle.setStyle("-fx-text-fill: Black;");

        //Buttons for stock options:
        //increase stock
        Button increaseStockButton = mainButton("Increase stock", () -> {
            inventory.promptProductID(id -> inventory.userIncreaseStock(id,
                            //on success action
                            this::goToMenu));
        });
        increaseStockButton.setPrefWidth(160);

        //decrease stock
        Button decreaseStockButton = mainButton("Decrease stock", () -> {
            inventory.promptProductID(id -> inventory.userDecreaseStock(id,
                    //on sucess action
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
        button.setFont(Font.font("Ariel", BODY_FONT_SIZE));
        button.setMaxWidth(BUTTON_WIDTH);
        button.setMinHeight(BUTTON_HEIGHT);
        button.setOnAction(event -> action.run());
        button.setStyle(cuteTheme.buttonStyleString());
        return button;
    }
}