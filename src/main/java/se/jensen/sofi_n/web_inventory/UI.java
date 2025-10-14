package se.jensen.sofi_n.web_inventory;

import java.util.function.Consumer;

/// The UI interface is intended to act as a bridge between the logic in the Inventory class and any user interface
/// running it.
///
public interface UI {

    /* The promptInput function is intended to take a String prompt to display and a Consumer<String> onResult for the
     * desired action for the input String. Returns void. */
    void promptInput(String prompt, Consumer<String> onResult);

    /* The showError function takes a String describing the error and displays it to the user*/
    void showError(String error);

    /* The showTextWithTitle function takes two string arguments, a title and a message, and displays them.*/
    void showTextWithTitle(String title, String message);

    /* The promptProducitDetails function is intended to take an integer for the articeID and a Consumer variable of
     * the type ProductInputDetails (See ProductsInputDetails class) and use user input to create an object of
     * ProductInputDetails then preform the onResult actions passed.*/
    void promptProductDetails(int articleID, Consumer<ProductInputDetails> onResult);

    /*The showCategorySelection function is intended for taking a consumer String type with actions to preform on
     * resulting String as argument. The function should then show the category selection then run passed actions on
     * a String variable representing the selected category.*/
    void showCategorySelection(Consumer<String> onCategorySelected);

    /* The promptNutrientInfo function should take a FoodProduct type object, and a Consumer argument of
     * NutrientTableValues type with actions to perform on resulting NutrientTableValues object. The function should
     * then prompt the user for nutrient information, with an added prompt Caffeine for products of EnergyDrink type,
     * then create an object of the helper class NutrientTableValues and perform the passed action*/
    void promptNutrientInfo(FoodProduct foodProduct, Consumer<NutrientTableValues> onResult);

    /*The displayNutrientInformation function should take a Product type object as argument and display its information
    * to the user.*/
    void displayProductInformation(Product product);

}
