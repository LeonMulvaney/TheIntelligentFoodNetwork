package ie.ncirl.a14445618.theintelligentfoodnetwork;

import com.google.firebase.database.DatabaseReference;

import java.util.Date;

/**
 * Created by Leon on 08/11/2017.
 */

public class FoodItem {
    public String category;
    public String calories;
    public String protein;
    public String expiryDate;

    public FoodItem(){

    }

    public FoodItem(String category, String calories, String protein,String expiryDate){
        this.category = category;
        this.calories = calories;
        this.protein = protein;
        this.expiryDate = expiryDate;
    }


}


