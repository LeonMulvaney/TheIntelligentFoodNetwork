package ie.ncirl.a14445618.theintelligentfoodnetwork;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Recipes extends AppCompatActivity {
    Button favourtieRecipesBtn;
    Button searchRecipesBtn;
    String recipeSearchString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Change Action Bar Title From: https://stackoverflow.com/questions/3438276/how-to-change-the-text-on-the-action-bar
        setTitle(R.string.recipes);
        //Add Back Button to Action Bar - From https://stackoverflow.com/questions/12070744/add-back-button-to-action-bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_recipes);

        favourtieRecipesBtn = findViewById(R.id.favouriteRecipesBtn);
        searchRecipesBtn = findViewById(R.id.searchRecipesBtn);

        //Android Alertbox with EditText From: https://stackoverflow.com/questions/18799216/how-to-make-a-edittext-box-in-a-dialog
        searchRecipesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(Recipes.this);
                //AlertDialog.Builder alert = new AlertDialog.Builder(Recipes.this, R.style.AlertDialogCustom); //Custom AlertDialog Theme From: https://stackoverflow.com/questions/2422562/how-to-change-theme-for-alertdialog

                final EditText searchEt = new EditText(Recipes.this);
                alert.setMessage("Enter an ingredient...");
                alert.setTitle("Recipes Search");

                alert.setView(searchEt);

                alert.setPositiveButton("Search", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //What ever you want to do with the value
                         recipeSearchString = searchEt.getText().toString();
                         openRecipesFromFoodContents();
                    }
                });

                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                });

                alert.show();
            }
        });
    }

    //Function to return when back button is pressed From --> Same link as "Add Back Button" above
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    public void openFavouriteRecipes(View view){
        Intent intent = new Intent(this,FavouriteRecipes.class);
        startActivity(intent);
    }

    public void openRecipesFromFoodContents(){
        Intent intent = new Intent(this,RecipesFromFoodContents.class);
        intent.putExtra("foodType",recipeSearchString); //Pass String from one Activity to another From: https://stackoverflow.com/questions/6707900/pass-a-string-from-one-activity-to-another-activity-in-android
        startActivity(intent);
    }





}
