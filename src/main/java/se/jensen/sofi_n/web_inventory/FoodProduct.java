package se.jensen.sofi_n.web_inventory;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;


/// The FoodProduct abstract class inherits from the abstract class Product and contains all functions and attributes
/// of its super as well as a LinkedHashMap<String, Integer> attribute nutrientTable for storing nutrient values,
/// along with functions to set, serialize and deserialize its values.
///
public abstract class FoodProduct extends Product {
    /// Nutrient table attribute
    private LinkedHashMap<String, Integer> nutrientTable = new LinkedHashMap<>();

    /// Constructor
    public FoodProduct(int articleID) {
        super(articleID);
    }

    /// Setter
    public void setNutrientTable(int kcal, int fat, int carbs, int protein ) {
        LinkedHashMap<String, Integer> nutrientTable = new LinkedHashMap<>();
        nutrientTable.put("Calories (kcal)",kcal);
        nutrientTable.put("Fat (g)", fat);
        nutrientTable.put("Carbs (g)", carbs);
        nutrientTable.put("Protein (g)", protein);

        this.nutrientTable = nutrientTable;
    }
    /// Getter
    public LinkedHashMap<String, Integer> getNutrientTable() {
        return nutrientTable;
    }

    /// File handeling for nutrienttable
    /// functions to serialize and deserialize nutrient table values
    ///
    /*The serializeNutrientValues function generates a string with articleID first, then each value from the nutrient
    * table of the Product on which its called, with semicolon in between, and returns that string.*/
    public String serializeNutrientValues(){
        //stringbuilder for easier concats
        StringBuilder returnString = new StringBuilder();
        //article ID as first entry to correctly match nutrient table with product
        returnString.append(articleID);
        // goes through each entry and adds only the value to the String, with semicolon before each value
        for (Map.Entry<String, Integer> entry : getNutrientTable().entrySet()){
            returnString.append(";").append(entry.getValue());
        }
        //return
        return returnString.toString();
    }

    /*The deserializeNutrientValues function takes a String argument line, a list of FoodProduct items and a Consumer
    * String type argument for relaying errors. The line is split in parts on semicolon, the parts are validated,
    * and if the first part(articleID) matches any if the FoodPorducts in the list, and the parts are valid,
    * the FoodProduct's nutrient table is set to the values retrieved in the following parts.*/
    public static void deserializeNutrientValues(String line, List<FoodProduct> foodProducts, Consumer<String> onError){
        //split
        String[] parts = line.split(";");
        //Validating length is 5 or 6 (6 for EnergyDrink objects, 5 for other FoodProduct objects)
        if (parts.length < 5) {
            onError.accept("Missing fields on line" + line);
        }
        if (parts.length > 6){
            onError.accept("Too many fields on line" + line);
        }
        //Validating numberformat and parsing
        try{
            int articleID = Integer.parseInt(parts[0]);
            int kcal = Integer.parseInt(parts[1]);
            int fat = Integer.parseInt(parts[2]);
            int carbs = Integer.parseInt(parts[3]);
            int protein = Integer.parseInt(parts[4]);

            //goes through FoodProduct list
            for (FoodProduct product : foodProducts) {
                //searches for articleID match
                if (product.articleID == articleID) {
                    //handles EnergyDrink objects
                    if (product instanceof EnergyDrink) {
                        if(parts.length == 6){
                            //sets nutrient table
                            int caffeine = Integer.parseInt(parts[5]);
                            ((EnergyDrink) product).setNutrientTable(kcal,fat,carbs,protein,caffeine);
                        }else{
                            onError.accept("Missing fields on line" + line);
                        }
                    }
                    //handles other FoodProduct objects
                    else {
                        if (parts.length == 5) {
                            //sets nutrient table
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