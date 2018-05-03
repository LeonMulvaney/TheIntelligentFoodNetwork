package ie.ncirl.a14445618.theintelligentfoodnetwork;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class UserAccount extends AppCompatActivity {

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
    DatabaseReference usersRef;
    DatabaseReference foodItemsRef;
    DatabaseReference favouriteRecipesRef;
    DatabaseReference shoppingListRef;

    //Firebase Storage From: https://code.tutsplus.com/tutorials/image-upload-to-firebase-in-android-application--cms-29934
    FirebaseStorage storage;
    StorageReference storageReference;

    ImageView profileIv;
    String profileImageUrl;

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

    View userAccountLayout;

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

        //Firebase Storage
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        profileIv = findViewById(R.id.profileIv);

        nameTv = findViewById(R.id.nameTv);
        emailTv = findViewById(R.id.emailTv);
        phoneTv = findViewById(R.id.phoneTv);
        weightTv = findViewById(R.id.weightTv);
        joinedTv = findViewById(R.id.joinedTv);

        foodItemCountTv = findViewById(R.id.foodItemCountTv);
        favouriteRecipeCountTv = findViewById(R.id.favouriteRecipeCountTv);
        shoppingItemCountTv = findViewById(R.id.shoppingItemCountTv);

        userAccountLayout = findViewById(R.id.userAccountLayout);

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
        mDrawerLayout = findViewById(R.id.userAccountLayout);
        addDrawerItems();
        setupDrawer();
        //Hamburger Menu End ------------------------------------------------

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

    //Android Snackbar From: https://spin.atomicobject.com/2017/07/10/android-snackbar-tutorial/
    public void showSnackbar(View view, String message, int duration) {
        Snackbar.make(view, message, duration).show();
    }
    public void getUserData() {
        //Android Getting Image From Firebase Storage From: https://stackoverflow.com/questions/38424203/firebase-storage-getting-image-url
        storageReference.child("profileImages/"+userId+"/profileImage").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                System.out.println("-----------------------------------Successfully Downloaded Image");
                Picasso.with(getApplicationContext()).load(uri).into(profileIv);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                String uploadTipImage = "https://firebasestorage.googleapis.com/v0/b/theintelligentfoodnetwork.appspot.com/o/profileImages%2Fuploadimage.png?alt=media&token=6dadee2f-cfcd-43ff-8d54-035153d0c12c";
                Picasso.with(getApplicationContext()).load(uploadTipImage).into(profileIv);

                View view = findViewById(R.id.userAccountLayout);
                String message = "Upload an image to complete your profile!";
                int duration = Snackbar.LENGTH_SHORT;
                showSnackbar(view, message, duration);
            }
        });

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
                numberofFavouriteRecipes =0;
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
                numberOfShoppingItems =0;
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
