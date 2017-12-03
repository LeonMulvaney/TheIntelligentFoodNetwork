package ie.ncirl.a14445618.theintelligentfoodnetwork;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddFoodItem extends AppCompatActivity {
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    DatabaseReference keyRef;

    EditText foodType;
    EditText expDate;
    EditText calories;
    EditText protein;
    EditText category;

    String foodTypeFromEt;
    String expDateFromEt;
    String caloriesFromEt;
    String proteinFromEt;
    String categoryFromEt;

    Button addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food_item);

        //Change Action Bar Title From: https://stackoverflow.com/questions/3438276/how-to-change-the-text-on-the-action-bar
        setTitle(R.string.add_food_item_action_bar_string);

        //Add Back Button to Action Bar - From https://stackoverflow.com/questions/12070744/add-back-button-to-action-bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Firebase
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReferenceFromUrl("https://theintelligentfoodnetwork.firebaseio.com/");
        keyRef = databaseReference.child("FridgeItems");

        foodType = findViewById(R.id.foodTypeEt);
        expDate = findViewById(R.id.expDateEt);
        calories = findViewById(R.id.caloriesEt);
        protein = findViewById(R.id.proteinEt);
        category = findViewById(R.id.categoryEt);

        addButton = findViewById(R.id.addToDbButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addItem();
            }
        });


    }

    //Function to return to home when back button is pressed From --> Same link as "Add Back Button" above
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }


    //Adding Items to Database From: https://www.androidhive.info/2016/10/android-working-with-firebase-realtime-database/
    public void addItem(){
        String itemId = keyRef.push().getKey();

        foodTypeFromEt = foodType.getText().toString();
        expDateFromEt = expDate.getText().toString();
        caloriesFromEt= calories.getText().toString();
        proteinFromEt = protein.getText().toString();
        categoryFromEt = protein.getText().toString();


        FoodItem foodItem = new FoodItem(foodTypeFromEt,expDateFromEt,caloriesFromEt,proteinFromEt,categoryFromEt);

        keyRef.child(itemId).setValue(foodItem);

        Intent intent = new Intent(this,MyFoodNetwork.class);
        startActivity(intent);
        Toast.makeText(this,"Food Item: " + foodTypeFromEt + " added to Fridge!  ",Toast.LENGTH_LONG).show();
    }
}
