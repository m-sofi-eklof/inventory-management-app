package se.jensen.sofi_n.web_inventory;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public abstract class FoodProduct extends Product {
    private LinkedHashMap<String, Integer> nutrientTable = new LinkedHashMap<>();

    public FoodProduct(int articleID) {
        super(articleID);
    }

    public void setNutrientTable(int kcal, int fat, int carbs, int protein ) {
        LinkedHashMap<String, Integer> nutrientTable = new LinkedHashMap<>();
        nutrientTable.put("Calories (kcal)",kcal);
        nutrientTable.put("Fat (g)", fat);
        nutrientTable.put("Carbs (g)", carbs);
        nutrientTable.put("Protein (g)", protein);

        this.nutrientTable = nutrientTable;
    }
    public LinkedHashMap<String, Integer> getNutrientTable() {
        return nutrientTable;
    }

    public String serializeNutrientValues(){
        StringBuilder returnString = new StringBuilder();
        returnString.append(articleID);
        for (Map.Entry<String, Integer> entry : getNutrientTable().entrySet()){
            returnString.append(";").append(entry.getValue());
        }
        return returnString.toString();
    }
    public static void deserializeNutrientValues(String line, List<FoodProduct> foodProducts, Consumer<String> onError){
        String[] parts = line.split(";");
        if (parts.length < 5) {
            onError.accept("Missing fields on line" + line);
        }
        if (parts.length > 6){
            onError.accept("Too many fields on line" + line);
        }
        try{
            int articleID = Integer.parseInt(parts[0]);
            int kcal = Integer.parseInt(parts[1]);
            int fat = Integer.parseInt(parts[2]);
            int carbs = Integer.parseInt(parts[3]);
            int protein = Integer.parseInt(parts[4]);

            for (FoodProduct product : foodProducts) {
                if (product.articleID == articleID) {
                    if (product instanceof EnergyDrink) {
                        if(parts.length == 6){
                            int caffeine = Integer.parseInt(parts[5]);
                            ((EnergyDrink) product).setNutrientTable(kcal,fat,carbs,protein,caffeine);
                        }else{
                            onError.accept("Missing fields on line" + line);
                        }
                    }
                    else {
                        if (parts.length == 5) {
                            product.setNutrientTable(kcal,fat,carbs,protein);
                        }else{
                            onError.accept("Too many fields on line" + line);
                        }
                    }
                }
            }
        }catch (NumberFormatException e){
            onError.accept("Incorrect format on line" + line);
        }
    }
}