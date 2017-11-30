package ie.ncirl.a14445618.theintelligentfoodnetwork;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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

    //Function to return to home when back button is pressed From --> Same link as "Add Back Button" above
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
