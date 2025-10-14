package se.jensen.sofi_n.web_inventory;


/// The ProductInputDetails class is a helper class intended to contain the name price and description details
/// for a product object, to allow for easier passing between the logic class(Inventory) and the front end class(JavaFXUI)
///
public class ProductInputDetails {
    /// Attributes
    public String name;
    public String priceStr;
    public String description;

    ///Constructor
    public ProductInputDetails(String name, String priceStr, String description) {
        this.name = name;
        this.priceStr = priceStr;
        this.description = description;
    }
}
