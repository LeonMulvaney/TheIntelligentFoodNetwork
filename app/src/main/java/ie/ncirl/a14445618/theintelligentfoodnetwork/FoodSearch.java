package ie.ncirl.a14445618.theintelligentfoodnetwork;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.rapidapi.rapidconnect.Argument;
import com.rapidapi.rapidconnect.RapidApiConnect;
import com.squareup.picasso.Picasso;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;


//RapidAPI Documentation From: https://docs.rapidapi.com/docs/java-android
public class FoodSearch extends AppCompatActivity{
    private static final String TAG = "";
    String searchString;
    EditText searchEditText;
    Map<String, Object> response;

    ListView resultListView;
    FoodSearchAdapter adapter;
    ArrayList<FoodSearchItem> searchResultList;
    ImageView foodSearchIv;
    ScrollView scrollView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_search);
        setTitle(R.string.food_search_action_bar_string);
        //Add Back Button to Action Bar - From https://stackoverflow.com/questions/12070744/add-back-button-to-action-bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        searchEditText = findViewById(R.id.searchET);
        foodSearchIv = findViewById(R.id.foodSearchImage);

        resultListView = findViewById(R.id.resultListView);
        searchResultList = new ArrayList<>();
        adapter = new FoodSearchAdapter(this,searchResultList);
        scrollView = findViewById(R.id.foodSearchScrollView);

        searchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    searchString = searchEditText.getText().toString();
                    searchEditText.setText("");
                    searchEditText.clearFocus(); //Clear Focus From: https://stackoverflow.com/questions/5056734/android-force-edittext-to-remove-focus
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(searchEditText.getWindowToken(), 0);
                    try {
                        response = new CallFoodSearchApi().execute(searchString).get();
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

        /*aSyncTaskBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                searchString = searchEditText.getText().toString();
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(searchEditText.getWindowToken(), 0);
                try {
                    response = new CallFoodSearchApi().execute(searchString).get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                setData();
            }
        });*/

    }

    //Function to return to home when back button is pressed From --> Same link as "Add Back Button" above
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    /*public void searchUsingAPI(){
       // searchString = searchEditText.getText().toString();
        Thread thread;

        if(StringUtils.isBlank(searchString)){         //How to check if is empty or whitespace - Using Apache Commons Library From: https://stackoverflow.com/questions/3247067/how-do-i-check-that-a-java-string-is-not-all-whitespaces
            Toast.makeText(this,"Please enter a search parameter",Toast.LENGTH_SHORT).show();
        }
        else{
            //Solving Android Thread Issues From: https://stackoverflow.com/questions/6343166/how-do-i-fix-android-os-networkonmainthreadexception
                thread = new Thread(new Runnable() { //Instead of using Async Task --> Open another thread
                @Override
                public void run() {
                    try  {
                        try {
                            RapidApiConnect connect = new RapidApiConnect("leonrapidapi_5a6ddd31e4b04737db92df77", "dd0794ae-5f4e-408d-a19e-cae245e00273");
                            Map<String, Argument> body = new HashMap<>();

                            body.put("applicationSecret", new Argument("data", "ca779495f21bc56c5feb950103517992"));
                            body.put("applicationId", new Argument("data", "8e2110d7"));
                            body.put("foodDescription", new Argument("data", searchString));

                            response = connect.call("Nutritionix", "getFoodsNutrients", body);
                            if(response.get("success") != null) { //If Success is not null (Not
                                System.out.println("Successful API Call--------------------");
                                System.out.println(response);
                                //setData();// This sub thread cannot alter the main threads views - to combat this, it simply calls another method called setData()



                            } else{ //If success is anything but null
                                System.out.println("Else Statement--------------------");

                            }
                        } catch(Exception e){ //Catch Clause
                            System.out.println("Catch Clause--------------------");
                            Log.d(TAG, "searchUsingAPI: " + e);

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            thread.start();
            while(thread.isAlive()){

            }

            setData();
        }

    }*/


    public void setData(){
        ArrayList<Object> foods = (ArrayList<Object>) response.get("success");
        Map<Object,Object> contents = (Map<Object, Object>) foods.get(0);
        ArrayList<Object> item = (ArrayList<Object>) contents.get("foods");
        Map<Object,Object> foodItem = (Map<Object, Object>) item.get(0);

        Map<Object,String> imageUrlObj = (Map<Object, String>) foodItem.get("photo");
        String imageUrl = imageUrlObj.get("thumb");

        String food_name = (String) foodItem.get("food_name");
        double serving_qty = (Double) foodItem.get("serving_qty");
        String serving_unit= (String) foodItem.get("serving_unit");
        double serving_weight_grams = (Double) foodItem.get("serving_weight_grams");
        double calories = (Double) foodItem.get("nf_calories");
        double total_fat = (Double) foodItem.get("nf_total_fat");
        double saturated_fat = (Double) foodItem.get("nf_saturated_fat");
        double cholesterol = (Double) foodItem.get("nf_cholesterol");
        double sodium = (Double) foodItem.get("nf_sodium");
        double total_carbohydrate = (Double) foodItem.get("nf_total_carbohydrate");
        double fibre = (Double) foodItem.get("nf_dietary_fiber");
        double sugars = (Double) foodItem.get("nf_sugars");
        double protein = (Double) foodItem.get("nf_protein");
        double potassium = (Double) foodItem.get("nf_potassium");

        //Loading an image from a URL From: https://stackoverflow.com/questions/2471935/how-to-load-an-imageview-by-url-in-android
        Picasso.with(FoodSearch.this).load(imageUrl).into(foodSearchIv);


        FoodSearchItem foodSearchItem = new FoodSearchItem(food_name,serving_qty,serving_unit,serving_weight_grams,
        calories,total_fat,saturated_fat,cholesterol,sodium,total_carbohydrate,fibre,sugars,protein,potassium);
        searchResultList.clear();
        searchResultList.add(foodSearchItem);
        resultListView.setAdapter(null);
        resultListView.setAdapter(adapter);




       /*searchResultTv.setText(food_name + ":\n" +
                                "Serving Quantity: " + serving_qty + "\n" +
                                "Serving Unit: " + serving_unit + "\n" +
                                "Serving Weight (g): " + serving_weight_grams + "\n" +
                                "Calories: " + calories + "\n" +
                                "Total Fat: " + total_fat + "\n" +
                                "Saturated Fat: " + saturated_fat + "\n" +
                                "Cholesterol: " + cholesterol + "\n");*/


    }





}
