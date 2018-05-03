package ie.ncirl.a14445618.theintelligentfoodnetwork;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;

public class ShoppingListRecommendation extends AppCompatActivity {

    private FirebaseAuth mAuth;
    String userId;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    DatabaseReference shoppingListRef;
    DatabaseReference foodItemsRef;

    ArrayList<String> shoppingList;
    ArrayList<String> foodItemsList;
    ArrayList<String> recommendationList;

    ListView shoppingListRecommendationLv;
    ArrayAdapter<String> recommendationListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Change Action Bar Title From: https://stackoverflow.com/questions/3438276/how-to-change-the-text-on-the-action-bar
        setTitle(R.string.shopping_list_recommendation_string);
        //Add Back Button to Action Bar - From https://stackoverflow.com/questions/12070744/add-back-button-to-action-bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setContentView(R.layout.activity_shopping_list_recommendation);

        //Get Instance of Firebase Authentication
        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getUid().toString();

        //Firebase
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReferenceFromUrl("https://theintelligentfoodnetwork.firebaseio.com/");
        shoppingListRef = databaseReference.child("Users/"+userId+"/shoppingList");
        foodItemsRef = databaseReference.child("Users/"+userId+"/foodItems");

        shoppingList = new ArrayList<>();
        foodItemsList = new ArrayList<>();
        recommendationList = new ArrayList<>();

        //Android Simple ListView From: https://androidexample.com/Create_A_Simple_Listview_-_Android_Example/index.php?view=article_discription&aid=65
        shoppingListRecommendationLv = findViewById(R.id.shoppingListRecommendationLv);
        recommendationListAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,android.R.id.text1,recommendationList);


       generateShoppingRecommendation();
    }

    //Function to return when back button is pressed From --> Same link as "Add Back Button" above
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    public void generateShoppingRecommendation() {
        shoppingListRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                shoppingList.clear();
                //Get ID From: https://stackoverflow.com/questions/43975734/get-parent-firebase-android
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String shoppingListItem = ds.child("title").getValue().toString().toLowerCase().trim();
                    shoppingList.add(shoppingListItem);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        foodItemsRef.addValueEventListener(new ValueEventListener() { //SingleValueEvent Listener to prevent the append method causing duplicate entries
            @Override
            public void onDataChange (DataSnapshot dataSnapshot){
                foodItemsList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String foodItem = ds.child("foodType").getValue().toString().toLowerCase().trim(); //Get values as lowercase and also trim so both string comparisons match
                    foodItemsList.add(foodItem);
                }
                recommendationList.clear();
                //Java comparing Arrays From: https://stackoverflow.com/questions/36122735/compare-two-string-arrays-and-print-out-the-strings-which-differ
                for(int i=0;i<shoppingList.size();i++){
                    String shoppingListItem = shoppingList.get(i).toString();
                    if(foodItemsList.contains(shoppingListItem)){
                        //Do nothing
                    }
                    else{
                        recommendationList.add(StringUtils.capitalize(shoppingListItem));
                    }
                }
                shoppingListRecommendationLv.setAdapter(recommendationListAdapter);
                System.out.println("Food Items List: --------------------------" + foodItemsList.toString());
                System.out.println("Shopping List: --------------------------" + shoppingList.toString());
                System.out.println("Recommendation List: --------------------------" + recommendationList.toString());
            }

            @Override
            public void onCancelled (DatabaseError databaseError){

            }
        });
    }




}
