package se.jensen.sofi_n.web_inventory;


/// The NutrientTableValues class is a helper class intended to contain the nutrient table values as String
/// for a FoodProduct object, to allow for easier passing between the logic class(Inventory) and the front end
/// class(JavaFXUI).
///
public class NutrientTableValues {
    /// Attributes
    String kcalString;
    String fatString;
    String carbsString;
    String proteinString;
    String caffeineString;
    /// Constructor
    public void setStandardValues(String kcalString, String fatString,  String carbsString, String proteinString) {
        this.kcalString = kcalString;
        this.fatString = fatString;
        this.carbsString = carbsString;
        this.proteinString = proteinString;
    }
    /// Caffeine setter
    public void setCaffeineString(String caffeineString) {
        this.caffeineString = caffeineString;
    }
}
