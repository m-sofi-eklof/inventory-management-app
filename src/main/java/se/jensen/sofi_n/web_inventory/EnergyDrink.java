package se.jensen.sofi_n.web_inventory;

/// The EnergyDrink class is intended for creating a product object of the type EnergyDrink. The class inherits from
/// FoodProduct and contains all of its supers attributes and functions as well as an override on the String category()
/// funtion, returning the String "EnergyDrinks". It also contains an overload of the setNutrientTable function.
///

public class EnergyDrink extends FoodProduct {

    /// Constructor
    public EnergyDrink(int articleID) {
        super(articleID);
    }

    /// category override
    @Override
    public String category(){
        return "Energy drinks";
    }

    ///  setNutrientTable overload
    public void setNutrientTable(int kcal, int fat, int carbs, int protein, int caffeine){
        super.setNutrientTable(kcal, fat, carbs, protein);
        getNutrientTable().put("Caffeine(mg)", caffeine);
    }
}
