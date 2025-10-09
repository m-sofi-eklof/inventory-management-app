package se.jensen.sofi_n.web_inventory;

public class EnergyDrink extends FoodProduct {
    public EnergyDrink(int articleID) {
        super(articleID);
    }
    public String category(){
        return "Energy drinks";
    }
}
