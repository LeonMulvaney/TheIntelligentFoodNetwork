package ie.ncirl.a14445618.theintelligentfoodnetwork;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.concurrent.ExecutionException;

//Http Request From: https://medium.com/@JasonCromer/android-asynctask-http-request-tutorial-6b429d833e28
public class RecipesFromFoodContents extends AppCompatActivity {
    String foodType;
    ListView recipesListView;
    TextView resultTv;
    String myUrl;
    String result;
    String chopped;
    HttpGetRequest getRequest = new HttpGetRequest();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes_from_food_contents);
        //Pass String from one Activity to another From: https://stackoverflow.com/questions/6707900/pass-a-string-from-one-activity-to-another-activity-in-android
        Intent intent = getIntent();
        foodType = intent.getStringExtra("foodType");

        setTitle("Recipes: " + foodType);
        //Add Back Button to Action Bar - From https://stackoverflow.com/questions/12070744/add-back-button-to-action-bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recipesListView = findViewById(R.id.recipeListView);
        resultTv = findViewById(R.id.resultTv);

        myUrl = "https://spoonacular-recipe-food-nutrition-v1.p.mashape.com/recipes/findByIngredients?fillIngredients=false&ingredients="+foodType+"&limitLicense=false&number=5&ranking=1";

        try {
            result = getRequest.execute(myUrl).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        //Substring of String From: https://stackoverflow.com/questions/8846173/how-to-remove-first-and-last-character-of-a-string/31896180
        chopped = StringUtils.substringBetween(result,"[","]");
        resultTv.setText(chopped);
        

    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
