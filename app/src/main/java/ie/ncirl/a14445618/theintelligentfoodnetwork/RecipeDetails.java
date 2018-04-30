package ie.ncirl.a14445618.theintelligentfoodnetwork;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

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
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class RecipeDetails extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    DatabaseReference shoppingListRef;
    DatabaseReference favouritesRef;

    ScrollView recipeDetailsScrollView;
    String myUrl;
    String result;
    String recipeId;
    GetRecipeFromIdApi getRequest = new GetRecipeFromIdApi();
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

    ArrayList<ModelIngredient> ingredientList;
    AdapterIngredients adapter;
    NonScrollListView ingredientLv;

    ArrayList<ModelInstruction> instructionsList;
    AdapterInstructions instructionsAdapter;
    NonScrollListView instructionsLv;

    TextView servingsTv;
    TextView wwSmartPointTv;
    TextView preparationTv;
    TextView cookTimeTv;

    String ingredient;
    ArrayList<String> favouritesList;
    ArrayList<String> shoppingList;

    Button similarRecipesBtn;


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
        adapter = new AdapterIngredients(this, ingredientList);

        instructionsList = new ArrayList<>();
        instructionsAdapter = new AdapterInstructions(this,instructionsList);

        similarRecipesBtn = findViewById(R.id.similarRecipesBtn);
        similarRecipesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSimilarRecipe();
            }
        });


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

            //Get Ingredients, Store in Model called ModelIngredient, save objects to Arraylist, then parse arraylist to View using ListView Adapter (With custom Layout)
            JSONArray extendedIngredientsArray = object.getJSONArray("extendedIngredients");

            for(int i=0;i<extendedIngredientsArray.length();i++){
                JSONObject object = (JSONObject) extendedIngredientsArray.get(i);
                String originalString = object.getString("original");
                double amount = object.getDouble("amount");
                String unit = object.getString("unit");
                String name = object.getString("name");

                ModelIngredient ingredientModel = new ModelIngredient(originalString,amount,unit,name);
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
                ModelInstruction instructionModel = new ModelInstruction(stepNumber,instruction);
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


        //Firebase
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReferenceFromUrl("https://theintelligentfoodnetwork.firebaseio.com/");
        shoppingListRef = databaseReference.child("ShoppingList");
        favouritesRef = databaseReference.child("Favourites");

        //Grab Favourites from Firebase and save in ArrayList - This prevents the user adding duplicate favourites
        favouritesList = new ArrayList<>();
        favouritesRef.addValueEventListener(new ValueEventListener() { //SingleValueEvent Listener to prevent the append method causing duplicate entries
            @Override
            public void onDataChange (DataSnapshot dataSnapshot){
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String firebaseRecipeId = ds.child("recipeId").getValue().toString();
                    favouritesList.add(firebaseRecipeId);
                }
            }

            @Override
            public void onCancelled (DatabaseError databaseError){
            }
        });

        //Grab Shopping List from Firebase and save in ArrayList - This prevents the user adding duplicate Shopping List Items
        shoppingList = new ArrayList<>();
        shoppingListRef.addValueEventListener(new ValueEventListener() { //SingleValueEvent Listener to prevent the append method causing duplicate entries
            @Override
            public void onDataChange (DataSnapshot dataSnapshot){
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String firebaseShoppingItemtitle = ds.child("title").getValue().toString();
                    shoppingList.add(firebaseShoppingItemtitle);
                }
            }

            @Override
            public void onCancelled (DatabaseError databaseError){
            }
        });


        ingredientLv.setOnItemClickListener(new AdapterView.OnItemClickListener() { // Wait to see what element the user clicks on in the ListView
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                ModelIngredient ingredientModel = ingredientList.get(position); //Use original list as not filtered
                ingredient = StringUtils.capitalize(ingredientModel.getIngredientName().toString());

                //toast();
                //Alert Dialog From: http://rajeshvijayakumar.blogspot.ie/2013/04/alert-dialog-dialog-with-item-list.html
                final CharSequence[] items = {
                        "Add to Shopping List", "View Nutritional Info"
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(RecipeDetails.this);
                builder.setTitle("Ingredient: " + ingredient);
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        // Do something with the selection
                        if(item ==0){
                            addItemToShoppingList();
                        }
                        else{
                            openNutrientsResult();
                        }
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();

            }
        }); //End of listView onClickListener

    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
    //Adding Share & Favourite Button to TitleBar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.share_and_back, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.shareRecipe:
                shareRecipe();
                return true;

            case R.id.favouriteRecipe:
                addToFavourites();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void shareRecipe(){
        //Share Intent From: https://stackoverflow.com/questions/19683297/how-to-send-message-from-android-app-through-viber-message
        Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
        String recipeData = "Hey, check out this cool recipe from The Intelligent Food Network App: " + spoonacularSourceUrl;
        shareIntent.putExtra(Intent.EXTRA_TEXT, recipeData);
        shareIntent.setType("text/plain");
        startActivity(Intent.createChooser(shareIntent, "Please choose an application to share your recipe on..."));
    }

    //Android Snackbar From: https://spin.atomicobject.com/2017/07/10/android-snackbar-tutorial/
    public void showSnackbar(View view, String message, int duration)
    {
        Snackbar.make(view, message, duration).show();
    }

    public void addItemToShoppingList(){
        //Pushing Data to Firebase From: https://firebase.google.com/docs/database/admin/save-data
        String addItemToShoppingList = "true";

        for(int i=0;i<shoppingList.size();i++){
            if(shoppingList.get(i).toString().equals(ingredient)){
                addItemToShoppingList = "false";
                System.out.println("____________________________________");
                System.out.println("i is : " + i);
            }
            else{
                //Do nothing
            }
        }

        if(addItemToShoppingList.equals("true")){
            String itemId = shoppingListRef.push().getKey();
            Map<String,String> ingredientHmap = new HashMap<>();
            ingredientHmap.put("title",ingredient);
            shoppingListRef.child(itemId).setValue(ingredientHmap);

            View view = findViewById(R.id.recipeDetailsScrollView);
            String message = ingredient + " added to Shopping List."; //Capitalize Using StringUtils From: https://stackoverflow.com/questions/5725892/how-to-capitalize-the-first-letter-of-word-in-a-string-using-java
            int duration = Snackbar.LENGTH_SHORT;
            showSnackbar(view, message, duration);
        }

        else{
            View view = findViewById(R.id.recipeDetailsScrollView);
            String message = ingredient + " is already in your shopping list."; //Capitalize Using StringUtils From: https://stackoverflow.com/questions/5725892/how-to-capitalize-the-first-letter-of-word-in-a-string-using-java
            int duration = Snackbar.LENGTH_SHORT;
            showSnackbar(view, message, duration);
        }

    }

    public void addToFavourites(){
        //Pushing Data to Firebase From: https://firebase.google.com/docs/database/admin/save-data
        String addRecipeToFavourites = "true";

        for(int i=0;i<favouritesList.size();i++){
            if(favouritesList.get(i).toString().equals(recipeId)){
                addRecipeToFavourites = "false";
                System.out.println("____________________________________");
                System.out.println("i is : " + i);
            }
            else{
                //Do nothing
            }
        }

        System.out.println("____________________________________");
        System.out.println(addRecipeToFavourites);

        if(addRecipeToFavourites.equals("true")){
            String itemId = favouritesRef.push().getKey();
            Map<String,String> ingredientHmap = new HashMap<>();
            ingredientHmap.put("recipeId",recipeId);
            ingredientHmap.put("recipeTitle",title);
            ingredientHmap.put("recipeImgUrl",imageUrl);

            favouritesRef.child(itemId).setValue(ingredientHmap);

            View view = findViewById(R.id.recipeDetailsScrollView);
            String message = title + " added to your favourites."; //Capitalize Using StringUtils From: https://stackoverflow.com/questions/5725892/how-to-capitalize-the-first-letter-of-word-in-a-string-using-java
            int duration = Snackbar.LENGTH_SHORT;


            showSnackbar(view, message, duration);
        }

        else{
            View view = findViewById(R.id.recipeDetailsScrollView);
            String message = title + " is already in your favourites."; //Capitalize Using StringUtils From: https://stackoverflow.com/questions/5725892/how-to-capitalize-the-first-letter-of-word-in-a-string-using-java
            int duration = Snackbar.LENGTH_SHORT;
            showSnackbar(view, message, duration);
        }
    }



    public void openNutrientsResult(){
        Intent intent = new Intent(this,NutrientsResult.class);
        intent.putExtra("foodType",ingredient); //Pass String from one Activity to another From: https://stackoverflow.com/questions/6707900/pass-a-string-from-one-activity-to-another-activity-in-android
        startActivity(intent);
    }

    public void openSimilarRecipe(){
        Intent intent = new Intent(this,SimilarRecipe.class);
        intent.putExtra("similarRecipeId",recipeId); //Pass String from one Activity to another From: https://stackoverflow.com/questions/6707900/pass-a-string-from-one-activity-to-another-activity-in-android
        startActivity(intent);
    }

}
