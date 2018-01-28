package ie.ncirl.a14445618.theintelligentfoodnetwork;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.rapidapi.rapidconnect.Argument;
import com.rapidapi.rapidconnect.RapidApiConnect;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Text;

import java.io.Console;
import java.util.HashMap;
import java.util.Map;

//RapidAPI Documentation From: https://docs.rapidapi.com/docs/java-android
public class FoodSearch extends AppCompatActivity {

    RapidApiConnect connect = new RapidApiConnect("leonrapidapi_5a6ddd31e4b04737db92df77", "dd0794ae-5f4e-408d-a19e-cae245e00273");
    Map<String, Argument> body = new HashMap<>();
    String searchString;

    EditText searchEditText;
    Button searchButton;
    TextView searchResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_search);

        body.put("applicationSecret", new Argument("data", "ca779495f21bc56c5feb950103517992"));
        body.put("applicationId", new Argument("data", "8e2110d7"));
        body.put("foodDescription", new Argument("data", "Chicken"));


        searchEditText = findViewById(R.id.searchET);
        searchButton = findViewById(R.id.searchBtn);
        searchResult = findViewById(R.id.searchResultTV);

        searchButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                searchUsingAPI();
            }
        });
    }

    public void searchUsingAPI(){
        searchString = searchEditText.getText().toString();

        //How to check if is empty or whitespace - Using Apache Commons Library From: https://stackoverflow.com/questions/3247067/how-do-i-check-that-a-java-string-is-not-all-whitespaces
        if(StringUtils.isBlank(searchString)){
            Toast.makeText(this,"Please enter a search parameter",Toast.LENGTH_SHORT).show();
        }

        else{
            try {
                Map<String, Object> response = connect.call("Nutritionix", "getFoodsNutrients", body);
                if(response.get("success") != null) { //If Success is not null (Not
                    System.out.println("Successful API Call--------------------");

                } else{ //If success is anything but null
                    System.out.println("Else Statement--------------------");

                }
            } catch(Exception e){ //Catch Clause
                System.out.println("Catch Clause--------------------");


            }

            Toast.makeText(this,"You Searched for: " + searchString, Toast.LENGTH_SHORT).show();
        }
    }








}
