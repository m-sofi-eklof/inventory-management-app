package se.jensen.sofi_n.web_inventory;

import java.util.function.Consumer;

public interface UI {
    void showTextWithTitle(String title, String message);
    void promptInput(String prompt, Consumer<String> onResult);
    void showCategorySelection(Consumer<String> onCategorySelected);
    void showError(String error);
    void displayProductInformation(Product product);
    void promptNutrientInfo(FoodProduct foodProduct, Consumer<NutrientTableValues> onResult);
    void promptProductDetails(int articleID, Consumer<ProductInputDetails> onResult);
}
