package se.jensen.sofi_n.web_inventory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Inventory {

    private final UI ui;
    private List<Product> products = new ArrayList<>();
    private List<FoodProduct> foodProducts = new ArrayList<>();
    private final int LOWEST_ID = 10000;
    private String PRODUCTS_FILE = "Products.txt";

    /// Constructor
    public Inventory(UI ui) {
        this.ui = ui;
        loadProductsFromFile(PRODUCTS_FILE);
        updateFoodProductsList();
    }

    /// File handling logic
    public void updateProductFile(String fileName) {
        try(PrintWriter out = new PrintWriter(new FileWriter(fileName))) {
            for (Product product : products) {
                out.println(product);//one product per line
            }
        }catch (IOException e){
            ui.showError("Couldn't save: " + e.getMessage());
        }
    }
    public void loadProductsFromFile(String fileName) {
        products.clear();
        try(BufferedReader in = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = in.readLine()) != null){
                Product product = Product.deserialize(line, ui::showError);
                if (product != null)
                    products.add(product);
            }
        }catch (IOException e){
            ui.showError("Couldn't load: " + e.getMessage());
        }
    }

    /// List storage functions
    public void updateFoodProductsList(){
        foodProducts.clear(); //avoid duplicates
        for(Product product : products){
            if (product instanceof FoodProduct) {
                foodProducts.add((FoodProduct) product); //Cast and add
            }
        }
    }

    public Product searchProduct(int id){
        for (Product product : products){
            if(product.articleID==id){
                return product;
            }
        }
        return null;
    }

    private FoodProduct searchFoodProduct(int id){
        for (FoodProduct product : foodProducts){
            if(product.articleID==id){
                return product;
            }
        }
        return null;
    }

    /// Adding product logic
    public void startAddProductFlow(Runnable returnToMenu){
        ui.showCategorySelection(category->{
            promptAddDetails(category, returnToMenu);
        },returnToMenu);
    }

    private void promptAddDetails(String category, Runnable returnToMenu){
        int id =generateArticleID();
        ui.promptProductDetails(id, details-> processAddDetails(category, id, details, returnToMenu));
    }

    public int generateArticleID(){
        int id =LOWEST_ID;
        if (products.isEmpty()){
            return id;
        }
        //set id to highest articleID in list
        for (Product product : products){
            if(product.articleID>id){
                id = product.articleID;
            }
        }
        id++; //new id is one greater than current max
        return id;
    }

    private void processAddDetails(String category, int id, ProductInputDetails details, Runnable returnToMenu){
        //validating details
        if (details == null || details.name == null || details.name.trim().isEmpty()) {
            ui.showError("Product name can't be empty");
            return;
        }
        if(details.priceStr==null || details.priceStr.trim().isEmpty()){
            ui.showError("Product price can't be empty");
            return;
        }
        double price;
        try {
            price = Double.parseDouble(details.priceStr);
        }catch (NumberFormatException e){
            ui.showError("Invalid price format");
            return;
        }
        if (details.description == null || details.description.trim().isEmpty()){
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
                break;
            case "Protein bars":
                //create
                ProteinBar proteinBar = new ProteinBar(id);
                proteinBar.setName(details.name);
                proteinBar.setDescription(details.description);
                proteinBar.setPrice(price);
                //store
                products.add(proteinBar);
                break;
            case "Protein powders":
                //create
                ProteinPowder proteinPowder = new ProteinPowder(id);
                proteinPowder.setName(details.name);
                proteinPowder.setDescription(details.description);
                proteinPowder.setPrice(price);
                //store
                products.add(proteinPowder);
                break;
            default:
                ui.showError("Invalid category");
        }
        updateProductFile(PRODUCTS_FILE);
        updateFoodProductsList();
    }

    public void startProductInfoFlow(Runnable returnToMenu){
        ui.showCategorySelection(category->{
            promptProductID(category,returnToMenu);
        }, returnToMenu);
    }

    private void promptProductID(String category, Runnable returnToMenu){
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
            showProductInfo(category, id, returnToMenu);
        });
    }


    public void showProductInfo(String category, int id, Runnable returnToMenu){
        //Fetch product
        Product product = searchProduct(id);
        FoodProduct foodProduct = searchFoodProduct(id);

        String info;
        if("Accessories".equals(category)){
            info = (product==null || product.toString()==null) ?
                    "Product information does not exist or was not found" : product.toString();
        } else {
            if(foodProduct == null || foodProduct.toString() == null){
                info = "Product information does not exist or was not found";
            } else{
                info = foodProduct.toString();{
                    if(foodProduct.nutrientTable!=null){
                        info += "\n" +  foodProduct.nutrientTable.toString();
                    }
                }
            }
        }
        ui.showDialog(info, returnToMenu);
    }

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

    public void removeProduct(int id){
        products.removeIf(product -> product.articleID == id);
        foodProducts.removeIf(foodProduct -> foodProduct.articleID == id);
    }
    private void increaseStock (int id, int quantity){
        searchProduct(id).stock += quantity;
    }

    public void userIncreaseStock(int id, Runnable onSuccess, Runnable onFailure){
        if (id==0 || searchProduct(id)==null){
            ui.showError("Product not found");
            onFailure.run();
        }
        else {
            ui.promptInput("Quantity: ", quantityString -> {
                try {
                    int quantity = Integer.parseInt(quantityString);
                    increaseStock(id, quantity);
                    onSuccess.run();
                } catch (NumberFormatException e) {
                    ui.showError("Invalid quantity");
                }
            });
        }
    }
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
    public void userDecreaseStock(int id,  Runnable onSuccess, Runnable onFailure){
        if (id==0 || searchProduct(id)==null){
            ui.showError("Product not found");
            onFailure.run();
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
                    onFailure.run();
                }
            });
        }
    }
    public void userPromptID(Consumer<Integer> idAction){
        ui.promptInput("Enter product ID:", inputString ->{
            try{
                int id = Integer.parseInt(inputString);
                if (id < LOWEST_ID){
                    ui.showError("Invalid product ID");
                }
                else{
                    idAction.accept(id);
                }
            } catch (NumberFormatException e){
                ui.showError("Invalid product ID");
            }
        });
    }

}
