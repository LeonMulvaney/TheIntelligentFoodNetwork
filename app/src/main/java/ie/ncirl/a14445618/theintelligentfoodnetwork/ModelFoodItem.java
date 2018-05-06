package ie.ncirl.a14445618.theintelligentfoodnetwork;

import com.google.firebase.database.DatabaseReference;

import java.util.Date;

/**
 * Created by Leon on 08/11/2017.
 */

//Source Ideas From: https://firebase.google.com/docs/database/android/read-and-write
public class ModelFoodItem {
    public String foodType;
    public String expiryDate;
    public String category;

    public ModelFoodItem(){

    }

    public ModelFoodItem(String foodType, String expiryDate,String category) {
        this.foodType = foodType;
        this.expiryDate = expiryDate;
        this.category = category;
    }

    public String getFoodType() {
        return foodType;
    }

    public void setFoodType(String foodType) {
        this.foodType = foodType;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }


    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }



}


