package ie.ncirl.a14445618.theintelligentfoodnetwork;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserAccount extends AppCompatActivity {

    private FirebaseAuth mAuth;
    String userId;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    DatabaseReference usersRef;
    DatabaseReference foodItemsRef;
    DatabaseReference favouriteRecipesRef;
    DatabaseReference shoppingListRef;

    String name;
    String email;
    String phone;
    String weight;
    String joined;

    TextView nameTv;
    TextView emailTv;
    TextView phoneTv;
    TextView weightTv;
    TextView joinedTv;

    TextView foodItemCountTv;
    TextView favouriteRecipeCountTv;
    TextView shoppingItemCountTv;

    int numberOfFoodItems;
    int numberofFavouriteRecipes;
    int numberOfShoppingItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Change Action Bar Title From: https://stackoverflow.com/questions/3438276/how-to-change-the-text-on-the-action-bar
        setTitle(R.string.account_action_bar_string);
        //Add Back Button to Action Bar - From https://stackoverflow.com/questions/12070744/add-back-button-to-action-bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_user_account);

        //Get Instance of Firebase Authentication
        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getUid().toString();

        //Firebase Database
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReferenceFromUrl("https://theintelligentfoodnetwork.firebaseio.com/");
        usersRef = databaseReference.child("Users/"+userId+"/accountDetails");
        foodItemsRef = databaseReference.child("Users/"+userId+"/foodItems");
        favouriteRecipesRef = databaseReference.child("Users/"+userId+"/favourites");
        shoppingListRef = databaseReference.child("Users/"+userId+"/shoppingList");

        nameTv = findViewById(R.id.nameTv);
        emailTv = findViewById(R.id.emailTv);
        phoneTv = findViewById(R.id.phoneTv);
        weightTv = findViewById(R.id.weightTv);
        joinedTv = findViewById(R.id.joinedTv);

        foodItemCountTv = findViewById(R.id.foodItemCountTv);
        favouriteRecipeCountTv = findViewById(R.id.favouriteRecipeCountTv);
        shoppingItemCountTv = findViewById(R.id.shoppingItemCountTv);

        //Call the Methods at the end of OnCreate to get User data and other information
        getUserData();
        getFoodItemsCount();
        getFavouriteRecipesCount();
        getShoppingItemsCount();
    }
    //Function to return when back button is pressed From --> Same link as "Add Back Button" above
    @Override
    public boolean onSupportNavigateUp(){
        Intent intent = new Intent(getApplicationContext(),Home.class);
        finish();
        startActivity(intent);
        return true;

    }

    public void getUserData() {
        //Get contents from Firebase into String From : https://www.youtube.com/watch?v=WDGmpvKpHyw
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                     name = ds.child("name").getValue().toString();
                     email = ds.child("email").getValue().toString();
                     phone = ds.child("phone").getValue().toString();
                     weight = ds.child("weight").getValue().toString();
                     joined = ds.child("joined").getValue().toString();
                }

                nameTv.setText(name);
                emailTv.setText(email);
                phoneTv.setText(phone);
                weightTv.setText(weight + " Kg");
                joinedTv.setText(joined);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void getFoodItemsCount() {

        foodItemsRef.addValueEventListener(new ValueEventListener() {
            //SingleValueEvent Listener
            @Override
            public void onDataChange (DataSnapshot dataSnapshot){
                numberOfFoodItems =0; //Initially set the value to 0 in case additional items are added
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    //The DataSnapshot will iterate through all elements within the specified table(JSON Object), add to the counter for each item iterated
                     numberOfFoodItems = numberOfFoodItems +1;
                }
                //Append the Number of items in the table to the relevant TextView
                foodItemCountTv.setText(Long.toString(numberOfFoodItems));
                System.out.println("Food Item Count:--------------------------" + numberOfFoodItems);
            }

            @Override
            public void onCancelled (DatabaseError databaseError){

            }
        });
    }

    public void getFavouriteRecipesCount() {
        favouriteRecipesRef.addValueEventListener(new ValueEventListener() { //SingleValueEvent Listener
            @Override
            public void onDataChange (DataSnapshot dataSnapshot){
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    //The DataSnapshot will iterate through all elements within the specified table(JSON Object), add to the counter for each item iterated
                    numberofFavouriteRecipes = numberofFavouriteRecipes +1;
                }
                favouriteRecipeCountTv.setText(Long.toString(numberofFavouriteRecipes));
                System.out.println("Favourite Recipe Count:--------------------------" + numberofFavouriteRecipes);
            }

            @Override
            public void onCancelled (DatabaseError databaseError){

            }
        });
    }

    public void getShoppingItemsCount() {
        shoppingListRef.addValueEventListener(new ValueEventListener() { //SingleValueEvent Listener
            @Override
            public void onDataChange (DataSnapshot dataSnapshot){
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    //The DataSnapshot will iterate through all elements within the specified table(JSON Object), add to the counter for each item iterated
                    numberOfShoppingItems = numberOfShoppingItems +1;

                }
                shoppingItemCountTv.setText(Long.toString(numberOfShoppingItems));
                System.out.println("Shopping Item Count:--------------------------" + numberOfShoppingItems);
            }

            @Override
            public void onCancelled (DatabaseError databaseError){

            }
        });
    }

    public void openFoodContents(View view){
        Intent intent = new Intent(this,FoodContents.class);
        startActivity(intent);
    }

    public void openFavouriteRecipes(View view){
        Intent intent = new Intent(this,FavouriteRecipes.class);
        startActivity(intent);
    }

    public void openShoppingList(View view){
        Intent intent = new Intent(this,ShoppingList.class);
        startActivity(intent);
    }

    public void editProfile(View view){
        Intent intent = new Intent(this,EditProfile.class);
        startActivity(intent);
    }
}
