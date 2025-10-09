package se.jensen.sofi_n.web_inventory;

import java.util.function.Consumer;

public interface UI {
    void showDialog(String message, Runnable onBackToMenu);
    void showTextWithTitle(String title, String message);
    void promptInput(String prompt, Consumer<String> onResult);
    void showCategorySelection(Consumer<String> onCategorySelected, Runnable onBackToMenu);
    void showError(String error);

    void promptProductDetails(int id, Consumer<ProductInputDetails> onResult);
}
