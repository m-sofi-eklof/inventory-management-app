package se.jensen.sofi_n.web_inventory;

/// The ProteinPowder class is intended for creating a product object of the type ProteinPowder. The class inherits from
/// FoodProduct and contains all of its supers attributes and functions as well as an override on the String category()
/// funtion, returning the String "Protein powders".
///

public class ProteinPowder extends FoodProduct {
    /// Constructor
    public ProteinPowder(int articleID) {
        super(articleID);
    }

    /// Category override
    @Override
    public String category(){
        return "Protein powders";
    }
}
