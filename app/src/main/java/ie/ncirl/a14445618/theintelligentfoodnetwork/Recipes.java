package ie.ncirl.a14445618.theintelligentfoodnetwork;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutionException;

public class Recipes extends AppCompatActivity {

    //Hamburger Menu
    private ListView mDrawerList;
    private ArrayAdapter<String> mAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;

    //Firebase Authentication
    private FirebaseAuth mAuth;
    String userId;

    //Firebase Database
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    DatabaseReference keyRef;

    GetSimilarRecipesApi getRequest = new GetSimilarRecipesApi();
    String myUrl;
    String result;
    String chopped;
    ArrayList<ModelRecipeFromIngredient> recipesList;
    JSONArray array;


    String recipeSearchString;
    ArrayList<ModelFavouriteRecipe> favouriteRecipesList;
    Random rand; //Random Number Java From: https://stackoverflow.com/questions/5887709/getting-random-numbers-in-java
    long listSize;
    int randomRecipeFromFavourites;
    int randomRecipeFromSimilar;

    ImageView recommendationImg;
    TextView recommendationTitleTv;
    TextView basedOnTv;

    CardView recommendationCv;
    int recommendationId;

    //Declare Recommendation Linear Layout so it can be targeted and set as un-clickable (If the user has no Favourite Recipes)
    View searchLinearLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Change Action Bar Title From: https://stackoverflow.com/questions/3438276/how-to-change-the-text-on-the-action-bar
        setTitle(R.string.recipes);
        //Add Back Button to Action Bar - From https://stackoverflow.com/questions/12070744/add-back-button-to-action-bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_recipes);

        //Get Instance of Firebase Authentication
        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getUid().toString();

        //Firebase
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReferenceFromUrl("https://theintelligentfoodnetwork.firebaseio.com/");
        //keyRef = databaseReference.child("Favourites");
        keyRef = databaseReference.child("Users/"+userId+"/favourites");


        searchLinearLayout = findViewById(R.id.searchLinearLayout);

        favouriteRecipesList = new ArrayList<>();
        recipesList = new ArrayList<>();

        //Views to fill for Recipe Recommendation
        recommendationImg = findViewById(R.id.recommendationImg);
        recommendationTitleTv = findViewById(R.id.recommendationTitleTv);
        basedOnTv = findViewById(R.id.recipeBasedOnTv);
        recommendationCv = findViewById(R.id.recommendationCv);

        recommendationCv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRecipeDetails();
            }
        });



        //Android Alertbox with EditText From: https://stackoverflow.com/questions/18799216/how-to-make-a-edittext-box-in-a-dialog
        searchLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(Recipes.this);
                //AlertDialog.Builder alert = new AlertDialog.Builder(Recipes.this, R.style.AlertDialogCustom); //Custom AlertDialog Theme From: https://stackoverflow.com/questions/2422562/how-to-change-theme-for-alertdialog

                final EditText searchEt = new EditText(Recipes.this);
                alert.setMessage("Enter an ingredient...");
                alert.setTitle("Recipes Search");

                alert.setView(searchEt);

                alert.setPositiveButton("Search", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //What ever you want to do with the value
                         recipeSearchString = searchEt.getText().toString();
                         openRecipesFromFoodContents();
                    }
                });

                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                });
                alert.show();
            }
        });

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
        mDrawerLayout = findViewById(R.id.recipesLayout);
        addDrawerItems();
        setupDrawer();
        //Hamburger Menu End ------------------------------------------------

        getContents();
    }


    //Android Snackbar From: https://spin.atomicobject.com/2017/07/10/android-snackbar-tutorial/
    public void showSnackbar(View view, String message, int duration) {
        Snackbar.make(view, message, duration).show();
    }

    public void openFavouriteRecipes(View view){
        Intent intent = new Intent(this,FavouriteRecipes.class);
        startActivity(intent);
    }

    public void openRecipesFromFoodContents(){
        Intent intent = new Intent(this,RecipesFromFoodContents.class);
        intent.putExtra("foodType",recipeSearchString); //Pass String from one Activity to another From: https://stackoverflow.com/questions/6707900/pass-a-string-from-one-activity-to-another-activity-in-android
        startActivity(intent);
    }

    public void getContents() {
        //Get contents from Firebase into String From : https://www.youtube.com/watch?v=WDGmpvKpHyw
        keyRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange (DataSnapshot dataSnapshot){
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String title = ds.child("recipeTitle").getValue().toString();
                    String id = ds.child("recipeId").getValue().toString();
                    String img = ds.child("recipeImgUrl").getValue().toString();

                    ModelFavouriteRecipe modelFavouriteRecipe = new ModelFavouriteRecipe(title,id,img);
                    favouriteRecipesList.add(modelFavouriteRecipe);

                }

                //Check if the user has recipes in their Favourites - If they don't the recipe recommendations cannot work, thus notify the user
                //Check applied here as well as above so the Snackbar does not display a message each time Firebase table is updated
                if(favouriteRecipesList.isEmpty()){
                    View view = findViewById(R.id.recipesLayout);
                    String message = "No Favourite Recipes - Cannot generate Recommendation";
                    int duration = Snackbar.LENGTH_SHORT;
                    showSnackbar(view, message, duration);
                    recommendationTitleTv.setText("Please add a recipe to favourites to avail of our customised recommendations");
                    basedOnTv.setText("Please add a recipe to favourites to avail of our customised recommendations");
                    recommendationCv.setClickable(false);
                }

                else{
                    listSize = dataSnapshot.getChildrenCount();//Firebase Get Children Count From: https://stackoverflow.com/questions/43606235/android-firebase-get-childrens-count
                    rand = new Random();
                    System.out.println("_____________________________");
                    System.out.println("List Size ----------> " + listSize);
                    randomRecipeFromFavourites = rand.nextInt((int) listSize)+0;
                    System.out.println("Random Recipe Number ---------->" + randomRecipeFromFavourites);
                    System.out.println("_____________________________");
                    String idForSimilarRecipeApi = favouriteRecipesList.get(randomRecipeFromFavourites).getRecipeId();
                    String titleForSimilarRecipeApi = favouriteRecipesList.get(randomRecipeFromFavourites).getRecipeTitle();
                    recommendationCv.setClickable(true);


                    //Get Data From API
                    myUrl = "https://spoonacular-recipe-food-nutrition-v1.p.mashape.com/recipes/"+idForSimilarRecipeApi+ "/similar";
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
                            System.out.println("_____________________________");
                            System.out.println(title);
                            String imageUrl = "https://spoonacular.com/recipeImages/" + (String) jsonObj.get("image");
                            ModelRecipeFromIngredient recipe = new ModelRecipeFromIngredient(id,title,imageUrl);
                            recipesList.add(recipe);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    randomRecipeFromSimilar = rand.nextInt(recipesList.size())+0;
                    recommendationId = recipesList.get(randomRecipeFromSimilar).getId();
                    String similarRecipeImageUrl = recipesList.get(randomRecipeFromSimilar).getImageUrl();
                    String similarRecipeTitle = recipesList.get(randomRecipeFromSimilar).getTitle();
                    Picasso.with(Recipes.this).load(similarRecipeImageUrl).into(recommendationImg); //Use picasso library to load images instead of setImageResource
                    recommendationTitleTv.setText(similarRecipeTitle);
                    basedOnTv.setText(titleForSimilarRecipeApi);
                }

            }

            @Override
            public void onCancelled (DatabaseError databaseError){

            }
        });
    }

    public void openRecipeDetails(){
        Intent intent = new Intent(this,RecipeDetails.class);
        String id = Integer.toString(recommendationId);
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
