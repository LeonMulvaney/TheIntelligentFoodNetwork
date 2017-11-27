package ie.ncirl.a14445618.theintelligentfoodnetwork;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

// Get Data From Firebase into Android: https://www.captechconsulting.com/blogs/firebase-realtime-database-android-tutorial
public class MyFoodNetwork extends AppCompatActivity {
     DatabaseReference myDatabase;
     TextView dbContentTextView;
     ListView myListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_food_network);

        myDatabase = FirebaseDatabase.getInstance().getReference("FridgeItems");



        Button viewDBButton = findViewById(R.id.dbViewButton);
        viewDBButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
            }
        });


        //Add to Database Button & Listener
        Button addDBButton = findViewById(R.id.dbAddButton);
        addDBButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                addItem();
            }
        });

        myListView =(ListView) findViewById(R.id.foodList);


        myDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void writeNewItem(String foodType,String expiryDate,String calories,String protein){
        FoodItem foodItem = new FoodItem(foodType,expiryDate,calories,protein);
        myDatabase.child("FridgeItems").child("Beef").setValue(foodItem);
    }


//Adding Items to Database From: https://www.androidhive.info/2016/10/android-working-with-firebase-realtime-database/
    public void addItem(){
        String userId = myDatabase.push().getKey();

        FoodItem foodItem = new FoodItem("Yop","30/11/2017","70","10");

        myDatabase.child(userId).setValue(foodItem);
    }


    public void getContents(DataSnapshot dataSnapshot){
        for(DataSnapshot ds :dataSnapshot.getChildren()){
            String userId = myDatabase.push().getKey();

            FoodItem foodItem = new FoodItem();
            foodItem.setFoodType(ds.child(userId).getValue(FoodItem.class).getFoodType());
            foodItem.setExpiryDate(ds.child(userId).getValue(FoodItem.class).getExpiryDate());
            foodItem.setCalories(ds.child(userId).getValue(FoodItem.class).getCalories());
            foodItem.setProtein(ds.child(userId).getValue(FoodItem.class).getProtein());

            ArrayList<String> array = new ArrayList<>();
            array.add(foodItem.getFoodType());
            array.add(foodItem.getExpiryDate());
            array.add(foodItem.getCalories());
            array.add(foodItem.getProtein());

            ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,array);
            myListView.setAdapter(adapter);

        }

    }







}
