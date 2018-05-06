package ie.ncirl.a14445618.theintelligentfoodnetwork;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FavouriteRecipes extends AppCompatActivity {

    private FirebaseAuth mAuth;
    String userId;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    DatabaseReference keyRef;

    GridView favouriteRecipesGridView;
    AdapterFavouriteRecipe adapterFavouriteRecipe;
    ArrayList<ModelFavouriteRecipe> favouriteRecipesList;

    String recipeId;
    String recipeTitle;

    long favouriteRecipeCount;
    String favouriteRecipeAmount;
    TextView favouriteRecipeCountTv;

    //GridView From: https://www.raywenderlich.com/127544/android-gridview-getting-started

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Change Action Bar Title From: https://stackoverflow.com/questions/3438276/how-to-change-the-text-on-the-action-bar
        setTitle(R.string.recipes);
        //Add Back Button to Action Bar - From https://stackoverflow.com/questions/12070744/add-back-button-to-action-bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_favourite_recipes);

        //Get Instance of Firebase Authentication
        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getUid().toString();

        //Firebase
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReferenceFromUrl("https://theintelligentfoodnetwork.firebaseio.com/");
        //keyRef = databaseReference.child("Favourites");
        keyRef = databaseReference.child("Users/"+userId+"/favourites");

        favouriteRecipesList = new ArrayList<>();
        favouriteRecipesGridView = findViewById(R.id.favouriteRecipesGrid);
        adapterFavouriteRecipe = new AdapterFavouriteRecipe(this,favouriteRecipesList);

        favouriteRecipeCountTv = findViewById(R.id.favouriteRecipeCountTv);



        //GridView OnClickListener From: https://stackoverflow.com/questions/14675695/how-to-use-onclicklistener-for-grid-view
        favouriteRecipesGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() { // Wait to see what element the user clicks on in the ListView
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                ModelFavouriteRecipe modelFavouriteRecipe = favouriteRecipesList.get(position); //Use original list as not filtered
                recipeId = modelFavouriteRecipe.getRecipeId().toString();
                recipeTitle = modelFavouriteRecipe.getRecipeTitle().toString();

                //Alert Dialog From: http://rajeshvijayakumar.blogspot.ie/2013/04/alert-dialog-dialog-with-item-list.html
                final CharSequence[] items = {
                        "View", "Remove"
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(FavouriteRecipes.this);
                builder.setTitle(recipeTitle);
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        // Do something with the selection
                        if(item ==0){
                            //foodType = foodList.get(position).getFoodType().toString();
                            openRecipeDetails();
                        }
                        else{
                            removeFavouriteRecipe();
                        }
                        View view = findViewById(R.id.favouriteRecipesLinearLayout);
                        String message = recipeTitle + " removed from favourites."; //Capitalize Using StringUtils From: https://stackoverflow.com/questions/5725892/how-to-capitalize-the-first-letter-of-word-in-a-string-using-java
                        int duration = Snackbar.LENGTH_SHORT;
                        showSnackbar(view, message, duration);
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();

            }
        }); //End of listView onClickListener



        getContents();

    }

    //Function to return when back button is pressed From --> Same link as "Add Back Button" above
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    //Android Snackbar From: https://spin.atomicobject.com/2017/07/10/android-snackbar-tutorial/
    public void showSnackbar(View view, String message, int duration)
    {
        Snackbar.make(view, message, duration).show();
    }

    public void getContents() {
        //Get contents from Firebase into String From : https://www.youtube.com/watch?v=WDGmpvKpHyw
        keyRef.addValueEventListener(new ValueEventListener() { //SingleValueEvent Listener to prevent the append method causing duplicate entries

            @Override
            public void onDataChange (DataSnapshot dataSnapshot){
                favouriteRecipesList.clear(); //Clear foodlist before adding items again
                //Get ID From: https://stackoverflow.com/questions/43975734/get-parent-firebase-android
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String title = ds.child("recipeTitle").getValue().toString();
                    String id = ds.child("recipeId").getValue().toString();
                    String img = ds.child("recipeImgUrl").getValue().toString();


                    ModelFavouriteRecipe modelFavouriteRecipe = new ModelFavouriteRecipe(title,id,img);
                    favouriteRecipesList.add(modelFavouriteRecipe);
                }

                favouriteRecipeCount = dataSnapshot.getChildrenCount();//Set recipe Count Icon to match the amount of recipes in the users' favourites
                favouriteRecipeCountTv.setText((int) favouriteRecipeCount + " recipes");

                favouriteRecipesGridView.setAdapter(null); //Clear adapter so the information is not duplicated
                favouriteRecipesGridView.setAdapter(adapterFavouriteRecipe);
                isFavouritesListEmpty();
            }


            @Override
            public void onCancelled (DatabaseError databaseError){

            }
        });
    }

    //Check if the Users Favourites List is Empty
    public void isFavouritesListEmpty(){
        if(favouriteRecipesList.isEmpty()){
            View view = findViewById(R.id.favouriteRecipesLinearLayout);
            String message = "No Favourites - Add Recipes to avail of recommendations!";
            int duration = Snackbar.LENGTH_SHORT;
            showSnackbar(view, message, duration);
        }
    }

    public void removeFavouriteRecipe(){
        //Firebase Remove Files From: https://firebase.google.com/docs/storage/android/delete-files
        keyRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String firebaseId = ds.child("recipeId").getValue().toString();
                    String recipeTitle = ds.child("recipeTitle").getValue().toString();
                    if(firebaseId.equals(recipeId)){
                        String key = ds.getKey().toString();
                        keyRef.child(key).removeValue();
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void openRecipeDetails(){
        Intent intent = new Intent(this,RecipeDetails.class);
        String id = recipeId;
        intent.putExtra("recipeId",id); //Pass String from one Activity to another From: https://stackoverflow.com/questions/6707900/pass-a-string-from-one-activity-to-another-activity-in-android
        startActivity(intent);
    }









}
