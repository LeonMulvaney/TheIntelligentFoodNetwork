package ie.ncirl.a14445618.theintelligentfoodnetwork;

/**
 * Created by Leon on 05/02/2018.
 */

public class ModelNutrientsResult {
    private String imgUrl;
    private String food_name;
    private String serving_qty;
    private String serving_unit;
    private String serving_weight_grams;
    private String calories;
    private String total_fat;
    private String saturated_fat;
    private String cholesterol;
    private String sodium;
    private String total_carbohydrate;
    private String fibre;
    private String sugars;
    private String protein;
    private String potassium;

    public ModelNutrientsResult() {
    }

    public ModelNutrientsResult(String imgUrl, String food_name, String serving_qty, String serving_unit, String serving_weight_grams, String calories, String total_fat, String saturated_fat, String cholesterol, String sodium, String total_carbohydrate, String fibre, String sugars, String protein, String potassium) {
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

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getFood_name() {
        return food_name;
    }

    public void setFood_name(String food_name) {
        this.food_name = food_name;
    }

    public String getServing_qty() {
        return serving_qty;
    }

    public void setServing_qty(String serving_qty) {
        this.serving_qty = serving_qty;
    }

    public String getServing_unit() {
        return serving_unit;
    }

    public void setServing_unit(String serving_unit) {
        this.serving_unit = serving_unit;
    }

    public String getServing_weight_grams() {
        return serving_weight_grams;
    }

    public void setServing_weight_grams(String serving_weight_grams) {
        this.serving_weight_grams = serving_weight_grams;
    }

    public String getCalories() {
        return calories;
    }

    public void setCalories(String calories) {
        this.calories = calories;
    }

    public String getTotal_fat() {
        return total_fat;
    }

    public void setTotal_fat(String total_fat) {
        this.total_fat = total_fat;
    }

    public String getSaturated_fat() {
        return saturated_fat;
    }

    public void setSaturated_fat(String saturated_fat) {
        this.saturated_fat = saturated_fat;
    }

    public String getCholesterol() {
        return cholesterol;
    }

    public void setCholesterol(String cholesterol) {
        this.cholesterol = cholesterol;
    }

    public String getSodium() {
        return sodium;
    }

    public void setSodium(String sodium) {
        this.sodium = sodium;
    }

    public String getTotal_carbohydrate() {
        return total_carbohydrate;
    }

    public void setTotal_carbohydrate(String total_carbohydrate) {
        this.total_carbohydrate = total_carbohydrate;
    }

    public String getFibre() {
        return fibre;
    }

    public void setFibre(String fibre) {
        this.fibre = fibre;
    }

    public String getSugars() {
        return sugars;
    }

    public void setSugars(String sugars) {
        this.sugars = sugars;
    }

    public String getProtein() {
        return protein;
    }

    public void setProtein(String protein) {
        this.protein = protein;
    }

    public String getPotassium() {
        return potassium;
    }

    public void setPotassium(String potassium) {
        this.potassium = potassium;
    }
}



