package se.jensen.sofi_n.web_inventory;

public class ProductInputDetails {
    public String name;
    public String priceStr;
    public String description;

    public ProductInputDetails(String name, String priceStr, String description) {
        this.name = name;
        this.priceStr = priceStr;
        this.description = description;
    }
}
