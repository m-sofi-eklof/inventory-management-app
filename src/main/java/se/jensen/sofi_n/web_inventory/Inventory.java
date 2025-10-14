package se.jensen.sofi_n.web_inventory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/// The Inventory class handles the logic for the application. It takes a UI implementation as argument and
/// uses the UI interface functions for all its user interaction. All the logic in this class is UI flexible
/// as long as an implementation class of the corresponding UI interface is made.
///
/// The Inventory class consists of an arraylist of type Product, an arraylist of type FoodProduct, and funtions
/// for listing products, adding and removing products, creating/displaying nutrient tables for food products,
/// getting product info from id, as well as adding/reducing stock and loading/writing product and nutrient table
/// info to files.
///
public class Inventory {
    /// Attributes
    //user interface
    private final UI ui;
    //files
    private final String PRODUCTS_FILE;
    private final String NUTRIENT_TABLE_FILE;
    //storing products
    private List<Product> products = new ArrayList<>();
    private List<FoodProduct> foodProducts = new ArrayList<>();
    //ID minimun
    private final int LOWEST_ID = 10000;


    /// Constructor
    /*
    The Inventory class constructor takes a UI implementing object and sets the inventorys UI attribute,
    downloads the information stored in the product nutrient table files respectively and stores in product list
    */
    public Inventory(UI ui, String productFileName, String nutrientTableFileName) {
        this.ui = ui; //sets ui
        this.PRODUCTS_FILE =productFileName;
        this.NUTRIENT_TABLE_FILE = nutrientTableFileName;
        loadProductsFromFile(PRODUCTS_FILE); //reads information and saves products to product list
        updateFoodProductsList(); //get any food products in product list and stores in food product list
        loadNutrientTablesFromFile(NUTRIENT_TABLE_FILE); //download nutrient tables and sets to food product objects
    }

    /// File handling logic
    /// all functions relating to reading and writing to files
    ///
    /* The updateProductFile function takes a String file name as argument, goes through product list and
    * calls the Product class's serialize function and writes the information into the file.*/
    private void updateProductFile(String fileName) {
        try (PrintWriter out = new PrintWriter(new FileWriter(fileName))) { //try with resources
            for (Product product : products) {
                out.println(product.serialize());//one product per line
            }
        } catch (IOException e) {
            ui.showError("Couldn't save: " + e.getMessage());
        }
    }

    /* The loadProductsFromFile function uses the Product class's deserialize function to read information from
    * the products file at the root of the project folder and create an object per line using the information
    * on each line*/
    private void loadProductsFromFile(String fileName) {
        products.clear(); //make sure product list is empty before loading
        try (BufferedReader in = new BufferedReader(new FileReader(fileName))) { //try with resources
            String line;
            while ((line = in.readLine()) != null) { //reads line by line
                Product product = Product.deserialize(line, ui::showError); //ui.ShowError as onError String action
                if (product != null)
                    products.add(product); //add product to product-list
            }
        } catch (IOException e) {
            ui.showError("Couldn't load: " + e.getMessage());
        }
    }

    /*The loadNutrientTablesFromFile function takes a String filename as argument, reds ech line and passes it, along
     the current food product list into the food product class's deserializeNutrientValues function, which matches
    * the articleIDs in each of the nutirenttablefile lines with a product in that list and sets its nutrienttable
    * from the values stored in that line of file */
    private void loadNutrientTablesFromFile(String fileName) {
        try (BufferedReader in = new BufferedReader(new FileReader(fileName))) { //try with resources
            String line;
            while ((line = in.readLine()) != null) {
                FoodProduct.deserializeNutrientValues(line, foodProducts, ui::showError);//passes line to desiralise
            }
        } catch (IOException e) {
            ui.showError("Couldn't load: " + e.getMessage());
        }
    }

    /*The function updateNutrientTableFile takes a String argument, opens a FileWriter for the filename passed,
    * goes through the food product list, checck if it has a nutrient table with sufficient number of elements,
    *  and if so, writes the products articleID and its nitrient table its values as a line in the file using
    * the serializeNutrientValues and the println function*/
    private void updateNutrientTablesFile(String fileName) {
        try (PrintWriter out = new PrintWriter(new FileWriter(fileName))) {
            for (FoodProduct foodProduct : foodProducts) {
                if (foodProduct.getNutrientTable() == null) {
                    ui.showError("No nutrient table found for article " + foodProduct.articleID);
                } else if (foodProduct.getNutrientTable().size() < 4) {
                    ui.showError("Wrong format nutrient table for article " + foodProduct.articleID);
                } else {
                    out.println(foodProduct.serializeNutrientValues());
                }
            }
        } catch (IOException e) {
            ui.showError("Couldn't save: " + e.getMessage());
        }
    }

    /// List storage functions
    /// all functions managing the lists <FoodProduct>products and <Products>products
    ///
    /*The updateFoodProductsList function clears the food product list, loops through the product list,
    * checks for FoodProduct objects and adds them to the food product list*/
    public void updateFoodProductsList() {
        foodProducts.clear(); //avoid duplicates
        for (Product product : products) {
            if (product instanceof FoodProduct) {
                foodProducts.add((FoodProduct) product); //Cast and add
            }
        }
    }

    /*The searchProduct function takes an integer id, loops through product list and returns product if
    * a match is found.*/
    public Product searchProduct(int id) {
        for (Product product : products) {
            if (product.articleID == id) {
                return product;
            }
        }
        return null;
    }

    /// Listing product flow
    /// function for applications the list product functionality
    ///
    /* The function listProducts uses the UI method showTextWithTitle to display a list of all products
    * in the Inventory class's product list with articleID, name and stock*/
    public void listProducts(){
        String title = "Current stock: ";
        if (products.isEmpty()){
            ui.showTextWithTitle(title, "No product information implemented");
        }else{
            String show = "";
            for (Product product : products){
                show = show.concat("Article ID: " + product.articleID + " " + product.name + " stock: " + product.stock + "\n");
            }
            ui.showTextWithTitle(title, show);
        }
    }

    /// Adding product flow
    /// functions relevant to the add product flow in mostly chronological call order
    ///
    /*The startAddProduct flow calls the UI interface function showCategorySelection and passes the
    * onResult action to pass the resulting String to function promptAddDetails here in the Inventory class*/
    public void startAddProductFlow() {
        ui.showCategorySelection(category -> {
            promptAddDetails(category);
        });
    }

    /*The prompAddDetails function takes a category String argument, generates an avaliable articleID and
    * passes the id to the UI interface function promptProductDetails as first argument and the onResult
    * ProductInputDetails-action to call processAddDetails with category, id and details, as the second argument */
    private void promptAddDetails(String category) {
        int id = generateArticleID();
        ui.promptProductDetails(id, details -> processAddDetails(category, id, details));
    }

    /*The generatArticleID function returns the value of the lowest avaliable articleID higher than the
    * LOWEST_ID constant*/
    public int generateArticleID() {
        int low = LOWEST_ID;
        if (products.isEmpty()) {
            return low;
        }
        int max=low;
        //find highest articleID in list
        for (Product product : products) {
            if (product.articleID > max) {
                max = product.articleID;
            }
        }
        //search for gaps(removed products)
        for (int i=low; i<=max; i++) {
            for (Product product : products) {
                if (product.articleID == i) {
                    return i;
                }
            }
        }
        //return max+1 if no gaps
        return max++;
    }

    /*The processAddDetails function takes a String representing category, and integer ID and an object
    * of the supporting input class ProductInputDetails, validates the input and if valid creates a product
    * using said details. Once a product is created its added to the relevant Inventory list/s and and the
    * products file is updated. For any object of classes inheriting from the FoodProduct class,
    * the UI function promptNutrientInfo is called.*/
    private void processAddDetails(String category, int id, ProductInputDetails details) {
        //validating details
        if (details == null || details.name == null || details.name.trim().isEmpty()) {
            ui.showError("Product name can't be empty");
            return;
        }
        if (details.priceStr == null || details.priceStr.trim().isEmpty()) {
            ui.showError("Product price can't be empty");
            return;
        }
        double price;
        try {
            price = Double.parseDouble(details.priceStr);
        } catch (NumberFormatException e) {
            ui.showError("Invalid price format");
            return;
        }
        if (details.description == null || details.description.trim().isEmpty()) {
            ui.showError("Product description can't be empty");
            return;
        }

        //add product
        switch (category) {
            case "Accessories":
                //create
                Accessory accessory = new Accessory(id);
                accessory.setName(details.name);
                accessory.setDescription(details.description);
                accessory.setPrice(price);
                //store
                products.add(accessory);
                break;
            case "Energy drinks":
                //create
                EnergyDrink energyDrink = new EnergyDrink(id);
                energyDrink.setName(details.name);
                energyDrink.setDescription(details.description);
                energyDrink.setPrice(price);
                //store
                products.add(energyDrink);
                ui.promptNutrientInfo(energyDrink, values -> {
                    processNutrientDetails(values, energyDrink);
                });
                break;
            case "Protein bars":
                //create
                ProteinBar proteinBar = new ProteinBar(id);
                proteinBar.setName(details.name);
                proteinBar.setDescription(details.description);
                proteinBar.setPrice(price);
                //store
                products.add(proteinBar);
                ui.promptNutrientInfo(proteinBar, values -> {
                    processNutrientDetails(values, proteinBar);
                });
                break;
            case "Protein powders":
                //create
                ProteinPowder proteinPowder = new ProteinPowder(id);
                proteinPowder.setName(details.name);
                proteinPowder.setDescription(details.description);
                proteinPowder.setPrice(price);
                //store
                products.add(proteinPowder);
                ui.promptNutrientInfo(proteinPowder, values -> {
                    processNutrientDetails(values, proteinPowder);
                });
                break;
            default:
                ui.showError("Invalid category");
        }
        updateProductFile(PRODUCTS_FILE);
        updateFoodProductsList();
    }


    /*The function processNutrientDetails take NutrientTableValues object and a FoodProduct object as
    * arguments, parses them to integer and sets the FoodProducts nutrientTable LinkedHashMap attribute
    * using the parsed values. The function handles objects of EnergyDrink class seperately from other
    * FoodProduct object as the energyDrink objects have an extra element for caffeine */
    public void processNutrientDetails(NutrientTableValues values, FoodProduct foodProduct) {
        //validate and set energydrink nutrient tables
        if (foodProduct instanceof EnergyDrink) {
            try {
                int kcal = Integer.parseInt(values.kcalString);
                int fat = Integer.parseInt(values.fatString);
                int carbs = Integer.parseInt(values.carbsString);
                int protein = Integer.parseInt(values.proteinString);
                int caffeine = Integer.parseInt(values.caffeineString);
                ((EnergyDrink) foodProduct).setNutrientTable(kcal, fat, carbs, protein, caffeine);

            } catch (NumberFormatException e) {
                ui.showError("Invalid input format: " + e.getMessage());
            } catch (NullPointerException e) {
                ui.showError(e.getMessage());
            }
        } else { //validate and set nutrient tables for other food objects
            try {
                int kcal = Integer.parseInt(values.kcalString);
                int fat = Integer.parseInt(values.fatString);
                int carbs = Integer.parseInt(values.carbsString);
                int protein = Integer.parseInt(values.proteinString);
                foodProduct.setNutrientTable(kcal, fat, carbs, protein);
            } catch (NumberFormatException e) {
                ui.showError("Invalid input format: " + e.getMessage());
            } catch (NullPointerException e) {
                ui.showError(e.getMessage());
            }
        }
        updateNutrientTablesFile(NUTRIENT_TABLE_FILE);
    }


    /// Show product info - flow
    /// all functions handeling the product info - flow
    ///
    /* The start product info flow calls the UI function promptProductID with the integer type onResult
    * action to pass the resulting integer ID to the showProductInfo function here in the Inventory class*/
    public void startProductInfoFlow(){
        promptProductID(this::showProductInfo);
    }

    /* The promptProductID function uses UI.promptInput to get an article ID, parses it to int and runs
     * the passed Consumer<Integer> actions with the resulting integer.*/
    public void promptProductID(Consumer<Integer> onResult) {
        ui.promptInput("Enter product ID:", IDstr ->{
            int id;
            try {
                id = Integer.parseInt(IDstr);
                if (id < LOWEST_ID){
                    ui.showError("Product ID is too low");
                    return;
                }
            } catch (NumberFormatException e){
                ui.showError("Invalid ID format");
                return;
            }
            onResult.accept(id);
        });
    }

    /*The showProductInfo function takes an integer ID, looks for a match in the product list and displayes
    * the information using the UI function displayProductInformation, or displayes an error message*/
    public void showProductInfo(int id){
        Product product = searchProduct(id);
        if (product==null){
            ui.showError("Invalid product ID");
        }else if (product.toString()==null || product.toString().isEmpty()){
            ui.showError("Product information does not exist or was not found");
        }else{
            ui.displayProductInformation(product);
        }
    }

    /* The function removeProduct takes an integer ID and removes an object with this article ID from the list/s
    * product and foodProducts if they contain such an object. The function then calls updateProductFile and
    * updatesNutrientTableFile to account for any discrepencies between file and lists.*/
    public void removeProduct(int id){
        products.removeIf(product -> product.articleID == id);
        foodProducts.removeIf(foodProduct -> foodProduct.articleID == id);
        updateProductFile(PRODUCTS_FILE);
        updateNutrientTablesFile(NUTRIENT_TABLE_FILE);

    }

    /// Increase and decrease stock - flow
    /// all functions to manage stock
    ///
    /* The function userIncreaseStock takes an int ID, searches the Inventory products list for the corresponding
    * product, prompts the user for a quantity by which to increase the stock and calls the increaseStock
    * function on success*/
    public void userIncreaseStock(int id, Runnable onSuccess){
        if (id==0 || searchProduct(id)==null){
            ui.showError("Product not found");
            userIncreaseStock(id, onSuccess);
        }
        else {
            ui.promptInput("Quantity: ", quantityString -> {
                try {
                    int quantity = Integer.parseInt(quantityString);
                    increaseStock(id, quantity);
                    onSuccess.run();
                } catch (NumberFormatException e) {
                    ui.showError("Invalid quantity");
                    userIncreaseStock(id, onSuccess);
                }
            });
        }
    }

    /* The function userDecreaseStock takes an int ID, searches the Inventory products list for the corresponding
     * product, prompts the user for a quantity by which to increase the stock and calls the decreaseStock
     * function on success*/
    public void userDecreaseStock(int id,  Runnable onSuccess){
        if (id==0 || searchProduct(id)==null){
            ui.showError("Product not found");
            userIncreaseStock(id, onSuccess);
        }else{
            ui.promptInput("Quantity: ", quantityString -> {
                try {
                    int quantity = Integer.parseInt(quantityString);
                    if(decreaseStock(id, quantity)){
                        onSuccess.run();
                    }
                }
                catch (NumberFormatException e){
                    ui.showError("Invalid quantity");
                    userDecreaseStock(id, onSuccess);
                }
            });
        }
    }


    /*The increaseStock function takes two integers, ID and quantity, searches for such a product in
    * products list, add increases its stock by the passed quantity*/
    private void increaseStock (int id, int quantity){
        searchProduct(id).stock += quantity;
    }

    /*The decreaseStock function takes two integers, ID and quantity, searches for such a product in
     * products list, add if stock is sufficient, decreases its stock by the passed quantity*/
    private boolean decreaseStock(int id, int quantity){
        boolean result = false;
        if (searchProduct(id).stock < quantity){
            ui.showError("Not enough stock");
        }
        else {
            searchProduct(id).stock -= quantity;
            result = true;
        }
        return result;
    }
}
