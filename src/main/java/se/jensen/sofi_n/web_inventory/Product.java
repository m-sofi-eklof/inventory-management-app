package se.jensen.sofi_n.web_inventory;

import java.util.function.Consumer;

/// The abstract class Product contains all the attributes and functions that each inheriting product type should have
/// for this application.
public abstract class Product {
    /// Attributes
    protected int articleID;
    protected String name;
    protected double price;
    protected String description;
    protected int stock=0;

    /// Constructor
    public Product(int articleID) {
        this.articleID = articleID;
    }

    /// Getters Setters
    public int getArticleID() {return articleID;}
    public void setArticleID(int articleID) {this.articleID = articleID;}

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}

    public double getPrice() {return price;}
    public void setPrice(double price) {this.price = price;}

    public int getStock() {return stock;}
    public void setStock(int stock) {this.stock = stock;}

    public String getDescription() {return description;}
    public void setDescription(String description) {this.description = description;}

    /// Abstract Method
    /*The function category returns a String representing the category name*/
    public abstract String category();

    /// File handling methods
    /*The serialize function returns a String with classname, article id, name, price, description and stock, separated
    * by semicolon.*/
    public String serialize(){
        return getClass().getSimpleName() + ";" + articleID + ";" + name + ";" + price + ";" + description + ";" + stock;
    }

    /*The deserialize function takes a String argument consisting of one line of code from the products file
    * splits, iit at the semicolons, verifies the parts and if valid, creates a product using them.
    * The product is then returned as returnvalue. The functions second argument is a Consumer string type
    * with actions to preform on any resulting error-message String.*/
    public static Product deserialize(String line, Consumer<String> onErrorMessage){
        //split into parts
        String[] parts = line.split(";");
        //validate lenght
        if (parts.length < 6) {
            onErrorMessage.accept("Missing fields in line: " + line);
            return null;
        }
        if (parts.length > 6) {
            onErrorMessage.accept("Too many fields in line: " + line);
            return null;
        }
        //validate parts
        String className = parts[0];
        try {
            int articleID = Integer.parseInt(parts[1]);
            String name = parts[2];

            double price = Double.parseDouble(parts[3]);
            String description = parts[4];

            int stock = Integer.parseInt(parts[5]);
            //create product
            switch (className) {
                case "Accessory":
                    Accessory accessory = new Accessory(articleID);
                    accessory.setName(name); accessory.setPrice(price); accessory.setDescription(description); accessory.setStock(stock);
                    return accessory;
                case "EnergyDrink":
                    EnergyDrink eDrink = new EnergyDrink(articleID);
                    eDrink.setName(name); eDrink.setPrice(price); eDrink.setDescription(description); eDrink.setStock(stock);
                    return eDrink;
                case "ProteinBar":
                    ProteinBar bar = new ProteinBar(articleID);
                    bar.setName(name); bar.setPrice(price); bar.setDescription(description); bar.setStock(stock);
                    return bar;
                case "ProteinPowder":
                    ProteinPowder protein = new ProteinPowder(articleID);
                    protein.setName(name); protein.setPrice(price); protein.setDescription(description); protein.setStock(stock);
                    return protein;
                default:
                    //unknown type error
                    onErrorMessage.accept("Unknown class: " + className);
                    return null;

            }
        //numberformat error
        }catch (NumberFormatException e){
                onErrorMessage.accept("Inccorect number format in line: " + line);
                return null;
        //other error
        }catch (Exception e){
                onErrorMessage.accept("Error reading line: " + line);
                return null;
        }
    }

    /// toString override
    @Override
    public String toString() {
        String str = "{";
        str += "articleID=" + articleID;
        if (name != null) {
            str += ", name=" + name;
        }
        if (price != 0.0) {
            str += ", price=" + price;
        }
        if (description != null) {
            str += ", description=" + description;
        }
        str += ", stock=" + stock;
        str += "}";
        return str;
    }
}
