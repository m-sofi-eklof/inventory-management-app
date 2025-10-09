package se.jensen.sofi_n.web_inventory;

import java.util.LinkedHashMap;

public abstract class FoodProduct extends Product {
    LinkedHashMap<String, Integer> nutrientTable = new LinkedHashMap<>();

    public FoodProduct(int articleID) {
        super(articleID);
    }

    public void setNutrientTable(LinkedHashMap<String, Integer> nutrientTable) {
        this.nutrientTable = nutrientTable;
    }
}