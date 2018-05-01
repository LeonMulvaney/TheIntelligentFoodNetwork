package ie.ncirl.a14445618.theintelligentfoodnetwork;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ExecutionException;


//RapidAPI Documentation From: https://docs.rapidapi.com/docs/java-android
public class FoodSearch extends AppCompatActivity{
    private static final String TAG = "";
    String searchString;
    EditText searchEditText;
    Map<String, Object> response;

    ListView resultListView;
    AdapterNutrientsResult adapter;
    ArrayList<ModelNutrientsResult> searchResultList;
    ScrollView scrollView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_search);
        setTitle(R.string.food_search_action_bar_string);
        //Add Back Button to Action Bar - From https://stackoverflow.com/questions/12070744/add-back-button-to-action-bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        searchEditText = findViewById(R.id.searchET);

        resultListView = findViewById(R.id.resultListView);
        searchResultList = new ArrayList<>();
        adapter = new AdapterNutrientsResult(this, searchResultList);
        scrollView = findViewById(R.id.foodSearchScrollView);

        searchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    searchString = searchEditText.getText().toString();
                    searchEditText.setText("");
                    searchEditText.clearFocus(); //Clear Focus From: https://stackoverflow.com/questions/5056734/android-force-edittext-to-remove-focus
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(searchEditText.getWindowToken(), 0);
                    try {
                        response = new GetNutrientsApi().execute(searchString).get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                    setData();
                    return true;
                }
                return false;
            }
        });
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

        //Issues - API Returning Null values - Add "" to each variable to cast it to a string
        //From: https://stackoverflow.com/questions/13973233/convert-null-object-to-string

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
