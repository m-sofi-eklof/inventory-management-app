package se.jensen.sofi_n.web_inventory;

public class NutrientTableValues {
    String kcalString;
    String fatString;
    String carbsString;
    String proteinString;
    String caffeineString;

    public void setStandardValues(String kcalString, String fatString,  String carbsString, String proteinString) {
        this.kcalString = kcalString;
        this.fatString = fatString;
        this.carbsString = carbsString;
        this.proteinString = proteinString;
    }
    public void setCaffeineString(String caffeineString) {
        this.caffeineString = caffeineString;
    }
}
