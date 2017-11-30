package ie.ncirl.a14445618.theintelligentfoodnetwork;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

// Get Data From Firebase into Android: https://www.captechconsulting.com/blogs/firebase-realtime-database-android-tutorial
//https://stackoverflow.com/questions/39800547/read-data-from-firebase-database

public class FoodContents extends AppCompatActivity {
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    DatabaseReference keyRef;

    //Views
    ListView foodListView;

    // Android Populating ListView using ArrayAdapter From: https://stackoverflow.com/questions/5070830/populating-a-listview-using-an-arraylist
    ArrayAdapter<FoodItem> myArrayAdapter;
    FoodItemAdapter adapter;

    //Variables
    ArrayList<FoodItem> foodList;


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
        adapter = new FoodItemAdapter(this,foodList);

        //Firebase
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReferenceFromUrl("https://theintelligentfoodnetwork.firebaseio.com/");
        keyRef = databaseReference.child("FridgeItems");

        //Target List View in Activity
        foodListView = findViewById(R.id.recipesListView);

        //Get contents from Firebase into String From : https://www.youtube.com/watch?v=WDGmpvKpHyw
        keyRef.addListenerForSingleValueEvent(new ValueEventListener() { //SingleValueEvent Listener to prevent the append method causing duplicate entries
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //Get ID From: https://stackoverflow.com/questions/43975734/get-parent-firebase-android
                for (DataSnapshot ds: dataSnapshot.getChildren()) {
                    String type = ds.child("foodType").getValue().toString();
                    String expDate = ds.child("expiryDate").getValue().toString();
                    String calories = ds.child("calories").getValue().toString();
                    String protein = ds.child("protein").getValue().toString();

                    FoodItem newItem = new FoodItem(type, expDate, calories, protein);
                    foodList.add(newItem);
                }
                //foodListView.setAdapter(myArrayAdapter);
                foodListView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    } //End of OnCreate

    // Add Buttons to Action Bar From: http://android.xsoftlab.net/training/basics/actionbar/adding-buttons.html
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.refresh_actionbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_refresh:
                refreshActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //1. Refresh the List - Take a new Snapshot of Firebase
    //2. Clear the original foodlist array so it doesn't re-enter the same contents
    //3. Re-populate the foodlist array
    //4. Set the adapter as null to clear the ListView
    //5. Re-populate the adapter with the new, updated foodlist array
    public void refreshActivity(){
        foodList.clear(); //Remove items from arraylist

        keyRef.addListenerForSingleValueEvent(new ValueEventListener() { //SingleValueEvent Listener to prevent the append method causing duplicate entries
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //Get ID From: https://stackoverflow.com/questions/43975734/get-parent-firebase-android
                for (DataSnapshot ds: dataSnapshot.getChildren()) {
                    String type = ds.child("foodType").getValue().toString();
                    String expDate = ds.child("expiryDate").getValue().toString();
                    String calories = ds.child("calories").getValue().toString();
                    String protein = ds.child("protein").getValue().toString();

                    FoodItem newItem = new FoodItem(type, expDate, calories, protein);
                    foodList.add(newItem);//Add objects to arraylist
                }
                //foodListView.setAdapter(myArrayAdapter);
                foodListView.setAdapter(null); //Clear the ListView
                foodListView.setAdapter(adapter);//Re-Populate the list view
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        Toast.makeText(this,"List Refreshed!",Toast.LENGTH_SHORT).show();//Send the user confirmation that they have refreshed the List
    }

    //Function to return to home when back button is pressed From --> Same link as "Add Back Button" above
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
