package ie.ncirl.a14445618.theintelligentfoodnetwork;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.rapidapi.rapidconnect.Argument;
import com.rapidapi.rapidconnect.RapidApiConnect;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.Console;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;


//RapidAPI Documentation From: https://docs.rapidapi.com/docs/java-android
public class FoodSearch extends AppCompatActivity{
    private static final String TAG = "";
    String searchString;
    EditText searchEditText;
    Button searchButton;
    TextView searchResultTv;
    Map<String, Object> response;
    String responseToString = "null";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_search);


        searchEditText = findViewById(R.id.searchET);
        searchButton = findViewById(R.id.searchBtn);
        searchResultTv = findViewById(R.id.searchResultTV);

        searchButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                searchUsingAPI();
            }
        });
    }

    public void searchUsingAPI(){
        searchString = searchEditText.getText().toString();
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
                                responseToString = response.toString();


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

    }



    public void setData(){
        ArrayList<Object> foods = (ArrayList<Object>) response.get("success");
        Map<Object,Object> contents = (Map<Object, Object>) foods.get(0);
        ArrayList<Object> item = (ArrayList<Object>) contents.get("foods");
        Map<Object,Object> foodItem = (Map<Object, Object>) item.get(0);
        String food_name = (String) foodItem.get("food_name");
        double serving_qty = (Double) foodItem.get("serving_qty");
        String serving_unit= (String) foodItem.get("serving_unit");
        double serving_weight_grams = (Double) foodItem.get("serving_weight_grams");
        double calories = (Double) foodItem.get("nf_calories");
        double total_fat = (Double) foodItem.get("nf_total_fat");
        double saturated_fat = (Double) foodItem.get("nf_saturated_fat");
        double cholesterol = (Double) foodItem.get("nf_cholesterol");




        // Map<Object,Object>contents = (Map<Object, Object>) foods.get("foods");


        //searchResultTv.setText(response.toString());
       //System.out.println("Foods in the array are::::: " + contents.toString());
       searchResultTv.setText(food_name + ":\n" +
                                "Serving Quantity: " + serving_qty + "\n" +
                                "Serving Unit: " + serving_unit + "\n" +
                                "Serving Weight (g): " + serving_weight_grams + "\n" +
                                "Calories: " + calories + "\n" +
                                "Total Fat: " + total_fat + "\n" +
                                "Saturated Fat: " + saturated_fat + "\n" +
                                "Cholesterol: " + cholesterol + "\n");


    }









}
