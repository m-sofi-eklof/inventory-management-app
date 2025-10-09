package se.jensen.sofi_n.web_inventory;

public class Accessory extends Product {

    public Accessory(int articleID){
        super(articleID);
    }

    @Override
    public String category() {
        return "Accessories";
    }
}
