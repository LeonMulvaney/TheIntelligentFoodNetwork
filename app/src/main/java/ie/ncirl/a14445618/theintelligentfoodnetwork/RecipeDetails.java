package ie.ncirl.a14445618.theintelligentfoodnetwork;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class RecipeDetails extends AppCompatActivity {
    String myUrl;
    String result;
    String recipeId;
    RecipeFromIdApi getRequest = new RecipeFromIdApi();
    JSONObject object;

    String title;
    String instructions;
    String imageUrl;

    ImageView recipeImage;
    TextView recipeTitleTv;
    TextView recipeInstructionsTv;
    String spoonacularSourceUrl;

    Button shareRecipeBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

        Intent intent = getIntent();
        recipeId = intent.getStringExtra("recipeId");

        setTitle("Recipe Details");
        //Add Back Button to Action Bar - From https://stackoverflow.com/questions/12070744/add-back-button-to-action-bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


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

        } catch (JSONException e) {
            e.printStackTrace();
        }


        recipeImage = findViewById(R.id.recipeImage);
        recipeTitleTv = findViewById(R.id.recipeTitle);
        recipeInstructionsTv = findViewById(R.id.recipeInstructions);

        Picasso.with(RecipeDetails.this).load(imageUrl).into(recipeImage);
        recipeTitleTv.setText(title);
        recipeInstructionsTv.setText(instructions);
        System.out.println(result);


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
