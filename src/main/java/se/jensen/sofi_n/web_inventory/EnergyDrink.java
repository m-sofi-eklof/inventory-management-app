package se.jensen.sofi_n.web_inventory;

public class EnergyDrink extends FoodProduct {
    public EnergyDrink(int articleID) {
        super(articleID);
    }

    @Override
    public String category(){
        return "Energy drinks";
    }

    //overload
    public void setNutrientTable(int kcal, int fat, int carbs, int protein, int caffeine){
        super.setNutrientTable(kcal, fat, carbs, protein);
        getNutrientTable().put("Caffeine(mg)", caffeine);
    }
}
