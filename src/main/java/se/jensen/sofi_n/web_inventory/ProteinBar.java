package se.jensen.sofi_n.web_inventory;

public class ProteinBar extends FoodProduct{
    public ProteinBar(int articleID) {
        super(articleID);
    }

    @Override
    public String category() {
        return "Protein bars";
    }
}
