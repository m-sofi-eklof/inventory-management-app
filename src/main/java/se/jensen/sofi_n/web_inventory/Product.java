package se.jensen.sofi_n.web_inventory;

import java.util.function.Consumer;

public abstract class Product {
    /// Variables
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
    public abstract String category();

    /// File handling methods
    public String serialize(){
        return getClass().getSimpleName() + ";" + articleID + ";" + name + ";" + price + ";" + description + ";" + stock;
    }

    public static Product deserialize(String line, Consumer<String> onErrorMessage){
        String[] parts = line.split(";");
        if (parts.length < 6) {
            onErrorMessage.accept("Missing fields in line: " + line);
            return null;
        }
        String className = parts[0];
        try {
            int articleID = Integer.parseInt(parts[1]);
            String name = parts[2];

            double price = Double.parseDouble(parts[3]);
            String description = parts[4];

            int stock = Integer.parseInt(parts[5]);
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
                    onErrorMessage.accept("Unknown class: " + className);
                    return null;

            }
        }catch (NumberFormatException e){
                onErrorMessage.accept("Inccorect number format in line: " + line);
                return null;
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
