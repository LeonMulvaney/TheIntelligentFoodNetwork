package ie.ncirl.a14445618.theintelligentfoodnetwork;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

// Get Data From Firebase into Android: https://www.captechconsulting.com/blogs/firebase-realtime-database-android-tutorial
//https://stackoverflow.com/questions/39800547/read-data-from-firebase-database

public class FoodContents extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

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

    //Views
    GridView foodGridView;

    // Android Populating ListView using ArrayAdapter From: https://stackoverflow.com/questions/5070830/populating-a-listview-using-an-arraylist
    ArrayAdapter<ModelFoodItem> myArrayAdapter;
    AdapterFoodItem adapter;

    //Variables
    ArrayList<ModelFoodItem> foodList;
    String foodType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_contents);

        //Change Action Bar Title From: https://stackoverflow.com/questions/3438276/how-to-change-the-text-on-the-action-bar
        setTitle(R.string.recipes_action_bar_string);
        //Add Back Button to Action Bar - From https://stackoverflow.com/questions/12070744/add-back-button-to-action-bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        foodList = new ArrayList<>();
        //myArrayAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,foodList);
        adapter = new AdapterFoodItem(this, foodList);


        //Get Instance of Firebase Authentication
        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getUid().toString();

        //Firebase
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReferenceFromUrl("https://theintelligentfoodnetwork.firebaseio.com/");
        keyRef = databaseReference.child("Users/"+userId+"/foodItems");

        //Target List View in Activity
        foodGridView = findViewById(R.id.recipesListView);



        //Spinner From: https://developer.android.com/guide/topics/ui/controls/spinner.html
        Spinner spinner =  findViewById(R.id.filterByCategorySpinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.food_contents_spinner_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
             spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(this);
        getContents(); //Call the GetContents Method which pulls data from Firebase and populates it within the ListView

        registerForContextMenu(foodGridView);

        foodGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() { // Wait to see what element the user clicks on in the ListView
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                    ModelFoodItem item = foodList.get(position); //Use original list as not filtered
                    foodType = item.getFoodType().toString().toLowerCase().trim();
                    //toast();
                //Alert Dialog From: http://rajeshvijayakumar.blogspot.ie/2013/04/alert-dialog-dialog-with-item-list.html
                final CharSequence[] items = {
                        "View Recpies", "View Nutritional Info"
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(FoodContents.this);
                builder.setTitle("Please choose...");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        // Do something with the selection
                        if(item ==0){
                            //foodType = foodList.get(position).getFoodType().toString();
                            openRecipesFromFoodContents();
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
        mDrawerLayout = findViewById(R.id.foodContentsLayout);
        addDrawerItems();
        setupDrawer();
        //Hamburger Menu End ------------------------------------------------

    } //End of OnCreate

    //Function to return to home when back button is pressed From --> Same link as "Add Back Button" above
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    //Android Snackbar From: https://spin.atomicobject.com/2017/07/10/android-snackbar-tutorial/
    public void showSnackbar(View view, String message, int duration) {
        Snackbar.make(view, message, duration).show();
    }

    public void getContents() {
        //Get contents from Firebase into String From : https://www.youtube.com/watch?v=WDGmpvKpHyw
        keyRef.addValueEventListener(new ValueEventListener() { //SingleValueEvent Listener to prevent the append method causing duplicate entries

            @Override
            public void onDataChange (DataSnapshot dataSnapshot){
                foodList.clear(); //Clear foodlist before adding items again
                //Get ID From: https://stackoverflow.com/questions/43975734/get-parent-firebase-android
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String type = ds.child("foodType").getValue().toString();
                    String expDate = ds.child("expiryDate").getValue().toString();
                    String calories = "50";
                    String protein = "22";
                    String category = ds.child("category").getValue().toString();

                    ModelFoodItem newItem = new ModelFoodItem(type, expDate, calories, protein,category);
                    foodList.add(newItem);
                }
                //foodListView.setAdapter(myArrayAdapter);
                foodGridView.setAdapter(null); //Clear adapter so the information is not duplicated
                foodGridView.setAdapter(adapter);
                isFoodListEmpty();
            }

            @Override
            public void onCancelled (DatabaseError databaseError){

            }
        });
    }

    //If foodList is empty, notify the user (Call Snackbar)
    public void isFoodListEmpty(){
        if(foodList.isEmpty()){
            View view = findViewById(R.id.foodContentsLayout);
            String message = "No Items present";
            int duration = Snackbar.LENGTH_SHORT;
            showSnackbar(view, message, duration);
        }
    }

    //1. Refresh the List - Take a new Snapshot of Firebase
    //2. Clear the original foodlist array so it doesn't re-enter the same contents
    //3. Re-populate the foodlist array
    //4. Set the adapter as null to clear the ListView
    //5. Re-populate the adapter with the new, updated foodlist array
    public void refreshList(){

        keyRef.addValueEventListener(new ValueEventListener() { //SingleValueEvent Listener to prevent the append method causing duplicate entries
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                foodList.clear(); //Remove items from arraylist
                //Get ID From: https://stackoverflow.com/questions/43975734/get-parent-firebase-android
                for (DataSnapshot ds: dataSnapshot.getChildren()) {
                    String type = ds.child("foodType").getValue().toString();
                    String expDate = ds.child("expiryDate").getValue().toString();
                    String calories = ds.child("calories").getValue().toString();
                    String protein = ds.child("protein").getValue().toString();
                    String category = ds.child("category").getValue().toString();

                    ModelFoodItem newItem = new ModelFoodItem(type, expDate, calories, protein,category);
                    foodList.add(newItem);//Add objects to arraylist
                }
                //foodListView.setAdapter(myArrayAdapter);
                foodGridView.setAdapter(null); //Clear the ListView
                foodGridView.setAdapter(adapter);//Re-Populate the list view
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Toast.makeText(this,"List Refreshed!",Toast.LENGTH_SHORT).show();//Send the user confirmation that they have refreshed the List

    }

    public void filterByPoultry(){
        keyRef.addListenerForSingleValueEvent(new ValueEventListener() { //SingleValueEvent Listener to prevent the append method causing duplicate entries
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                foodList.clear(); //Remove items from arraylist
                //Get ID From: https://stackoverflow.com/questions/43975734/get-parent-firebase-android
                for (DataSnapshot ds: dataSnapshot.getChildren()) {
                    String type = ds.child("foodType").getValue().toString();
                    String expDate = ds.child("expiryDate").getValue().toString();
                    String calories = ds.child("calories").getValue().toString();
                    String protein = ds.child("protein").getValue().toString();
                    String category = ds.child("category").getValue().toString();

                        ModelFoodItem newItem = new ModelFoodItem(type, expDate, calories, protein,category);
                        foodList.add(newItem);//Add objects to arraylist

                    for(int i =0;i<foodList.size();i++){
                        if(foodList.get(i).getCategory().equals("Poultry")){
                        }
                        else{
                            foodList.remove(i);
                        }
                    }


                }
                //foodListView.setAdapter(myArrayAdapter);
                foodGridView.setAdapter(null); //Clear the ListView
                foodGridView.setAdapter(adapter);//Re-Populate the list view
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Toast.makeText(this,"Filtered by Poultry!",Toast.LENGTH_SHORT).show();//Send the user confirmation that they have refreshed the List

}

    public void filterByFruitAndVeg(){

        keyRef.addListenerForSingleValueEvent(new ValueEventListener() { //SingleValueEvent Listener to prevent the append method causing duplicate entries
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                foodList.clear(); //Remove items from arraylist
                //Get ID From: https://stackoverflow.com/questions/43975734/get-parent-firebase-android
                for (DataSnapshot ds: dataSnapshot.getChildren()) {
                    String type = ds.child("foodType").getValue().toString();
                    String expDate = ds.child("expiryDate").getValue().toString();
                    String calories = ds.child("calories").getValue().toString();
                    String protein = ds.child("protein").getValue().toString();
                    String category = ds.child("category").getValue().toString();

                    ModelFoodItem newItem = new ModelFoodItem(type, expDate, calories, protein,category);
                    foodList.add(newItem);//Add objects to arraylist

                    for(int i =0;i<foodList.size();i++){
                        if(foodList.get(i).getCategory().equals("Fruit and Veg")){
                        }
                        else{
                            foodList.remove(i);
                        }
                    }


                }
                //foodListView.setAdapter(myArrayAdapter);
                foodGridView.setAdapter(null); //Clear the ListView
                foodGridView.setAdapter(adapter);//Re-Populate the list view
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Toast.makeText(this,"Filtered by Fruit and Veg!",Toast.LENGTH_SHORT).show();//Send the user confirmation that they have refreshed the List

    }

    public void filterByDairy(){

        keyRef.addListenerForSingleValueEvent(new ValueEventListener() { //SingleValueEvent Listener to prevent the append method causing duplicate entries
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                foodList.clear(); //Remove items from arraylist
                //Get ID From: https://stackoverflow.com/questions/43975734/get-parent-firebase-android
                for (DataSnapshot ds: dataSnapshot.getChildren()) {
                    String type = ds.child("foodType").getValue().toString();
                    String expDate = ds.child("expiryDate").getValue().toString();
                    String calories = ds.child("calories").getValue().toString();
                    String protein = ds.child("protein").getValue().toString();
                    String category = ds.child("category").getValue().toString();

                    ModelFoodItem newItem = new ModelFoodItem(type, expDate, calories, protein,category);
                    foodList.add(newItem);//Add objects to arraylist

                    for(int i =0;i<foodList.size();i++){
                        if(foodList.get(i).getCategory().equals("Dairy")){
                        }
                        else{
                            foodList.remove(i);
                        }
                    }


                }
                //foodListView.setAdapter(myArrayAdapter);
                foodGridView.setAdapter(null); //Clear the ListView
                foodGridView.setAdapter(adapter);//Re-Populate the list view
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Toast.makeText(this,"Filtered by Dairy!",Toast.LENGTH_SHORT).show();//Send the user confirmation that they have refreshed the List

    }

    public void filterByMisc(){

        keyRef.addListenerForSingleValueEvent(new ValueEventListener() { //SingleValueEvent Listener to prevent the append method causing duplicate entries
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                foodList.clear(); //Remove items from arraylist
                //Get ID From: https://stackoverflow.com/questions/43975734/get-parent-firebase-android
                for (DataSnapshot ds: dataSnapshot.getChildren()) {
                    String type = ds.child("foodType").getValue().toString();
                    String expDate = ds.child("expiryDate").getValue().toString();
                    String calories = ds.child("calories").getValue().toString();
                    String protein = ds.child("protein").getValue().toString();
                    String category = ds.child("category").getValue().toString();

                    ModelFoodItem newItem = new ModelFoodItem(type, expDate, calories, protein,category);
                    foodList.add(newItem);//Add objects to arraylist

                    for(int i =0;i<foodList.size();i++){
                        if(foodList.get(i).getCategory().equals("Misc")){
                        }
                        else{
                            foodList.remove(i);
                        }
                    }


                }
                //foodListView.setAdapter(myArrayAdapter);
                foodGridView.setAdapter(null); //Clear the ListView
                foodGridView.setAdapter(adapter);//Re-Populate the list view
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Toast.makeText(this,"Filtered by Miscellaneous!",Toast.LENGTH_SHORT).show();//Send the user confirmation that they have refreshed the List

    }



    //Spinner Item Listeners
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        //Call Method from Spinner From: https://stackoverflow.com/questions/36093106/how-to-call-a-function-at-every-selection-in-spinner
        if(parent.getSelectedItemPosition() == 0){
            getContents();
        }

        else if(parent.getSelectedItemPosition() == 1){
            filterByPoultry();
        }

        else if(parent.getSelectedItemPosition() == 2){
            filterByFruitAndVeg();
        }

        else if(parent.getSelectedItemPosition() == 3){
            filterByDairy();
        }

        else if(parent.getSelectedItemPosition() == 4){
            filterByMisc();
        }
    }
    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

    public void openRecipesFromFoodContents(){
        Intent intent = new Intent(this,RecipesFromFoodContents.class);
        intent.putExtra("foodType",foodType); //Pass String from one Activity to another From: https://stackoverflow.com/questions/6707900/pass-a-string-from-one-activity-to-another-activity-in-android
        startActivity(intent);
    }

    public void openNutrientsResult(){
        Intent intent = new Intent(this,NutrientsResult.class);
        intent.putExtra("foodType",foodType); //Pass String from one Activity to another From: https://stackoverflow.com/questions/6707900/pass-a-string-from-one-activity-to-another-activity-in-android
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
