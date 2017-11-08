package ie.ncirl.a14445618.theintelligentfoodnetwork;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;


public class MyFoodNetwork extends AppCompatActivity {
     DatabaseReference mDatabase;
     TextView dbContentTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_food_network);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        Button viewDBButton = findViewById(R.id.dbViewButton);
        Button addDBButton = findViewById(R.id.dbAddButton);

        addDBButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                submitPost();
            }

        });

        viewDBButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                showDBContent();
            }

        });

    }
    //From: https://firebase.google.com/docs/database/android/start/
    public void submitPost(){
        String testString = "Bacon";
        //mDatabase.child("FridgeItems").setValue(testString);
        //mDatabase.child("FridgeItems").setValue(testString);
        FoodItem foodItem = new FoodItem("Poultry","90","22","10/11/2017");
        mDatabase.child("FridgeItems").child("Chicken").setValue(foodItem);
    }

    public void showDBContent(){
        dbContentTextView.setText("Content Here!");
    }








}
