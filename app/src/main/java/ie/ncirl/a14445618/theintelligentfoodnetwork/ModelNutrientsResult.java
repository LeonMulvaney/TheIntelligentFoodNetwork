package ie.ncirl.a14445618.theintelligentfoodnetwork;

/**
 * Created by Leon on 05/02/2018.
 */

public class ModelNutrientsResult {
    private String imgUrl;
    private String food_name;
    private double serving_qty;
    private String serving_unit;
    private double serving_weight_grams;
    private double calories;
    private double total_fat;
    private double saturated_fat;
    private double cholesterol;
    private double sodium;
    private double total_carbohydrate;
    private double fibre;
    private double sugars;
    private double protein;
    private double potassium;

    public ModelNutrientsResult() {
    }


    public ModelNutrientsResult(String imgUrl,String food_name, double serving_qty, String serving_unit, double serving_weight_grams, double calories, double total_fat, double saturated_fat, double cholesterol, double sodium, double total_carbohydrate, double fibre, double sugars, double protein, double potassium) {
        this.imgUrl = imgUrl;
        this.food_name = food_name;
        this.serving_qty = serving_qty;
        this.serving_unit = serving_unit;
        this.serving_weight_grams = serving_weight_grams;
        this.calories = calories;
        this.total_fat = total_fat;
        this.saturated_fat = saturated_fat;

        this.cholesterol = cholesterol;
        this.sodium = sodium;
        this.total_carbohydrate = total_carbohydrate;
        this.fibre = fibre;
        this.sugars = sugars;
        this.protein = protein;
        this.potassium = potassium;
    }

    public String getFood_name() {
        return food_name;
    }

    public void setFood_name(String food_name) {
        this.food_name = food_name;
    }

    public double getServing_qty() {
        return serving_qty;
    }

    public void setServing_qty(double serving_qty) {
        this.serving_qty = serving_qty;
    }

    public String getServing_unit() {
        return serving_unit;
    }

    public void setServing_unit(String serving_unit) {
        this.serving_unit = serving_unit;
    }

    public double getServing_weight_grams() {
        return serving_weight_grams;
    }

    public void setServing_weight_grams(double serving_weight_grams) {
        this.serving_weight_grams = serving_weight_grams;
    }

    public double getCalories() {
        return calories;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public void setCalories(double calories) {
        this.calories = calories;
    }

    public double getTotal_fat() {
        return total_fat;
    }

    public void setTotal_fat(double total_fat) {
        this.total_fat = total_fat;
    }

    public double getSaturated_fat() {
        return saturated_fat;
    }

    public void setSaturated_fat(double saturated_fat) {
        this.saturated_fat = saturated_fat;
    }

    public double getCholesterol() {
        return cholesterol;
    }

    public void setCholesterol(double cholesterol) {
        this.cholesterol = cholesterol;
    }

    public double getSodium() {
        return sodium;
    }

    public void setSodium(double sodium) {
        this.sodium = sodium;
    }

    public double getTotal_carbohydrate() {
        return total_carbohydrate;
    }

    public void setTotal_carbohydrate(double total_carbohydrate) {
        this.total_carbohydrate = total_carbohydrate;
    }

    public double getFibre() {
        return fibre;
    }

    public void setFibre(double fibre) {
        this.fibre = fibre;
    }

    public double getSugars() {
        return sugars;
    }

    public void setSugars(double sugars) {
        this.sugars = sugars;
    }

    public double getProtein() {
        return protein;
    }

    public void setProtein(double protein) {
        this.protein = protein;
    }

    public double getPotassium() {
        return potassium;
    }

    public void setPotassium(double potassium) {
        this.potassium = potassium;
    }
}
