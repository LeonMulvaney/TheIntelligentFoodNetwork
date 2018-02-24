package ie.ncirl.a14445618.theintelligentfoodnetwork;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class RecipeDetails extends AppCompatActivity {
    ScrollView recipeDetailsScrollView;
    String myUrl;
    String result;
    String recipeId;
    RecipeFromIdApi getRequest = new RecipeFromIdApi();
    JSONObject object;

    String title;
    String instructions;
    String imageUrl;
    String servings;
    String wwSmartPoints;
    String preparation;
    String cookTime;

    ImageView recipeImage;
    TextView recipeTitleTv;
    String spoonacularSourceUrl;

    ArrayList<IngredientModel> ingredientList;
    IngredientsAdapter adapter;
    NonScrollListView ingredientLv;

    ArrayList<InstructionModel> instructionsList;
    InstructionsAdapter instructionsAdapter;
    NonScrollListView instructionsLv;

    TextView servingsTv;
    TextView wwSmartPointTv;
    TextView preparationTv;
    TextView cookTimeTv;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);


        Intent intent = getIntent();
        recipeId = intent.getStringExtra("recipeId");

        setTitle("Recipe Details");
        //Add Back Button to Action Bar - From https://stackoverflow.com/questions/12070744/add-back-button-to-action-bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ingredientLv = findViewById(R.id.ingredientLv);
        ingredientList = new ArrayList();
        adapter = new IngredientsAdapter(this, ingredientList);

        instructionsList = new ArrayList<>();
        instructionsAdapter = new InstructionsAdapter(this,instructionsList);


        //Get Data From API
        myUrl = "https://spoonacular-recipe-food-nutrition-v1.p.mashape.com/recipes/"+recipeId+"/information";
        try {
            result = getRequest.execute(myUrl).get();
            System.out.println("---------------------------------");
            System.out.println(result);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        try {
            object = new JSONObject(result); //Create JSON Object so values can be retrieved
            title = object.getString("title");
            imageUrl = object.getString("image");
            instructions = object.getString("instructions");
            spoonacularSourceUrl = object.getString("spoonacularSourceUrl");
            servings = object.getString("servings");
            wwSmartPoints = object.getString("weightWatcherSmartPoints");

            if(object.has("preparationMinutes") && object.has("cookingMinutes")){ //Error Handling - Some API Responses contain different data - App was not loading...
                preparation = object.getString("preparationMinutes");                   //...data if it could not find element in JSONObject
                cookTime = object.getString("cookingMinutes");
            }
            else{
                preparation = object.getString("readyInMinutes");
                cookTime = "-";
            }



            //Get Ingredients, Store in Model called IngredientModel, save objects to Arraylist, then parse arraylist to View using ListView Adapter (With custom Layout)
            JSONArray extendedIngredientsArray = object.getJSONArray("extendedIngredients");

            for(int i=0;i<extendedIngredientsArray.length();i++){
                JSONObject object = (JSONObject) extendedIngredientsArray.get(i);
                String originalString = object.getString("originalString");
                double amount = object.getDouble("amount");
                String unit = object.getString("unit");
                String name = object.getString("name");

                IngredientModel ingredientModel = new IngredientModel(originalString,amount,unit,name);
                ingredientList.add(ingredientModel);
            }

            //Get Instructions
            JSONArray analyzedInstructions = object.getJSONArray("analyzedInstructions");
            JSONObject instructionsObject = (JSONObject) analyzedInstructions.get(0);
            JSONArray instructionsObjectArray = (JSONArray) instructionsObject.get("steps");
            for(int i=0;i<instructionsObjectArray.length();i++){
                JSONObject object = (JSONObject) instructionsObjectArray.get(i);
                String stepNumber = object.getString("number");
                String instruction = object.getString("step");
                InstructionModel instructionModel = new InstructionModel(stepNumber,instruction);
                instructionsList.add(instructionModel);
            }




        } catch (JSONException e) {
            e.printStackTrace();
        }


        //Locate all Views to Reference
        recipeImage = findViewById(R.id.recipeImage);
        servingsTv = findViewById(R.id.servingsTv);
        wwSmartPointTv = findViewById(R.id.wwSmartPointsTv);
        preparationTv = findViewById(R.id.preparationTv);
        cookTimeTv = findViewById(R.id.cookTimeTv);
        recipeTitleTv = findViewById(R.id.recipeTitle);
        instructionsLv = findViewById(R.id.instructionsLv);





        recipeTitleTv.setText(title);
        servingsTv.setText(servings);
        wwSmartPointTv.setText(wwSmartPoints);
        preparationTv.setText(preparation);
        cookTimeTv.setText(cookTime);

        ingredientLv.setAdapter(adapter);
        instructionsLv.setAdapter(instructionsAdapter);
        Picasso.with(RecipeDetails.this).load(imageUrl).into(recipeImage);
        System.out.println(result);

        //Page Scrolling to Centre Fix From: https://stackoverflow.com/questions/4119441/how-to-scroll-to-top-of-long-scrollview-layout
        //ingredientLv.setFocusable(false);
        //instructionsLv.setFocusable(false);


    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
    //Adding Share Button to TitleBar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.share_and_back, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.shareRecipe:
                share();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void share(){
        //Share Intent From: https://stackoverflow.com/questions/19683297/how-to-send-message-from-android-app-through-viber-message
        Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
        String recipeData = "Hey, check out this cool recipe from The Intelligent Food Network App: " + spoonacularSourceUrl;
        shareIntent.putExtra(Intent.EXTRA_TEXT, recipeData);
        shareIntent.setType("text/plain");
        startActivity(Intent.createChooser(shareIntent, "Please choose an application to share your recipe on..."));
    }
}
