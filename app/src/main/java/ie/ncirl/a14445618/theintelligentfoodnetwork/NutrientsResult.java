package ie.ncirl.a14445618.theintelligentfoodnetwork;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class NutrientsResult extends AppCompatActivity {
    String foodType;
    Map<String, Object> response;
    ListView resultListView;
    AdapterNutrientsResult adapter;
    ArrayList<ModelNutrientsResult> searchResultList;
    ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nutrients_result);

        //Pass String from one Activity to another From: https://stackoverflow.com/questions/6707900/pass-a-string-from-one-activity-to-another-activity-in-android
        Intent intent = getIntent();
        foodType = intent.getStringExtra("foodType");

        //Change Action Bar Title From: https://stackoverflow.com/questions/3438276/how-to-change-the-text-on-the-action-bar
        setTitle("Nutitional Values: " + foodType);
        //Add Back Button to Action Bar - From https://stackoverflow.com/questions/12070744/add-back-button-to-action-bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        resultListView = findViewById(R.id.resultListView);
        searchResultList = new ArrayList<>();
        adapter = new AdapterNutrientsResult(this, searchResultList);
        scrollView = findViewById(R.id.foodSearchScrollView);

        try {
            response = new GetNutrientsApi().execute(foodType).get();
            setData();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

        //Function to return to home when back button is pressed From --> Same link as "Add Back Button" above
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    public void setData(){
        ArrayList<Object> foods = (ArrayList<Object>) response.get("success");
        Map<Object,Object> contents = (Map<Object, Object>) foods.get(0);
        ArrayList<Object> item = (ArrayList<Object>) contents.get("foods");
        Map<Object,Object> foodItem = (Map<Object, Object>) item.get(0);

        Map<Object,String> imageUrlObj = (Map<Object, String>) foodItem.get("photo");
        String imageUrl = imageUrlObj.get("thumb");

        String food_name = foodItem.get("food_name")+"";
        String serving_qty = foodItem.get("serving_qty")+"";
        String serving_unit= foodItem.get("serving_unit")+"";
        String serving_weight_grams = foodItem.get("serving_weight_grams")+"";
        String calories = foodItem.get("nf_calories")+"";
        String total_fat = foodItem.get("nf_total_fat")+"";
        String saturated_fat = foodItem.get("nf_saturated_fat")+"";
        String cholesterol = foodItem.get("nf_cholesterol")+"";
        String sodium = foodItem.get("nf_sodium")+"";
        String total_carbohydrate = foodItem.get("nf_total_carbohydrate")+"";
        String fibre = foodItem.get("nf_dietary_fiber")+"";
        String sugars = foodItem.get("nf_sugars") + "";
        String protein = foodItem.get("nf_protein")+"";
        String potassium = foodItem.get("nf_potassium")+"";



        ModelNutrientsResult modelNutrientsResult = new ModelNutrientsResult(imageUrl,food_name,serving_qty,serving_unit,serving_weight_grams,
                calories,total_fat,saturated_fat,cholesterol,sodium,total_carbohydrate,fibre,sugars,protein,potassium);
        searchResultList.clear();
        searchResultList.add(modelNutrientsResult);
        resultListView.setAdapter(null);
        resultListView.setAdapter(adapter);
    }

}
