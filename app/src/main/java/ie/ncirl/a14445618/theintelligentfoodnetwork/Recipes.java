package ie.ncirl.a14445618.theintelligentfoodnetwork;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

    private FirebaseAuth mAuth;
    String userId;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    DatabaseReference keyRef;

    GetSimilarRecipesApi getRequest = new GetSimilarRecipesApi();
    String myUrl;
    String result;
    String chopped;
    ArrayList<ModelRecipeFromIngredient> recipesList;
    JSONArray array;

    Button favourtieRecipesBtn;
    Button searchRecipesBtn;
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
    View recommendationLinearLayout;


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

        recommendationLinearLayout = findViewById(R.id.recommendationLinearLayout);

        favourtieRecipesBtn = findViewById(R.id.favouriteRecipesBtn);
        searchRecipesBtn = findViewById(R.id.searchRecipesBtn);
        favouriteRecipesList = new ArrayList<>();
        //recipeRecommendationGv = findViewById(R.id.recipeRecommendationGv);
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
        searchRecipesBtn.setOnClickListener(new View.OnClickListener() {
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

        getContents();
    }


    //Function to return when back button is pressed From --> Same link as "Add Back Button" above
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
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

                    //Check if the user has any Favourite Recipes - if they do not, make sure they cannot click the linear layout
                    if(favouriteRecipesList.isEmpty()){
                        recommendationLinearLayout.setClickable(false);
                    }
                    else{
                        recommendationLinearLayout.setClickable(true);
                    }
                }

                //Check if the user has recipes in their Favourites - If they don't the recipe recommendations cannot work, thus notify the user
                //Check applied here as well as above so the Snackbar does not display a message each time Firebase table is updated
                if(favouriteRecipesList.isEmpty()){
                    View view = findViewById(R.id.recipesAcitivty);
                    String message = "No Favourite Recipes - Cannot generate Recommendation";
                    int duration = Snackbar.LENGTH_SHORT;
                    showSnackbar(view, message, duration);
                    recommendationTitleTv.setText("Please add a recipe to favourites to avail of our customised recommendations");
                    basedOnTv.setText("Please add a recipe to favourites to avail of our customised recommendations");
                    //Set the Linear Layout as Un-clickable if the user has no Favourite Recipes
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
                    //Set the Linear Layout as Clickable if a Favourite Recipe has loaded
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



}
