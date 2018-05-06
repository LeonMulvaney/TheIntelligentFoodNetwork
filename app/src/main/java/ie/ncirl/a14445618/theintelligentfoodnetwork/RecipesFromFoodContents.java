package ie.ncirl.a14445618.theintelligentfoodnetwork;

import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

//Http Request From: https://medium.com/@JasonCromer/android-asynctask-http-request-tutorial-6b429d833e28
public class RecipesFromFoodContents extends AppCompatActivity {

    //Hamburger Menu
    private ListView mDrawerList;
    private ArrayAdapter<String> mAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;

    String foodType;
    ListView recipesListView;
    TextView resultTv;
    String myUrl;
    String result;

    JSONObject jsonObject;
    GetRecipesByIngredientApi getRecipesByIngredientRequest = new GetRecipesByIngredientApi();
    JSONArray array;

    ArrayList<ModelRecipeFromIngredient> recipesList = new ArrayList<>();
    ListView recipeListView;
    AdapterRecipesByIngredients adapter;

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
            result = getRecipesByIngredientRequest.execute(myUrl).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
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
                ModelRecipeFromIngredient recipe = new ModelRecipeFromIngredient(id,title,imageUrl);
                recipesList.add(recipe);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        //Set Data in View
        recipeListView = findViewById(R.id.recipeListView);
        adapter = new AdapterRecipesByIngredients(this,recipesList);
        recipeListView.setAdapter(adapter);

        recipeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() { // Wait to see what element the user clicks on in the ListView
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                ModelRecipeFromIngredient recipe = recipesList.get(position); //Use original list as not filtered
                recipeId = recipe.getId();
                recipeTitle = recipe.getTitle();

                openRecipeDetails();

            }
        }); //End of listView onClickListener
        //Hamburger Menu ------------------------------------------------
        mDrawerList = findViewById(R.id.navList);
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(id == 0){
                    finish();
                    openHome();
                }
                else if(id == 1){
                    finish();
                    openFoodContents();
                }
                else if(id == 2){
                    finish();
                    openShoppping();
                }
                else if(id == 3){
                    finish();
                    openRecipes();
                }
                else if(id == 4){
                    finish();
                    openNutrientsSearch();
                }
                else{
                    finish();
                    openAccount();
                }
            }
        });
        mDrawerLayout = findViewById(R.id.recipesFromFoodContentsLayout);
        addDrawerItems();
        setupDrawer();
        //Hamburger Menu End ------------------------------------------------

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

    //Hamburger Menu From: http://blog.teamtreehouse.com/add-navigation-drawer-android
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        // Activate the navigation drawer toggle
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void addDrawerItems() {
        String[] array = { "Home", "Food Network", "Shopping", "Recipes", "Nutrient Search", "Account" };
        mAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, array);
        mDrawerList.setAdapter(mAdapter);
    }

    private void setupDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }
    public void openHome(){
        Intent intent = new Intent(this,Home.class);
        startActivity(intent);
    }
    public void openFoodContents(){
        Intent intent = new Intent(this,FoodContents.class);
        startActivity(intent);
    }
    public void openShoppping(){
        Intent intent = new Intent(this,Shopping.class);
        startActivity(intent);
    }
    public void openRecipes(){
        Intent intent = new Intent(this,Recipes.class);
        startActivity(intent);
    }
    public void openNutrientsSearch(){
        Intent intent = new Intent(this,NutrientSearch.class);
        startActivity(intent);
    }
    public void openAccount(){
        Intent intent = new Intent(this,UserAccount.class);
        startActivity(intent);
    }


}
