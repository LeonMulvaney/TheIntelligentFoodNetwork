package ie.ncirl.a14445618.theintelligentfoodnetwork;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Get Data From Firebase into Android: https://www.captechconsulting.com/blogs/firebase-realtime-database-android-tutorial
//https://stackoverflow.com/questions/39800547/read-data-from-firebase-database

public class MyFoodNetwork extends AppCompatActivity {
    //--------------------------------------------------------------------
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    Button readButton;
    TextView databaseTextview;

     String key;
     ListView myListView;

     //OnCreate ----------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_food_network);

        //myListView =(ListView) findViewById(R.id.foodList);



        databaseTextview = findViewById(R.id.showDbTv);
        readButton = findViewById(R.id.showDbBtn);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReferenceFromUrl("https://theintelligentfoodnetwork.firebaseio.com/");
        //key = databaseReference.child("FridgeItems").push().getKey();

        //Target Button and Declare OnClickListener - Encapsulating Method

//Get contents from Firebase into String From : https://www.youtube.com/watch?v=WDGmpvKpHyw
        readButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                databaseTextview.setText("");

                DatabaseReference keyRef = databaseReference.child("FridgeItems");

                //-------------------------------------------------------
                keyRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //String value = dataSnapshot.getKey();
                        //databaseTextview.setText(value);

                        //Get ID From: https://stackoverflow.com/questions/43975734/get-parent-firebase-android
                        for (DataSnapshot ds: dataSnapshot.getChildren()) {
                            key = ds.getKey();

                            String type = ds.child("foodType").getValue().toString();
                            String expDate = ds.child("expiryDate").getValue().toString();
                            String calories = ds.child("calories").getValue().toString();
                            String protein = ds.child("protein").getValue().toString();

                            ArrayList<FoodItem> foodList = new ArrayList<>();
                            FoodItem newItem = new FoodItem(type,expDate,calories,protein);
                            foodList.add(newItem);


                            for(int i=0;i<foodList.size();i++){
                                int index = i;
                                //System.out.println(foodList.get(i).getFoodType());
                                String foodItem = (foodList.get(i).getFoodType());
                                databaseTextview.append("Item No " + Integer.toString(index) + " : " + foodItem);
                                databaseTextview.append("\n");

                            }

                            //System.out.println(foodList);
                            //databaseTextview.setText(foodList.toString());


                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });

    }




//Adding Items to Database From: https://www.androidhive.info/2016/10/android-working-with-firebase-realtime-database/
    /*public void addItem(){
        String userId = myDatabase.push().getKey();

        FoodItem foodItem = new FoodItem("Yop","30/11/2017","70","10");

        myDatabase.child(userId).setValue(foodItem);
    }*/


    /*public void getContents(DataSnapshot dataSnapshot){
        for(DataSnapshot ds :dataSnapshot.getChildren()){

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

        }*/

    //}







}
