package ie.ncirl.a14445618.theintelligentfoodnetwork;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
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
    JSONObject jsonObject;
    HttpGetRequest getRequest = new HttpGetRequest();
    JSONArray array;

    ArrayList<RecipeFromIngredientModel> recipesList = new ArrayList<>();
    ListView recipeListView;
    RecipesByIngredientsAdapter adapter;

    int recipeId;
    String recipeTitle;


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

        //Get Data From API
        myUrl = "https://spoonacular-recipe-food-nutrition-v1.p.mashape.com/recipes/findByIngredients?fillIngredients=false&ingredients="+foodType+"&limitLicense=false&number=10&ranking=1";
        try {
            result = getRequest.execute(myUrl).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        //Substring of String From: https://stackoverflow.com/questions/8846173/how-to-remove-first-and-last-character-of-a-string/31896180
        chopped = StringUtils.substringBetween(result,"[","]");

        //Convert String to JSON in Java From: https://stackoverflow.com/questions/35722646/how-to-read-json-string-in-java

        try {
            array = new JSONArray(result); //Create JSON Array
        } catch (JSONException e) {
            e.printStackTrace();
        }

        for(int i=0; i<array.length(); i++){
            try {
            JSONObject jsonObj;
                jsonObj = array.getJSONObject(i);
                int id = (int) jsonObj.get("id");
                String title = (String) jsonObj.get("title");
                String imageUrl = (String) jsonObj.get("image");
                RecipeFromIngredientModel recipe = new RecipeFromIngredientModel(id,title,imageUrl);
                recipesList.add(recipe);
                /*System.out.println("-------------------------------------------");
                System.out.println(recipe.getId());
                System.out.println(recipe.getTitle());
                System.out.println(recipe.getImageUrl());
                System.out.println("-------------------------------------------");*/
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        //Set Data in View
        recipeListView = findViewById(R.id.recipeListView);
        adapter = new RecipesByIngredientsAdapter(this,recipesList);
        recipeListView.setAdapter(adapter);

        recipeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() { // Wait to see what element the user clicks on in the ListView
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                RecipeFromIngredientModel recipe = recipesList.get(position); //Use original list as not filtered
                recipeId = recipe.getId();
                recipeTitle = recipe.getTitle();

                openRecipeDetails();

            }
        }); //End of listView onClickListener


    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    public void openRecipeDetails(){
        Intent intent = new Intent(this,RecipeDetails.class);
        String id = Integer.toString(recipeId);
        intent.putExtra("recipeId",id); //Pass String from one Activity to another From: https://stackoverflow.com/questions/6707900/pass-a-string-from-one-activity-to-another-activity-in-android
        startActivity(intent);
    }


}
