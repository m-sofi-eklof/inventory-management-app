package se.jensen.sofi_n.web_inventory;

/// The ProteinBar class is intended for creating a product object of the type ProteinBar. The class inherits from
/// FoodProduct and contains all of its supers attributes and functions as well as an override on the String category()
/// funtion, returning the String "Protein bars".
///
public class ProteinBar extends FoodProduct{
    /// Constructor
    public ProteinBar(int articleID) {
        super(articleID);
    }

    /// Category override
    @Override
    public String category() {
        return "Protein bars";
    }
}
