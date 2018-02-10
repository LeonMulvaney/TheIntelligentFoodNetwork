package ie.ncirl.a14445618.theintelligentfoodnetwork;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.List;

public class RecipesFromFoodContents extends AppCompatActivity {
    String foodType;
    ListView recipesListView;

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




    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
