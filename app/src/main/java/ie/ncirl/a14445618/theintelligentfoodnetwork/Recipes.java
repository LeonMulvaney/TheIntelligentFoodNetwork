package ie.ncirl.a14445618.theintelligentfoodnetwork;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class Recipes extends AppCompatActivity {
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    DatabaseReference keyRef;

    ListView favouriteRecipesListView;
    ArrayList<String> favouriteRecipesList;
    ArrayAdapter<String> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Change Action Bar Title From: https://stackoverflow.com/questions/3438276/how-to-change-the-text-on-the-action-bar
        setTitle(R.string.recipes);
        //Add Back Button to Action Bar - From https://stackoverflow.com/questions/12070744/add-back-button-to-action-bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_recipes);

        //Firebase
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReferenceFromUrl("https://theintelligentfoodnetwork.firebaseio.com/");
        keyRef = databaseReference.child("Favourites");

        favouriteRecipesList = new ArrayList<>();
        favouriteRecipesListView = findViewById(R.id.favouriteRecipesLv);
        adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,favouriteRecipesList);

        getContents();


    }

    //Function to return when back button is pressed From --> Same link as "Add Back Button" above
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    public void getContents() {
        //Get contents from Firebase into String From : https://www.youtube.com/watch?v=WDGmpvKpHyw
        keyRef.addValueEventListener(new ValueEventListener() { //SingleValueEvent Listener to prevent the append method causing duplicate entries

            @Override
            public void onDataChange (DataSnapshot dataSnapshot){
                favouriteRecipesList.clear(); //Clear foodlist before adding items again
                //Get ID From: https://stackoverflow.com/questions/43975734/get-parent-firebase-android
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String item = ds.child("recipeTitle").getValue().toString();
                    favouriteRecipesList.add(item);
                }

                favouriteRecipesListView.setAdapter(null); //Clear adapter so the information is not duplicated
                favouriteRecipesListView.setAdapter(adapter);
            }

            @Override
            public void onCancelled (DatabaseError databaseError){

            }
        });
        Toast.makeText(this,"All Favourite Recipes Loaded!",Toast.LENGTH_SHORT).show();//Send the user confirmation that they have refreshed the List
    }




}
