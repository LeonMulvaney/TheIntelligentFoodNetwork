package ie.ncirl.a14445618.theintelligentfoodnetwork;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ShoppingList extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    DatabaseReference shoppingListRef;

    GridView shoppingListGridView;
    AdapterShoppingList adapter;
    ArrayList<ModelShoppingListItem> shoppingList; //Shopping List is used to hold the Model of ShoppingListItem
    ArrayList<String> comparisonList; //Comparison List is used to compare what is in Firebase and what the user tries to add (Prevent Duplicate entries)

    String shoppingListItem;
    String newShoppingListItem;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Change Action Bar Title From: https://stackoverflow.com/questions/3438276/how-to-change-the-text-on-the-action-bar
        setTitle(R.string.shopping);
        //Add Back Button to Action Bar - From https://stackoverflow.com/questions/12070744/add-back-button-to-action-bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_shopping_list);

        //Firebase
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReferenceFromUrl("https://theintelligentfoodnetwork.firebaseio.com/");
        shoppingListRef = databaseReference.child("ShoppingList");

        shoppingList = new ArrayList<>();
        comparisonList = new ArrayList<>();
        shoppingListGridView = findViewById(R.id.shoppingGv);
        adapter = new AdapterShoppingList(this, shoppingList);

        shoppingListGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() { // Wait to see what element the user clicks on in the ListView
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                ModelShoppingListItem item = shoppingList.get(position);
                shoppingListItem = item.getShoppingListItemTitle().toString();
                //toast();
                //Alert Dialog From: http://rajeshvijayakumar.blogspot.ie/2013/04/alert-dialog-dialog-with-item-list.html
                final CharSequence[] items = {
                        "View Nutritional Info", "Remove"
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(ShoppingList.this);
                builder.setTitle(shoppingListItem);
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        // Do something with the selection
                        if(item ==0){
                            openNutrientsResult();

                        }
                        else{
                            removeItemFromShoppingList();
                        }
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();

            }
        }); //End of listView onClickListener
        getContents();

    }

    //Function to return when back button is pressed From --> Same link as "Add Back Button" above
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    //Adding Share & Favourite Button to TitleBar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addToShoppingList:
                enterItem();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //Android Snackbar From: https://spin.atomicobject.com/2017/07/10/android-snackbar-tutorial/
    public void showSnackbar(View view, String message, int duration) {
        Snackbar.make(view, message, duration).show();
    }

    public void getContents() {
        //Get contents from Firebase into String From : https://www.youtube.com/watch?v=WDGmpvKpHyw
        shoppingListRef.addValueEventListener(new ValueEventListener() { //SingleValueEvent Listener to prevent the append method causing duplicate entries

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Get ID From: https://stackoverflow.com/questions/43975734/get-parent-firebase-android
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String item = ds.child("title").getValue().toString();
                    ModelShoppingListItem shoppingListItem = new ModelShoppingListItem(item);
                    shoppingList.add(shoppingListItem);
                    comparisonList.add(item);
                }

                shoppingListGridView.setAdapter(null); //Clear adapter so the information is not duplicated
                shoppingListGridView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        Toast.makeText(this, "All shopping list items loaded!", Toast.LENGTH_SHORT).show();//Send the user confirmation that they have refreshed the List
    }

    public void enterItem() {
        AlertDialog.Builder alert = new AlertDialog.Builder(ShoppingList.this);
        //AlertDialog.Builder alert = new AlertDialog.Builder(Recipes.this, R.style.AlertDialogCustom); //Custom AlertDialog Theme From: https://stackoverflow.com/questions/2422562/how-to-change-theme-for-alertdialog

        final EditText addItemEt = new EditText(ShoppingList.this);
        alert.setMessage("Please enter a food item...");
        alert.setTitle("Add Item to Shopping List");

        alert.setView(addItemEt);

        alert.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //What ever you want to do with the value
                newShoppingListItem = StringUtils.capitalize(addItemEt.getText().toString());//Get user input, capitalize and save as String
                addNewItemToShoppingList();
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });
        alert.show();
    }

    public void addNewItemToShoppingList() {
        //Pushing Data to Firebase From: https://firebase.google.com/docs/database/admin/save-data
        String addItemToShoppingList = "true";

        for (int i = 0; i < comparisonList.size(); i++) {
            if (comparisonList.get(i).equals(newShoppingListItem)) {
                addItemToShoppingList = "false";
                System.out.println("____________________________________");
                System.out.println("i is : " + i);
            } else {
                //Do nothing
            }
        }

        if (addItemToShoppingList.equals("true")) {
            String itemId = shoppingListRef.push().getKey();

            Map<String, String> newItemHmap = new HashMap<>();
            newItemHmap.put("title", newShoppingListItem);
            shoppingListRef.child(itemId).setValue(newItemHmap);

            View view = findViewById(R.id.shoppingListActivity);
            String message = newShoppingListItem + " added to shopping list."; //Capitalize Using StringUtils From: https://stackoverflow.com/questions/5725892/how-to-capitalize-the-first-letter-of-word-in-a-string-using-java
            int duration = Snackbar.LENGTH_SHORT;
            showSnackbar(view, message, duration);
        }

        else {
            View view = findViewById(R.id.shoppingListActivity);
            String message = newShoppingListItem + " already in your shopping list."; //Capitalize Using StringUtils From: https://stackoverflow.com/questions/5725892/how-to-capitalize-the-first-letter-of-word-in-a-string-using-java
            int duration = Snackbar.LENGTH_SHORT;
            showSnackbar(view, message, duration);
        }
    }

    public void removeItemFromShoppingList(){
        //Firebase Remove Files From: https://firebase.google.com/docs/storage/android/delete-files
        shoppingListRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) { //Grab "Snapshot" of Shopping List Table
                for (DataSnapshot ds : dataSnapshot.getChildren()) { //Loop through all children
                    String item = ds.child("title").getValue().toString(); //Get each string each child inhibits
                    if(item.equals(shoppingListItem)){ //If item matches, remove the item
                        String key = ds.getKey().toString();
                        shoppingListRef.child(key).removeValue();//Remove value
                        shoppingList.clear();//Once item is removed, clear the Shopping ArrayList so the other event listener can update the changes
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void openNutrientsResult(){
        Intent intent = new Intent(this,NutrientsResult.class);
        intent.putExtra("foodType",shoppingListItem); //Pass String from one Activity to another From: https://stackoverflow.com/questions/6707900/pass-a-string-from-one-activity-to-another-activity-in-android
        startActivity(intent);
    }
}