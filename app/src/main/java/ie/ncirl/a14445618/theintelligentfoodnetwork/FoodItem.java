package ie.ncirl.a14445618.theintelligentfoodnetwork;

import com.google.firebase.database.DatabaseReference;

import java.util.Date;

/**
 * Created by Leon on 08/11/2017.
 */

//Source Ideas From: https://firebase.google.com/docs/database/android/read-and-write
public class FoodItem {
    public String foodType;
    public String expiryDate;
    public String calories;
    public String protein;


    public FoodItem(){

    }

    public FoodItem(String foodType,String expiryDate, String calories, String protein) {
        this.foodType = foodType;
        this.expiryDate = expiryDate;
        this.calories = calories;
        this.protein = protein;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getCalories() {
        return calories;
    }

    public String getFoodType() {
        return foodType;
    }

    public void setFoodType(String foodType) {
        this.foodType = foodType;
    }

    public void setCalories(String calories) {
        this.calories = calories;
    }

    public String getProtein() {
        return protein;
    }

    public void setProtein(String protein) {
        this.protein = protein;
    }


    //Allow ListView to get elements from Array of Objects into String From: https://stackoverflow.com/questions/16937347/populating-listview-with-arraylistobject
    //This method now redundant as Custom Adapter is implemented
   /* public String toString(){
        String string = this.foodType + "\n" +
                     "Expiry Date: " + this.expiryDate+ "\n" +
                     "Calories: " + this.calories+ "\n" +
                     "Protein(grams): " + this.protein;
        return string;
    }*/


}


