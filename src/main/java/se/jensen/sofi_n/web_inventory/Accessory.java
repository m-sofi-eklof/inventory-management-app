package se.jensen.sofi_n.web_inventory;

/// The Accessory class is intended for creating a product object of the type Accessory. The class inherits from
/// Product and contains all of its supers attributes and functions as well as an override on the String category()
/// funtion, returning the String "Accessories".
///
public class Accessory extends Product {

    /// Constructor
    public Accessory(int articleID){
        super(articleID);
    }

    /// category override
    @Override
    public String category() {
        return "Accessories";
    }
}
