package ie.ncirl.a14445618.theintelligentfoodnetwork;

import java.util.ArrayList;

/**
 * Created by Leon on 24/02/2018.
 */

public class IngredientModel {
    private String originalString;
    private double amount;
    private String unit;
    private String ingredientName;



    public IngredientModel() {
    }

    public IngredientModel(String originalString, double amount, String unit, String ingredientName) {
        this.originalString = originalString;
        this.amount = amount;
        this.unit = unit;
        this.ingredientName = ingredientName;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getIngredientName() {
        return ingredientName;
    }

    public void setIngredientName(String ingredientName) {
        this.ingredientName = ingredientName;
    }

    public String getOriginalString() {
        return originalString;
    }

    public void setOriginalString(String originalString) {
        this.originalString = originalString;
    }
}
