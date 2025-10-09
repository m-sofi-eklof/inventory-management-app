package se.jensen.sofi_n.web_inventory;

public class ProteinPowder extends FoodProduct {
    public ProteinPowder(int articleID) {
        super(articleID);
    }

    @Override
    public String category(){
        return "Protein powders";
    }
}
